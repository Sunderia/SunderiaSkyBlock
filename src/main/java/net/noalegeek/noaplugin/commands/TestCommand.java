package net.noalegeek.noaplugin.commands;

import fr.sunderia.sunderiautils.SunderiaUtils;
import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import fr.sunderia.sunderiautils.customblock.CustomBlock;
import fr.sunderia.sunderiautils.utils.ItemBuilder;
import net.noalegeek.noaplugin.objects.Inventories;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
        player.openWorkbench(player.getLocation(), true);
        getArg(args, 0).ifPresentOrElse(s -> {
            player.openInventory(Inventories.TEST_GUI);
        }, () -> player.openWorkbench(player.getLocation(), true));
        player.getInventory().addItem(new CustomBlock.Builder(SunderiaUtils.key("ruby_ore"), 1)
                .setDrops(new ItemBuilder(Material.EMERALD).setDisplayName(ChatColor.RED + "RUBY").build()).build().getAsItem());
    }

}