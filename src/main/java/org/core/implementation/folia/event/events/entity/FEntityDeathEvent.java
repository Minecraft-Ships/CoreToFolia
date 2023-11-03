package org.core.implementation.folia.event.events.entity;

import org.core.entity.LiveEntity;
import org.core.event.events.entity.EntityDeathEvent;

public class FEntityDeathEvent<E extends LiveEntity> implements EntityDeathEvent<E> {

    private boolean isCancelled;
    private final E entity;

    public FEntityDeathEvent(E entity) {
        this.entity = entity;
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
    public E getEntity() {
        return this.entity;
    }
}
