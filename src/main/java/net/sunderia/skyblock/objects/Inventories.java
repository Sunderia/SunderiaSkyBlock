package net.sunderia.skyblock.objects;

import fr.sunderia.sunderiautils.utils.InventoryBuilder;
import fr.sunderia.sunderiautils.utils.ItemBuilder;
import net.sunderia.skyblock.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.*;
import java.util.stream.Stream;

public class Inventories {

    public static final Inventory CRAFTING_GUI = new InventoryBuilder("Test Gui", 6)
            .onOpen(event -> {
                InventoryUtils.fillAll(event.getInventory(), new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build(), 6);
                InventoryUtils.fillRow(event.getInventory(), new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").build(), 6);
                InventoryUtils.fillRectangle(event.getInventory(), new ItemStack(Material.AIR), 2, 2, 4, 4);
                InventoryUtils.setSlot(event.getInventory(), new ItemBuilder(Material.BARRIER).setDisplayName("No Craft").build(), 6, 5);
                InventoryUtils.setSlot(event.getInventory(), new ItemBuilder(Material.BARRIER).setDisplayName("No Craft").build(), 3, 6);
            })
            .onClick(event -> {
                if (event.getCurrentItem() != null && (event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE ||
                        event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.BARRIER)) event.setCancelled(true);
                if (event.getSlot() == 23 && event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.BARRIER) {
                    Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30).map(i -> event.getInventory().getItem(i)).filter(Objects::nonNull).forEach(is -> is.setAmount(is.getAmount() - 1));
                }
            })
            .onUpdate(event -> {
                if (event.getInventory().getItem(23) == null || event.getInventory().getItem(23).getType() == Material.AIR) {
                    event.getInventory().setItem(23, new ItemBuilder(Material.BARRIER).setDisplayName("No Craft").build());
                }
                Iterator<Recipe> sourceRecipes = Bukkit.recipeIterator();
                List<ItemStack> specifiedIngredients = new ArrayList<>();
                List.of(10, 11, 12, 19, 20, 21, 28, 29, 30).forEach(index -> specifiedIngredients.add(event.getInventory().getItem(index) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(index)));
                boolean craftable = false;
                while (sourceRecipes.hasNext()) {
                    if (sourceRecipes.next() instanceof ShapedRecipe shapedRecipe) {
                        if (Arrays.stream(shapedRecipe.getIngredientMap().values().toArray()).anyMatch(Objects::nonNull)) {
                            List<ItemStack> neededIngredients = new ArrayList<>();
                            System.out.println(shapedRecipe.getIngredientMap());
                            shapedRecipe.getIngredientMap().values().forEach(itemStack -> neededIngredients.add(itemStack == null ? new ItemStack(Material.AIR) : itemStack));
                            for (int index = 0; index < shapedRecipe.getIngredientMap().size(); index++) {
                                System.out.println("result " + shapedRecipe.getResult());
                                System.out.println("specified item " + specifiedIngredients.get(index));
                                System.out.println("needed item " + neededIngredients.get(index));
                                if (!specifiedIngredients.get(index).isSimilar(neededIngredients.get(index))) {
                                    craftable = false;
                                    break;
                                } else
                                    craftable = true;
                            }
                            System.out.println(craftable);
                            if (craftable) {
                                InventoryUtils.setSlot(event.getInventory(), shapedRecipe.getResult(), 3, 6);
                                break;
                            } else InventoryUtils.setSlot(event.getInventory(), new ItemBuilder(Material.BARRIER).setDisplayName("No Craft").build(), 3, 6);
                        }
                    }
                }
            }, 10, 10)
            .build();

}
