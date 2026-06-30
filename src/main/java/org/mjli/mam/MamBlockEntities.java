package org.mjli.mam;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mjli.mam.block_entity.ApothecaryBlockEntity;
import org.mjli.mam.block_entity.flower.DaybloomBlockEntity;
import org.mjli.mam.block_entity.flower.EndoflameBlockEntity;
import org.mjli.mam.block_entity.flower.HydroangeasBlockEntity;
import org.mjli.mam.block_entity.flower.PureDaisyBlockEntity;
import org.mjli.mam.api.mana.ManaEnergyType;
import org.mjli.mam.block_entity.mana.ManaPoolBlockEntity;
import org.mjli.mam.verdant.VerdantFlowers;
import org.mjli.mam.verdant.VerdantGeneratingFlowers;
import org.mjli.mam.verdant.VerdantMana;

public class MamBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MightAndMagic.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PureDaisyBlockEntity>> PURE_DAISY =
            BLOCK_ENTITY_TYPES.register("pure_daisy",
                    () -> BlockEntityType.Builder.of(PureDaisyBlockEntity::new, VerdantFlowers.PURE_DAISY.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ManaPoolBlockEntity>> MANA_POOL =
            BLOCK_ENTITY_TYPES.register("mana_pool",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new ManaPoolBlockEntity(MamBlockEntities.MANA_POOL.get(), pos, state, ManaPoolBlockEntity.MAX_CAPACITY_TIER_1, ManaEnergyType.MANA, false),
                            VerdantMana.MANA_POOL.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ManaPoolBlockEntity>> INFUSED_MANA_POOL =
            BLOCK_ENTITY_TYPES.register("infused_mana_pool",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new ManaPoolBlockEntity(MamBlockEntities.INFUSED_MANA_POOL.get(), pos, state, ManaPoolBlockEntity.MAX_CAPACITY_TIER_2, ManaEnergyType.MANA, false),
                            VerdantMana.INFUSED_MANA_POOL.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ManaPoolBlockEntity>> SACRED_MANA_POOL =
            BLOCK_ENTITY_TYPES.register("sacred_mana_pool",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new ManaPoolBlockEntity(MamBlockEntities.SACRED_MANA_POOL.get(), pos, state, ManaPoolBlockEntity.MAX_CAPACITY_TIER_3, ManaEnergyType.MANA, true),
                            VerdantMana.SACRED_MANA_POOL.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ManaPoolBlockEntity>> DESECRATED_MANA_POOL =
            BLOCK_ENTITY_TYPES.register("desecrated_mana_pool",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new ManaPoolBlockEntity(MamBlockEntities.DESECRATED_MANA_POOL.get(), pos, state, ManaPoolBlockEntity.MAX_CAPACITY_TIER_3, ManaEnergyType.NOX, true),
                            VerdantMana.DESECRATED_MANA_POOL.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ApothecaryBlockEntity>> APOTHECARY =
            BLOCK_ENTITY_TYPES.register("apothecary",
                    () -> BlockEntityType.Builder.of(ApothecaryBlockEntity::new, VerdantMana.APOTHECARY.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DaybloomBlockEntity>> DAYBLOOM =
            BLOCK_ENTITY_TYPES.register("daybloom",
                    () -> BlockEntityType.Builder.of(DaybloomBlockEntity::new, VerdantGeneratingFlowers.DAYBLOOM.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<EndoflameBlockEntity>> ENDOFLAME =
            BLOCK_ENTITY_TYPES.register("endoflame",
                    () -> BlockEntityType.Builder.of(EndoflameBlockEntity::new, VerdantGeneratingFlowers.ENDOFLAME.get()).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HydroangeasBlockEntity>> HYDROANGEAS =
            BLOCK_ENTITY_TYPES.register("hydroangeas",
                    () -> BlockEntityType.Builder.of(HydroangeasBlockEntity::new, VerdantGeneratingFlowers.HYDROANGEAS.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITY_TYPES.register(bus);
    }
}
