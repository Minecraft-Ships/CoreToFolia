package org.core.implementation.folia.inventory.inventories.snapshot.block;

import org.core.inventory.inventories.general.block.ChestInventory;
import org.core.inventory.inventories.snapshots.block.ChestInventorySnapshot;
import org.core.inventory.parts.Slot;
import org.core.inventory.parts.snapshot.SlotSnapshot;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BChestInventorySnapshot extends ChestInventorySnapshot {

    protected final Set<SlotSnapshot> slots = new HashSet<>();

    public BChestInventorySnapshot() {

    }

    public BChestInventorySnapshot(ChestInventory inventory) {
        this.position = inventory.getPosition();
        this.slots.addAll(inventory.getItemSlots().map(Slot::createSnapshot).collect(Collectors.toSet()));
    }

    @Override
    public Optional<Slot> getSlot(int slotPos) {
        return this
                .getItemSlots()
                .filter(s -> s.getPosition().isPresent())
                .filter(s -> s.getPosition().get() == slotPos)
                .findAny();
    }

    @Override
    public Stream<Slot> getItemSlots() {
        return this.slots.stream().map(slot -> slot);
    }

    @Override
    public ChestInventorySnapshot createSnapshot() {
        return new BChestInventorySnapshot(this);
    }
}
