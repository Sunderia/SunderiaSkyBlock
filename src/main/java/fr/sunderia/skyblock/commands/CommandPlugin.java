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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String aliasUsed, @NotNull String[] strings) {
        if(commandInfo.needsPlayer() && !(sender instanceof Player)){
        sender.sendMessage(SunderiaSkyblock.header + "Only players can use this command.");
            return false;
        }
        if(!commandInfo.permission().isEmpty() && !sender.hasPermission(commandInfo.permission())) {
            sender.sendMessage(SunderiaSkyblock.header + ChatColor.RED + "You do not have the permission to execute this command !");
            return false;
        }

        if (sender instanceof Player) {
            execute((Player) sender, strings);
        } else {
            execute(sender, strings);
        }
        return true;
    }

    public void execute(Player player, String[] args) {}
    public void execute(CommandSender sender, String[] args) {}

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }
}
