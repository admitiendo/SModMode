package me.sofdev.staff.manager;

import me.sofdev.staff.utils.chat.CC;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class StaffItems {
    public ConfigStrings strings = new ConfigStrings();

    public ItemStack vanish(String status) {
        ItemStack item = new ItemStack(strings.ItemsVanishMaterial);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        meta.setDisplayName(CC.translate(status));

        for (String string : strings.ItemsVanishLore) {
            lore.add(CC.translate(string));
        }

        meta.setLore(lore);

        if (strings.ItemsVanishGlow) {
            meta.addEnchant(Enchantment.OXYGEN, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack randomTeleport() {
        ItemStack item = new ItemStack(strings.ItemsRandomTeleportMaterial);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        meta.setDisplayName(CC.translate(strings.ItemsRandomTeleportName));

        for (String string : strings.ItemsRandomTeleportLore) {
            lore.add(CC.translate(string));
        }

        meta.setLore(lore);

        if (strings.ItemsRandomTeleportGlow) {
            meta.addEnchant(Enchantment.OXYGEN, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack inspect() {
        ItemStack item = new ItemStack(strings.ItemsInventorySeeMaterial);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();

        meta.setDisplayName(CC.translate(strings.ItemsInventorySeeName));

        for (String string : strings.ItemsInventorySeeLore) {
            lore.add(CC.translate(string));
        }

        meta.setLore(lore);

        if (strings.ItemsInventorySeeGlow) {
            meta.addEnchant(Enchantment.OXYGEN, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);
        return item;
    }
}
