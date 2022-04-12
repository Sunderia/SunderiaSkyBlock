package net.sunderia.skyblock.commands;

import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import net.sunderia.skyblock.SunderiaSkyblock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@CommandInfo(name = "visibleholo", aliases = {"vh"}, permission = "sunderiaskyblock.visibleholo", usage = "/<command> true/false")
public class VisibleHoloCommand extends PluginCommand {

    /**
     * This constructor is used to register the command and check if the command has the correct annotation.
     *
     * @param plugin An instance of the plugin.
     */
    public VisibleHoloCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String[] args) {
        getArg(args, 0).ifPresentOrElse(arg -> {
            if (!arg.equalsIgnoreCase("true") && !arg.equalsIgnoreCase("false")) {
                player.sendMessage(SunderiaSkyblock.header + ChatColor.RED + getInfo().usage());
                return;
            }
            SunderiaSkyblock.getInstance().getConfig().set("visibleholo." + player.getUniqueId(), Boolean.parseBoolean(arg));
            SunderiaSkyblock.getInstance().saveConfig();
            player.sendMessage(SunderiaSkyblock.header + "The visibleholo config has successfully been set to " + (Boolean.parseBoolean(arg) ? ChatColor.GREEN + "true" : ChatColor.RED + "false"));
        }, () -> player.sendMessage(SunderiaSkyblock.header + ChatColor.RED + getInfo().usage()));
    }

}
