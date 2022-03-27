package net.sunderia.skyblock.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public Inventory fill(Inventory inventory, ItemStack itemStack, int rows){
        for(int index = 0; index < rows * 9; index++){
            inventory.setItem(index, itemStack);
        }
        return inventory;
    }

    public Inventory fillRow(Inventory inventory, ItemStack itemStack, int row){
        for(int index = row * 9 - 9; index < row * 9; index++){
            inventory.setItem(index, itemStack);
        }
        return inventory;
    }

    public Inventory fillColumn(Inventory inventory, ItemStack itemStack, int column, int rows){
        for(int index = column - 1; index < column + (rows * 9); index += 9){
            inventory.setItem(index, itemStack);
        }
        return inventory;
    }

}
