package org.core.implementation.folia.event.events.connection;

import net.kyori.adventure.text.Component;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.connection.ClientConnectionEvent;

public class BQuitEvent extends AbstractLeaveEvent implements ClientConnectionEvent.Leave.Quit {

    public BQuitEvent(LivePlayer player, Component leaveMessage) {
        super(player, leaveMessage);
    }
}
