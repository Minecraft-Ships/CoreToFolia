package org.core.implementation.folia.world.position.block.details.blocks.data.keyed.attachableworkarounds;

import org.bukkit.Tag;
import org.bukkit.block.data.BlockData;
import org.core.implementation.folia.world.position.block.details.blocks.IBBlockDetails;
import org.core.implementation.folia.world.position.block.details.blocks.data.keyed.BAttachableKeyedData;
import org.core.world.direction.Direction;
import org.core.world.direction.FourFacingDirection;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.blocktypes.post.BlockTypes1V13;
import org.core.world.position.block.grouptype.BlockGroup;
import org.core.world.position.block.grouptype.BlockGroups;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CommonAttachableWorkAround implements BAttachableKeyedData.AttachableBlockWorkAround {

    CARPET(b -> FourFacingDirection.DOWN, e -> {
    }, BlockGroups.CARPET.get().getBlocks().collect(Collectors.toSet())),
    FIRE(b -> FourFacingDirection.DOWN, e -> {
    }, BlockTypes.FIRE),
    REDSTONE(b -> FourFacingDirection.DOWN, e -> {
    }, BlockTypes.REDSTONE_WIRE),
    REPEATER(b -> FourFacingDirection.DOWN, e -> {
    }, BlockTypes1V13.REPEATER),
    SNOW(b -> FourFacingDirection.DOWN, e -> {
    }, BlockTypes.SNOW),
    RAIL(b -> FourFacingDirection.DOWN, e -> {
    }, BlockTypes.RAIL, BlockTypes.ACTIVATOR_RAIL, BlockTypes.DETECTOR_RAIL, BlockTypes1V13.POWERED_RAIL),
    STANDING_TORCH(b -> FourFacingDirection.DOWN, e -> {
    }, BlockTypes.TORCH, BlockTypes.REDSTONE_TORCH),
    LADDER(BAttachableKeyedData.AttachableBlockWorkAround.GET_DIRECTION_FROM_BLOCK_DATA,
           BAttachableKeyedData.AttachableBlockWorkAround.SET_BLOCK_DATA_FROM_DIRECTION, BlockTypes.LADDER),
    LEVER(BAttachableKeyedData.AttachableBlockWorkAround.GET_DIRECTION_FROM_BLOCK_DATA,
          BAttachableKeyedData.AttachableBlockWorkAround.SET_BLOCK_DATA_FROM_DIRECTION, BlockTypes.LEVER),
    WALL_TORCH(BAttachableKeyedData.AttachableBlockWorkAround.GET_DIRECTION_FROM_BLOCK_DATA,
               BAttachableKeyedData.AttachableBlockWorkAround.SET_BLOCK_DATA_FROM_DIRECTION, BlockTypes.WALL_TORCH,
               BlockTypes.REDSTONE_WALL_TORCH),
    BUTTON(BAttachableKeyedData.AttachableBlockWorkAround.GET_DIRECTION_FROM_BLOCK_DATA,
           BAttachableKeyedData.AttachableBlockWorkAround.SET_BLOCK_DATA_FROM_DIRECTION,
           BlockGroups.BUTTONS.get().getBlocks().collect(Collectors.toSet())),
    PISTON(BAttachableKeyedData.AttachableBlockWorkAround.GET_DIRECTION_FROM_BLOCK_DATA,
           BAttachableKeyedData.AttachableBlockWorkAround.SET_BLOCK_DATA_FROM_DIRECTION, BlockTypes.MOVING_PISTON,
           BlockTypes.PISTON_HEAD),
    SIGN(BAttachableKeyedData.AttachableBlockWorkAround.GET_DIRECTION_FROM_BLOCK_DATA,
         BAttachableKeyedData.AttachableBlockWorkAround.SET_BLOCK_DATA_FROM_DIRECTION,
         BlockGroups.SIGNS.get().getBlocks().collect(Collectors.toSet()));

    private final Collection<BlockType> collection;
    private final Function<? super BlockData, ? extends Direction> functionDirection;
    private final Consumer<? super Map.Entry<BlockData, Direction>> consumer;

    CommonAttachableWorkAround(Function<? super BlockData, ? extends Direction> getDir,
                               Consumer<? super Map.Entry<BlockData, Direction>> setDir,
                               BlockType... types) {
        this(getDir, setDir, Arrays.asList(types));
    }

    CommonAttachableWorkAround(Function<? super BlockData, ? extends Direction> getDir,
                               Consumer<? super Map.Entry<BlockData, Direction>> setDir,
                               Collection<BlockType> blockTypes) {
        this.consumer = setDir;
        this.functionDirection = getDir;
        this.collection = blockTypes;
    }

    @Override
    public Collection<BlockType> getTypes() {
        return this.collection;
    }

    @Override
    public Direction getAttachedDirection(BlockData data) {
        return this.functionDirection.apply(data);
    }

    @Override
    public BlockData setAttachedDirection(BlockData data, Direction direction) {
        this.consumer.accept(new AbstractMap.SimpleImmutableEntry<>(data, direction));
        return data;
    }
}
