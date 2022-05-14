package net.sunderia.skyblock;

import fr.sunderia.sunderiautils.SunderiaUtils;
import net.sunderia.skyblock.listener.Events;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SunderiaSkyblock extends JavaPlugin {

    private static SunderiaSkyblock instance;
    public static final String header = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SunderiaSkyblock" + ChatColor.DARK_GREEN + "] ";

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        SunderiaUtils.of(this);
        this.saveDefaultConfig();
        try {
            SunderiaUtils.registerListeners(this.getClass().getPackageName() + ".listener");
            SunderiaUtils.registerCommands(this.getClass().getPackageName() + ".commands");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().runTaskTimer(this, Events::onSecondPassEvent, 20, 20);
        getLogger().info("[SunderiaSkyblock] Plugin is enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("[SunderiaSkyblock] Plugin is disabled.");
    }

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(getInstance(), key);
    }

    public static SunderiaSkyblock getInstance() {
        return instance;
    }
}