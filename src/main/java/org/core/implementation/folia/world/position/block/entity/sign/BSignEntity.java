package org.core.implementation.folia.world.position.block.entity.sign;

import org.bukkit.block.Sign;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.implementation.folia.world.position.block.entity.AbstractLiveTileEntity;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignSide;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        if (this.isMultiSideSupported()) {
            //TODO
        }
        if (frontSide) {
            return new FLiveLegacySignSide(this.getBukkitSign());
        }
        throw new IllegalStateException("multi-sign is only supported on 1.20+");
    }

    @Override
    public boolean isMultiSideSupported() {
        //TODO
        return false;
    }

    @Override
    @Deprecated
    public List<AText> getText() {
        return this.getBukkitSign().lines().stream().map(AdventureText::new).collect(Collectors.toList());
    }

    @Override
    @Deprecated
    public SignTileEntity setText(Collection<? extends AText> text) {
        int i = 0;
        Sign sign = this.getBukkitSign();
        for (AText line : text) {
            sign.line(i, ((AdventureText) line).getComponent());
            i++;
        }
        sign.update(true, false);
        return this;
    }
}
