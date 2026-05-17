package org.core.implementation.folia.world.position.block.entity.unknown;

import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.core.implementation.folia.world.position.block.entity.AbstractLiveTileEntity;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;

public class BLiveUnknownTileEntity extends AbstractLiveTileEntity {

    public BLiveUnknownTileEntity(@SuppressWarnings("TypeMayBeWeakened") TileState state) {
        super(state);
    }

    @Override
    public TileEntitySnapshot<? extends TileEntity> getSnapshot() {
        return null;
    }
}
