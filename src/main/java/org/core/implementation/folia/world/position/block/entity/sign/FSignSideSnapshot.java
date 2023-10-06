package org.core.implementation.folia.world.position.block.entity.sign;

import net.kyori.adventure.text.Component;
import org.core.world.position.block.entity.sign.SignSide;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FSignSideSnapshot implements SignSide {

    private final BSignEntitySnapshot snapshot;
    private final boolean isFront;
    private final List<Component> lines;
    private boolean isGlowing;

    public FSignSideSnapshot(BSignEntitySnapshot snapshot, boolean isFront, Component... lines) {
        this(snapshot, isFront, Arrays.asList(lines));
    }

    public FSignSideSnapshot(BSignEntitySnapshot snapshot, boolean isFront, Collection<Component> lines) {
        this.lines = new LinkedList<>(lines);
        this.isFront = isFront;
        this.snapshot = snapshot;
    }

    @Override
    public BSignEntitySnapshot getSign() {
        return this.snapshot;
    }

    @Override
    public List<Component> getLines() {
        return this.lines;
    }

    @Override
    public SignSide setLines(Collection<Component> componentCollection) {
        this.lines.clear();
        this.lines.addAll(componentCollection);
        return this;
    }

    @Override
    public boolean isFront() {
        return this.isFront;
    }

    @Override
    public boolean isGlowing() {
        return this.isGlowing;
    }

    @Override
    public void setGlowing(boolean glowing) {
        this.isGlowing = glowing;
    }
}
