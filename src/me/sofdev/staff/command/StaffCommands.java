package me.sofdev.staff.command;

import me.sofdev.staff.SModMode;
import me.sofdev.staff.listener.StaffListener;
import me.sofdev.staff.manager.ConfigStrings;
import me.sofdev.staff.manager.StaffItems;
import me.sofdev.staff.manager.StaffManager;
import me.sofdev.staff.utils.chat.CC;
import me.sofdev.staff.utils.chat.titles.Title;
import me.sofdev.staff.utils.commands.Command;
import me.sofdev.staff.utils.commands.CommandArgs;
import me.sofdev.staff.utils.item.ItemBuilder;
import me.sofdev.staff.utils.player.PlayerUtil;
import me.sofdev.staff.utils.player.Skull;
import me.sofdev.staff.utils.time.TimeUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffect;

import java.text.DecimalFormat;
import java.util.*;

import static me.sofdev.staff.listener.StaffListener.staffChat;
import static me.sofdev.staff.listener.StaffListener.staffMode;

public class StaffCommands {
    public StaffItems items = new StaffItems();
    FileConfiguration config = SModMode.get().getConfig();
    public ConfigStrings strings = new ConfigStrings();
    public StaffManager manager = new StaffManager();
    private final String uar = StringEscapeUtils.unescapeJava("\u25B6");
    public int[][] borders =
            {{0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 26, 25, 23, 22, 21, 20, 19, 18, 9}
                    , {0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 26, 35, 34, 33, 32, 31, 30, 29, 28, 27, 18, 9}
                    , {0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 26, 35, 44, 43, 42, 41, 40, 39, 38, 37, 36, 27, 18, 9}
                    , {0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 26, 35, 44, 53, 52, 51, 50, 49, 48, 47, 46, 45, 36, 27, 18, 9}};

    @Command(name = "staff", aliases = {"mod", "staffmode", "modmode"}, inGameOnly = true)
    public void staff(CommandArgs args) {
        Player player = args.getPlayer();
        if (player.hasPermission(strings.Permission)) {
            if (!manager.isInModMode(player)) {
                manager.enableStaff(player);
                if (strings.MessagesTitles) {
                    Title title = new Title
                            (
                                    CC.translate(strings.MessagesStaffModeEnabledTitleNormal),
                                    CC.translate(strings.MessagesStaffModeEnabledTitleSubTitle),
                                    40, 40, 40
                            );
                    title.send(player);
                } else {
                    player.sendMessage(CC.translate(strings.MessagesStaffModeEnabledMessage));
                }
                player.playSound(player.getLocation(), strings.MessagesStaffModeEnabledSound, 3, 3);
            } else {
                manager.disableStaff(player);
                if (strings.MessagesTitles) {
                    Title title = new Title
                            (
                                    CC.translate(strings.MessagesStaffModeDisabledTitleNormal),
                                    CC.translate(strings.MessagesStaffModeDisabledTitleSubTitle),
                                    40, 40, 40
                            );
                    title.send(player);
                } else {
                    player.sendMessage(CC.translate(strings.MessagesStaffModeDisabledMessage));
                }
                player.playSound(player.getLocation(), strings.MessagesStaffModeDisabledSound, 3, 3);
            }
        } else {
            player.sendMessage(CC.translate(strings.NoPermission));
        }
    }

    @Command(name = "vanish", aliases = {"v", "modvanish", "staffvanish"})
    public void vanish(CommandArgs args) {
        Player player = args.getPlayer();
        if (player.hasPermission(strings.Permission)) {
            if (manager.isVanished(player)) {
                manager.disableVanish(player);
            } else {
                manager.enableVanish(player);
            }
        } else {
            player.sendMessage(CC.translate(strings.NoPermission));
        }
    }

