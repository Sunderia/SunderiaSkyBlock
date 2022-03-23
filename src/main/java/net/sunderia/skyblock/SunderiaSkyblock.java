package net.sunderia.skyblock;

import net.sunderia.skyblock.annotation.CommandInfo;
import net.sunderia.skyblock.listener.Events;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

public class SunderiaSkyblock extends JavaPlugin {

    private static SunderiaSkyblock instance;
    public static final String header = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SunderiaSkyblock" + ChatColor.DARK_GREEN + "] ";

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        registerListeners(new Reflections("net.sunderia.skyblock.listener").getSubTypesOf(Listener.class).stream().map(clazz -> (Class<? extends Listener>) clazz).collect(Collectors.toSet()));
        registerCommands(new Reflections("net.sunderia.skyblock.commands").getTypesAnnotatedWith(CommandInfo.class).stream().map(clazz -> (Class<? extends CommandExecutor>) clazz).collect(Collectors.toSet()));
        Events.update();
        getLogger().info("[SunderiaSkyblock] Plugin is enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("[SunderiaSkyblock] Plugin is disabled.");
    }

    private void registerListeners(@NotNull Set<Class<? extends Listener>> listeners){
        listeners.forEach(clazz -> {
            try {
                Bukkit.getPluginManager().registerEvents(clazz.getDeclaredConstructor().newInstance(), this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                exception.printStackTrace();
            }
        });
    }

    private void registerCommands(@NotNull Set<Class<? extends CommandExecutor>> commands){
        commands.forEach(clazz -> {
            try {
                this.getCommand(clazz.getAnnotation(CommandInfo.class).name()).setExecutor(clazz.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                exception.printStackTrace();
            }
        });
    }

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(getInstance(), key);
    }

    public static SunderiaSkyblock getInstance(){
        return instance;
    }

}
