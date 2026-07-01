package org.mjli.mam.block_entity.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.mjli.mam.MamDataComponents;
import org.mjli.mam.api.internal.EnergyBlockType;
import org.mjli.mam.api.internal.EnergyNetworkAction;
import org.mjli.mam.api.internal.EnergyNetworkHandler;
import org.mjli.mam.api.energy.EnergyContainer;
import org.mjli.mam.api.energy.EnergyType;
import org.mjli.mam.api.energy.EnergyPool;

import java.util.Optional;

public class ManaPoolBlockEntity extends BlockEntity implements EnergyPool {
    public static final int MAX_CAPACITY_TIER_1 = 1_000_000;
    public static final int MAX_CAPACITY_TIER_2 = 4_000_000;
    public static final int MAX_CAPACITY_TIER_3 = 16_000_000;

    // Placeholder — drain/charge rate TBD during balancing pass (design/magic/17_trinkets.md)
    private static final int CHARGE_RATE = 10_000;

    private EnergyContainer container;
    private boolean outputting;
    private Optional<DyeColor> color = Optional.empty();
    private boolean addedToNetwork;
    private ItemStack chargingItem = ItemStack.EMPTY;

    public ManaPoolBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int maxEnergy, EnergyType energyType, boolean aligned) {
        super(type, pos, state);
        this.container = new EnergyContainer(0, maxEnergy, energyType, aligned);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaPoolBlockEntity self) {
        if (!self.addedToNetwork) {
            EnergyNetworkHandler.INSTANCE.fireEvent(self, EnergyBlockType.POOL, EnergyNetworkAction.ADD);
            self.addedToNetwork = true;
        }
        self.transferChargingItem();
    }

    // Right-click empty-handed to retrieve; right-click holding a mana item to insert. Item lives in
    // this internal slot (not as a world ItemEntity) so it can't despawn, burn, or be picked up by mobs.
    public InteractionResult interact(Player player) {
        ItemStack held = player.getMainHandItem();
        if (held.isEmpty()) {
            if (chargingItem.isEmpty()) return InteractionResult.PASS;
            ItemStack out = chargingItem;
            chargingItem = ItemStack.EMPTY;
            setChanged();
            if (!player.getInventory().add(out)) player.drop(out, false);
            return InteractionResult.SUCCESS;
        }

        if (!chargingItem.isEmpty() || !held.has(MamDataComponents.ENERGY_CONTAINER.get())) return InteractionResult.PASS;
        chargingItem = held.split(1);
        setChanged();
        return InteractionResult.SUCCESS;
    }

    private void transferChargingItem() {
        if (chargingItem.isEmpty()) return;
        EnergyContainer item = chargingItem.get(MamDataComponents.ENERGY_CONTAINER.get());
        if (item == null) return;

        // Direction picked by fill level only — .receive() below handles same-type transfer,
        // aligned-mismatch draining, and unaligned-mismatch conversion identically to pool<->pool.
        if (!item.isFull() && container.energy() > 0) {
            int amount = Math.min(CHARGE_RATE, Math.min(item.maxEnergy() - item.energy(), container.energy()));
            chargingItem.set(MamDataComponents.ENERGY_CONTAINER.get(), item.receive(amount, container.energyType()));
            container = container.receive(-amount, container.energyType());
            setChanged();
        } else if (item.energy() > 0 && !container.isFull()) {
            int amount = Math.min(CHARGE_RATE, Math.min(item.energy(), container.maxEnergy() - container.energy()));
            container = container.receive(amount, item.energyType());
            chargingItem.set(MamDataComponents.ENERGY_CONTAINER.get(), item.receive(-amount, item.energyType()));
            setChanged();
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        EnergyNetworkHandler.INSTANCE.fireEvent(this, EnergyBlockType.POOL, EnergyNetworkAction.REMOVE);
    }

    public static int calculateComparatorLevel(int mana, int max) {
        int val = (int) ((double) mana / (double) max * 15.0);
        if (mana > 0) val = Math.max(val, 1);
        return val;
    }

    // EnergyPool
    @Override public boolean isOutputtingPower() { return outputting; }
    @Override public Optional<DyeColor> getColor() { return color; }
    @Override public void setColor(Optional<DyeColor> color) { this.color = color; setChanged(); }
    @Override public EnergyType getEnergyType() { return container.energyType(); }

    @Override
    public void receiveEnergy(int amount, EnergyType incomingType) {
        container = container.receive(amount, incomingType);
        setChanged();
    }

    // EnergyReceiver
    @Override public int getCurrentEnergy() { return container.energy(); }
    @Override public int getMaxEnergy() { return container.maxEnergy(); }
    @Override public boolean isFull() { return container.isFull(); }
    @Override public void receiveEnergy(int amount) { receiveEnergy(amount, container.energyType()); }
    @Override public boolean canReceiveEnergyFromBursts() { return true; }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("energy", container.energy());
        tag.putBoolean("outputting", outputting);
        tag.putString("energyType", container.energyType().name());
        color.ifPresent(c -> tag.putByte("color", (byte) c.getId()));
        if (!chargingItem.isEmpty()) tag.put("chargingItem", chargingItem.save(registries));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        EnergyType loadedType = tag.contains("energyType") ? EnergyType.valueOf(tag.getString("energyType")) : container.energyType();
        container = new EnergyContainer(tag.getInt("energy"), container.maxEnergy(), loadedType, container.aligned());
        outputting = tag.getBoolean("outputting");
        color = tag.contains("color") ? Optional.of(DyeColor.byId(tag.getByte("color"))) : Optional.empty();
        chargingItem = tag.contains("chargingItem")
                ? ItemStack.parse(registries, tag.getCompound("chargingItem")).orElse(ItemStack.EMPTY)
                : ItemStack.EMPTY;
    }
}
