package org.core.implementation.folia.entity.scene.live;

import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.core.entity.scene.itemframe.LiveItemFrame;
import org.core.exceptions.DirectionNotSupported;
import org.core.implementation.folia.entity.BLiveEntity;
import org.core.implementation.folia.entity.scene.snapshot.BItemFrameSnapshot;
import org.core.implementation.folia.inventory.inventories.live.entity.BLiveItemFrameSlot;
import org.core.implementation.folia.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.folia.utils.DirectionUtils;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Slot;
import org.core.world.direction.Direction;
import org.core.world.direction.FourFacingDirection;

import java.util.stream.Stream;

public class BLiveItemFrame extends BLiveEntity<ItemFrame> implements LiveItemFrame {

    @Deprecated
    public BLiveItemFrame(Entity entity) {
        this((org.bukkit.entity.ItemFrame) entity);
    }

    public BLiveItemFrame(org.bukkit.entity.ItemFrame entity) {
        super(entity);
    }

    @Override
    public void setItem(ItemStack stack) {
        this.getBukkitEntity().setItem(((BAbstractItemStack) stack).getBukkitItem());
    }

    @Override
    public Direction getItemRotation() {
        Rotation rotation = this.getBukkitEntity().getRotation();
        return switch (rotation) {
            case NONE, FLIPPED -> FourFacingDirection.SOUTH;
            case CLOCKWISE_45, FLIPPED_45 -> FourFacingDirection.EAST;
            case CLOCKWISE, COUNTER_CLOCKWISE -> FourFacingDirection.NORTH;
            case CLOCKWISE_135, COUNTER_CLOCKWISE_45 -> FourFacingDirection.WEST;
        };
    }

    @Override
    public boolean getItemRotationFlip() {
        Rotation rotation = this.getBukkitEntity().getRotation();
        return switch (rotation) {
            case NONE, CLOCKWISE_135, CLOCKWISE, CLOCKWISE_45 -> false;
            case FLIPPED, COUNTER_CLOCKWISE_45, COUNTER_CLOCKWISE, FLIPPED_45 -> true;
        };
    }

    @Override
    public BLiveItemFrame setItemRotation(Direction direction, boolean flip) throws DirectionNotSupported {
        if (Stream.of(this.getDirections()).noneMatch(d -> d.equals(direction))) {
            throw new DirectionNotSupported(direction, "ItemFrame");
        }
        org.bukkit.Rotation rotation = null;
        if (direction.equals(FourFacingDirection.NORTH)) {
            if (flip) {
                rotation = Rotation.FLIPPED;
            } else {
                rotation = Rotation.NONE;
            }
        } else if (direction.equals(FourFacingDirection.EAST)) {
            if (flip) {
                rotation = Rotation.FLIPPED_45;
            } else {
                rotation = Rotation.CLOCKWISE_45;
            }
        } else if (direction.equals(FourFacingDirection.SOUTH)) {
            if (flip) {
                rotation = Rotation.COUNTER_CLOCKWISE;
            } else {
                rotation = Rotation.CLOCKWISE;
            }
        } else if (direction.equals(FourFacingDirection.WEST)) {
            if (flip) {
                rotation = Rotation.COUNTER_CLOCKWISE_45;
            } else {
                rotation = Rotation.CLOCKWISE_135;
            }
        }
        this.getBukkitEntity().setRotation(rotation);
        return this;
    }

    @Override
    public Slot getHoldingItem() {
        return new BLiveItemFrameSlot(this.getBukkitEntity());
    }

    @Override
    public Direction[] getDirections() {
        return FourFacingDirection.getFourFacingDirections();
    }

    @Override
    public Direction getDirection() {
        return DirectionUtils.toDirection(this.getBukkitEntity().getAttachedFace());
    }

    @Override
    public BLiveItemFrame setDirection(Direction direction) throws DirectionNotSupported {
        for (Direction dir : this.getDirections()) {
            if (dir.equals(direction)) {
                throw new DirectionNotSupported(direction, "ItemFrame");
            }
        }
        this.getBukkitEntity().setFacingDirection(DirectionUtils.toFace(direction.getOpposite()));
        return this;
    }

    @Override
    public void remove() {
        this.getBukkitEntity().setItem(null);
        this.getBukkitEntity().remove();
    }

    @Override
    public BItemFrameSnapshot createSnapshot() {
        return new BItemFrameSnapshot(this);
    }
}
