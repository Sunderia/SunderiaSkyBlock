package net.sunderia.skyblock.utils;

import fr.sunderia.sunderiautils.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {

    public static Inventory fillAll(Inventory inventory, ItemStack itemStack, int rows, boolean cancelled) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for(int i = 0; i < rows * 9; i++){
            itemStackList.add(itemStack);
        }
        return fillAll(inventory, itemStackList, rows, cancelled);
    }

    public static Inventory fillAll(Inventory inventory, ItemStack itemStack, int rows) {
        return fillAll(inventory, itemStack, rows, false);
    }

    public static Inventory fillAll(Inventory inventory, List<ItemStack> itemStack, int rows, boolean cancelled) {
        for (int index = 0; index < rows * 9; index++) {
            inventory.setItem(index, cancelled ? new ItemBuilder(itemStack.get(index)).onInteract(event -> event.setCancelled(true)).build() : itemStack.get(index));
        }
        return inventory;
    }

    public static Inventory fillAll(Inventory inventory, List<ItemStack> itemStack, int rows) {
        return fillAll(inventory, itemStack, rows, false);
    }

    public static Inventory fillRow(Inventory inventory, ItemStack itemStack, int rows, boolean cancelled) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            itemStackList.add(itemStack);
        }
        return fillRow(inventory, itemStackList, rows, cancelled);
    }

    public static Inventory fillRow(Inventory inventory, ItemStack itemStack, int rows) {
        return fillRow(inventory, itemStack, rows, false);
    }

    public static Inventory fillRow(Inventory inventory, List<ItemStack> itemStack, int rows, boolean cancelled) {
        for (int index = rows * 9 - 9; index < rows * 9; index++) {
            inventory.setItem(index, cancelled ? new ItemBuilder(itemStack.get(index - 9 * (rows - 1))).onInteract(event -> event.setCancelled(true)).build() : itemStack.get(index - 9 * (rows - 1)));
        }
        return inventory;
    }

    public static Inventory fillRow(Inventory inventory, List<ItemStack> itemStack, int rows) {
        return fillRow(inventory, itemStack, rows, false);
    }

    public static Inventory fillColumn(Inventory inventory, ItemStack itemStack, int column, int rows, boolean cancelled) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            itemStackList.add(itemStack);
        }
        return fillColumn(inventory, itemStackList, column, rows, cancelled);
    }

    public static Inventory fillColumn(Inventory inventory, ItemStack itemStack, int column, int rows) {
        return fillColumn(inventory, itemStack, column, rows, false);
    }

    public static Inventory fillColumn(Inventory inventory, List<ItemStack> itemStack, int column, int rows, boolean cancelled) {
        for (int row = 0; row < rows; row++) {
            inventory.setItem(row * 9 + column - 1, cancelled ? new ItemBuilder(itemStack.get(row)).onInteract(event -> event.setCancelled(true)).build() : itemStack.get(row));
        }
        return inventory;
    }

    public static Inventory fillColumn(Inventory inventory, List<ItemStack> itemStack, int column, int rows) {
        return fillColumn(inventory, itemStack, column, rows, false);
    }

    public static Inventory clearAll(Inventory inventory, int rows){
        return fillAll(inventory, new ItemStack(Material.AIR), rows);
    }

}
