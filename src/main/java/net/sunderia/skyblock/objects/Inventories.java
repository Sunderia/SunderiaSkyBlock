package net.sunderia.skyblock.objects;

import fr.sunderia.sunderiautils.utils.InventoryBuilder;
import net.sunderia.skyblock.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Inventories {

    //public static Inventory TESTGUI = InventoryUtils.fillBorders(new InventoryBuilder("Test Gui", 5).build(), new ItemStack(Material.BLACK_STAINED_GLASS_PANE), 5, false);
    public static final Inventory TESTGUI = new InventoryBuilder("Test Gui", 5)
            .onOpen(e -> InventoryUtils.fillBorders(e.getInventory(), new ItemStack(Material.BLACK_STAINED_GLASS_PANE), 5))
            .build();

}
