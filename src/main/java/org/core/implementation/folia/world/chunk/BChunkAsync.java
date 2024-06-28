package org.core.implementation.folia.world.chunk;

import org.bukkit.ChunkSnapshot;
import org.bukkit.block.data.BlockData;
import org.core.implementation.folia.world.position.block.details.blocks.BBlockDetails;
import org.core.vector.type.Vector3;
import org.core.world.chunk.AsyncChunk;
import org.core.world.position.block.details.BlockDetails;
import org.jetbrains.annotations.NotNull;

public class BChunkAsync implements AsyncChunk {

    private final ChunkSnapshot chunkSnapshot;
    private final Vector3<Integer> min;
    private final Vector3<Integer> max;

    public BChunkAsync(ChunkSnapshot snapshot, Vector3<Integer> min, Vector3<Integer> max) {
        this.chunkSnapshot = snapshot;
        this.min = min;
        this.max = max;
    }


    @Override
    public BlockDetails getDetails(@NotNull Vector3<Integer> vector) {
        if(vector.getX() >= this.max.getX()){
            throw new IllegalArgumentException("Vector was outside chunk bounds on X axis");
        }
        if(vector.getZ() >= this.max.getZ()){
            throw new IllegalArgumentException("Vector was outside chunk bounds on Z axis");
        }
        int blockX = vector.getX() < 16 && vector.getX() >= 0 ? vector.getX() : vector.getX() - this.min.getX();
        int blockY = vector.getY();
        int blockZ = vector.getZ() < 16 && vector.getZ() >= 0 ? vector.getZ() : vector.getZ() - this.min.getZ();
        if(blockZ >= 16){
            throw new IllegalStateException("Z calculation was wrong");
        }
        if(blockX >= 16){
            throw new IllegalStateException("X calculation was wrong");
        }

        BlockData data = this.chunkSnapshot.getBlockData(blockX, blockY, blockZ);
        return new BBlockDetails(data, true);
    }

    @Override
    public Vector3<Integer> getChunkPosition() {
        return Vector3.valueOf(chunkSnapshot.getX(), 1, chunkSnapshot.getZ());
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public Vector3<Integer> getMinimumBlock() {
        return this.min;
    }

    @Override
    public Vector3<Integer> getMaximumBlock() {
        return this.max;
    }

    @Override
    public AsyncChunk createAsync() {
        return this;
    }
}
