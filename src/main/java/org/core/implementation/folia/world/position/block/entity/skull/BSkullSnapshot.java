package org.core.implementation.folia.world.position.block.entity.skull;

import org.core.entity.living.human.player.User;
import org.core.implementation.folia.world.position.block.entity.AbstractTileEntitySnapshot;
import org.core.implementation.folia.world.position.block.entity.CommonTileEntity;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.block.entity.skull.LiveSkull;
import org.core.world.position.block.entity.skull.Skull;
import org.core.world.position.block.entity.skull.SkullSnapshot;
import org.core.world.position.block.grouptype.versions.BlockGroups1V13;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public class BSkullSnapshot extends AbstractTileEntitySnapshot<LiveSkull> implements SkullSnapshot {

    private User owner;

    public BSkullSnapshot(Skull skull) {
        super(((CommonTileEntity)skull).bukkitState());
        this.owner = skull.getOwner().orElse(null);
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
    public Collection<BlockType> getSupportedBlocks() {
        Collection<BlockType> blocks = new HashSet<>();
        blocks.addAll(Arrays.asList(BlockGroups1V13.STANDARD_HEAD.getGrouped()));
        blocks.addAll(Arrays.asList(BlockGroups1V13.WALL_HEAD.getGrouped()));
        return blocks;
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
