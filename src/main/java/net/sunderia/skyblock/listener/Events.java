package net.sunderia.skyblock.listener;

import fr.sunderia.sunderiautils.utils.InventoryBuilder;
import fr.sunderia.sunderiautils.utils.ItemStackUtils;
import net.sunderia.skyblock.objects.Inventories;
import net.sunderia.skyblock.objects.Items;
import net.sunderia.skyblock.utils.InventoryUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Events implements Listener {

    public static void onTickEvent() {

    }

    @EventHandler
    public void whenGrenadaExplodaYourTerritoria(ProjectileHitEvent event) {
        if(!(event.getEntity().getShooter() instanceof Player) || event.getEntityType() != EntityType.SNOWBALL || event.getEntity().getCustomName() == null || !event.getEntity().getCustomName().equals("Grenada")) return;
        Location loc = event.getHitBlock() == null ? event.getHitEntity().getLocation() : event.getHitBlock().getLocation();
        event.getEntity().getWorld().createExplosion(loc, 1, false, false);
    }

    /*@EventHandler
    public void onBlockBroken(BlockBreakEvent event){
    }*/

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event){
        event.getPlayer().getInventory().setItem(4, Items.MINEMOBS_GUN);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().getInventory().setItem(4, Items.MINEMOBS_GUN);
    }
}
