package me.sofdev.staff;

import me.sofdev.staff.command.StaffCommands;
import me.sofdev.staff.manager.StaffManager;
import me.sofdev.staff.utils.chat.ActionBar;
import me.sofdev.staff.utils.chat.CC;
import me.sofdev.staff.listener.StaffListener;
import me.sofdev.staff.utils.commands.Command;
import me.sofdev.staff.utils.commands.CommandFramework;
import me.sofdev.staff.utils.player.Server;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SModMode extends JavaPlugin {
    double version = 2.0;
    String configPath;
    StaffManager manager;

    @Override
    public void onEnable() {
        CC.log(CC.normalLine());
        registerConfig();
        Bukkit.getPluginManager().registerEvents(new StaffListener(), this);
        CommandFramework framework = new CommandFramework(this);
        framework.registerCommands(new StaffCommands());
        CC.log("&bSModMode &e| &2Loaded");
        CC.log("&bSModMode &e| &3Version: &f" + version);
        CC.log(CC.normalLine());
        manager = new StaffManager();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                // "&bStaff Mode : Vanished : Chat: Staff : TPS: 20.0"
                if (StaffListener.staffMode.contains(player.getUniqueId())) {
                    String msg = CC.translate(
                            (manager.isVanished(player) ? "&2&lVanished" : "&c&lUnVanished")
                                    + " &0\u2503 "
                                    + "&2" + getServer().getOnlinePlayers().size() + "&7&l/&c" + getServer().getMaxPlayers()
                                    + " &0\u2503 "
                                    + (manager.isOnStaffChat(player) ? "&b&lChat: &2&lStaff" : "&b&lChat: &c&lPublic")
                                    + " &0\u2503 "
                                    + "&b&lTPS: " + Server.getNiceTPS().replace(",", "."));
                    ActionBar.sendActionBar(player, msg);
                }
            }
        }, 5, 5);
    }

    @Override
    public void onDisable() {
        CC.log(CC.normalLine());
        CC.log("&bSModMode &e| &2Disabled.");
        CC.log("&bSModMode &e| &3Version: &f" + version);
        CC.log(CC.normalLine());
    }

    public static SModMode get() {
        return getPlugin(SModMode.class);
    }

    public void registerConfig() {
        File config = new File(this.getDataFolder(), "config.yml");
        configPath = config.getPath();
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }
}
