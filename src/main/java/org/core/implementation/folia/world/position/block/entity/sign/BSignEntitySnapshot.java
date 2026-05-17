package org.core.implementation.folia.world.position.block.entity.sign;

import net.kyori.adventure.text.Component;
import org.core.exceptions.BlockNotSupported;
import org.core.implementation.folia.world.position.block.entity.AbstractTileEntitySnapshot;
import org.core.implementation.folia.world.position.block.entity.CommonTileEntity;
import org.core.utils.ComponentUtils;
import org.core.utils.Else;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignSide;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BSignEntitySnapshot extends AbstractTileEntitySnapshot<LiveSignTileEntity> implements SignTileEntitySnapshot {

    private final FSignSideSnapshot front;
    private final FSignSideSnapshot back;

    public BSignEntitySnapshot(SignTileEntity entity) {
        super(((CommonTileEntity)entity).bukkitState());
        this.front = new FSignSideSnapshot(this, true, entity.getFront().getLines());
        this.front.setGlowing(entity.getFront().isGlowing());

        this.back = entity.getBack().map(side -> {
            FSignSideSnapshot newSide = new FSignSideSnapshot(this, false, side.getLines());
            newSide.setGlowing(side.isGlowing());
            return newSide;
        }).orElseGet(() -> new FSignSideSnapshot(this, false));
    }

    @Override
    public LiveSignTileEntity apply(SyncBlockPosition position) throws BlockNotSupported {
        return super.apply(position);
    }

    @Override
    public LiveSignTileEntity apply(LiveSignTileEntity lste) {
        super.apply(lste);
        applyTo(lste.getFront());
        lste.getBack().ifPresent(this::applyTo);
        return lste;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return BlockTypes.OAK_SIGN.getLike();
    }

    private void applyTo(SignSide side) {
        SignSide from = this.getSide(side.isFront());
        side.setLines(from.getLines());
        side.setGlowing(from.isGlowing());
    }

    @Override
    public SignTileEntitySnapshot getSnapshot() {
        return new BSignEntitySnapshot(this);
    }

    @Override
    public SignSide getSide(boolean frontSide) {
        return frontSide ? front : back;
    }

    @Override
    public boolean isMultiSideSupported() {
        return true;
    }
}
