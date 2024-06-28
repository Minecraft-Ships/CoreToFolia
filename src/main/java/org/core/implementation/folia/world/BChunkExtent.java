package org.core.implementation.folia.world;

import org.bukkit.Chunk;
import org.core.TranslateCore;
import org.core.entity.LiveEntity;
import org.core.implementation.folia.platform.BukkitPlatform;
import org.core.implementation.folia.world.chunk.BChunkAsync;
import org.core.implementation.folia.world.position.impl.async.BAsyncBlockPosition;
import org.core.implementation.folia.world.position.impl.async.BAsyncExactPosition;
import org.core.implementation.folia.world.position.impl.sync.BBlockPosition;
import org.core.implementation.folia.world.position.impl.sync.BExactPosition;
import org.core.vector.type.Vector3;
import org.core.world.ChunkExtent;
import org.core.world.WorldExtent;
import org.core.world.chunk.AsyncChunk;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.async.ASyncExactPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class BChunkExtent implements ChunkExtent {

    private final Chunk chunk;

    public BChunkExtent(Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public SyncExactPosition getPosition(double x, double y, double z) {
        return new BExactPosition(x, y, z, this.chunk.getWorld());
    }

    @Override
    @Deprecated(forRemoval = true)
    public ASyncExactPosition getAsyncPosition(double x, double y, double z) {
        return new BAsyncExactPosition(this.chunk.getWorld(), x, y, z);
    }

    @Override
    public SyncBlockPosition getPosition(int x, int y, int z) {
        return new BBlockPosition(x, y, z, this.chunk.getWorld());
    }

    @Override
    @Deprecated(forRemoval = true)
    public ASyncBlockPosition getAsyncPosition(int x, int y, int z) {
        return new BAsyncBlockPosition(this.chunk.getWorld(), x, y, z);
    }

    @Override
    public boolean isLoaded() {
        return this.chunk.isLoaded();
    }

    @Override
    public Stream<LiveEntity> getLiveEntities() {
        return Arrays
                .stream(this.chunk.getEntities())
                .map(entity -> ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(entity));
    }

    @Override
    public Stream<LiveTileEntity> getLiveTileEntities() {
        return Arrays
                .stream(this.chunk.getTileEntities())
                .map(entity -> ((BukkitPlatform) TranslateCore.getPlatform())
                        .createTileEntityInstance(entity)
                        .orElse(null))
                .filter(Objects::nonNull);
    }

    @Override
    public Vector3<Integer> getChunkPosition() {
        return Vector3.valueOf(this.chunk.getX(), 0, this.chunk.getZ());
    }

    @Override
    public Vector3<Integer> getMinimumBlock() {
        int blockX = 16 * chunk.getX();
        int blockZ = 16 * chunk.getZ();
        int blockY = chunk.getWorld().getMinHeight();
        return Vector3.valueOf(blockX, blockY, blockZ);
    }

    @Override
    public Vector3<Integer> getMaximumBlock() {
        int blockX = 16 * (chunk.getX() + 1);
        int blockZ = 16 * (chunk.getZ() + 1);
        int blockY = chunk.getWorld().getMaxHeight();
        return Vector3.valueOf(blockX, blockY, blockZ);
    }

    @Override
    public AsyncChunk createAsync() {
        return new BChunkAsync(chunk.getChunkSnapshot(true, false, false), getMinimumBlock(), getMaximumBlock());
    }

    @Override
    public WorldExtent getWorld() {
        return new BWorldExtent(this.chunk.getWorld());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BChunkExtent chunk)) {
            return false;
        }
        return this.chunk.equals(chunk.chunk);
    }
}
