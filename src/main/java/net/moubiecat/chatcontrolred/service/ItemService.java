package net.moubiecat.chatcontrolred.service;

import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public final class ItemService {
    public static @NotNull ItemFactory build(@NotNull Material material) {
        return new ItemFactory(material);
    }

    public static @NotNull ItemFactory edit(@NotNull ItemStack itemStack) {
        return new ItemFactory(itemStack);
    }

    public static final class ItemFactory {
        private final ItemStack builder;

        public ItemFactory(@NotNull Material material) {
            this(new ItemStack(material));
        }

        public ItemFactory(@NotNull ItemStack itemStack) {
            this.builder = itemStack;
        }

        @NotNull
        public Optional<ItemStack> build() {
            return Optional.of(this.builder);
        }

        @NotNull
        public ItemFactory type(@NotNull Material material) {
            this.builder.setType(material);
            return this;
        }

        @NotNull
        public ItemFactory amount(int amount) {
            this.builder.setAmount(amount);
            return this;
        }

        @NotNull
        public ItemFactory name(@NotNull String name) {
            final ItemMeta itemMeta = this.builder.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
                this.builder.setItemMeta(itemMeta);
            }
            return this;
        }

        @NotNull
        public ItemFactory lore(@NotNull String... lore) {
            final ItemMeta itemMeta = this.builder.getItemMeta();
            if (itemMeta != null) {
                final List<String> list = Optional.ofNullable(itemMeta.getLore())
                        .orElseGet(ArrayList::new);
                list.addAll(Arrays.stream(lore)
                        .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                        .toList());
                itemMeta.setLore(list);
                this.builder.setItemMeta(itemMeta);
            }
            return this;
        }

        @NotNull
        public ItemFactory lore(@NotNull List<String> lore) {
            final ItemMeta itemMeta = this.builder.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setLore(lore.stream()
                        .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                        .collect(Collectors.toList()));
                this.builder.setItemMeta(itemMeta);
            }

            return this;
        }

        @NotNull
        public ItemFactory enchantment(@NotNull Enchantment enchantment, int level) {
            this.builder.addUnsafeEnchantment(enchantment, level);
            return this;
        }

        @NotNull
        public ItemFactory enchantments(@NotNull Map<Enchantment, Integer> enchantments) {
            this.builder.addUnsafeEnchantments(enchantments);
            return this;
        }

        @NotNull
        public ItemFactory localizedName(@NotNull String name) {
            final ItemMeta itemMeta = this.builder.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setLocalizedName(name);
                this.builder.setItemMeta(itemMeta);
            }

            return this;
        }

        @NotNull
        public ItemFactory customModelData(@Nullable Integer data) {
            final ItemMeta itemMeta = this.builder.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setCustomModelData(data);
                this.builder.setItemMeta(itemMeta);
            }

            return this;
        }

        @NotNull
        public ItemFactory flags(@NotNull ItemFlag... flags) {
            final ItemMeta itemMeta = this.builder.getItemMeta();
            if (itemMeta != null) {
                itemMeta.addItemFlags(flags);
                this.builder.setItemMeta(itemMeta);
            }

            return this;
        }

        @NotNull
        public ItemFactory unbreakable(boolean unbreakable) {
            final ItemMeta itemMeta = this.builder.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setUnbreakable(unbreakable);
                this.builder.setItemMeta(itemMeta);
            }

            return this;
        }

        @NotNull
        public ItemFactory attributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
            final ItemMeta itemMeta = this.builder.getItemMeta();
            if (itemMeta != null) {
                itemMeta.addAttributeModifier(attribute, modifier);
                this.builder.setItemMeta(itemMeta);
            }

            return this;
        }

        @NotNull
        public ItemFactory attributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> attributes) {
            final ItemMeta itemMeta = this.builder.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setAttributeModifiers(attributes);
                this.builder.setItemMeta(itemMeta);
            }

            return this;
        }

        @NotNull
        public <T, Z>ItemFactory addPersistentDataContainer(@NotNull NamespacedKey var1, @NotNull PersistentDataType<T, Z> var2, @NotNull Z var3) {
            final ItemMeta itemMeta = this.builder.getItemMeta();
            if (itemMeta != null) {
                itemMeta.getPersistentDataContainer().set(var1, var2, var3);
                this.builder.setItemMeta(itemMeta);
            }

            return this;
        }
    }
}
