package net.sunderia.skyblock.objects;

import net.sunderia.skyblock.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Items {

    PICKAXE_3X3(new ItemBuilder(Material.IRON_PICKAXE).onInteract(event -> event.getPlayer().sendMessage("Test good")).setLore("This is a 3x3 pickaxe like a hammer").build());

    public final ItemStack itemStack;

    Items(ItemStack stack) {this.itemStack = stack.clone();}

    public ItemStack getItemStack() {
        return itemStack;
    }

}
