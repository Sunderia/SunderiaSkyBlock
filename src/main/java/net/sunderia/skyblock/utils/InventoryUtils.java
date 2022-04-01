package net.sunderia.skyblock.utils;

import fr.sunderia.sunderiautils.utils.ItemBuilder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryUtils {

    public Inventory fill(Inventory inventory, ItemStack itemStack, int rows, boolean cancelled) {
        for (int index = 0; index < rows * 9; index++) {
            inventory.setItem(index, cancelled ? new ItemBuilder(itemStack).onInteract(event -> event.setCancelled(true)).build() : itemStack);
        }
        return inventory;
    }

    public Inventory fill(Inventory inventory, ItemStack itemStack, int rows) {
        return fill(inventory, itemStack, rows, false);
    }

    public Inventory fill(Inventory inventory, List<ItemStack> itemStack, int rows, boolean cancelled) {
        for (int index = 0; index < rows * 9; index++) {
            inventory.setItem(index, cancelled ? new ItemBuilder(itemStack.get(index)).onInteract(event -> event.setCancelled(true)).build() : itemStack.get(index));
        }
        return inventory;
    }

    public Inventory fill(Inventory inventory, List<ItemStack> itemStack, int rows) {
        return fill(inventory, itemStack, rows, false);
    }

    public Inventory fillRow(Inventory inventory, ItemStack itemStack, int row, boolean cancelled) {
        for (int index = row * 9 - 9; index < row * 9; index++) {
            inventory.setItem(index, cancelled ? new ItemBuilder(itemStack).onInteract(event -> event.setCancelled(true)).build() : itemStack);
        }
        return inventory;
    }

    public Inventory fillRow(Inventory inventory, ItemStack itemStack, int row) {
        return fillRow(inventory, itemStack, row, false);
    }

    public Inventory fillRow(Inventory inventory, List<ItemStack> itemStack, int row, boolean cancelled) {
        for (int index = row * 9 - 9; index < row * 9; index++) {
            inventory.setItem(index, cancelled ? new ItemBuilder(itemStack.get(index)).onInteract(event -> event.setCancelled(true)).build() : itemStack.get(index));
        }
        return inventory;
    }

    public Inventory fillRow(Inventory inventory, List<ItemStack> itemStack, int row) {
        return fillRow(inventory, itemStack, row, false);
    }

    public Inventory fillColumn(Inventory inventory, ItemStack itemStack, int column, int rows, boolean cancelled) {
        for (int index = column - 1; index < column + (rows * 9); index += 9) {
            inventory.setItem(index, cancelled ? new ItemBuilder(itemStack).onInteract(event -> event.setCancelled(true)).build() : itemStack);
        }
        return inventory;
    }

    public Inventory fillColumn(Inventory inventory, ItemStack itemStack, int column, int row) {
        return fillColumn(inventory, itemStack, column, row, false);
    }

    public Inventory fillColumn(Inventory inventory, List<ItemStack> itemStack, int column, int rows, boolean cancelled) {
        for (int index = column - 1; index < column + (rows * 9); index += 9) {
            inventory.setItem(index, cancelled ? new ItemBuilder(itemStack.get(index)).onInteract(event -> event.setCancelled(true)).build() : itemStack.get(index));
        }
        return inventory;
    }

    public Inventory fillColumn(Inventory inventory, List<ItemStack> itemStack, int column, int row) {
        return fillColumn(inventory, itemStack, column, row, false);
    }

}
