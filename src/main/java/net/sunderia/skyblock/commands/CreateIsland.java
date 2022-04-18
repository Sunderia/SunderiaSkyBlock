package net.sunderia.skyblock.commands;

import fr.sunderia.sunderiautils.SunderiaUtils;
import fr.sunderia.sunderiautils.commands.CommandInfo;
import fr.sunderia.sunderiautils.commands.PluginCommand;
import net.sunderia.skyblock.objects.Structures;
import org.bukkit.Location;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author minemobs
 * Yes, again...
 */
@CommandInfo(name = "createisland", aliases = {"is", "cis", "ci"})
public class CreateIsland extends PluginCommand {

    public CreateIsland(JavaPlugin plugin) {
        super(plugin);
    }


    @Override
    public void onCommand(Player player, String[] args) {
        var loc = new Location(player.getWorld(), 0, 100, 0);
        Structures.ISLAND.getStructure().place(loc, false, StructureRotation.NONE, Mirror.NONE, 0, 1, SunderiaUtils.getRandom());
        player.teleport(loc.clone().add(0, 4, 0));
    }
}
