package org.core.implementation.folia.world.position.block.entity;

import org.bukkit.block.TileState;
import org.core.implementation.folia.world.position.impl.sync.BBlockPosition;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.impl.sync.SyncBlockPosition;

public abstract class AbstractLiveTileEntity implements LiveTileEntity, CommonTileEntity {

    protected final TileState state;

    public AbstractLiveTileEntity(TileState state) {
        this.state = state;
    }

    @Override
    public TileState bukkitState() {
        return this.state;
    }

    @Override
    public SyncBlockPosition getPosition() {
        return new BBlockPosition(this.state.getBlock());
    }


}
