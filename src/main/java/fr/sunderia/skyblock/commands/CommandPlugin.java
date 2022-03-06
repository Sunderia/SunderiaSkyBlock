package fr.sunderia.skyblock.commands;

import fr.sunderia.skyblock.SunderiaSkyblock;
import fr.sunderia.skyblock.annotation.CommandInfo;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandPlugin implements CommandExecutor {

    private final CommandInfo commandInfo;

    protected CommandPlugin() {
        this.commandInfo = getClass().getDeclaredAnnotation(CommandInfo.class);
        if(commandInfo == null) throw new IllegalArgumentException("Commands classes must have ClassInfo annotation.");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String aliasUsed, @NotNull String[] args) {
        if(!commandInfo.permission().isEmpty() && !sender.hasPermission(commandInfo.permission())) {
            sender.sendMessage(SunderiaSkyblock.header + ChatColor.RED + "You do not have the permission to execute this command !");
            return true;
        }
        
        if(commandInfo.needsPlayer()) {
            if(!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            } else {
                execute(player, args);
            }
            return true;
        }
        execute(sender, args);
        return true;
    }

    public void execute(Player player, String[] args) {}
    public void execute(CommandSender sender, String[] args) {}

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }
}
