package net.sunderia.skyblock.objects;

import fr.sunderia.sunderiautils.utils.InventoryBuilder;
import fr.sunderia.sunderiautils.utils.ItemBuilder;
import fr.sunderia.sunderiautils.utils.ItemStackUtils;
import net.sunderia.skyblock.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.*;

import java.util.*;
import java.util.stream.Stream;

public class Inventories {

    /*public static final Inventory CRAFTING_GUI = new InventoryBuilder("Test Gui", 6)
            .onOpen(event -> {
                InventoryUtils.fillAll(event.getInventory(), new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build(), 6);
                InventoryUtils.fillRow(event.getInventory(), new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").build(), 6);
                InventoryUtils.fillRectangle(event.getInventory(), new ItemStack(Material.AIR), 2, 2, 4, 4);
                InventoryUtils.setSlot(event.getInventory(), new ItemBuilder(Material.BARRIER).setDisplayName("No Craft").build(), 3, 6);
                InventoryUtils.setSlot(event.getInventory(), new ItemBuilder(Material.BARRIER).setDisplayName("Exit").build(), 6, 5);
            })
            .onClick(event -> {
                if (event.getCurrentItem() != null && (event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.BARRIER))
                    event.setCancelled(true);
                if (event.getSlot() == 23 && event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.BARRIER)
                    Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30).map(i -> event.getInventory().getItem(i)).filter(Objects::nonNull).forEach(is -> is.setAmount(is.getAmount() - 1));
            })
            .onUpdate(event -> {
                if (event.getInventory().getItem(23) == null || event.getInventory().getItem(23).getType() == Material.AIR)
                    event.getInventory().setItem(23, new ItemBuilder(Material.BARRIER).setDisplayName("No Craft").build());
                Iterator<Recipe> sourceRecipes = Bukkit.recipeIterator();
                List<ItemStack> specifiedIngredients = Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30).map(i -> event.getInventory().getItem(i) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(i)).toList();
                while (sourceRecipes.hasNext()) {
                    Recipe recipe = sourceRecipes.next();
                    if((recipe instanceof ShapedRecipe shapedRecipe && craftShapedRecipe(shapedRecipe, specifiedIngredients, event.getInventory())) || (recipe instanceof ShapelessRecipe shapelessRecipe && craftShapelessRecipe(shapelessRecipe, specifiedIngredients, event.getInventory())))
                        break;
                }
            }, 10, 10)
            .build();*/

