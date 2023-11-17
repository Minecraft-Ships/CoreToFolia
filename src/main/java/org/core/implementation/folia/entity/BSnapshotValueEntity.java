package org.core.implementation.folia.entity;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.core.TranslateCore;
import org.core.entity.Entity;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.implementation.folia.platform.BukkitPlatform;
import org.core.implementation.folia.world.BWorldExtent;
import org.core.implementation.folia.world.position.impl.sync.BExactPosition;
import org.core.utils.ComponentUtils;
import org.core.utils.Else;
import org.core.utils.entry.AbstractSnapshotValue;
import org.core.vector.type.Vector3;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BSnapshotValueEntity<BE extends org.bukkit.entity.Entity, E extends BLiveEntity<BE>>
        implements EntitySnapshot<E> {

    private final EntityType<E, ? extends EntitySnapshot<E>> type;
    private final E createdFrom;
    private final @Nullable byte[] persistentData;
    private Set<EntitySnapshotValue<? super BE, ?>> snapshotValues = new HashSet<>();

    public BSnapshotValueEntity(E entity) {
        BE bukkitEntity = entity.getBukkitEntity();
        this.snapshotValues = EntitySnapshotValue.getSnapshotValues(bukkitEntity);
        this.type = entity.getType();
        this.createdFrom = entity;
        this.snapshotValues.forEach(sv -> sv.storeValue(bukkitEntity));
        this.persistentData = Else.throwOr(IOException.class, () -> entity
                .getBukkitEntity()
                .getPersistentDataContainer()
                .serializeToBytes(), null);
    }

    public BSnapshotValueEntity(BSnapshotValueEntity<BE, E> entity) {
        this.snapshotValues.addAll(entity.snapshotValues);
        this.type = entity.getType();
        this.createdFrom = entity.createdFrom;
        this.persistentData = entity.persistentData;
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> addPassengers(Collection<? extends EntitySnapshot<?
            extends LiveEntity>> entities) {
        return this;
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setCustomName(@Nullable Component text) {
        if (text == null) {
            this.<String>getSnapshotValue("CUSTOM_NAME").get().setValue(null);
            return this;
        }
        this.<String>getSnapshotValue("CUSTOM_NAME").get().setValue(ComponentUtils.toGson(text));
        return this;
    }

    @Override
    public Optional<Component> getCustomNameComponent() {
        Optional<EntitySnapshotValue<?, String>> opText = this.getSnapshotValue("CUSTOM_NAME");
        return opText.map(stringEntitySnapshotValue -> ComponentUtils.fromGson(stringEntitySnapshotValue.getValue()));
    }

    @Override
    public Collection<EntitySnapshot<? extends LiveEntity>> getPassengers() {
        return new HashSet<>();
    }

    @Override
    public double getPitch() {
        return this.<Location>getSnapshotValue("LOCATION").get().getValue().getPitch();
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setPitch(double value) {
        Location loc = this.<Location>getSnapshotValue("LOCATION").get().getValue();
        loc.setPitch((float) value);
        return this;
    }

    @Override
    public SyncExactPosition getPosition() {
        return new BExactPosition(this.<Location>getSnapshotValue("LOCATION").get().getValue());
    }

    @Override
    public double getRoll() {
        return 0;
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setRoll(double value) {
        return this;
    }

    @Override
    public EntityType<E, ? extends EntitySnapshot<E>> getType() {
        return this.type;
    }

    @Override
    public Vector3<Double> getVelocity() {
        Vector vector = this.<Vector>getSnapshotValue("VELOCITY").get().getValue();
        return Vector3.valueOf(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setVelocity(Vector3<Double> velocity) {
        this
                .<Vector>getSnapshotValue("VELOCITY")
                .get()
                .setValue(new Vector(velocity.getX(), velocity.getY(), velocity.getZ()));
        return this;
    }

    @Override
    public double getYaw() {
        return this.<Location>getSnapshotValue("LOCATION").get().getValue().getYaw();
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setYaw(double value) {
        Location loc = this.<Location>getSnapshotValue("LOCATION").get().getValue();
        loc.setYaw((float) value);
        return this;
    }

    @Override
    public boolean hasGravity() {
        return this.<Boolean>getSnapshotValue("GRAVITY").get().getValue();
    }

    @Override
    public boolean isCustomNameVisible() {
        return this.<Boolean>getSnapshotValue("CUSTOM_NAME_VISIBLE").get().getValue();
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setCustomNameVisible(boolean visible) {
        this.<Boolean>getSnapshotValue("CUSTOM_NAME_VISIBLE").get().setValue(visible);
        return this;
    }

    @Override
    public boolean isOnGround() {
        return this.<Boolean>getSnapshotValue("IS_ON_GROUND").get().getValue();
    }

    @Override
    public boolean isRemoved() {
        return this.<Boolean>getSnapshotValue("IS_REMOVED").map(AbstractSnapshotValue::getValue).orElse(false);
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> removePassengers(Collection<EntitySnapshot<?
            extends LiveEntity>> entities) {
        return this;
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setGravity(boolean check) {
        this.<Boolean>getSnapshotValue("GRAVITY").get().setValue(check);
        return this;
    }

    @Override
    public boolean setPosition(Position<? extends Number> position) {
        EntitySnapshotValue<?, Location> locValue = this.<Location>getSnapshotValue("LOCATION").get();
        Location oldLoc = locValue.getValue();
        Location loc = new Location(((BWorldExtent) position.getWorld()).getBukkitWorld(),
                                    position.getX().doubleValue(), position.getY().doubleValue(),
                                    position.getZ().doubleValue());
        loc.setPitch(oldLoc.getPitch());
        loc.setYaw(oldLoc.getYaw());
        locValue.setValue(loc);
        return true;
    }

    private <V> Optional<EntitySnapshotValue<?, V>> getSnapshotValue(String id) {
        return this.snapshotValues
                .parallelStream()
                .filter(v -> v.getId().equals(id))
                .findAny()
                .map(v -> (EntitySnapshotValue<?, V>) v);
    }

    @Override
    public E spawnEntity() {
        Location loc = this.<Location>getSnapshotValue("LOCATION").get().getValue();
        BE entity = (BE) loc.getWorld().spawnEntity(loc, ((BEntityType<?, ?>) this.type).getBukkitEntityType());
        if (this.persistentData != null) {
            try {
                entity.getPersistentDataContainer().readFromBytes(this.persistentData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.snapshotValues.stream().map(sv -> (EntitySnapshotValue<BE, ?>) sv).forEach(sv -> sv.applyValue(entity));
        return (E) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(entity);
    }

    @Override
    public EntitySnapshot<E> createSnapshot() {
        return new BSnapshotValueEntity<>(this);
    }

    @Override
    public Optional<E> getCreatedFrom() {
        return Optional.ofNullable(this.createdFrom);
    }
}
