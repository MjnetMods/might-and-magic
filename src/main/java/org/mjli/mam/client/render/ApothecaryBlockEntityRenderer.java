package org.mjli.mam.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;
import org.mjli.mam.block_entity.ApothecaryBlockEntity;

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
