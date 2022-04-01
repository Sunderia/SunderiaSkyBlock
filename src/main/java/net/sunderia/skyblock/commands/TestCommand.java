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
     * ur mom
     * @param plugin An instance of the plugin.
     */
    public TestCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull String[] args) {
        getArg(args, 0).ifPresent(s -> {
            int number = Integer.parseInt(args[0]);
            int number1 = Integer.parseInt(args[1]);
            for(int index = number * 9 - 9; index < number * 9; index++){
                System.out.println(index);
            }
            System.out.println("\n\n\n");
            for(int index = number1 - 1; index < number1 + (number * 9); index += 9){
                System.out.println(index);
            }
        });
    }

}