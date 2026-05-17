package org.core.implementation.folia.world.position.block.entity.container.furnace;

import org.core.implementation.folia.inventory.inventories.snapshot.block.BFurnaceInventorySnapshot;
import org.core.implementation.folia.world.position.block.entity.AbstractTileEntitySnapshot;
import org.core.implementation.folia.world.position.block.entity.CommonTileEntity;
import org.core.inventory.inventories.snapshots.block.FurnaceInventorySnapshot;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.container.furnace.FurnaceTileEntity;
import org.core.world.position.block.entity.container.furnace.FurnaceTileEntitySnapshot;
import org.core.world.position.block.entity.container.furnace.LiveFurnaceTileEntity;

import java.util.Collection;
import java.util.Collections;

public class BFurnaceEntitySnapshot extends AbstractTileEntitySnapshot<LiveFurnaceTileEntity> implements FurnaceTileEntitySnapshot {

    protected final FurnaceInventorySnapshot inventory;

    public BFurnaceEntitySnapshot(@SuppressWarnings("TypeMayBeWeakened") FurnaceTileEntity fte) {
        super(((CommonTileEntity)fte).bukkitState());
        this.inventory = fte.getInventory().createSnapshot();
    }

    @Override
    public LiveFurnaceTileEntity apply(LiveFurnaceTileEntity lfte) {
        this.inventory.apply(lfte.getInventory());
        return lfte;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return Collections.singletonList(BlockTypes.FURNACE);
    }

    @Override
    public FurnaceInventorySnapshot getInventory() {
        return this.inventory;
    }

    @Override
    public FurnaceTileEntitySnapshot getSnapshot() {
        return new BFurnaceEntitySnapshot(this);
    }
}

