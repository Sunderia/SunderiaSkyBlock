package net.sunderia.skyblock.objects;

import fr.sunderia.sunderiautils.utils.ItemBuilder;
import fr.sunderia.sunderiautils.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public class Items {

    public static final ItemStack PICKAXE_3X3 = new ItemBuilder(Material.IRON_PICKAXE).setDisplayName("3x3 Pickaxe").setLore("This is a 3x3 pickaxe like a hammer").onInteract(event -> {
        if(event.getAction() != Action.RIGHT_CLICK_AIR || !ItemStackUtils.isSameItem(event.getItem(), Items.getItemFromName("PICKAXE_3X3"))) return;
        Snowball entity = (Snowball) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.SNOWBALL);
        entity.setShooter(event.getPlayer());
        entity.setVelocity(event.getPlayer().getLocation().getDirection().multiply(1.5));
        entity.setCustomName("Grenada");
        entity.setCustomNameVisible(false);
    }).setLore("It throws GRENADA.").build();

    /**
     * @author minemobs
     * I made this function on Noa's computer
     * @param name Variable name
     * @return {@link Items item}
     */
    public static ItemStack getItemFromName(String name) {
        try {
            return (ItemStack) Items.class.getField(name).get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
