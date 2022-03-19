package net.sunderia.skyblock.commands;

import net.sunderia.skyblock.SunderiaSkyblock;
import net.sunderia.skyblock.annotation.CommandInfo;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@CommandInfo(name = "visibleholo", aliases = {"vh"}, permission = "sunderiaskyblock.visibleholo", arguments = "true/false")
public class VisibleHoloCommand extends CommandPlugin {

    @Override
    public void execute(@NotNull Player player, @NotNull String[] args) {
        SunderiaSkyblock.getInstance().getConfig().set("visibleholo." + player.getUniqueId(), args[0].toLowerCase(Locale.ROOT));
        SunderiaSkyblock.getInstance().saveConfig();
        player.sendMessage(SunderiaSkyblock.header + "The visibleholo config has successfully been set to " + (args[0].toLowerCase(Locale.ROOT).equals("true") ? ChatColor.GREEN : ChatColor.RED) + args[0].toLowerCase(Locale.ROOT) + ChatColor.RESET + ".");
    }

}
