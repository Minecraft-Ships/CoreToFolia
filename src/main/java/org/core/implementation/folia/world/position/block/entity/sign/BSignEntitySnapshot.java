package org.core.implementation.folia.world.position.block.entity.sign;

import net.kyori.adventure.text.Component;
import org.bukkit.block.Sign;
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

    private FSignSideSnapshot front;
    private FSignSideSnapshot back;

    public BSignEntitySnapshot(SignTileEntity entity) {
        this.front = new FSignSideSnapshot(this, true, entity.getFront().getLines());
        this.front.setGlowing(entity.getFront().isGlowing());

        this.back = entity.getBack().map(side -> {
            var newSide = new FSignSideSnapshot(this, false, side.getLines());
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
        apply(lste.getFront());
        lste.getBack().ifPresent(this::apply);
        return lste;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return BlockTypes.OAK_SIGN.getLike();
    }

    private void apply(SignSide side) {
        SignSide to = this.getSide(side.isFront());
        to.setLines(side.getLines());
        to.setGlowing(side.isGlowing());
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
            Sign.class.getClass().getDeclaredMethod("");
            return true;
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
