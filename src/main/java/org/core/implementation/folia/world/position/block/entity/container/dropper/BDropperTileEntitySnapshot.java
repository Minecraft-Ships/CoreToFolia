package org.core.implementation.folia.world.position.block.entity.container.dropper;

import org.core.implementation.folia.inventory.inventories.snapshot.block.dispenser.BDropperInventorySnapshot;
import org.core.implementation.folia.world.position.block.entity.AbstractTileEntitySnapshot;
import org.core.implementation.folia.world.position.block.entity.CommonTileEntity;
import org.core.inventory.inventories.general.block.dispenser.DropperInventory;
import org.core.inventory.inventories.snapshots.block.dispenser.DropperInventorySnapshot;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.container.dropper.DropperTileEntity;
import org.core.world.position.block.entity.container.dropper.DropperTileEntitySnapshot;
import org.core.world.position.block.entity.container.dropper.LiveDropperTileEntity;

import java.util.Collection;
import java.util.Collections;

public class BDropperTileEntitySnapshot extends AbstractTileEntitySnapshot<LiveDropperTileEntity> implements DropperTileEntitySnapshot {

    protected final DropperInventorySnapshot inventory;

    public BDropperTileEntitySnapshot(@SuppressWarnings("TypeMayBeWeakened") DropperTileEntity dte) {
        super(((CommonTileEntity)dte).bukkitState());
        this.inventory = dte.getInventory().createSnapshot();
    }

    @Override
    public LiveDropperTileEntity apply(LiveDropperTileEntity ldte) {
        this.inventory.apply(ldte.getInventory());
        return ldte;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return Collections.singletonList(BlockTypes.DROPPER);
    }

    @Override
    public DropperInventory getInventory() {
        return this.inventory;
    }

    @Override
    public DropperTileEntitySnapshot getSnapshot() {
        return new BDropperTileEntitySnapshot(this);
    }
}
