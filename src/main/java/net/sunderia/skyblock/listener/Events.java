package net.sunderia.skyblock.listener;

import net.sunderia.skyblock.objects.Inventories;
import net.sunderia.skyblock.objects.Items;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {

    public static void onSecondEvent() {

    }

    @EventHandler
    public void whenGrenadaExplodaYourTerritoria(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player) || event.getEntityType() != EntityType.SNOWBALL || event.getEntity().getCustomName() == null ||
                !event.getEntity().getCustomName().equals("Grenada")) return;
        Location loc = event.getHitBlock() == null ? event.getHitEntity().getLocation() : event.getHitBlock().getLocation();
        event.getEntity().getWorld().createExplosion(loc, 1, false, false);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CRAFTING_TABLE && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            event.getPlayer().openInventory(Inventories.CRAFTING_GUI);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().setItem(4, Items.MINEMOBS_GUN);
    }
}
