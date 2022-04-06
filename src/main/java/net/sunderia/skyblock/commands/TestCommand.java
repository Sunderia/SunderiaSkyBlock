package net.sunderia.skyblock.commands;

import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import fr.sunderia.sunderiautils.utils.ItemBuilder;
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
        getArg(args, 0).ifPresent(s -> {
            int column = Integer.parseInt(args[0]);
            for (int row = 0; row < 6; row++) {
                System.out.println(row * 9 + column - 1);
            }
        });
    }

}