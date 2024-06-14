package org.core.implementation.folia.world.position.block.entity.skull;

import org.core.entity.living.human.player.User;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.block.entity.skull.LiveSkull;
import org.core.world.position.block.entity.skull.Skull;
import org.core.world.position.block.entity.skull.SkullSnapshot;

import java.util.Optional;
import java.util.stream.Stream;

public class BSkullSnapshot implements SkullSnapshot {

    private User owner;

    public BSkullSnapshot(Skull skull) {
        this.owner = skull.getOwner().orElse(null);
    }

    public BSkullSnapshot(User user) {
        this.owner = user;
    }

    @Override
    public Class<LiveSkull> getDeclaredClass() {
        return LiveSkull.class;
    }

    @Override
    public LiveSkull apply(LiveSkull tileEntity) {
        if (this.owner != null) {
            tileEntity.setOwner(this.owner);
        }
        return tileEntity;
    }

    @Override
    public Stream<BlockType> getApplicableBlocks() {
        return Stream.of(BlockTypes.PLAYER_HEAD, BlockTypes.PLAYER_WALL_HEAD);
    }

    @Override
    public Optional<User> getOwner() {
        return Optional.ofNullable(this.owner);
    }

    @Override
    public Skull setOwner(User user) {
        this.owner = user;
        return this;
    }

    @Override
    public TileEntitySnapshot<? extends TileEntity> getSnapshot() {
        return new BSkullSnapshot(this);
    }
}
