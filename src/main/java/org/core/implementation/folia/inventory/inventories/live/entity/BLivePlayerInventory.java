package org.core.implementation.folia.inventory.inventories.live.entity;

import org.bukkit.Material;
import org.core.entity.living.human.player.LivePlayer;
import org.core.implementation.folia.entity.living.human.player.live.BLivePlayer;
import org.core.implementation.folia.inventory.inventories.snapshot.entity.BPlayerInventorySnapshot;
import org.core.implementation.folia.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.folia.inventory.item.stack.BLiveItemStack;
import org.core.inventory.inventories.live.entity.LivePlayerInventory;
import org.core.inventory.inventories.snapshots.entity.PlayerInventorySnapshot;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BLivePlayerInventory implements LivePlayerInventory {

    private class PlayerCraftingSlots implements Grid2x2 {

        @Override
        public Stream<Slot> getItemSlots() {
            return IntStream.range(80, 83).mapToObj(PlayerItemSlot::new);
        }
    }

    private class PlayerArmorSlots implements ArmorPart {

        final BLivePlayerInventory.PlayerItemSlot helmetSlot = new BLivePlayerInventory.PlayerItemSlot(103);
        final BLivePlayerInventory.PlayerItemSlot armorSlot = new BLivePlayerInventory.PlayerItemSlot(102);
        final BLivePlayerInventory.PlayerItemSlot leggingsSlot = new BLivePlayerInventory.PlayerItemSlot(101);
        final BLivePlayerInventory.PlayerItemSlot shoeSlot = new BLivePlayerInventory.PlayerItemSlot(100);

        @Override
        public Slot getHelmetSlot() {
            return this.helmetSlot;
        }

        @Override
        public Slot getArmorSlot() {
            return this.armorSlot;
        }

        @Override
        public Slot getLeggingsSlot() {
            return this.leggingsSlot;
        }

        @Override
        public Slot getShoesSlot() {
            return this.shoeSlot;
        }
    }

    private final class PlayerMainInventory implements MainPlayerInventory {

        @Override
        public Stream<Slot> getItemSlots() {
            return IntStream.range(9, 35).mapToObj(PlayerItemSlot::new);
        }
    }

    private final class PlayerHotbar implements Hotbar {

        @Override
        public int getSelectedSlotPos() {
            return BLivePlayerInventory.this.player.getBukkitEntity().getInventory().getHeldItemSlot();
        }

        @Override
        public Stream<Slot> getItemSlots() {
            return IntStream.range(0, 9).mapToObj(PlayerItemSlot::new);
        }
    }

    private final class PlayerItemSlot implements Slot {

        final int position;

        private PlayerItemSlot(int slot) {
            this.position = slot;
        }

        @Override
        public Optional<Integer> getPosition() {
            return Optional.of(this.position);
        }

        @Override
        public Optional<ItemStack> getItem() {
            org.bukkit.inventory.ItemStack stack = BLivePlayerInventory.this.player
                    .getBukkitEntity()
                    .getInventory()
                    .getItem(this.position);
            if (stack == null || stack.getType() == Material.VOID_AIR) {
                return Optional.empty();
            }
            return Optional.of(new BLiveItemStack(stack));
        }

        @Override
        public Slot setItem(ItemStack stack) {
            org.bukkit.inventory.ItemStack bstack = ((BAbstractItemStack) stack).getBukkitItem();
            BLivePlayerInventory.this.player.getBukkitEntity().getInventory().setItem(this.position, bstack);
            return this;
        }

    }

    protected final BLivePlayer player;
    protected final BLivePlayerInventory.PlayerItemSlot offHandSlot;
    protected final BLivePlayerInventory.PlayerArmorSlots armorSlots;
    protected final BLivePlayerInventory.PlayerHotbar hotbar;
    protected final BLivePlayerInventory.PlayerCraftingSlots craftingSlots;
    protected final BLivePlayerInventory.PlayerMainInventory main;

    public BLivePlayerInventory(BLivePlayer player) {
        this.player = player;
        this.hotbar = new BLivePlayerInventory.PlayerHotbar();
        this.armorSlots = new BLivePlayerInventory.PlayerArmorSlots();
        this.offHandSlot = new BLivePlayerInventory.PlayerItemSlot(40);
        this.craftingSlots = new BLivePlayerInventory.PlayerCraftingSlots();
        this.main = new BLivePlayerInventory.PlayerMainInventory();
    }

    @Override
    public ArmorPart getArmor() {
        return this.armorSlots;
    }

    @Override
    public Slot getOffHoldingItem() {
        return this.offHandSlot;
    }

    @Override
    public Optional<LivePlayer> getAttachedEntity() {
        return Optional.of(this.player);
    }

    @Override
    public Hotbar getHotbar() {
        return this.hotbar;
    }

    @Override
    public Grid2x2 getCraftingGrid() {
        return this.craftingSlots;
    }

    @Override
    public MainPlayerInventory getMainInventory() {
        return this.main;
    }

    @Override
    public PlayerInventorySnapshot createSnapshot() {
        return new BPlayerInventorySnapshot(this);
    }

}
