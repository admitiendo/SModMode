package me.sofdev.staff.listener;

import me.sofdev.staff.SModMode;
import me.sofdev.staff.manager.ConfigStrings;
import me.sofdev.staff.manager.StaffItems;
import me.sofdev.staff.manager.StaffManager;
import me.sofdev.staff.utils.chat.CC;
import me.sofdev.staff.utils.item.ItemBuilder;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class StaffListener implements Listener {
    public StaffItems items = new StaffItems();
    FileConfiguration config = SModMode.get().getConfig();
    public StaffManager manager = new StaffManager();
    public ConfigStrings strings = new ConfigStrings();
    public static ArrayList<Player> vanishedPlayers = new ArrayList<>();
    public static ArrayList<UUID> staffMode = new ArrayList<>();
    public static ArrayList<UUID> toAdd = new ArrayList<>();
    public static ArrayList<UUID> staffChat = new ArrayList<>();
    public static Map<UUID, ItemStack[]> inventoryMap = new HashMap<>();
    public static Map<UUID, ItemStack[]> armorMap = new HashMap<>();
    public int[][] borders =
            {{0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 26, 25, 23, 22, 21, 20, 19, 18, 9, 24}
                    , {0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 26, 35, 34, 33, 32, 31, 30, 29, 28, 27, 18, 9}
                    , {0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 26, 35, 44, 43, 42, 41, 40, 39, 38, 37, 36, 27, 18, 9}
                    , {0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 26, 35, 44, 53, 52, 51, 50, 49, 48, 47, 46, 45, 36, 27, 18, 9}};

    private final String l = StringEscapeUtils.unescapeJava("\u25B6");

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission(strings.Permission)) {
            return;
        }

        for (Player vanished : vanishedPlayers) {
            player.hidePlayer(vanished);
        }

        if (toAdd.contains(player.getUniqueId())) {
            manager.disableStaff(player);
            toAdd.remove(player.getUniqueId());
            Bukkit.getScheduler().runTaskLater(SModMode.get(), () -> {
                if (inventoryMap.containsKey(player.getUniqueId())) {
                    player.getInventory().setContents((ItemStack[]) inventoryMap.get(player.getUniqueId()));
                    inventoryMap.remove(player.getUniqueId());
                }

                if (armorMap.containsKey(player.getUniqueId())) {
                    player.getInventory().setArmorContents((ItemStack[]) armorMap.get(player.getUniqueId()));
                    armorMap.remove(player.getUniqueId());
                }
            }, 2);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        for (Player vanished : vanishedPlayers) {
            player.showPlayer(vanished);
        }

        try {
            toAdd.add(player.getUniqueId());
        } catch (Exception ignored){
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        if (!player.hasPermission(strings.Permission)) {
            return;
        }
        if (staffChat.contains(player.getUniqueId())) {
            String format = config.getString("Messages.StaffChat.Format");

            e.setCancelled(true);
            for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                if (players.hasPermission(strings.Permission)) {
                    players.sendMessage(CC.translate(
                            format
                                    .replace("%player%", player.getName())
                                    .replace("%message%", e.getMessage())));
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (staffMode.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (staffMode.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (staffMode.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void inspect(PlayerInteractAtEntityEvent e) {
        if (staffMode.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
        if (e.getRightClicked() instanceof Player && e.getPlayer().hasPermission(strings.Permission)) {
            e.getPlayer().performCommand("invsee " + e.getRightClicked().getName());
        }
    }

    @EventHandler
    public void onInteract(InventoryClickEvent e) {
        if (manager.isInModMode((Player) e.getWhoClicked())) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getInventory().getTitle().equals(CC.translate("&cStaff Online"))) {
                if (!e.getCurrentItem().getItemMeta().getDisplayName().equals(CC.translate("&c&b&e&9&o&l&2"))) {
                    Player clicked = Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName());
                    e.getWhoClicked().teleport(clicked.getLocation().clone().add(0, 1, 0));
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (staffMode.contains((Player) e.getEntity())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            if (staffMode.contains((Player) e.getEntity())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (staffMode.contains(e.getEntity())) {
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();

        if (!player.hasPermission(strings.Permission)) {
            return;
        }

        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            if (manager.isInModMode(player)) {
                if (player.getItemInHand().equals(items.vanish(strings.ItemsVanishStatusEnabled))) {
                    player.performCommand("vanish");
                } else if (player.getItemInHand().equals(items.vanish(strings.ItemsVanishStatusDisabled))) {
                    player.performCommand("vanish");
                } else if (player.getItemInHand().equals(items.randomTeleport())) {
                    player.performCommand("staffrtp");
                } else if (e.getClickedBlock() instanceof Chest) {
                    e.setCancelled(true);
                    player.openInventory(((Chest) e.getClickedBlock()).getInventory());
                }
            }
        }
    }
}
