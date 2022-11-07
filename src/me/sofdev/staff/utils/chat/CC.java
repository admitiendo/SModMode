package me.sofdev.staff.utils.chat;

import org.bukkit.*;
import org.bukkit.entity.*;


/**
 * Created by SofDev w/Apreciada
 * 14/06/2022 - 02:52:27
 */

public class CC {
    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void log(String string) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', string));
    }

    public static String normalLine() {
        return ChatColor.translateAlternateColorCodes('&', "&c&m-------------------");
    }
}