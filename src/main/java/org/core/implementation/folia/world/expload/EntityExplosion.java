package org.core.implementation.folia.world.expload;

import org.core.entity.Entity;
import org.core.world.expload.Explosion;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

public class EntityExplosion implements Explosion.EntityExplosion {

    protected final Entity<?> source;
    protected final Collection<SyncBlockPosition> affected;

    public EntityExplosion(Entity<?> entity, Collection<SyncBlockPosition> affected) {
        this.source = entity;
        this.affected = affected;
    }

    @Override
    public Stream<SyncBlockPosition> getAffectedBlockPositions() {
        return this.affected.stream();
    }

    @Override
    public Entity<?> getSource() {
        return this.source;
    }
}
