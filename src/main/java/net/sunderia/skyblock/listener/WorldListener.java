package net.sunderia.skyblock.listener;

import fr.sunderia.sunderiautils.SunderiaUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

public class WorldListener implements Listener {

    @EventHandler
    public void onCobbleStone(BlockFormEvent event) {
        if (event.getNewState().getType() != Material.COBBLESTONE) return;
        int random = SunderiaUtils.getRandom().nextInt(100);
        if (random < 50) {
            event.getNewState().setType(Material.COBBLESTONE);
        } else if (random < 75) {
            event.getNewState().setType(Material.COAL_ORE);
        } else if (random < 90) {
            event.getNewState().setType(Material.IRON_ORE);
        } else if (random < 97) {
            event.getNewState().setType(Material.GOLD_ORE);
        } else {
            event.getNewState().setType(Material.DIAMOND_ORE);
        }
    }

}
