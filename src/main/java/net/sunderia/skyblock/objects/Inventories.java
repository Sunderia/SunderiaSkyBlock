package net.sunderia.skyblock.objects;

import fr.sunderia.sunderiautils.utils.InventoryBuilder;
import fr.sunderia.sunderiautils.utils.ItemBuilder;
import net.sunderia.skyblock.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Inventories {

    public static final Inventory TEST_GUI = new InventoryBuilder("Test Gui", 6)
            .onOpen(event -> {
                InventoryUtils.fillAll(event.getInventory(), new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build(), 6);
                InventoryUtils.fillRow(event.getInventory(), new ItemBuilder(Material.RED_STAINED_GLASS_PANE).build(), 6);
                InventoryUtils.fillRectangle(event.getInventory(), new ItemStack(Material.AIR), 2, 2, 4, 4);
            })
            .onClick(event -> {
                if(event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE) event.setCancelled(true);
            })
            .build();

}
