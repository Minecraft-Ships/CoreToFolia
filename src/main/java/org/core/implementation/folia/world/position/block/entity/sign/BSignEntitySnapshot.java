package org.core.implementation.folia.world.position.block.entity.sign;

import net.kyori.adventure.text.Component;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.utils.ComponentUtils;
import org.core.utils.Else;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.sign.LiveSignTileEntity;
import org.core.world.position.block.entity.sign.SignSide;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BSignEntitySnapshot implements SignTileEntitySnapshot {

    private final FSignSideSnapshot front;
    private final FSignSideSnapshot back;

    public BSignEntitySnapshot(SignTileEntity entity) {
        this.front = new FSignSideSnapshot(this, true, entity.getFront().getLines());
        this.front.setGlowing(entity.getFront().isGlowing());

        this.back = entity.getBack().map(side -> {
            FSignSideSnapshot newSide = new FSignSideSnapshot(this, false, side.getLines());
            newSide.setGlowing(side.isGlowing());
            return newSide;
        }).orElseGet(() -> new FSignSideSnapshot(this, false));

    }

    public BSignEntitySnapshot(Component... lines) {
        this(Arrays.asList(lines), Collections.emptyList());
    }

    public BSignEntitySnapshot(Collection<Component> front, Collection<Component> back) {
        this.front = new FSignSideSnapshot(this, true, front);
        this.back = new FSignSideSnapshot(this, false, back);
    }

    @Override
    public LiveSignTileEntity apply(LiveSignTileEntity lste) {
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
        return Else.throwOr(Exception.class, () -> {
            /*Sign.class.getDeclaredMethod("getSide", Class.forName("org.bukkit.block.sign.Side"));
            return true;*/
            return false;
        }, false);
    }

    @Override
    public List<AText> getText() {
        return this.getFront().getLines().stream().map(AdventureText::new).collect(Collectors.toList());
    }

    @Override
    public SignTileEntity setText(Collection<? extends AText> text) {
        List<Component> lines = text.stream().map(t -> {
            if (t instanceof AdventureText adventureText) {
                return adventureText.getComponent();
            }
            return ComponentUtils.fromLegacy(t.toLegacy());
        }).collect(Collectors.toList());
        this.getFront().setLines(lines);
        return this;
    }
}
