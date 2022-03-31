package net.sunderia.skyblock.commands;

import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "test", aliases = {"t"}, permission = "sunderiaskyblock.test")
public class TestCommand extends PluginCommand {

    /**
     * This constructor is used to register the command and check if the command has the correct annotation.
     *
     * @param plugin An instance of the plugin.
     */
    public TestCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String[] args) {
        net.md_5.bungee.api.ChatColor integer = net.md_5.bungee.api.ChatColor.of(new java.awt.Color(38, 84, 181));
        getArg(args, 0).ifPresent(s -> {
            if(!s.equalsIgnoreCase("Noa's Familly")/*Is null every time*/) {
                player.sendMessage("You're right.");
                player.sendMessage("Now compile this code with notes.\n" + String.format("""
                    %s#include <iostream>
                    
                    %sint %smain() {
                        %schar %sf[%s11%s] = %s"Noa Familly"%s;
                        f = null;
                        std::cout << %s"Noa Familly == null ? " %s<< %s"yes"%s << std::endl;
                        %sreturn %s0%s;
                    }
                    %s//Made by minemobs with love.
                    """, ChatColor.BLUE,
                        ChatColor.BLUE, ChatColor.RESET,
                        ChatColor.BLUE, ChatColor.RESET, integer, ChatColor.RESET, ChatColor.GREEN, ChatColor.RESET,
                        ChatColor.GREEN, ChatColor.RESET, ChatColor.GREEN, ChatColor.RESET,
                        ChatColor.BLUE, integer, ChatColor.RESET, ChatColor.GRAY));
            }
        });
        int number = Integer.parseInt(args[0]);
        int number1 = Integer.parseInt(args[1]);
        for(int index = number * 9 - 9; index < number * 9; index++){
            System.out.println(index);
        }
        System.out.println("\n\n\n");
        for(int index = number1 - 1; index < number1 + (number * 9); index += 9){
            System.out.println(index);
        }
    }

}