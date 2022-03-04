package fr.sunderia.skyblock.commands;

import fr.sunderia.skyblock.SunderiaSkyblock;
import fr.sunderia.skyblock.annotation.CommandInfo;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@CommandInfo(name = "visibleholo", aliases = {"vh"})
public class VisibleHoloCommand extends CommandPlugin {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String aliasUsed, @NotNull String[] args) {
        if(!(sender instanceof Player player)){

            return false;
        }
        if(args[0] == null || !args[0].toLowerCase(Locale.ROOT).equals("true") && !args[0].toLowerCase(Locale.ROOT).equals("false")){
            player.sendMessage();
            return false;
        }
        SunderiaSkyblock.instance.getConfig().set("visibleholo." + player.getUniqueId(), args[0].toLowerCase(Locale.ROOT));
        SunderiaSkyblock.instance.saveConfig();
        return true;
    }
}
