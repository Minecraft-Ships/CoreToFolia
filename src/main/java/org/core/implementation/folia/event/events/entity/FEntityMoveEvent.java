package org.core.implementation.folia.event.events.entity;

import org.core.entity.LiveEntity;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.entity.EntityMoveEvent;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.jetbrains.annotations.NotNull;

public class FEntityMoveEvent<E extends LiveEntity> implements EntityMoveEvent<E> {

    public static class FPlayerMoveEvent extends FEntityMoveEvent<LivePlayer> implements EntityMoveEvent.AsPlayer {

        private final MoveReason reason;

        public FPlayerMoveEvent(SyncExactPosition before,
                                SyncExactPosition after,
                                LivePlayer entity,
                                MoveReason reason) {
            super(before, after, entity);
            this.reason = reason;
        }

        @Override
        public @NotNull MoveReason getReason() {
            return this.reason;
        }
    }

    private final E entity;
    private boolean isCancelled;
    private final SyncExactPosition before;
    private final SyncExactPosition after;

    public FEntityMoveEvent(SyncExactPosition before, SyncExactPosition after, E entity) {
        this.after = after;
        this.before = before;
        this.entity = entity;
    }

    @Override
    public E getEntity() {
        return this.entity;
    }

    @Override
    public @NotNull SyncExactPosition getBeforePosition() {
        return this.before;
    }

    @Override
    public @NotNull SyncExactPosition getAfterPosition() {
        return this.after;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        this.isCancelled = value;
    }
}
