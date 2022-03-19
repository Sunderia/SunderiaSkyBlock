package fr.sunderia.skyblock.commands;

import fr.sunderia.skyblock.SunderiaSkyblock;
import fr.sunderia.skyblock.annotation.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CommandPlugin implements CommandExecutor {

    private final CommandInfo commandInfo;

    protected CommandPlugin() {
        this.commandInfo = getClass().getDeclaredAnnotation(CommandInfo.class);
        if (commandInfo == null) throw new IllegalArgumentException("Commands classes must have ClassInfo annotation.");
    }

    public static String arrayToString(String[] array) {
        return Arrays.toString(array).replace("[", "").replace(",", "").replace("]", "");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String aliasUsed, @NotNull String[] arguments) {
        if (commandInfo.needsPlayer() && !(sender instanceof Player)) {
            sender.sendMessage(SunderiaSkyblock.header + ChatColor.RED + "Only players can use this command.");
            return true;
        }
        if (!commandInfo.permission().isEmpty() && !sender.hasPermission(commandInfo.permission())) {
            sender.sendMessage(SunderiaSkyblock.header + ChatColor.RED + "You do not have the permission to execute this command !");
            return true;
        }
        if (!getCommandInfo().arguments().isEmpty()) {
            if (arguments.length != getCommandInfo().arguments()) {
                sender.sendMessage(SunderiaSkyblock.header + ChatColor.RED + "Use these arguments: " + Bukkit.getPluginCommand(getCommandInfo().name()).getUsage());
                return true;
            }
            for (int index1 = 0; index1 < commandInfo.arguments().split("\\s+").length; index1++) {
                int finalIndex = index1;
                if(Arrays.stream(commandInfo.arguments().split("\\s+")[index1].split("/")).noneMatch(arg -> arg.equalsIgnoreCase(arguments[finalIndex]))){
                    sender.sendMessage(SunderiaSkyblock.header + ChatColor.RED + "Use these arguments: /" + getCommandInfo().name() + " " + Bukkit.getPluginCommand(getCommandInfo().name()).getUsage());
                    return true;
                }
            }
        }
        if (sender instanceof Player) execute((Player) sender, arguments);
        else execute(sender, arguments);
        return true;
    }

    public void execute(CommandSender sender, String[] args) {
    }

    public void execute(Player player, String[] args) {
    }

    public CommandInfo getCommandInfo() {
        return commandInfo;
    }
}
