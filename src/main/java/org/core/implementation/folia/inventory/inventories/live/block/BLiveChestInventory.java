package org.core.implementation.folia.inventory.inventories.live.block;

import org.core.implementation.folia.inventory.inventories.snapshot.block.BChestInventorySnapshot;
import org.core.implementation.folia.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.folia.inventory.item.stack.BLiveItemStack;
import org.core.implementation.folia.world.position.impl.sync.BBlockPosition;
import org.core.inventory.inventories.live.block.LiveChestInventory;
import org.core.inventory.inventories.snapshots.block.ChestInventorySnapshot;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Slot;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    protected final Set<Slot> slots = new HashSet<>();

    public BLiveChestInventory(org.bukkit.block.Chest chest) {
        this.chest = chest;
        for (int index = 0; index < this.chest.getSnapshotInventory().getSize(); index++) {
            this.slots.add(new BLiveChestInventory.ChestSlot(index));
        }
    }

    @Override
    public SyncBlockPosition getPosition() {
        return new BBlockPosition(this.chest.getBlock());
    }

    @Override
    public Set<Slot> getSlots() {
        return this.slots;
    }

    @Override
    public Optional<Slot> getSlot(int slotPos) {
        return this.slots
                .stream()
                .filter(s -> s.getPosition().isPresent())
                .filter(s -> s.getPosition().get() == slotPos)
                .findAny();
    }

    @Override
    public ChestInventorySnapshot createSnapshot() {
        return new BChestInventorySnapshot(this);
    }
}
