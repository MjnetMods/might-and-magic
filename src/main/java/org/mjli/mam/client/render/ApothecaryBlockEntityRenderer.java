package org.mjli.mam.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;
import org.mjli.mam.block_entity.ApothecaryBlockEntity;

import java.util.List;

/**
 * Continuous fill-fraction height lerp, generalized to any registered fluid via
 * {@link IClientFluidTypeExtensions} — pattern adapted from Botania's ManaPoolBlockEntityRenderer.
 */
public class ApothecaryBlockEntityRenderer implements BlockEntityRenderer<ApothecaryBlockEntity> {
    // Goblet cavity per assets/mam/models/block/shapes/apothecary.json: basin floor at y=12,
    // walls to y=16, open interior footprint x/z 3..13 — matches INSET below.
    private static final float BOTTOM_Y = 12F / 16F;
    private static final float TOP_Y = 15F / 16F;
    private static final float INSET = 3F;

    // Ingredients orbit the basin center just above the fluid surface, well inside the
    // INSET=3 walls (interior half-extent is (16-2*INSET)/2/16 = 0.3125).
    private static final float ORBIT_RADIUS = 0.18F;
    private static final float ORBIT_RADIUS_WOBBLE = 0.02F;
    private static final float ITEM_HOVER = 0.06F;
    private static final float ITEM_SCALE = 0.32F;

    private static final RenderType FLUID_QUAD = RenderType.create(
            "mam_apothecary_fluid",
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
            VertexFormat.Mode.QUADS,
            128,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getPositionColorTexLightmapShader))
                    .setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                    .createCompositeState(true));

    public ApothecaryBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(ApothecaryBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffers, int light, int overlay) {
        FluidStack fluidStack = be.getFluidTank().getFluid();
        if (fluidStack.isEmpty()) return;

        float fillFraction = (float) fluidStack.getAmount() / be.getFluidTank().getCapacity();
        float y = Mth.clampedMap(fillFraction, 0F, 1F, BOTTOM_Y, TOP_Y);

        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(extensions.getStillTexture(fluidStack));
        int tint = extensions.getTintColor(fluidStack);

        poseStack.pushPose();
        poseStack.translate(0, y, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(90F));

        VertexConsumer buffer = buffers.getBuffer(FLUID_QUAD);
        renderQuad(poseStack, buffer, sprite, tint, light);

        poseStack.popPose();

        renderIngredients(be, partialTick, poseStack, buffers, overlay, y);
    }

    /**
     * Ingested ingredients have no visible presence otherwise — the {@link net.minecraft.world.entity.item.ItemEntity}
     * that carried them is discarded on ingestion ({@link ApothecaryBlockEntity#collideEntityItem}), same as
     * Botania's {@code PetalApothecaryBlockEntity}. Botania draws its floating petals itself
     * (see {@code PetalApothecaryBlockEntityRenderer}) rather than relying on the entity render path;
     * this mirrors that pattern, orbiting each stored stack above the fluid surface.
     *
     * <p>Rendered full-bright ({@link LightTexture#FULL_BRIGHT}) rather than lit from the block entity's
     * own light value or resampled per-position: these are small flat 2D icons, not solid blocks, so
     * accurate shading isn't the point — legibility is. Resampling world light per-item was tried and
     * reverted: the bob animation crosses the block-above boundary every cycle, so the sampled light
     * value flickered between two discrete levels ("blinking") as it crossed.
     */
    private static void renderIngredients(ApothecaryBlockEntity be, float partialTick, PoseStack poseStack,
            MultiBufferSource buffers, int overlay, float fluidY) {
        List<ItemStack> ingredients = be.getIngredients();
        int count = ingredients.size();
        if (count == 0) return;

        double ticks = (be.getLevel().getGameTime() + partialTick) * 0.5;
        float offsetPerItem = 360F / count;

        for (int i = 0; i < count; i++) {
            float deg = (float) ((ticks * 4F) % 360F) + offsetPerItem * i;
            float rad = deg * Mth.DEG_TO_RAD;
            float radius = ORBIT_RADIUS + ORBIT_RADIUS_WOBBLE * Mth.sin((float) ((ticks + i * 20) / 6F));
            float x = radius * Mth.cos(rad);
            float z = radius * Mth.sin(rad);
            float bob = Mth.cos((float) ((ticks + i * 50) / 5F)) * 0.02F;
            float itemY = fluidY + ITEM_HOVER + bob;

            poseStack.pushPose();
            poseStack.translate(0.5 + x, itemY, 0.5 + z);
            poseStack.mulPose(Axis.YP.rotationDegrees(deg));
            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            Minecraft.getInstance().getItemRenderer().renderStatic(ingredients.get(i), ItemDisplayContext.GROUND,
                    LightTexture.FULL_BRIGHT, overlay, poseStack, buffers, be.getLevel(), i);
            poseStack.popPose();
        }
    }

    private static void renderQuad(PoseStack poseStack, VertexConsumer buffer, TextureAtlasSprite sprite, int tint, int light) {
        Matrix4f mat = poseStack.last().pose();
        float red = ((tint >> 16) & 0xFF) / 255F;
        float green = ((tint >> 8) & 0xFF) / 255F;
        float blue = (tint & 0xFF) / 255F;
        float min = INSET / 16F;
        float max = 1F - min;
        // TextureAtlasSprite.getU/getV take a 0-1 fraction of the sprite in this MC version
        // (not 0-16 pixel units), so reuse the same min/max fractions for UV as for position.
        float uMin = sprite.getU(min);
        float uMax = sprite.getU(max);
        float vMin = sprite.getV(min);
        float vMax = sprite.getV(max);

        buffer.addVertex(mat, min, max, 0).setColor(red, green, blue, 1F).setUv(uMin, vMax).setLight(light);
        buffer.addVertex(mat, max, max, 0).setColor(red, green, blue, 1F).setUv(uMax, vMax).setLight(light);
        buffer.addVertex(mat, max, min, 0).setColor(red, green, blue, 1F).setUv(uMax, vMin).setLight(light);
        buffer.addVertex(mat, min, min, 0).setColor(red, green, blue, 1F).setUv(uMin, vMin).setLight(light);
    }
}
