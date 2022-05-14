package net.sunderia.skyblock.objects;

import fr.sunderia.sunderiautils.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class Items {

    public static final ItemStack PICKAXE_3X3 = new ItemBuilder(Material.IRON_PICKAXE).setDisplayName("3x3 Pickaxe").setLore("This is a 3x3 pickaxe like a hammer").onInteract(event -> event.getPlayer().sendMessage("Test pickaxe")).setLore("It throws GRENADA.").build();
    public static final ItemStack MINEMOBS_GUN = new ItemBuilder(Material.IRON_HORSE_ARMOR).setDisplayName("Minemobs gun")
            .setLore("Minemobs's gun, he coded all himself in like 2 minutes while noa was eating his cereal.").onInteract(event -> {
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
        Snowball entity = (Snowball) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.SNOWBALL);
        entity.setShooter(event.getPlayer());
        entity.setVelocity(event.getPlayer().getLocation().getDirection().multiply(1.5));
        entity.setCustomName("Grenada");
        entity.setCustomNameVisible(false);
    }).setLore("It throws GRENADA.").build();

    /**
     * @param name Item name
     * @return {@link Items item}
     * @author minemobs
     */
    public static ItemStack getItemFromName(String name) {
        try {
            return (ItemStack) Items.class.getField(name.replace(" ", "_").toUpperCase(Locale.ROOT)).get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
