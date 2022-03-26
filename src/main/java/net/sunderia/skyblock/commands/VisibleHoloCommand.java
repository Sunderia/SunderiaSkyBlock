package net.sunderia.skyblock.commands;

import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import net.sunderia.skyblock.SunderiaSkyblock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@CommandInfo(name = "visibleholo", aliases = {"vh"}, permission = "sunderiaskyblock.visibleholo", usage = "/<command> true/false", requiresPlayer = true)
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
        SunderiaSkyblock.getInstance().getConfig().set("visibleholo." + player.getUniqueId(), args[0].toLowerCase(Locale.ROOT));
        SunderiaSkyblock.getInstance().saveConfig();
        player.sendMessage(SunderiaSkyblock.header + "The visibleholo config has successfully been set to " + (args[0].toLowerCase(Locale.ROOT).equals("true") ? ChatColor.GREEN : ChatColor.RED) + args[0].toLowerCase(Locale.ROOT) + ChatColor.RESET + ".");
    }

}