    @Command(name = "staffrtp", aliases = {"srtp", "modrtp", "__rtp"})
    public void rtp(CommandArgs args) {
        Player player = args.getPlayer();
        if (player.hasPermission(strings.Permission)) {
            List<Player> onlinePlayers = new ArrayList<>();
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                if (!online.hasPermission(strings.Permission)) {
                    onlinePlayers.add(online);
                }
            }
            if (onlinePlayers.size() > 0) {
                Player random = onlinePlayers.get(new Random().nextInt(onlinePlayers.size()));

                if (strings.MessagesTitles) {
                    String sub = strings.MessagesRandomTeleportTitleSubTitle;
                    Title title = new Title
                            (
                                    CC.translate(strings.MessagesRandomTeleportTitleNormal),
                                    CC.translate(sub.replace("%rtp_player%", random.getName())),
                                    40, 40, 40
                            );
                    title.send(player);
                } else {
                    String message = strings.MessagesRandomTeleportMessage;
                    player.sendMessage(CC.translate(message.replace("%rtp_player%", random.getName())));
                }
                player.playSound(player.getLocation(), strings.MessagesRandomTeleportSound, 3, 3);

                player.teleport(random.getLocation());
            } else {
                player.sendMessage(CC.translate("&cThere are no players online."));
            }
        } else {
            player.sendMessage(CC.translate(strings.NoPermission));
        }
    }

    @Command(name = "profile", aliases = {"sprofile", "smodmodeprofile"})
    public void profile(CommandArgs args) {
        Player player = args.getPlayer();

        if (player.hasPermission(strings.AdminPermission)) {
            if (args.length() == 1) {
                Player target = Bukkit.getPlayer(args.getArgs(0));
                List<String> msg = new ArrayList<>();
                msg.add(CC.translate("&b&m-------------------"));
                msg.add(CC.translate("&d\u25CF &b" + target.getName() + "'s profile"));
                msg.add(CC.translate("  &5" + uar + " &bPing: " + PlayerUtil.getPing(player)));
                msg.add(CC.translate("  &5" + uar + " &bHealth: " + PlayerUtil.getHealthNice(player)));
                msg.add(CC.translate("  &5" + uar + " &bIP: &c"
                        + (player.hasPermission(strings.AdminPermission)
                        ? target.getAddress().getAddress()
                        : "&4No Permission!")));
                msg.add(CC.translate("  &5" + uar + " &bCountry: &c"
                                + (player.hasPermission(strings.AdminPermission)
                                ? PlayerUtil.getCountry(target.getAddress().getAddress().toString())
                                : "&4No Permission!")));
                msg.add(CC.translate("  &5" + uar + " &bAlts: &cIn development!"));
                msg.add(CC.translate("&b&m-------------------"));

                for (String t : msg) {
                    player.sendMessage(t);
                }
            } else {
                player.sendMessage(CC.translate("&cUsage: /profile <player>"));
            }
        } else {
            player.sendMessage(CC.translate(strings.NoPermission));
        }
    }

    @Command(name = "onlinestaff", aliases = "staffs")
    public void onlineStaff(CommandArgs args) {
        Player player = args.getPlayer();
        if (player.hasPermission(strings.Permission)) {
            if (manager.isInModMode(player)) {
                Inventory inv = Bukkit.createInventory(null, 54, CC.translate("&cStaff Online"));
                ItemStack item = new ItemStack(
                        Material.STAINED_GLASS_PANE, 1, (byte) 4);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(CC.translate("&c&b&e&9&o&l&2"));
                itemMeta.addEnchant(Enchantment.THORNS, 1, false);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(itemMeta);

                int[] border = borders[inv.getSize() / 9 - 3];
                for (int i = 0; i < border.length; i++) {
                    inv.setItem(border[i], item);
                }

                DecimalFormat f = new DecimalFormat("0.##");
                for (Player staffs : Bukkit.getOnlinePlayers()) {
                    if (staffs.hasPermission(strings.Permission)) {
                        Location location = staffs.getLocation();
                        inv.addItem(
                                Skull.getSkullFromName(staffs.getName(), staffs.getName(),
                                        Arrays.asList(
                                                CC.translate("&d\u25CF &bLocation&f"),
                                                CC.translate("  &5" + uar + " &bX: &f" + f.format(location.getX()).replace(",", ".")),
                                                CC.translate("  &5" + uar + " &bY: &f" + f.format(location.getY()).replace(",", ".")),
                                                CC.translate("  &5" + uar + " &bZ: &f" + f.format(location.getZ()).replace(",", ".")),
                                                CC.translate(" "),
                                                CC.translate("&d\u25CF &bStaff Mode"),
                                                CC.translate("  &5" + uar + " &BVanish: " + manager.getVanishStatus(staffs)),
                                                CC.translate("  &5" + uar + " &bChat: " + (manager.isOnStaffChat(staffs) ? "&2Staff" : "&cPublic")),
                                                CC.translate(" "),
                                                CC.translate("&d\u25CF &bExtra Info"),
                                                CC.translate("  &5" + uar + " &bPing: " + PlayerUtil.getPing(staffs)),
                                                CC.translate("  &5" + uar + " &bHearts: " + PlayerUtil.getHealthNice(staffs)),
                                                CC.translate("  &5" + uar + " &bGameMode: &c" + player.getGameMode().name().toLowerCase(Locale.ROOT)),
                                                CC.translate("  &5" + uar + " &bIP: &c" + (player.hasPermission(strings.AdminPermission) ? staffs.getAddress().getAddress() : "&4No Permission!")),
                                                CC.translate("  &5" + uar + " &bCountry: &c" + (player.hasPermission(strings.AdminPermission) ? PlayerUtil.getCountry(staffs.getAddress().getAddress().toString()) : "&4No Permission!")),
                                                CC.translate("  &5" + uar + " &bUUID: &c" + staffs.getUniqueId().toString()),
                                                CC.translate(" "),
                                                CC.translate("&4\u26a0 &cClick to teleport.")

                                        )

                                ));
                    }
                    player.openInventory(inv);
                }
            } else {
                player.sendMessage(CC.translate("&cYou must be in staff mode to perform this command."));
            }
        } else {
            player.sendMessage(CC.translate(strings.NoPermission));
        }
    }

    @Command(name = "invsee", aliases = "inspect")
    public void invsee(CommandArgs args) {
        Player player = args.getPlayer();
        if (player.hasPermission(strings.Permission)) {
            if (manager.isInModMode(player)) {
                if (args.length() == 1) {
                    Player victim = Bukkit.getPlayer(args.getArgs(0));
                    Inventory inv = Bukkit.createInventory(null, 54, CC.translate(strings.MessagesInventorySeeMenuTitle.replace("%inv_player%", victim.getName())));
                    ItemStack item = new ItemStack(
                            Material.STAINED_GLASS_PANE, 1, (byte) 4);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName(CC.translate("&c&b&e&9&o&l&2"));
                    itemMeta.addEnchant(Enchantment.THORNS, 1, false);
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.setItemMeta(itemMeta);
                    int[] slots = {36, 37, 38, 39, 40, 41, 42, 43, 44, 49, 52, 51};

                    inv.setContents(victim.getInventory().getContents());

                    inv.setItem(45, victim.getInventory().getHelmet());
                    inv.setItem(46, victim.getInventory().getChestplate());
                    inv.setItem(47, victim.getInventory().getLeggings());
                    inv.setItem(48, victim.getInventory().getBoots());
                    inv.setItem(50, victim.getInventory().getItemInHand());
                    inv.setItem(53, Skull.getSkullFromName(victim.getName(),
                            ChatColor.WHITE + victim.getName(),
                            Arrays.asList(
                                    CC.translate("&d\u25CF &bStaff Mode"),
                                    CC.translate("  &5" + uar + " &BVanish: " + manager.getVanishStatus(victim)),
                                    CC.translate("  &5" + uar + " &bChat: " + (manager.isOnStaffChat(victim) ? "&2Staff" : "&cPublic")),
                                    CC.translate(" "),
                                    CC.translate("&d\u25CF &bExtra Info"),
                                    CC.translate("  &5" + uar + " &bPing: " + PlayerUtil.getPing(victim)),
                                    CC.translate("  &5" + uar + " &bHearts: " + PlayerUtil.getHealthNice(victim)),
                                    CC.translate("  &5" + uar + " &bGameMode: &c" + player.getGameMode().name().toLowerCase(Locale.ROOT)),
                                    CC.translate("  &5" + uar + " &bIP: &c" + (player.hasPermission(strings.AdminPermission) ? victim.getAddress().getAddress() : "&4No Permission!")),
                                    CC.translate("  &5" + uar + " &bCountry: &c" + (player.hasPermission(strings.AdminPermission) ? PlayerUtil.getCountry(victim.getAddress().getAddress().toString()) : "&4No Permission!")),
                                    CC.translate("  &5" + uar + " &bUUID: &c" + victim.getUniqueId().toString())
                            )));

                    inv.setItem(50, victim.getItemInHand());
                    player.openInventory(inv);

                    for (int slot : slots) {
                        inv.setItem(slot, item);
                    }

                    Bukkit.getScheduler().scheduleSyncRepeatingTask(SModMode.get(), () -> {
                        inv.clear();
                        inv.setContents(victim.getInventory().getContents());

                        inv.setItem(45, victim.getInventory().getHelmet());
                        inv.setItem(46, victim.getInventory().getChestplate());
                        inv.setItem(47, victim.getInventory().getLeggings());
                        inv.setItem(48, victim.getInventory().getBoots());
                        inv.setItem(50, victim.getItemInHand());

                        inv.setItem(53, Skull.getSkullFromName(victim.getName(),
                                ChatColor.WHITE + victim.getName(),
                                Arrays.asList(
                                        CC.translate("&d\u25CF &bStaff Mode"),
                                        CC.translate("  &5" + uar + " &BVanish: " + manager.getVanishStatus(victim)),
                                        CC.translate("  &5" + uar + " &bChat: " + (manager.isOnStaffChat(victim) ? "&2Staff" : "&cPublic")),
                                        CC.translate(" "),
                                        CC.translate("&d\u25CF &bExtra Info"),
                                        CC.translate("  &5" + uar + " &bPing: " + PlayerUtil.getPing(victim)),
                                        CC.translate("  &5" + uar + " &bHearts: " + PlayerUtil.getHealthNice(victim)),
                                        CC.translate("  &5" + uar + " &bGameMode: &c" + player.getGameMode().name().toLowerCase(Locale.ROOT)),
                                        CC.translate("  &5" + uar + " &bIP: &c" + (player.hasPermission(strings.AdminPermission) ? victim.getAddress().getAddress() : "&4No Permission!")),
                                        CC.translate("  &5" + uar + " &bCountry: &c" + (player.hasPermission(strings.AdminPermission) ? PlayerUtil.getCountry(victim.getAddress().getAddress().toString()) : "&4No Permission!")),
                                        CC.translate("  &5" + uar + " &bUUID: &c" + victim.getUniqueId().toString())
                                )));
                        for (int slot : slots) {
                            inv.setItem(slot, item);
                        }

                        player.updateInventory();
                    }, 40, 40);
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: /invsee <player>");
                }
            } else {
                player.sendMessage(CC.translate("&cYou must be in mod mode to use this."));
            }
        } else {
            player.sendMessage(CC.translate(strings.NoPermission));
        }
    }

    @Command(name = "staffchat", aliases = {"sc", "chatstaff"})
    public void sc(CommandArgs args) {
        Player player = args.getPlayer();
        if (!player.hasPermission(strings.Permission)) {
            player.sendMessage(CC.translate(strings.NoPermission));
            return;
        }
        if (!staffChat.contains(player.getUniqueId())) {
            staffChat.add(player.getUniqueId());
            if (strings.MessagesTitles) {
                Title title = new Title
                        (
                                CC.translate(strings.MessagesStaffChatEnabledTitleNormal),
                                CC.translate(strings.MessagesStaffChatEnabledTitleSubTitle),
                                40, 40, 40
                        );
                title.send(player);
            } else {
                player.sendMessage(CC.translate(strings.MessagesStaffChatEnabledMessage));
            }
        } else {
            staffChat.remove(player.getUniqueId());
            if (strings.MessagesTitles) {
                Title title = new Title
                        (
                                CC.translate(strings.MessagesStaffChatDisabledTitleNormal),
                                CC.translate(strings.MessagesStaffChatDisabledTitleSubTitle),
                                40, 40, 40
                        );
                title.send(player);
            } else {
                player.sendMessage(CC.translate(strings.MessagesStaffChatDisabledMessage));
            }
        }
    }
}
