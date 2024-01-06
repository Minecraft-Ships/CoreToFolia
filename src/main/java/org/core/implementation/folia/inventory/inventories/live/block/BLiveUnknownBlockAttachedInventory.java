package org.core.implementation.folia.inventory.inventories.live.block;

import org.core.implementation.folia.inventory.inventories.snapshot.block.BUnknownBlockAttachedInventorySnapshot;
import org.core.implementation.folia.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.folia.inventory.item.stack.BLiveItemStack;
import org.core.implementation.folia.world.position.block.BBlockType;
import org.core.implementation.folia.world.position.impl.sync.BBlockPosition;
import org.core.inventory.inventories.live.block.LiveUnknownBlockAttachedInventory;
import org.core.inventory.inventories.snapshots.block.UnknownBlockAttachedInventorySnapshot;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Slot;
import org.core.world.position.block.BlockType;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BLiveUnknownBlockAttachedInventory implements LiveUnknownBlockAttachedInventory {

    public class UnknownSlot implements Slot {

        protected final int position;

        public UnknownSlot(int position) {
            this.position = position;
        }

        @Override
        public Optional<Integer> getPosition() {
            return Optional.of(this.position);
        }

        @Override
        public Optional<ItemStack> getItem() {
            org.bukkit.inventory.ItemStack is = BLiveUnknownBlockAttachedInventory.this.state
                    .getInventory()
                    .getItem(this.position);
            if (is == null) {
                return Optional.empty();
            }
            ItemStack isC = new BLiveItemStack(is);
            return Optional.of(isC);
        }

        @Override
        public Slot setItem(ItemStack stack) {
            org.bukkit.block.Container container = BLiveUnknownBlockAttachedInventory.this.state;
            org.bukkit.inventory.ItemStack is = stack == null ? null : ((BAbstractItemStack) stack).getBukkitItem();
            container.getSnapshotInventory().setItem(this.position, is);
            container.update();
            return this;
        }
    }

    protected final org.bukkit.block.Container state;
    protected final Set<Slot> slots = new HashSet<>();

    public BLiveUnknownBlockAttachedInventory(org.bukkit.block.Container state) {
        this.state = state;
        for (int index = 0; index < state.getInventory().getSize(); index++) {
            this.slots.add(new UnknownSlot(index));
        }
    }

    @Override
    public Set<Slot> getSlots() {
        return new HashSet<>(this.slots);
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
    public UnknownBlockAttachedInventorySnapshot createSnapshot() {
        return new BUnknownBlockAttachedInventorySnapshot(this);
    }

    @Override
    public BlockType[] getAllowedBlockType() {
        return new BlockType[]{new BBlockType(this.state.getType())};
    }

    @Override
    public SyncBlockPosition getPosition() {
        return new BBlockPosition(this.state.getBlock());
    }
}
