package net.sunderia.skyblock.objects;

import fr.sunderia.sunderiautils.utils.InventoryBuilder;
import fr.sunderia.sunderiautils.utils.ItemBuilder;
import net.sunderia.skyblock.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.*;

import java.util.*;
import java.util.stream.Stream;

public class Inventories {
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
                /*
                First part : Verify if the clicked item isn't null and if the clicked item is a black stained glass pane, a green stained glass pane, a red stained glass pane or a barrier
                Second part : Verify if the ingredients are still well placed while the recipe's result is already set in the result slot (24th slot)
                 */
                if(InventoryUtils.isSameSlot(event.getSlot(), 6, 5) && InventoryUtils.getItem(event.getInventory(), 6, 5) != null && InventoryUtils.getItem(event.getInventory(), 6, 5).isSimilar(new ItemBuilder(Material.BARRIER).setDisplayName("Exit").build()))
                    event.getWhoClicked().closeInventory();
                int[][] craftingSlots = new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}};
                List<List<ItemStack>> specifiedIngredients = Stream.of(0, 1, 2).map(x -> Stream.of(0, 1, 2).map(y -> event.getInventory().getItem(craftingSlots[x][y]) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(craftingSlots[x][y])).toList()).toList();
                if (event.getCurrentItem() != null && (((event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.BARRIER)) || (InventoryUtils.getItem(event.getInventory(), 3, 6).getType() != Material.BARRIER && (Bukkit.getRecipesFor(InventoryUtils.getItem(event.getInventory(), 3, 6)).stream().filter(recipe -> recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe).findFirst().get() instanceof ShapedRecipe recipe ? !craftShapedRecipe(event.getInventory(), recipe, specifiedIngredients) : !craftShapelessRecipe(event.getInventory(), (ShapelessRecipe) Bukkit.getRecipesFor(InventoryUtils.getItem(event.getInventory(), 3, 6)).stream().filter(recipe -> recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe).findFirst().get(), specifiedIngredients)))))
                    event.setCancelled(true);
                if (InventoryUtils.isSameSlot(event.getSlot(), 3, 6) && event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.BARRIER)
                    Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30).map(i -> event.getInventory().getItem(i)).filter(Objects::nonNull).forEach(is -> is.setAmount(is.getAmount() - 1));
            })
            .onUpdate(event -> {
                if (InventoryUtils.getItem(event.getInventory(), 3, 6) == null || InventoryUtils.getItem(event.getInventory(), 3, 6).getType() == Material.AIR)
                    InventoryUtils.setItem(event.getInventory(), new ItemBuilder(Material.BARRIER).setDisplayName("Recipe excepted").build(), 3, 6);
                Iterator<Recipe> sourceRecipes = Bukkit.recipeIterator();
                int[][] craftingSlots = new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}};
                List<List<ItemStack>> specifiedIngredients = Stream.of(0, 1, 2).map(x -> Stream.of(0, 1, 2).map(y -> event.getInventory().getItem(craftingSlots[x][y]) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(craftingSlots[x][y])).toList()).toList();
                while (sourceRecipes.hasNext()) {
                    Recipe recipe = sourceRecipes.next();
                    if ((recipe instanceof ShapedRecipe shapedRecipe && craftShapedRecipe(event.getInventory(), shapedRecipe, specifiedIngredients)) || (recipe instanceof ShapelessRecipe shapelessRecipe && craftShapelessRecipe(event.getInventory(), shapelessRecipe, specifiedIngredients)))
                        break;
                }
            }, 10, 10)
            .build();

    private static boolean craftShapedRecipe(Inventory inventory, ShapedRecipe shapedRecipe, List<List<ItemStack>> specifiedIngredients) {
        boolean craftable = false;
        int coordinateX = 0;
        int coordinateY = 0;
        List<ItemStack[][]> positions = new ArrayList<>(switch (shapedRecipe.getShape().length * shapedRecipe.getShape()[0].length()) {
            case 2 -> 6;
            case 6 -> 2;
            case 3 -> 3;
            case 4 -> 4;
            case 9 -> 1;
            default -> throw new IllegalStateException("Unexpected value: " + shapedRecipe.getShape().length * shapedRecipe.getShape()[0].length());
        });
        for(int i = 0; i < switch (shapedRecipe.getShape().length * shapedRecipe.getShape()[0].length()) {
            case 2 -> 6;
            case 6 -> 2;
            case 3 -> 3;
            case 4 -> 4;
            case 9 -> 1;
            default -> throw new IllegalStateException("Unexpected value: " + shapedRecipe.getShape().length * shapedRecipe.getShape()[0].length());
        }; i++){
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
            for(int y = 0; y < 3; y++){
                for(int x = 0; x < 3; x++){
                    if(!position[y][x].isSimilar(specifiedIngredients.get(y).get(x))) {
                        craftable = false;
                        break;
                    } else
                        craftable = true;
                }
                if(!craftable)
                    break;
            }
            if(craftable)
                break;
        }
        InventoryUtils.setItem(inventory, craftable ? shapedRecipe.getResult() : new ItemBuilder(Material.BARRIER).setDisplayName("No Craft").build(), 3, 6);
        InventoryUtils.fillRectangle(inventory, craftable ? new ItemStack(Material.GREEN_STAINED_GLASS_PANE) : new ItemStack(Material.RED_STAINED_GLASS_PANE), 6, 1, 6, 4);
        InventoryUtils.fillRectangle(inventory, craftable ? new ItemStack(Material.GREEN_STAINED_GLASS_PANE) : new ItemStack(Material.RED_STAINED_GLASS_PANE), 6, 6, 6, 9);
        return craftable;
    }

    private static boolean craftShapelessRecipe(Inventory inventory, ShapelessRecipe shapelessRecipe, List<List<ItemStack>> specifiedIngredients) {
        boolean craftable = false;

        return craftable;
    }
}
