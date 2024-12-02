package org.core.implementation.folia.inventory.part.dispenser;

import org.bukkit.inventory.Inventory;
import org.core.implementation.folia.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.folia.inventory.item.stack.BLiveItemStack;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Grid3x3;
import org.core.inventory.parts.Slot;
import org.core.inventory.parts.snapshot.Grid3x3Snapshot;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class DispenserBasedGrid implements Grid3x3 {

    public class DispenserSlot implements Slot {

        private final int position;

        public DispenserSlot(int pos) {
            this.position = pos;
        }

        @Override
        public Optional<Integer> getPosition() {
            return Optional.of(this.position);
        }

        @Override
        public Optional<ItemStack> getItem() {
            org.bukkit.inventory.ItemStack is = DispenserBasedGrid.this
                    .getContainer()
                    .getInventory()
                    .getItem(this.position);
            if (is == null) {
                return Optional.empty();
            }
            ItemStack stack = new BLiveItemStack(is);
            return Optional.of(stack);
        }

        @Override
        public Slot setItem(ItemStack stack) {
            org.bukkit.block.Container container = DispenserBasedGrid.this.getContainer();
            if (stack == null) {
                container.getSnapshotInventory().setItem(this.position, null);
                container.update();
                return this;
            }
            container.getSnapshotInventory().setItem(this.position, ((BAbstractItemStack) stack).getBukkitItem());
            container.update();
            return this;
        }
    }

    public DispenserBasedGrid() {
    }

    protected abstract org.bukkit.block.Container getContainer();

    @Override
    public Stream<Slot> getItemSlots() {
        Inventory inventory = this.getContainer().getSnapshotInventory();
        return IntStream.range(0, inventory.getSize()).mapToObj(DispenserSlot::new);
    }

    @Override
    public Grid3x3Snapshot createSnapshot() {
        return new Grid3x3Snapshot(this);
    }
}
