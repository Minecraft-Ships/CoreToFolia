package org.core.implementation.folia.entity.living.human.player.live;

import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.eco.Currency;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.implementation.folia.VaultService;
import org.core.implementation.folia.entity.BLiveEntity;
import org.core.implementation.folia.entity.living.human.player.snapshot.BPlayerSnapshot;
import org.core.implementation.folia.inventory.inventories.live.entity.BLivePlayerInventory;
import org.core.implementation.folia.world.position.impl.sync.BBlockPosition;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.core.permission.Permission;
import org.core.source.Messageable;
import org.core.world.position.impl.BlockPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class BLivePlayer extends BLiveEntity<Player> implements LivePlayer {

    @Deprecated
    public BLivePlayer(org.bukkit.entity.Entity entity) {
        this((org.bukkit.entity.Player) entity);
    }

    public BLivePlayer(org.bukkit.entity.Player entity) {
        super(entity);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BLivePlayer player2)) {
            return false;
        }
        return player2.getBukkitEntity().equals(this.getBukkitEntity());
    }

    @Override
    public boolean isViewingInventory() {
        return this.getBukkitEntity().getOpenInventory() != null;
    }

    @Override
    public PlayerInventory getInventory() {
        return new BLivePlayerInventory(this);
    }

    @Override
    public int getFoodLevel() {
        return this.getBukkitEntity().getFoodLevel();
    }

    @Override
    public double getExhaustionLevel() {
        return this.getBukkitEntity().getExhaustion();
    }

    @Override
    public LivePlayer setExhaustionLevel(double value) throws IndexOutOfBoundsException {
        this.getBukkitEntity().setExhaustion((float) value);
        return this;
    }

    @Override
    public double getSaturationLevel() {
        return this.getBukkitEntity().getSaturation();
    }

    @Override
    public LivePlayer setSaturationLevel(double value) throws IndexOutOfBoundsException {
        this.getBukkitEntity().setSaturation((float) value);
        return this;
    }

    @Override
    public String getName() {
        return this.getBukkitEntity().getName();
    }

    @Override
    public boolean isSneaking() {
        return this.getBukkitEntity().isSneaking();
    }

    @Override
    public LivePlayer setSneaking(boolean sneaking) {
        this.getBukkitEntity().setSneaking(sneaking);
        return this;
    }

    @Override
    public LivePlayer setFood(int value) throws IndexOutOfBoundsException {
        this.getBukkitEntity().setFoodLevel(value);
        return this;
    }

    @Override
    public UUID getUniqueId() {
        return this.getBukkitEntity().getUniqueId();
    }

    @Override
    public Optional<BlockPosition> getBlockLookingAt(int scanLength) {
        Block block = this.getBukkitEntity().getTargetBlockExact(scanLength);
        if (block == null) {
            return Optional.empty();
        }
        return Optional.of(new BBlockPosition(block));
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return this.getBukkitEntity().hasPermission(permission.getPermissionValue());
    }

    @Override
    public PlayerSnapshot createSnapshot() {
        return new BPlayerSnapshot(this);
    }

    @Override
    public BLivePlayer setGravity(boolean check) {
        this.getBukkitEntity().setGravity(check);
        return this;
    }

    @Override
    public boolean hasGravity() {
        return this.getBukkitEntity().hasGravity();
    }

    @Override
    public LivePlayer sendMessage(AText message, UUID uuid) {
        this.getBukkitEntity().sendMessage(uuid, message.toLegacy());
        return this;
    }

    @Override
    public LivePlayer sendMessage(AText message) {
        Player player = this.getBukkitEntity();
        if (message instanceof AdventureText text) {
            return (LivePlayer) this.sendMessage(text.getComponent());
        }
        player.sendMessage(message.toLegacy());
        return this;
    }

    @Override
    public Messageable sendMessage(@NotNull Component message, @Nullable UUID uuid) {
        Identity identity = uuid == null ? Identity.nil() : Identity.identity(uuid);
        Player player = this.getBukkitEntity();
        player.sendMessage(identity, message);
        return this;
    }

    @Override
    public boolean sudo(String wholeCommand) {
        return Bukkit.dispatchCommand(this.getBukkitEntity(), wholeCommand);
    }

    @Override
    public BigDecimal getBalance(@NotNull Currency currency) {
        return BigDecimal.valueOf(VaultService.getBalance(this.getBukkitEntity()).orElse(0.0));
    }

    @Override
    public void setBalance(@NotNull Currency currency, @NotNull BigDecimal decimal) {
        VaultService.setBalance(this.getBukkitEntity(), decimal);
    }
}
