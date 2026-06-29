package org.mjli.mam;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mjli.mam.block_entity.ApothecaryBlockEntity;
import org.mjli.mam.block_entity.flower.PureDaisyBlockEntity;
import org.mjli.mam.block_entity.mana.ManaPoolBlockEntity;
import org.mjli.mam.verdant.VerdantFlowers;
import org.mjli.mam.verdant.VerdantMana;

public class MamBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MightAndMagic.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PureDaisyBlockEntity>> PURE_DAISY =
            BLOCK_ENTITY_TYPES.register("pure_daisy",
                    () -> BlockEntityType.Builder.of(PureDaisyBlockEntity::new, VerdantFlowers.PURE_DAISY.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ManaPoolBlockEntity>> MANA_POOL =
            BLOCK_ENTITY_TYPES.register("mana_pool",
                    () -> BlockEntityType.Builder.of(ManaPoolBlockEntity::new, VerdantMana.MANA_POOL.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ApothecaryBlockEntity>> APOTHECARY =
            BLOCK_ENTITY_TYPES.register("apothecary",
                    () -> BlockEntityType.Builder.of(ApothecaryBlockEntity::new, VerdantMana.APOTHECARY.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITY_TYPES.register(bus);
    }
}
