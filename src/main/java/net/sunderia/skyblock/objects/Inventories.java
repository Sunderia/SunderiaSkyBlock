package net.sunderia.skyblock.objects;

import fr.sunderia.sunderiautils.utils.InventoryBuilder;
import net.sunderia.skyblock.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Inventories {

    public static final Inventory TEST_GUI = new InventoryBuilder("Test Gui", 6).onOpen(event -> {
        InventoryUtils.fillRectangle(event.getInventory(), 2, 2, 5, 6, new ItemStack(Material.STONE));
    }).build();

}
