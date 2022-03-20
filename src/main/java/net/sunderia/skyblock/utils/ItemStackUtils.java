package net.sunderia.skyblock.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author <a href="https://github.com/Minemobs/MinemobsUtils/blob/main/src/main/java/fr/minemobs/minemobsutils/utils/ItemStackUtils.java">minemobs</a>
 */

public class ItemStackUtils {

    private ItemStackUtils() {
    }

    public static final ItemStack EMPTY = new ItemStack(Material.AIR);

    public static boolean isAnArmor(ItemStack is) {
        return is.getType().name().endsWith("_HELMET") || is.getType().name().endsWith("_CHESTPLATE") || is.getType().name().endsWith("_LEGGINGS") ||
                is.getType().name().endsWith("_BOOTS");
    }

    /**
     * @param first  The first item
     * @param second The second item
     * @return true if the two items are the same
     * @since 1.1
     * @deprecated Use {@link ItemStackUtils#isSameItem(ItemStack, ItemStack)} instead
     */
    @Deprecated(since = "1.1", forRemoval = true)
    public static boolean isSimilar(ItemStack first, ItemStack second) {
        if (isAirOrNull(first) || isAirOrNull(second)) return false;
        ItemMeta im1 = first.getItemMeta();
        ItemMeta im2 = second.getItemMeta();
        if (im1 instanceof Damageable dmg1 && im2 instanceof Damageable dmg2) {
            dmg1.setDamage(dmg2.getDamage());
        }
        first.setItemMeta(im1);
        second.setItemMeta(im2);
        return first.isSimilar(second);
    }

    /**
     * This method use the lore to check if both items have the same name.
     * This method only works on custom items.
     *
     * @param firstItemStack  The first item
     * @param secondItemStack The second item
     * @return true if the two items are the same
     * @since 1.4
     */
    public static boolean isSameItem(ItemStack firstItemStack, ItemStack secondItemStack) {
        if (!hasLore(firstItemStack) || !hasLore(secondItemStack) || !ChatColor.stripColor(firstItemStack.clone().getItemMeta().getLore().get(firstItemStack.clone().getItemMeta().getLore().size() - 1)).startsWith("sunderiaskyblock:") || !ChatColor.stripColor(secondItemStack.clone().getItemMeta().getLore().get(secondItemStack.clone().getItemMeta().getLore().size() - 1)).startsWith("sunderiaskyblock:")) return false;
        return firstItemStack.clone().getItemMeta().getLore().get(firstItemStack.clone().getItemMeta().getLore().size() - 1).equals(secondItemStack.clone().getItemMeta().getLore().get(secondItemStack.clone().getItemMeta().getLore().size() - 1));
    }

    public static boolean isAirOrNull(ItemStack item) {
        return item == null || item.getType().isAir();
    }

    public static boolean isNotAirNorNull(ItemStack is) {
        return !isAirOrNull(is);
    }

    public static boolean hasLore(ItemStack is) {
        return !isAirOrNull(is) && is.hasItemMeta() && is.getItemMeta().hasLore();
    }

    public static Material randomBanner() {
        List<Material> banners = Arrays.stream(Material.values()).filter(banner -> banner.name().endsWith("_BANNER") && !banner.name().endsWith("_WALL_BANNER") &&
                !banner.name().startsWith("LEGACY")).toList();
        return banners.get(new Random().nextInt(banners.size()));
    }

    public static Material randomSkull() {
        List<Material> skulls = Arrays.stream(Material.values()).filter(material -> (!material.name().startsWith("PISTON") && material.name().endsWith("_HEAD") && !material.name().endsWith("_WALL_HEAD")) ||
                (material.name().endsWith("_SKULL") && !material.name().startsWith("LEGACY") && !material.name().endsWith("_WALL_SKULL"))).toList();
        return skulls.get(new Random().nextInt(skulls.size()));
    }

}