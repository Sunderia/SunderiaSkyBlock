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
        if(itemStackList.size() != rows * 9)
            throw new ArrayIndexOutOfBoundsException("The ItemStack list should have a length of" + rows * 9 + "but the list has a length of " + itemStackList.size());
        for (int index = 0; index < rows * 9; index++) {
            ItemStack item = itemStackList.get(index);
            inventory.setItem(index, cancelled ? !item.hasItemMeta() || item.getItemMeta().getDisplayName().isEmpty() ? new ItemBuilder(item).setDisplayName(item.getType().name()).onInteract(event -> event.setCancelled(true)).build() : new ItemBuilder(item).onInteract(event -> event.setCancelled(true)).build() : !item.hasItemMeta() || item.getItemMeta().getDisplayName().isEmpty() ? new ItemBuilder(item).setDisplayName(item.getType().name()).build() : item);
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
            ItemStack item = itemStackList.get(index - 9 * (row - 1));
            inventory.setItem(index, cancelled ? !item.hasItemMeta() || item.getItemMeta().getDisplayName().isEmpty() ? new ItemBuilder(item).setDisplayName(item.getType().name()).onInteract(event -> event.setCancelled(true)).build() : new ItemBuilder(item).onInteract(event -> event.setCancelled(true)).build() : !item.hasItemMeta() || item.getItemMeta().getDisplayName().isEmpty() ? new ItemBuilder(item).setDisplayName(item.getType().name()).build() : item);
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
        if(itemStackList.size() != rows)
            throw new ArrayIndexOutOfBoundsException("The ItemStack list should have a length of " + rows + " but the list has a length of " + itemStackList.size());
        for (int row = 0; row < rows; row++) {
            ItemStack item = itemStackList.get(row);
            inventory.setItem((row * 9) + column - 1, cancelled ? !item.hasItemMeta() || item.getItemMeta().getDisplayName().isEmpty() ? new ItemBuilder(item).setDisplayName(item.getType().name()).onInteract(event -> event.setCancelled(true)).build() : new ItemBuilder(item).onInteract(event -> event.setCancelled(true)).build() : !item.hasItemMeta() || item.getItemMeta().getDisplayName().isEmpty() ? new ItemBuilder(item).setDisplayName(item.getType().name()).build() : item);
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
                inventory.setItem((row * 9), cancelled ? !itemStackList.get(row + count).hasItemMeta() || itemStackList.get(row + count).getItemMeta().getDisplayName().isEmpty() ? new ItemBuilder(itemStackList.get(row + count)).setDisplayName(itemStackList.get(row + count).getType().name()).onInteract(event -> event.setCancelled(true)).build() : new ItemBuilder(itemStackList.get(row + count)).onInteract(event -> event.setCancelled(true)).build() : !itemStackList.get(row + count).hasItemMeta() || itemStackList.get(row + count).getItemMeta().getDisplayName().isEmpty() ? new ItemBuilder(itemStackList.get(row + count)).setDisplayName(itemStackList.get(row + count).getType().name()).build() : itemStackList.get(row + count));
                inventory.setItem((row * 9) + 8, cancelled ? !itemStackList.get(row + ++count).hasItemMeta() || itemStackList.get(row + count).getItemMeta().getDisplayName().isEmpty() ? new ItemBuilder(itemStackList.get(row + count)).setDisplayName(itemStackList.get(count + row).getType().name()).onInteract(event -> event.setCancelled(true)).build() : new ItemBuilder(itemStackList.get(row + count)).onInteract(event -> event.setCancelled(true)).build() : !itemStackList.get(row + ++count).hasItemMeta() || itemStackList.get(row + count).getItemMeta().getDisplayName().isEmpty() ? new ItemBuilder(itemStackList.get(row + count)).setDisplayName(itemStackList.get(row + count).getType().name()).build() : itemStackList.get(row + count));
            }
        }
    }

    public static void fillBorders(Inventory inventory, List<ItemStack> itemStackList, int rows) {
        fillBorders(inventory, itemStackList, rows, false);
    }

    public static void fillRectangle(Inventory inventory, int fromRow, int fromColumn, int toRow, int toColumn, List<ItemStack> itemStackList, boolean cancelled) {
        if(fromRow > 6 || fromRow < 1)
            throw new IllegalArgumentException("The fromRow has to be greater than 0 and lower than 7");
        if(fromColumn > 9 || fromColumn < 1)
            throw new IllegalArgumentException("The fromColumn has to be greater than 0 and lower than 10");
        if (itemStackList.size() != ((toRow - fromRow + 1) * (toColumn - fromColumn + 1)))
            throw new ArrayIndexOutOfBoundsException("The ItemStack list should have a length of " + ((toRow - fromRow + 1) * (toColumn - fromColumn + 1)) + " but the list has a length of " + itemStackList.size());
        for (int row = fromRow - 1; row < toRow; row++) {
            for(int column = fromColumn - 1; column < toColumn; column++) {
                System.out.println("row " + row + " column " + column);
                System.out.println("(column - (fromRow - 1)) " + (column - (fromRow - 1)));
                System.out.println("(row - (fromRow - 1)) " + (row - (fromRow - 1)));
                System.out.println("((row - (fromRow - 1)) * (toColumn - fromColumn + 1)) " + ((row - (fromRow - 1)) * (toColumn - fromColumn + 1)));
                System.out.println("(column - (fromRow - 1)) + ((row - (fromRow - 1)) * (toColumn - fromColumn + 1)) " + ((column - (fromRow - 1)) + ((row - (fromRow - 1)) * (toColumn - fromColumn + 1))));
                ItemStack item = itemStackList.get(((column - (fromRow - 1)) + ((row - (fromRow - 1)) * (toColumn - fromColumn + 1))));
                inventory.setItem((row * 9) + column, cancelled ? !item.hasItemMeta() || item.getItemMeta().getDisplayName().isEmpty() ? new ItemBuilder(item).onInteract(event -> event.setCancelled(true)).setDisplayName(item.getType().name()).build() : new ItemBuilder(item).onInteract(event -> event.setCancelled(true)).build() : !item.hasItemMeta() || item.getItemMeta().getDisplayName().isEmpty() ? item : new ItemBuilder(item).setDisplayName(item.getType().name()).build());
            }
        }
    }

    public static void fillRectangle(Inventory inventory, int fromRow, int fromColumn, int toRow, int toColumn, List<ItemStack> itemStackList) {
        fillRectangle(inventory, fromRow, fromColumn, toRow, toColumn, itemStackList, false);
    }

    public static void fillRectangle(Inventory inventory, int fromRow, int fromColumn, int toRow, int toColumn, ItemStack itemStack, boolean cancelled) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for (int i = 0; i < ((toRow - fromRow + 1) * (toColumn - fromColumn + 1)); i++) {
            itemStackList.add(itemStack);
        }
        fillRectangle(inventory, fromRow, fromColumn, toRow, toColumn, itemStackList, cancelled);
    }

    public static void fillRectangle(Inventory inventory, int fromRow, int fromColumn, int toRow, int toColumn, ItemStack itemStack) {
        fillRectangle(inventory, fromRow, fromColumn, toRow, toColumn, itemStack, false);
    }

    public static void clearAll(Inventory inventory, int rows) {
        fillAll(inventory, new ItemStack(Material.AIR), rows);
    }

}
