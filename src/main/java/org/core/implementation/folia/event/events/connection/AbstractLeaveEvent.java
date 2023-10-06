package org.core.implementation.folia.event.events.connection;

import net.kyori.adventure.text.Component;
import org.core.adventureText.AText;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.connection.ClientConnectionEvent;
import org.jetbrains.annotations.NotNull;

public class AbstractLeaveEvent implements ClientConnectionEvent.Leave {

    protected final LivePlayer player;
    protected Component leaveMessage;

    public AbstractLeaveEvent(LivePlayer player, Component component) {
        this.leaveMessage = component;
        this.player = player;
    }

    @Deprecated(forRemoval = true)
    public AbstractLeaveEvent(LivePlayer player, AText leaveMessage) {
        this.player = player;
        this.setLeavingMessage(leaveMessage);
    }

    @Override
    public LivePlayer getEntity() {
        return this.player;
    }

    @Override
    public @NotNull Component getLeaveMessage() {
        return this.leaveMessage;
    }

    @Override
    public Leave setLeaveMessage(@NotNull Component message) {
        this.leaveMessage = message;
        return this;
    }
}
