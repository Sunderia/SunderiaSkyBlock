package net.sunderia.skyblock.utils;

import net.sunderia.skyblock.SunderiaSkyblock;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="https://github.com/Minemobs/MinemobsUtils/blob/main/src/main/java/fr/minemobs/minemobsutils/utils/InventoryBuilder.java">minemobs</a>
 */

public class InventoryBuilder implements Listener {

    private int rows = 3;
    private String name;
    private final List<ItemStack> itemStacks;
    private Consumer<InventoryClickEvent> clickEventConsumer = InventoryEvent::getInventory;
    private Consumer<InventoryOpenEvent> openEventConsumer = InventoryEvent::getInventory;
    private Consumer<InventoryEvent> updateEventConsumer;
    private BukkitRunnable runnable;
    private int runnableTime = 20;
    private boolean cancelEvent = false;

    public InventoryBuilder(@NotNull String name) {
        this.name = name;
        this.itemStacks = new ArrayList<>();
    }

    public InventoryBuilder(@NotNull String name, int rows) {
        this.name = name;
        this.itemStacks = new ArrayList<>();
        this.setRows(rows);
    }

    public InventoryBuilder onOpen(Consumer<InventoryOpenEvent> eventConsumer) {
        this.openEventConsumer = eventConsumer;
        return this;
    }

    public InventoryBuilder onClick(Consumer<InventoryClickEvent> eventConsumer) {
        this.clickEventConsumer = eventConsumer;
        return this;
    }

    public InventoryBuilder onUpdate(Consumer<InventoryEvent> eventConsumer) {
        this.updateEventConsumer = eventConsumer;
        return this;
    }

    public InventoryBuilder onUpdate(Consumer<InventoryEvent> eventConsumer, int timeInSeconds) {
        this.updateEventConsumer = eventConsumer;
        this.runnableTime = timeInSeconds * 20;
        return this;
    }

    public InventoryBuilder setCancelled() {
        this.cancelEvent = !cancelEvent;
        return this;
    }

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        if(event.getInventory().getSize() != getSize() || !event.getView().getTitle().equalsIgnoreCase(name)) return;
        event.setCancelled(cancelEvent);
        this.clickEventConsumer.accept(event);
    }

    @EventHandler
    private void onOpen(InventoryOpenEvent event) {
        if(event.getInventory().getType() != InventoryType.CHEST || event.getInventory().getSize() != getSize() || !event.getView().getTitle().equalsIgnoreCase(name)) return;
        if(runnable != null) {
            this.runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    onTickUpdate(event);
                }
            };
            runnable.runTaskTimer(SunderiaSkyblock.getInstance(), runnableTime, runnableTime);
        }
        this.openEventConsumer.accept(event);
    }

    private void onTickUpdate(InventoryEvent event) {
        if(event.getInventory().getType() != InventoryType.CHEST || event.getInventory().getSize() != getSize() || !event.getView().getTitle().equalsIgnoreCase(name)) return;
        this.updateEventConsumer.accept(event);
    }

    @EventHandler
    private void onClose(InventoryCloseEvent event) {
        if(event.getInventory().getType() != InventoryType.CHEST || event.getInventory().getSize() != getSize() || !event.getView().getTitle().equalsIgnoreCase(name)) return;
        if(runnable == null) return;
        this.runnable.cancel();
        this.runnable = null;
    }

    public InventoryBuilder setRows(int rows) {
        if(rows > 6 || rows < 1) rows = 3;
        this.rows = rows;
        return this;
    }

    public InventoryBuilder addItems(ItemStack... itemStacks) {
        this.itemStacks.addAll(Arrays.asList(itemStacks));
        return this;
    }

    public InventoryBuilder addItems(List<ItemStack> itemStacks) {
        return addItems(itemStacks.toArray(itemStacks.toArray(new ItemStack[0])));
    }

    public InventoryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    private int getSize() {
        return rows * 9;
    }

    public Inventory build() {
        Bukkit.getServer().getPluginManager().registerEvents(this, SunderiaSkyblock.getInstance());
        Inventory inv = Bukkit.createInventory(null, rows * 9, name);
        itemStacks.forEach(inv::addItem);
        return inv;
    }

}
