package org.core.implementation.folia.event.events.connection;

import net.kyori.adventure.text.Component;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.connection.ClientConnectionEvent;

public class BKickEvent extends AbstractLeaveEvent implements ClientConnectionEvent.Leave.Kick {

    public BKickEvent(LivePlayer player, Component leaveMessage) {
        super(player, leaveMessage);
    }
}