    public static final Inventory CRAFTING_GUI = new InventoryBuilder("Test Gui", new InventoryBuilder.Shape("""
            BBBBBBBBB
            B   BBBBB
            B   BNBBB
            B   BBBBB
            BBBBBBBBB
            RRRRERRRR
            """, Map.of('B', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build(),
            'R', new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").build(),
            'N', new ItemBuilder(Material.BARRIER).setDisplayName("No Craft").build(),
            'E', new ItemBuilder(Material.BARRIER).setDisplayName("Exit").build())))
            .onClick(event -> {
                if (event.getCurrentItem() != null && (event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE ||
                        event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.BARRIER))
                    event.setCancelled(true);
                if (event.getSlot() == 23 && ItemStackUtils.isNotAirNorNull(event.getCurrentItem()) && event.getCurrentItem().getType() != Material.BARRIER)
                    Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30).map(i -> event.getInventory().getItem(i)).filter(Objects::nonNull)
                            .forEach(is -> is.setAmount(is.getAmount() - getAmount(Bukkit.getRecipesFor(event.getCurrentItem()).get(0), is)));
            })
            .onUpdate(event -> {
                if (event.getInventory().getItem(23) == null || event.getInventory().getItem(23).getType() == Material.AIR)
                    event.getInventory().setItem(23, new ItemBuilder(Material.BARRIER).setDisplayName("No Craft").build());
                Iterator<Recipe> sourceRecipes = Bukkit.recipeIterator();
                int[][] craftingSlots = new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}};
                @SuppressWarnings("ConstantConditions")
                List<List<ItemStack>> specifiedIngredients = Stream.of(0, 1, 2).map(x -> Stream.of(0, 1, 2)
                        .map(y -> event.getInventory().getItem(craftingSlots[x][y]) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(craftingSlots[x][y]))
                        .toList()).toList();
                while (sourceRecipes.hasNext()) {
                    Recipe recipe = sourceRecipes.next();
                    if ((recipe instanceof ShapedRecipe shapedRecipe && craftShapedRecipe(shapedRecipe, specifiedIngredients, event.getInventory())) ||
                            (recipe instanceof ShapelessRecipe shapelessRecipe && craftShapelessRecipe(shapelessRecipe, specifiedIngredients, event.getInventory())))
                        break;
                }
            }, 10, 10)
            .build();

    private static boolean craftShapedRecipe(ShapedRecipe shapedRecipe, List<List<ItemStack>> specifiedIngredients, Inventory inventory) {
        boolean craftable = false;
        int coordinateX = 0;
        int coordinateY = 0;
        int possiblePos = switch (shapedRecipe.getShape().length * shapedRecipe.getShape()[0].length()) {
            case 2 -> 6;
            case 6 -> 2;
            case 3 -> 3;
            case 4 -> 4;
            case 9 -> 1;
            default -> throw new IllegalStateException("Unexpected value: " + shapedRecipe.getShape().length * shapedRecipe.getShape()[0].length());
        };
        List<ItemStack[][]> positions = new ArrayList<>(possiblePos);
        for(int i = 0; i < possiblePos; i++){
            ItemStack[][] position = new ItemStack[][]{new ItemStack[3], new ItemStack[3], new ItemStack[3]};
            for(int y = 0; y < shapedRecipe.getShape().length; y++){
                for(int x = 0; x < shapedRecipe.getShape()[0].length(); x++){
                    position[y + coordinateY][x + coordinateX] = shapedRecipe.getIngredientMap().get(shapedRecipe.getShape()[y].charAt(x));
                }
            }
            positions.add(Arrays.stream(position).map(y -> Arrays.stream(y).map(x -> x == null ? new ItemStack(Material.AIR) : x).toArray(ItemStack[]::new)).toArray(ItemStack[][]::new));
            if(coordinateX + 1 > 3 - shapedRecipe.getShape()[0].length()) {
                coordinateX = 0;
                coordinateY++;
            } else coordinateX++;
        }
        for(ItemStack[][] position : positions) {
            for(int y = 0; y < 3; y++) {
                for(int x = 0; x < 3; x++) {
                    if(!position[y][x].isSimilar(specifiedIngredients.get(y).get(x))) {
                        craftable = false;
                        break;
                    } else {
                        if(position[y][x].getAmount() > 1 && specifiedIngredients.get(y).get(x).getAmount() < position[y][x].getAmount()) {
                            craftable = false;
                            break;
                        }
                        craftable = true;
                    }
                }
                if(!craftable)
                    break;
            }
            if(craftable)
                break;
        }
        InventoryUtils.setItem(inventory, craftable ? shapedRecipe.getResult() : new ItemBuilder(Material.BARRIER).setDisplayName("No Craft").build(), 3, 6);
        return craftable;
    }

    private static int getAmount(Recipe r, ItemStack is) {
       if(r instanceof ShapelessRecipe recipe) {
           //TODO: Implement
           return 1;
       } else if(r instanceof ShapedRecipe recipe) {
           return recipe.getIngredientMap().values().stream().filter(stack -> ItemStackUtils.isSimilar(stack, is)).findFirst().orElse(new ItemStack(Material.AIR)).getAmount();
       }
         return 1;
    }

    private static boolean craftShapelessRecipe(ShapelessRecipe shapelessRecipe, List<List<ItemStack>> specifiedIngredients, Inventory inventory) {
        boolean craftable = false;

        return craftable;
    }
}
