package net.sunderia.skyblock;

import fr.sunderia.sunderiautils.SunderiaUtils;
import fr.sunderia.sunderiautils.utils.ItemBuilder;
import net.sunderia.skyblock.listener.Events;
import net.sunderia.skyblock.objects.CustomEquipment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

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
            SunderiaUtils.registerListeners("net.sunderia.skyblock.listener");
            SunderiaUtils.registerCommands("net.sunderia.skyblock.commands");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().runTaskTimer(this, Events::onTickEvent, 20, 20);
        CustomEquipment e = new CustomEquipment(
                new ItemBuilder(Material.NETHERITE_BLOCK).build(),
                new ItemBuilder(Material.NETHERITE_INGOT).build(),
                true);
        e.initArmor();
        e.initTools();
        getLogger().info("[SunderiaSkyblock] Plugin is enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("[SunderiaSkyblock] Plugin is disabled.");
    }

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(getInstance(), key);
    }

    public static String arrayToString(String[] array){
        return Arrays.toString(array).replace("[", "").replace("]", "").replace(", ", "");
    }

    public static String stringToKey(String string){
        return string.strip().toLowerCase(Locale.ROOT);
    }

    public static SunderiaSkyblock getInstance(){
        return instance;
    }

}
