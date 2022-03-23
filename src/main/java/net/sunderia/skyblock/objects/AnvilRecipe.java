package net.sunderia.skyblock.objects;

import net.sunderia.skyblock.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author <a href="https://github.com/Minemobs/MinemobsUtils/blob/main/src/main/java/fr/minemobs/minemobsutils/objects/AnvilRecipe.java">minemobs</a>
 */

public class AnvilRecipe {

    private final ItemStack base;
    private final ItemStack addition;
    private final ItemStack result;

    public AnvilRecipe(ItemStack base, ItemStack addition, ItemStack result) {
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public AnvilRecipe(Material base, ItemStack addition, ItemStack result) {
        this(new ItemStack(base), addition, result);
    }

    public AnvilRecipe(ItemStack base, Material addition, ItemStack result) {
        this(base, new ItemStack(addition), result);
    }

    public AnvilRecipe(Material base, Material addition, ItemStack result) {
        this(new ItemStack(base), new ItemStack(addition), result);
    }

    public ItemStack getBase() {
        return base;
    }

    public ItemStack getAddition() {
        return addition;
    }

    public ItemStack getResult() {
        return result;
    }

    public boolean isEquals(AnvilInventory inv) {
        return ItemStackUtils.isSameItem(inv.getItem(0), getBase()) && ItemStackUtils.isSameItem(inv.getItem(1), getAddition());
    }
}