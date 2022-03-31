package net.sunderia.skyblock.listener;

import fr.sunderia.sunderiautils.utils.ItemStackUtils;
import net.sunderia.skyblock.objects.Items;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {

    public static void onTickEvent() {

    }

    @EventHandler
    public void whenGrenadaExplodaYourTerritoria(ProjectileHitEvent event) {
        if(!(event.getEntity().getShooter() instanceof Player) || event.getEntityType() != EntityType.SNOWBALL || event.getEntity().getCustomName() == null ||
                !event.getEntity().getCustomName().equals("Grenada")) return;
        Location loc = event.getHitBlock() == null ? event.getHitEntity().getLocation() : event.getHitBlock().getLocation();
        event.getEntity().getWorld().createExplosion(loc, 1, false, false);
    }

    //Don't listen to event when you do nothing
    /*@EventHandler
    public void onBlockBroken(BlockBreakEvent event){
        if(ItemStackUtils.isSameItem(event.getPlayer().getInventory().getItemInMainHand(), Items.PICKAXE_3X3.getItemStack())){
        }
    }*/

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event){
        event.getPlayer().getInventory().setItem(4, Items.PICKAXE_3X3);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().getInventory().setItem(4, Items.PICKAXE_3X3);
    }
}
