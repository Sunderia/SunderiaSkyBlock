package net.sunderia.skyblock.commands;

import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
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
        if(getArg(args, 0).isEmpty()) {
            player.sendMessage("fuck you.");
            return;
        }
        int number = Integer.parseInt(args[0]);
        for(int index = number * 9 - 9; index < number * 9; index++){
            System.out.println(index);
        }
    }

}