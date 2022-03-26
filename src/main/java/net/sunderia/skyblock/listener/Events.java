package net.sunderia.skyblock.listener;

import fr.sunderia.sunderiautils.utils.ItemStackUtils;
import net.sunderia.skyblock.objects.Items;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener {

    public static void onTickEvent() {

    }

    @EventHandler
    public void onBlockBroken(BlockBreakEvent event){
        if(ItemStackUtils.isSameItem(event.getPlayer().getInventory().getItemInMainHand(), Items.PICKAXE_3X3.getItemStack())){

        }
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event){
        event.getPlayer().getInventory().setItem(4, Items.PICKAXE_3X3.getItemStack());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().getInventory().setItem(4, Items.PICKAXE_3X3.getItemStack());
    }
}
