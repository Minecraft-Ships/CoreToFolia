package org.core.implementation.folia.world.position.block.entity.sign;

import org.bukkit.block.sign.Side;
import org.core.implementation.folia.world.position.block.entity.AbstractLiveTileEntity;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignSide;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;

public class BSignEntity extends AbstractLiveTileEntity implements LiveSignTileEntity {

    @Deprecated
    public BSignEntity(org.bukkit.block.BlockState state) {
        this((org.bukkit.block.Sign) state);
    }

    public BSignEntity(@SuppressWarnings("TypeMayBeWeakened") org.bukkit.block.Sign state) {
        super(state);
    }

    public org.bukkit.block.Sign getBukkitSign() {
        return (org.bukkit.block.Sign) this.state;
    }

    @Override
    public SignTileEntitySnapshot getSnapshot() {
        return new BSignEntitySnapshot(this);
    }

    @Override
    public SignSide getSide(boolean frontSide) {
        return new FLiveSignSide(this.getBukkitSign(), frontSide ? Side.FRONT : Side.BACK);
    }

    @Override
    public boolean isMultiSideSupported() {
        return true;
    }
}
