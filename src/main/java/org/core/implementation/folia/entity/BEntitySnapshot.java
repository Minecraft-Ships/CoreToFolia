package org.core.implementation.folia.entity;

import net.kyori.adventure.text.Component;
import org.core.entity.EntitySnapshot;
import org.core.entity.LiveEntity;
import org.core.vector.type.Vector3;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

public abstract class BEntitySnapshot<T extends LiveEntity> implements EntitySnapshot<T> {

    protected final Collection<EntitySnapshot<? extends LiveEntity>> passengers = new HashSet<>();
    protected T createdFrom;
    protected Component customName;
    protected boolean hasGravity;
    protected boolean isCustomNameVisible;
    protected boolean isOnGround;
    protected boolean isRemoved;
    protected double pitch;
    protected SyncExactPosition position;
    protected double roll;
    protected Vector3<Double> velocity;
    protected double yaw;

    public BEntitySnapshot(SyncExactPosition position) {
        this.position = position;
    }

    public BEntitySnapshot(T entity) {
        this.hasGravity = entity.hasGravity();
        this.customName = entity.getCustomNameComponent().orElse(null);
        this.velocity = entity.getVelocity();
        this.yaw = entity.getYaw();
        this.pitch = entity.getPitch();
        this.roll = entity.getRoll();
        this.position = entity.getPosition();
        entity.getPassengers().forEach(e -> this.passengers.add(e.createSnapshot()));
        this.createdFrom = entity;
        this.isOnGround = entity.isOnGround();
    }

    public BEntitySnapshot(EntitySnapshot<T> entity) {
        this.hasGravity = entity.hasGravity();
        this.customName = entity.getCustomNameComponent().orElse(null);
        this.velocity = entity.getVelocity();
        this.yaw = entity.getYaw();
        this.pitch = entity.getPitch();
        this.roll = entity.getRoll();
        this.position = entity.getPosition();
        this.passengers.addAll(entity.getPassengers());
        this.createdFrom = entity.getCreatedFrom().orElse(null);
        this.isOnGround = entity.isOnGround();
    }

    @Override
    public EntitySnapshot<T> addPassengers(Collection<? extends EntitySnapshot<? extends LiveEntity>> entities) {
        this.passengers.addAll(entities);
        return this;
    }

    @Override
    public EntitySnapshot<T> setCustomName(@Nullable Component text) {
        this.customName = text;
        return this;
    }

    @Override
    public Optional<Component> getCustomNameComponent() {
        return Optional.ofNullable(this.customName);
    }

    @Override
    public Collection<EntitySnapshot<? extends LiveEntity>> getPassengers() {
        return this.passengers;
    }

    @Override
    public double getPitch() {
        return this.pitch;
    }

    @Override
    public EntitySnapshot<T> setPitch(double value) {
        this.pitch = value;
        return this;
    }

    @Override
    public SyncExactPosition getPosition() {
        return this.position;
    }

    @Override
    public double getRoll() {
        return this.roll;
    }

    @Override
    public EntitySnapshot<T> setRoll(double value) {
        this.roll = value;
        return this;
    }

    @Override
    public Vector3<Double> getVelocity() {
        return this.velocity;
    }

    @Override
    public EntitySnapshot<T> setVelocity(Vector3<Double> velocity) {
        this.velocity = velocity;
        return this;
    }

    @Override
    public double getYaw() {
        return this.yaw;
    }

    @Override
    public EntitySnapshot<T> setYaw(double value) {
        this.yaw = value;
        return this;
    }

    @Override
    public boolean hasGravity() {
        return this.hasGravity;
    }

    @Override
    public boolean isCustomNameVisible() {
        return this.isCustomNameVisible;
    }

    @Override
    public EntitySnapshot<T> setCustomNameVisible(boolean visible) {
        this.isCustomNameVisible = visible;
        return this;
    }

    @Override
    public boolean isOnGround() {
        return this.isOnGround;
    }

    @Override
    public boolean isRemoved() {
        return this.isRemoved;
    }

    @Override
    public EntitySnapshot<T> removePassengers(Collection<EntitySnapshot<? extends LiveEntity>> entities) {
        this.passengers.removeAll(entities);
        return this;
    }

    @Override
    public EntitySnapshot<T> setGravity(boolean check) {
        this.hasGravity = check;
        return this;
    }

    @Override
    public boolean setPosition(@NotNull Position<? extends Number> position) {
        this.position = position instanceof SyncExactPosition ? (SyncExactPosition) position :
                ((SyncBlockPosition) position).toExactPosition();
        return true;
    }

    protected <L extends LiveEntity> L applyDefaults(L entity) {
        entity.setCustomNameVisible(this.isCustomNameVisible);
        if (this.customName != null) {
            entity.setCustomName(this.customName);
        }
        entity.setGravity(this.hasGravity);
        entity.setVelocity(this.velocity);
        entity.setPosition(this.position);
        entity.setPitch(this.pitch);
        entity.setRoll(this.roll);
        entity.setYaw(this.yaw);
        if (this.isRemoved) {
            entity.remove();
        }
        return entity;
    }

    @Override
    public Optional<T> getCreatedFrom() {
        return Optional.ofNullable(this.createdFrom);
    }
}
