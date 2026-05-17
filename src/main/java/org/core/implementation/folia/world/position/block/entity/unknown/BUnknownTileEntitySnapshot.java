package org.core.implementation.folia.world.position.block.entity.unknown;

import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.core.implementation.folia.world.position.block.BBlockType;
import org.core.implementation.folia.world.position.block.entity.AbstractTileEntitySnapshot;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class BUnknownTileEntitySnapshot extends AbstractTileEntitySnapshot<BLiveUnknownTileEntity> {

    public BUnknownTileEntitySnapshot(@NotNull TileState state) {
        super(state);
    }

    @Override
    public Class<BLiveUnknownTileEntity> getDeclaredClass() {
        return BLiveUnknownTileEntity.class;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return List.of(new BBlockType(this.state.getType()));
    }

    @Override
    public TileEntitySnapshot<? extends TileEntity> getSnapshot() {
        return new BUnknownTileEntitySnapshot((TileState) this.state.copy());
    }
}
