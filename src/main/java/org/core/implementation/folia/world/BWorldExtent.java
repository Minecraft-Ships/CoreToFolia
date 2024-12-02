package org.core.implementation.folia.world;

import org.bukkit.Chunk;
import org.core.TranslateCore;
import org.core.entity.LiveEntity;
import org.core.implementation.folia.platform.BukkitPlatform;
import org.core.implementation.folia.world.position.impl.async.BAsyncBlockPosition;
import org.core.implementation.folia.world.position.impl.async.BAsyncExactPosition;
import org.core.implementation.folia.world.position.impl.sync.BBlockPosition;
import org.core.implementation.folia.world.position.impl.sync.BExactPosition;
import org.core.vector.type.Vector3;
import org.core.world.ChunkExtent;
import org.core.world.Extent;
import org.core.world.WorldExtent;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.async.ASyncExactPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class BWorldExtent implements WorldExtent {

    protected final org.bukkit.World world;

    public BWorldExtent(org.bukkit.World world) {
        this.world = world;
    }

    public org.bukkit.World getBukkitWorld() {
        return this.world;
    }

    @Override
    public SyncExactPosition getPosition(double x, double y, double z) {
        return new BExactPosition(x, y, z, this.world);
    }

    @Override
    @Deprecated(forRemoval = true)
    public ASyncExactPosition getAsyncPosition(double x, double y, double z) {
        return new BAsyncExactPosition(this.world, x, y, z);
    }

    @Override
    public SyncBlockPosition getPosition(int x, int y, int z) {
        return new BBlockPosition(x, y, z, this.world);
    }

    @Override
    @Deprecated(forRemoval = true)
    public ASyncBlockPosition getAsyncPosition(int x, int y, int z) {
        return new BAsyncBlockPosition(this.world, x, y, z);
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public Stream<LiveEntity> getLiveEntities() {
        return this.world
                .getEntities()
                .stream()
                .map(entity -> ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(entity))
                .filter(Objects::nonNull)
                .distinct();
    }

    @Override
    public Stream<LiveTileEntity> getLiveTileEntities() {
        return this.getChunkExtents().flatMap(Extent::getLiveTileEntities);
    }

    @Override
    public String getName() {
        return this.world.getName();
    }

    @Override
    public UUID getUniqueId() {
        return this.world.getUID();
    }

    @Override
    public String getPlatformUniqueId() {
        return this.getName();
    }

    @Override
    public Stream<ChunkExtent> getChunkExtents() {
        return Stream.of(this.world.getLoadedChunks()).map(BChunkExtent::new);
    }

    @Override
    public @NotNull Optional<ChunkExtent> getChunk(Vector3<Integer> vector) {
        Chunk chunk = this.world.getChunkAt(vector.getX(), vector.getZ());
        if (!chunk.isLoaded()) {
            return Optional.empty();
        }
        return Optional.of(new BChunkExtent(chunk));
    }

    @Override
    public @NotNull CompletableFuture<ChunkExtent> loadChunkAsynced(Vector3<Integer> vector) {
        try {
            CompletableFuture<Chunk> future = (CompletableFuture<Chunk>) this.world
                    .getClass()
                    .getMethod("getChunkAtAsync", int.class, int.class)
                    .invoke(this.world, vector.getX(), vector.getZ());
            return future.thenApply(BChunkExtent::new);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            ChunkExtent extent = this.loadChunk(vector);
            return CompletableFuture.completedFuture(extent);
        }
    }

    @Override
    public @NotNull ChunkExtent loadChunk(Vector3<Integer> vector) {
        this.world.loadChunk(vector.getX(), vector.getZ());
        Chunk chunk = this.world.getChunkAt(vector.getX(), vector.getZ());
        return new BChunkExtent(chunk);
    }

    @Override
    public int getMinimumBlockHeight() {
        return this.world.getMinHeight();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WorldExtent)) {
            return false;
        }
        return ((WorldExtent) obj).getName().equals(this.getName());
    }
}
