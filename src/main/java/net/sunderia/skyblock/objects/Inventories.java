package net.sunderia.skyblock.objects;

import fr.sunderia.sunderiautils.utils.InventoryBuilder;
import fr.sunderia.sunderiautils.utils.ItemBuilder;
import fr.sunderia.sunderiautils.utils.ItemStackUtils;
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
                InventoryUtils.setSlot(event.getInventory(), new ItemBuilder(Material.BARRIER).setDisplayName("No Craft").build(), 3, 6);
                InventoryUtils.setSlot(event.getInventory(), new ItemBuilder(Material.BARRIER).setDisplayName("No Craft").build(), 6, 5);
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
                //List<ItemStack> specifiedIngredients = new ArrayList<>();
                ItemStack[] specifiedIngredients = new ItemStack[9];
                final int[] index = {0};
                List.of(10, 11, 12, 19, 20, 21, 28, 29, 30).forEach(i -> specifiedIngredients[index[0]++] = event.getInventory().getItem(i) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(i));
                while (sourceRecipes.hasNext()) {
                    if(sourceRecipes.next() instanceof ShapedRecipe recipe && craftShapedRecipe(recipe, specifiedIngredients, event.getInventory())) {
                        break;
                    }
                }
            }, 10, 10)
            .build();


    private static boolean craftShapedRecipe(ShapedRecipe recipe, ItemStack[] specifiedIngredients, Inventory inventory) {
        boolean craftable = false;
        if (Arrays.stream(recipe.getIngredientMap().values().toArray()).anyMatch(Objects::nonNull)) {
            List<ItemStack> neededIngredients = new ArrayList<>();
            recipe.getIngredientMap().values().forEach(itemStack -> neededIngredients.add(itemStack == null ? new ItemStack(Material.AIR) : itemStack));
            for (int i = 0; i < recipe.getIngredientMap().size(); i++) {
                ItemStack specifiedIngredient = specifiedIngredients[i];
                ItemStack neededIngredient = neededIngredients.get(i);
                if ((specifiedIngredient.getType() != Material.AIR || neededIngredient.getType() != Material.AIR) &&
                        (!ItemStackUtils.isSimilar(specifiedIngredient, neededIngredient) || neededIngredients.get(i).getAmount() > 1)) {
                    craftable = false;
                    break;
                }
                craftable = true;
            }
            if (craftable) {
                InventoryUtils.setSlot(inventory, recipe.getResult(), 3, 6);
            } else InventoryUtils.setSlot(inventory, new ItemBuilder(Material.BARRIER).setDisplayName("No Craft").build(), 3, 6);
        }
        return craftable;
    }

}
