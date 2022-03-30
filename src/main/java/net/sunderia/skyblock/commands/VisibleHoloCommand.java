package net.sunderia.skyblock.commands;

import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import net.sunderia.skyblock.SunderiaSkyblock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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
        //A bit more complicated but better.
        // - Minemobs
        getArg(args, 0).ifPresentOrElse(s -> {
            if(notABool(s)) {
                player.sendMessage("Not a boolean.");
                return;
            }
            boolean b = Boolean.parseBoolean(s);
            SunderiaSkyblock.getInstance().getConfig().set("visibleholo." + player.getUniqueId(), b);
            SunderiaSkyblock.getInstance().saveConfig();
            player.sendMessage(SunderiaSkyblock.header + "The visibleholo config has successfully been set to " + (b ? ChatColor.GREEN + "true" : ChatColor.RED + "false"));
        }, () -> player.sendMessage("Missing argument."));
    }

    private boolean notABool(String s) {
        return !s.equalsIgnoreCase("true") && !s.equalsIgnoreCase("false");
    }

}
