package me.sofdev.staff.manager;

import lombok.SneakyThrows;
import me.sofdev.staff.SModMode;
import me.sofdev.staff.utils.chat.CC;
import me.sofdev.staff.utils.chat.titles.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.sofdev.staff.listener.StaffListener.*;

public class StaffManager {
    public FileConfiguration config = SModMode.get().getConfig();
    public StaffItems items = new StaffItems();
    public Map<UUID, GameMode> gameModeMap = new HashMap<UUID, GameMode>();
    public ConfigStrings strings = new ConfigStrings();

    public void addStaff(Player player) {
        staffMode.add(player.getUniqueId());
    }

    public void removeStaff(Player player) {
        staffMode.remove(player.getUniqueId());
    }

    public boolean isInModMode(Player player) {
        return staffMode.contains(player.getUniqueId());
    }

    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player);
    }

    public void enableStaff(Player player) {
        inventoryMap.put(player.getUniqueId(), player.getInventory().getContents());
        armorMap.put(player.getUniqueId(), player.getInventory().getArmorContents());
        Bukkit.getScheduler().scheduleSyncDelayedTask(SModMode.get(), new Runnable() {
            @Override
            public void run() {
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                enableVanish(player);
                addStaff(player);
                gameModeMap.put(player.getUniqueId(), player.getGameMode());
                player.setGameMode(GameMode.CREATIVE);
                player.getInventory().setItem(config.getInt("Items.Vanish.Slot") - 1, items.vanish(strings.ItemsVanishStatusEnabled));
                player.getInventory().setItem(config.getInt("Items.RandomTeleport.Slot") - 1, items.randomTeleport());
                player.getInventory().setItem(config.getInt("Items.InventorySee.Slot") - 1, items.inspect());
                player.updateInventory();
            }
        },  1L);
    }

    public void disableStaff(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        Bukkit.getScheduler().scheduleSyncDelayedTask(SModMode.get(), new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                if (inventoryMap.containsKey(player.getUniqueId())) {
                    player.getInventory().setContents((ItemStack[]) inventoryMap.get(player.getUniqueId()));
                }

                if (armorMap.containsKey(player.getUniqueId())) {
                    player.getInventory().setArmorContents((ItemStack[]) armorMap.get(player.getUniqueId()));
                }
                disableVanish(player);
                removeStaff(player);
                player.updateInventory();
                if (gameModeMap.containsKey(player.getUniqueId())) {
                    player.setGameMode((GameMode) gameModeMap.get(player.getUniqueId()));
                }
            }
        },  1L);
    }

    public void enableVanish(Player player) {
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            players.hidePlayer(player);
        }
        vanishedPlayers.add(player);
        if (strings.MessagesTitles) {
            Title title = new Title
                    (
                            CC.translate(strings.MessagesVanishEnabledTitleNormal),
                            CC.translate(strings.MessagesVanishEnabledTitleSubTitle),
                            40, 40, 40
                    );
            title.send(player);
        } else {
            player.sendMessage(CC.translate(strings.MessagesVanishEnabledMessage));
        }
        player.playSound(player.getLocation(), strings.MessagesVanishEnabledSound, 3, 3);
        ItemMeta meta = items.vanish(strings.ItemsVanishStatusEnabled).getItemMeta();
        meta.setDisplayName(CC.translate(strings.ItemsVanishStatusEnabled));
        Bukkit.getScheduler().runTaskLater(SModMode.get(), () ->
        {
            player.getInventory().getItem(config.getInt("Items.Vanish.Slot") - 1).setItemMeta(meta);

        }, 2);
        player.updateInventory();
    }

    public boolean isOnStaffChat(Player player) {
        return staffChat.contains(player.getUniqueId());
    }

    public boolean isOnStaffChat(UUID uuid) {
        return staffChat.contains(uuid);
    }

    public void modifyVanish(Player player) {
        if (isVanished(player)) {
            disableVanish(player);
        } else {
            enableVanish(player);
        }
    }

    public void disableVanish(Player player) {
        for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            players.showPlayer(player);
        }
        vanishedPlayers.remove(player);
        if (strings.MessagesTitles) {
            Title title = new Title
                    (
                            CC.translate(strings.MessagesVanishDisabledTitleNormal),
                            CC.translate(strings.MessagesVanishDisabledTitleSubTitle),
                            40, 40, 40
                    );
            title.send(player);
        } else {
            player.sendMessage(CC.translate(strings.MessagesVanishDisabledMessage));
        }
        player.playSound(player.getLocation(), strings.MessagesVanishDisabledSound, 3, 3);
        ItemMeta meta = items.vanish(strings.ItemsVanishStatusDisabled).getItemMeta();
        meta.setDisplayName(CC.translate(strings.ItemsVanishStatusDisabled));
        Bukkit.getScheduler().runTaskLater(SModMode.get(), () ->
        {
            try {
                player.getInventory().getItem(config.getInt("Items.Vanish.Slot") - 1).setItemMeta(meta);
            } catch (Exception ignored) {

            }
        }, 2);
        player.updateInventory();
    }

    public String getVanishStatus(Player player) {
        String toReturn;
        if (isVanished(player)) {
            toReturn = ChatColor.GREEN.toString() + "Enabled";
        } else {
            toReturn = ChatColor.RED.toString() + "Disabled";
        }
        return toReturn;
    }
}
