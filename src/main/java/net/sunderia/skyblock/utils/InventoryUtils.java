package net.sunderia.skyblock.utils;

import fr.sunderia.sunderiautils.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {

    public static void fillAll(Inventory inventory, ItemStack itemStack, int rows, boolean cancelled) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for (int i = 0; i < rows * 9; i++) {
            itemStackList.add(itemStack);
        }
        fillAll(inventory, itemStackList, rows, cancelled);
    }

    public static void fillAll(Inventory inventory, ItemStack itemStack, int rows) {
        fillAll(inventory, itemStack, rows, false);
    }

    public static void fillAll(Inventory inventory, List<ItemStack> itemStackList, int rows, boolean cancelled) {
        if(rows > 6 || rows < 1)
            throw new IllegalArgumentException("The rows has to be greater than 0 and lower than 7");
        for (int index = 0; index < rows * 9; index++) {
            inventory.setItem(index, cancelled ? new ItemBuilder(itemStackList.get(index)).onInteract(event -> event.setCancelled(true)).build() : itemStackList.get(index));
        }
    }

    public static void fillAll(Inventory inventory, List<ItemStack> itemStackList, int rows) {
        fillAll(inventory, itemStackList, rows, false);
    }

    public static void fillRow(Inventory inventory, ItemStack itemStack, int row, boolean cancelled) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            itemStackList.add(itemStack);
        }
        fillRow(inventory, itemStackList, row, cancelled);
    }

    public static void fillRow(Inventory inventory, ItemStack itemStack, int row) {
        fillRow(inventory, itemStack, row, false);
    }

    public static void fillRow(Inventory inventory, List<ItemStack> itemStackList, int row, boolean cancelled) {
        if(row > 6 || row < 1)
            throw new IllegalArgumentException("The row has to be greater than 0 and lower than 7");
        if (itemStackList.size() != 9) {
            throw new ArrayIndexOutOfBoundsException("The ItemStack list should have a length of 9 but the list has a length of " + itemStackList.size());
        }
        for (int index = row * 9 - 9; index < row * 9; index++) {
            inventory.setItem(index, cancelled ? new ItemBuilder(itemStackList.get(index - 9 * (row - 1))).onInteract(event -> event.setCancelled(true)).build() : itemStackList.get(index - 9 * (row - 1)));
        }
    }

    public static void fillRow(Inventory inventory, List<ItemStack> itemStackList, int row) {
        fillRow(inventory, itemStackList, row, false);
    }

    public static void fillColumn(Inventory inventory, ItemStack itemStack, int rows, int column, boolean cancelled) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            itemStackList.add(itemStack);
        }
        fillColumn(inventory, itemStackList, rows, column, cancelled);
    }

    public static void fillColumn(Inventory inventory, ItemStack itemStack, int rows, int column) {
        fillColumn(inventory, itemStack, rows, column, false);
    }

    public static void fillColumn(Inventory inventory, List<ItemStack> itemStackList, int rows, int column, boolean cancelled) {
        if(rows > 6 || rows < 1)
            throw new IllegalArgumentException("The rows has to be greater than 0 and lower than 7");
        if(column > 9 || column < 1)
            throw new IllegalArgumentException("The column has to be greater than 0 and lower than 10");
        for (int row = 0; row < rows; row++) {
            inventory.setItem((row * 9) + column - 1, cancelled ? new ItemBuilder(itemStackList.get(row)).onInteract(event -> event.setCancelled(true)).build() : itemStackList.get(row));
        }
    }

    public static void fillColumn(Inventory inventory, List<ItemStack> itemStackList, int rows, int column) {
        fillColumn(inventory, itemStackList, rows, column, false);
    }

    public static void fillBorders(Inventory inventory, ItemStack itemStack, int rows, boolean cancelled) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for (int i = 0; i < (rows == 1 ? 11 : 18) + ((rows - 2) * 2); i++) {
            itemStackList.add(itemStack);
        }
        fillBorders(inventory, itemStackList, rows, cancelled);
    }

    public static void fillBorders(Inventory inventory, ItemStack itemStack, int rows) {
        fillBorders(inventory, itemStack, rows, false);
    }

    public static void fillBorders(Inventory inventory, List<ItemStack> itemStackList, int rows, boolean cancelled) {
        if(rows > 6 || rows < 1)
            throw new IllegalArgumentException("The rows has to be greater than 0 and lower than 7");
        if (itemStackList.size() != ((rows == 1 ? 11 : 18) + ((rows - 2) * 2)))
            throw new ArrayIndexOutOfBoundsException("The ItemStack list should have a length of " + ((rows == 1 ? 11 : 18) + ((rows - 2) * 2)) + " but the list has a length of " + itemStackList.size());
        fillRow(inventory, itemStackList.subList(0, 9), 1);
        if (rows >= 2) fillRow(inventory, itemStackList.subList(itemStackList.size() - 9, itemStackList.size()), rows);
        if (rows >= 3) {
            for (int row = 1, count = 9; row < rows - 1; row++) {
                inventory.setItem((row * 9), cancelled ? new ItemBuilder(itemStackList.get(row + count)).onInteract(event -> event.setCancelled(true)).build() : itemStackList.get(row + count));
                inventory.setItem((row * 9) + 8, cancelled ? new ItemBuilder(itemStackList.get(row + ++count)).onInteract(event -> event.setCancelled(true)).build() : itemStackList.get(row + count));
            }
        }
    }

    public static void fillBorders(Inventory inventory, List<ItemStack> itemStackList, int rows) {
        fillBorders(inventory, itemStackList, rows, false);
    }

    public static void clearAll(Inventory inventory, int rows) {
        fillAll(inventory, new ItemStack(Material.AIR), rows);
    }

}
