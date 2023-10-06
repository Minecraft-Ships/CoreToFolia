package org.core.implementation.folia.event.events.block.tileentity;

import net.kyori.adventure.text.Component;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.block.tileentity.SignChangeEvent;
import org.core.implementation.folia.world.position.block.entity.sign.FSignSideSnapshot;
import org.core.world.position.block.entity.sign.SignSide;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.List;

public class BSignChangeEvent implements SignChangeEvent.ByPlayer {

    protected final FSignSideSnapshot original;
    protected final SyncBlockPosition position;
    protected final LivePlayer player;
    private final FSignSideSnapshot to;
    protected boolean isCancelled;

    public BSignChangeEvent(LivePlayer player,
                            SyncBlockPosition position,
                            FSignSideSnapshot previous,
                            FSignSideSnapshot to) {
        this.position = position;
        this.original = previous;
        this.player = player;
        this.to = to;
    }

    @Override
    public SignTileEntitySnapshot getSign() {
        return this.original.getSign();
    }

    @Override
    public SignSide getPreviousSide() {
        return this.original;
    }

    @Override
    public SignSide getChangingSide() {
        return this.to;
    }

    @Override
    public boolean isEdit() {
        List<Component> lines = this.getPreviousSide().getLines();
        if (lines.isEmpty()) {
            return false;
        }
        return !lines.stream().allMatch(line -> line.equals(Component.empty()));
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        this.isCancelled = value;
    }

    @Override
    public SyncBlockPosition getPosition() {
        return this.position;
    }

    @Override
    public LivePlayer getEntity() {
        return this.player;
    }
}
