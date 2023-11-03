package org.core.implementation.folia.event;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.core.TranslateCore;
import org.core.entity.LiveEntity;
import org.core.event.EventPriority;
import org.core.implementation.folia.event.events.entity.FEntityMoveEvent;
import org.core.implementation.folia.platform.BukkitPlatform;
import org.core.implementation.folia.world.position.block.details.blocks.BBlockDetails;
import org.core.implementation.folia.world.position.impl.sync.BBlockPosition;
import org.core.implementation.folia.event.events.block.PBlockChangeEvent;
import org.core.implementation.folia.world.position.impl.sync.BExactPosition;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;

public class PaperListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockDestroyEvent event) {
        SyncBlockPosition position = new BBlockPosition(event.getBlock());
        BlockDetails details = new BBlockDetails(event.getNewState(), false);
        PBlockChangeEvent.BlockBreakEvent event2 = new PBlockChangeEvent.BlockBreakEvent(position,
                                                                                         position.getBlockDetails(),
                                                                                         details);
        BukkitListener.call(EventPriority.IGNORE, event2);
        event.setCancelled(event2.isCancelled());
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        if (event.getEntity() instanceof Player) {
            return;
        }
        SyncExactPosition before = new BExactPosition(event.getFrom());
        SyncExactPosition after = new BExactPosition(event.getTo());
        LiveEntity entity = ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(event.getEntity());

        FEntityMoveEvent<?> coreEvent = new FEntityMoveEvent<>(before, after, entity);
        BukkitListener.call(EventPriority.IGNORE, coreEvent);
        event.setCancelled(coreEvent.isCancelled());
    }
}
