package com.tsotne.fastarenas.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class getitem {
    public static ItemStack getItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getItemWithName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.translateColors(name));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getItemWithNameEnh(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getItemWithNamefShulker(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setUnbreakable(true);
        meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_UNBREAKABLE});
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getItemWithNameandlores(ItemStack item, String name, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.translateColors(name));
        List<String> Lore = (List<String>)lore.stream().map(line -> Color.translateColors(line)).collect(Collectors.toList());
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getItemWithNameandlore(ItemStack item, String name, String lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.translateColors(name));
        String Lore = Color.translateColors(lore);
        meta.setLore(Collections.singletonList(Lore));
        item.setItemMeta(meta);
        return item;
    }
}