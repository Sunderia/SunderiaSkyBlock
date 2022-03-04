package fr.sunderia.skyblock;

import fr.sunderia.skyblock.annotation.CommandInfo;
import fr.sunderia.skyblock.listener.Events;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

public class SunderiaSkyblock extends JavaPlugin {

    private static SunderiaSkyblock instance;
    private static String messagePrefix = "§2[§aSunderiaSkyblock§2]";

    @Override
    public void onEnable() {
        getLogger().info("[SunderiaSkyblock] Plugin is enabled.");
        registerListeners(new Reflections().getSubTypesOf(Listener.class).stream().map(clazz -> (Class<? extends Listener>) clazz).collect(Collectors.toSet()));
        registerCommands(new Reflections().getTypesAnnotatedWith(CommandInfo.class).stream().map(clazz -> (Class<? extends CommandExecutor>) clazz).collect(Collectors.toSet()));
        Events.update();
    }

    @Override
    public void onDisable() {
        getLogger().info("[SunderiaSkyblock] Plugin is disabled.");
    }

    private void registerListeners(Set<Class<? extends Listener>> listeners){
        listeners.forEach(clazz -> {
            try {
                Bukkit.getPluginManager().registerEvents(clazz.getDeclaredConstructor().newInstance(), this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                exception.printStackTrace();
            }
        });
    }

    private void registerCommands(Set<Class<? extends CommandExecutor>> commands){
        commands.forEach(clazz -> {
            try {
                this.getCommand(clazz.getAnnotation(CommandInfo.class).name()).setExecutor(clazz.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                exception.printStackTrace();
            }
        });
    }

    public static SunderiaSkyblock getInstance(){
        return instance;
    }

    public static String getMessagePrefix(){
        return messagePrefix;
    }

}
