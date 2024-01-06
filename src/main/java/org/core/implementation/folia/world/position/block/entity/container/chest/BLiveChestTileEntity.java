package org.core.implementation.folia.world.position.block.entity.container.chest;

import org.bukkit.block.Chest;
import org.core.implementation.folia.inventory.inventories.live.block.BLiveChestInventory;
import org.core.implementation.folia.world.position.block.entity.AbstractLiveTileEntity;
import org.core.inventory.inventories.general.block.ChestInventory;
import org.core.world.position.block.entity.container.chest.ChestTileEntitySnapshot;
import org.core.world.position.block.entity.container.chest.LiveChestTileEntity;

public class BLiveChestTileEntity extends AbstractLiveTileEntity implements LiveChestTileEntity {

    @Deprecated
    public BLiveChestTileEntity(org.bukkit.block.BlockState state) {
        this((org.bukkit.block.Chest) state);
    }

    public BLiveChestTileEntity(@SuppressWarnings("TypeMayBeWeakened") org.bukkit.block.Chest state) {
        super(state);
    }

    public Chest getBukkitTileEntity() {
        return (Chest) this.state;
    }

    @Override
    public ChestInventory getInventory() {
        return new BLiveChestInventory(this.getBukkitTileEntity());
    }

    @Override
    public ChestTileEntitySnapshot getSnapshot() {
        return new BChestTileEntitySnapshot(this);
    }
}
