package net.sunderia.skyblock.objects;

import fr.sunderia.sunderiautils.utils.InventoryBuilder;
import org.bukkit.inventory.Inventory;

/**
 * @author <a href="https://github.com/NoaLeGeek/MinemobsUtils/blob/main/src/main/java/fr/minemobs/minemobsutils/objects/Inventories.java">minemobs</a>
 */

public enum Inventories {

    TESTGUI(new InventoryBuilder("Test Gui", 3, 3).build());

    public final Inventory inv;

    Inventories(Inventory inv) {
        this.inv = inv;
    }

}
