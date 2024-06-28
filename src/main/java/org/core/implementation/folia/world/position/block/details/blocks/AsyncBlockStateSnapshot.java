package org.core.implementation.folia.world.position.block.details.blocks;

import org.bukkit.block.data.BlockData;
import org.core.implementation.folia.world.position.impl.async.BAsyncBlockPosition;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.jetbrains.annotations.NotNull;

@Deprecated(forRemoval = true)
public class AsyncBlockStateSnapshot extends BBlockDetails implements BlockSnapshot.AsyncBlockSnapshot {

    protected final ASyncBlockPosition position;

    public AsyncBlockStateSnapshot(@NotNull BlockStateSnapshot snapshot) {
        super(snapshot.getBukkitData(), snapshot.get(BTileEntityKeyedData.TILED_ENTITY).orElse(null), true);
        this.position = snapshot.getPosition().toAsyncPosition();
    }

    public AsyncBlockStateSnapshot(@NotNull BAsyncBlockPosition position) {
        super(position);
        this.position = position;
    }

    public AsyncBlockStateSnapshot(@NotNull ASyncBlockPosition position, @NotNull BlockData data) {
        super(data, true);
        this.position = position;
    }

    public AsyncBlockStateSnapshot(@NotNull BBlockDetails details, @NotNull ASyncBlockPosition position) {
        super(details);
        this.position = position;
    }

    @Override
    public ASyncBlockPosition getPosition() {
        return this.position;
    }

    @Override
    public AsyncBlockStateSnapshot createCopyOf() {
        return new AsyncBlockStateSnapshot(this, this.position);
    }
}
