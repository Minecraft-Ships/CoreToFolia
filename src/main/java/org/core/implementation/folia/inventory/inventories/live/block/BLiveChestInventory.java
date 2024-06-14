package org.core.implementation.folia.inventory.inventories.live.block;

import org.bukkit.inventory.Inventory;
import org.core.implementation.folia.inventory.inventories.snapshot.block.BChestInventorySnapshot;
import org.core.implementation.folia.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.folia.inventory.item.stack.BLiveItemStack;
import org.core.implementation.folia.world.position.impl.sync.BBlockPosition;
import org.core.inventory.inventories.live.block.LiveChestInventory;
import org.core.inventory.inventories.snapshots.block.ChestInventorySnapshot;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Slot;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BLiveChestInventory implements LiveChestInventory {

    public class ChestSlot implements Slot {

        protected final int pos;

        public ChestSlot(int pos) {
            this.pos = pos;
        }

        @Override
        public Optional<Integer> getPosition() {
            return Optional.of(this.pos);
        }

        @Override
        public Optional<ItemStack> getItem() {
            org.bukkit.inventory.ItemStack stack = BLiveChestInventory.this.chest.getBlockInventory().getItem(this.pos);
            if (stack == null) {
                return Optional.empty();
            }
            return Optional.of(new BLiveItemStack(stack));
        }

        @Override
        public Slot setItem(ItemStack stack) {
            if (stack == null) {
                BLiveChestInventory.this.chest.getSnapshotInventory().setItem(this.pos, null);
                BLiveChestInventory.this.chest.update();
                return this;
            }
            org.bukkit.inventory.ItemStack item = ((BAbstractItemStack) stack).getBukkitItem();
            BLiveChestInventory.this.chest.getSnapshotInventory().setItem(this.pos, item);
            BLiveChestInventory.this.chest.update();
            return this;
        }
    }

    protected final org.bukkit.block.Chest chest;

    public BLiveChestInventory(org.bukkit.block.Chest chest) {
        this.chest = chest;
    }

    @Override
    public SyncBlockPosition getPosition() {
        return new BBlockPosition(this.chest.getBlock());
    }

    @Override
    public Stream<Slot> getItemSlots() {
        Inventory inventory = this.chest.getSnapshotInventory();
        return IntStream.range(0, inventory.getSize()).mapToObj(ChestSlot::new);
    }

    @Override
    public Optional<Slot> getSlot(int slotPos) {
        Inventory inventory = this.chest.getSnapshotInventory();
        if (inventory.getSize() >= slotPos) {
            return Optional.empty();
        }
        return Optional.of(new ChestSlot(slotPos));
    }

    @Override
    public ChestInventorySnapshot createSnapshot() {
        return new BChestInventorySnapshot(this);
    }
}
