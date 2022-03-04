package fr.sunderia.skyblock.commands;

import fr.sunderia.skyblock.SunderiaSkyblock;
import fr.sunderia.skyblock.annotation.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@CommandInfo(name = "visibleholo", aliases = {"vh"})
public class VisibleHoloCommand extends CommandPlugin {
    @Override
    public void execute(Player player, String[] args) {
        if(args.length != 1 || !args[0].equalsIgnoreCase("true") && !args[0].equalsIgnoreCase("false")) {
            player.sendMessage(Bukkit.getPluginCommand(getCommandInfo().name()).getUsage());
        }
        SunderiaSkyblock.instance.getConfig().set("visibleholo." + player.getUniqueId(), args[0].toLowerCase(Locale.ROOT));
        SunderiaSkyblock.instance.saveConfig();
    }
}
