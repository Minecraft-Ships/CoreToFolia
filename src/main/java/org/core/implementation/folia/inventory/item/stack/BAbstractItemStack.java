package org.core.implementation.folia.inventory.item.stack;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;
import org.core.implementation.folia.inventory.item.BItemType;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.item.stack.ItemStackSnapshot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class BAbstractItemStack implements ItemStack {

    protected final org.bukkit.inventory.ItemStack stack;

    public BAbstractItemStack(org.bukkit.inventory.ItemStack stack) {
        this.stack = stack;
    }

    public org.bukkit.inventory.ItemStack getBukkitItem() {
        return this.stack;
    }

    @Override
    public @NotNull ItemType getType() {
        return new BItemType(this.stack.getType());
    }

    @Override
    public int getQuantity() {
        return this.stack.getAmount();
    }

    @Override
    public List<Component> getLore() {
        ItemMeta meta = this.stack.getItemMeta();
        if (meta == null) {
            return Collections.emptyList();
        }
        List<Component> lore = meta.lore();
        if (lore == null) {
            return Collections.emptyList();
        }
        return lore;
    }

    @Override
    public org.core.inventory.item.stack.ItemStack setLore(Collection<? extends Component> lore) {
        ItemMeta meta = this.stack.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(this.stack.getType());
            if (meta == null) {
                throw new IllegalStateException("Could not create ItemMeta for " + this.stack.getType().name());
            }
        }
        meta.lore(new ArrayList<>(lore));
        this.stack.setItemMeta(meta);
        return this;
    }

    @Override
    public ItemStackSnapshot createSnapshot() {
        return new BItemStackSnapshot(this);
    }
}
