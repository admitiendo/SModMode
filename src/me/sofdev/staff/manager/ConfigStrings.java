package me.sofdev.staff.manager;

import me.sofdev.staff.SModMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConfigStrings {
    public FileConfiguration config = SModMode.get().getConfig();

    // Default
    public String NoPermission = config.getString("Options.NoPermission");
    public String AdminPermission = config.getString("Options.AdminPermission");
    public String Permission = config.getString("Options.Permission");

    public String ItemsVanishStatusEnabled = config.getString("Items.Vanish.Status.Enabled");
    public String ItemsVanishStatusDisabled = config.getString("Items.Vanish.Status.Disabled");
    public Material ItemsVanishMaterial = Material.valueOf(config.getString("Items.Vanish.Material"));
    public List<String> ItemsVanishLore = config.getStringList("Items.Vanish.Lore");
    public boolean ItemsVanishGlow = config.getBoolean("Items.Vanish.Glow");

    // RandomTeleport
    public String ItemsRandomTeleportName = config.getString("Items.RandomTeleport.Name");
    public Material ItemsRandomTeleportMaterial = Material.valueOf(config.getString("Items.RandomTeleport.Material"));
    public List<String> ItemsRandomTeleportLore = config.getStringList("Items.RandomTeleport.Lore");
    public boolean ItemsRandomTeleportGlow = config.getBoolean("Items.RandomTeleport.Glow");

    // InvSEE
    public String ItemsInventorySeeName = config.getString("Items.InventorySee.Name");
    public Material ItemsInventorySeeMaterial = Material.valueOf(config.getString("Items.InventorySee.Material"));
    public List<String> ItemsInventorySeeLore = config.getStringList("Items.InventorySee.Lore");
    public boolean ItemsInventorySeeGlow = config.getBoolean("Items.InventorySee.Glow");

    // Messages
    public boolean MessagesTitles = config.getBoolean("Messages.Titles");

    // Vanish Enabled
    public String MessagesVanishEnabledMessage = config.getString("Messages.Vanish.Enabled.Message");

    public Sound MessagesVanishEnabledSound = Sound.valueOf(config.getString("Messages.Vanish.Enabled.Sound").toUpperCase(Locale.ROOT));
    public String MessagesVanishEnabledTitleNormal = config.getString("Messages.Vanish.Enabled.Title.Normal");
    public String MessagesVanishEnabledTitleSubTitle = config.getString("Messages.Vanish.Enabled.Title.SubTitle");

    // Vanish Disabled
    public String MessagesVanishDisabledMessage = config.getString("Messages.Vanish.Disable.Message");

    public Sound MessagesVanishDisabledSound = Sound.valueOf(config.getString("Messages.Vanish.Disable.Sound").toUpperCase(Locale.ROOT));
    public String MessagesVanishDisabledTitleNormal = config.getString("Messages.Vanish.Disable.Title.Normal");
    public String MessagesVanishDisabledTitleSubTitle = config.getString("Messages.Vanish.Disable.Title.SubTitle");

    // Staff Enabled
    public String MessagesStaffModeEnabledMessage = config.getString("Messages.StaffMode.Enabled.Message");

    public Sound MessagesStaffModeEnabledSound = Sound.valueOf(config.getString("Messages.StaffMode.Enabled.Sound").toUpperCase(Locale.ROOT));
    public String MessagesStaffModeEnabledTitleNormal = config.getString("Messages.StaffMode.Enabled.Title.Normal");
    public String MessagesStaffModeEnabledTitleSubTitle = config.getString("Messages.StaffMode.Enabled.Title.SubTitle");

    // Staff Disabled
    public String MessagesStaffModeDisabledMessage = config.getString("Messages.StaffMode.Disabled.Message");

    public Sound MessagesStaffModeDisabledSound = Sound.valueOf(config.getString("Messages.StaffMode.Disabled.Sound").toUpperCase(Locale.ROOT));
    public String MessagesStaffModeDisabledTitleNormal = config.getString("Messages.StaffMode.Disabled.Title.Normal");
    public String MessagesStaffModeDisabledTitleSubTitle = config.getString("Messages.StaffMode.Disabled.Title.SubTitle");

    // StaffChat Enabled
    public String MessagesStaffChatEnabledMessage = config.getString("Messages.StaffChat.Enabled.Message");

    public Sound MessageStaffChatEnabledSound = Sound.valueOf(config.getString("Messages.StaffChat.Enabled.Sound").toUpperCase(Locale.ROOT));
    public String MessagesStaffChatEnabledTitleNormal = config.getString("Messages.StaffChat.Enabled.Title.Normal");
    public String MessagesStaffChatEnabledTitleSubTitle = config.getString("Messages.StaffChat.Enabled.Title.SubTitle");

    // StaffChat Disabled
    public String MessagesStaffChatDisabledMessage = config.getString("Messages.StaffChat.Disabled.Message");

    public Sound MessagesStaffChatDisabledSound = Sound.valueOf(config.getString("Messages.StaffChat.Disabled.Sound").toUpperCase(Locale.ROOT));
    public String MessagesStaffChatDisabledTitleNormal = config.getString("Messages.StaffChat.Disabled.Title.Normal");
    public String MessagesStaffChatDisabledTitleSubTitle = config.getString("Messages.StaffChat.Disabled.Title.SubTitle");

    // RTP
    public String MessagesRandomTeleportMessage = config.getString("Messages.RandomTeleport.Message");

    public Sound MessagesRandomTeleportSound = Sound.valueOf(config.getString("Messages.RandomTeleport.Sound").toUpperCase(Locale.ROOT));
    public String MessagesRandomTeleportTitleNormal = config.getString("Messages.RandomTeleport.Normal");
    public String MessagesRandomTeleportTitleSubTitle = config.getString("Messages.RandomTeleport.SubTitle");

    // Inspect
    public String MessagesInventorySeeMessage = config.getString("Messages.InventorySee.Message");
    public String MessagesInventorySeeMenuTitle = config.getString("Messages.InventorySee.Menu.Title");
}
