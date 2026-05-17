package org.core.implementation.folia.world.position.block.entity;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.core.exceptions.BlockNotSupported;
import org.core.implementation.folia.world.position.impl.sync.BBlockPosition;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractTileEntitySnapshot<O extends LiveTileEntity>
        implements TileEntitySnapshot<O>, CommonTileEntity {

    protected final TileState state;

    protected AbstractTileEntitySnapshot(@NotNull TileState state) {
        this.state = Objects.requireNonNull(state);
    }

    @Override
    public TileState bukkitState() {
        return this.state;
    }

    @Override
    public O apply(O tileEntity) {
        if (!(tileEntity instanceof AbstractLiveTileEntity blockEntity)) {
            throw new IllegalArgumentException("Must be supported");
        }
        if (!blockEntity.state.getClass().equals(this.state.getClass())) {
            throw new IllegalStateException(
                    this.state.getClass().getSimpleName() + " cannot be applied to " + blockEntity.state
                            .getClass()
                            .getSimpleName());
        }
        BlockState copy = this.state.copy(blockEntity.state.getLocation());
        copy.update();
        return tileEntity;
    }

    @Override
    public O apply(SyncBlockPosition position) throws BlockNotSupported {
        BBlockPosition bukkitPosition = (BBlockPosition) position;
        Location loc = bukkitPosition.toBukkitLocation();
        this.state.copy(loc).update(true);
        O applied = (O) position.getTileEntity().orElseThrow(() -> new RuntimeException("Cannot find tile entity"));
        this.apply(applied);
        return applied;
    }
}
