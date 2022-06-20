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

    public static final Inventory TEST_GUI = new InventoryBuilder("Test Gui", new InventoryBuilder.Shape("""
            DDDDDDDDD
            DDDDDDDDD
            DDDDDDDDD
            DDDDDDDDD
            DDDDDDDDD
            DDDDDDDDD
            """, Map.of('D', new ItemStack(Material.DIAMOND))))
            .onClick(event -> {
                event.setCancelled(true);
                if (ItemStackUtils.isNotAirNorNull(event.getCurrentItem()))
                    event.getInventory().getItem(event.getSlot()).setType(Material.EMERALD);
            })
            .onUpdate(event -> {
                for (int i = 0; i < 54; i++) {
                    if (ItemStackUtils.isNotAirNorNull(event.getInventory().getItem(i)))
                        event.getInventory().getItem(i).setAmount(event.getInventory().getItem(i).getAmount() + 1);
                }
            }, 20, 20)
            .build();

    public static final Inventory CRAFTING_GUI = new InventoryBuilder("Crafting Table", new InventoryBuilder.Shape("""
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
                if (event.getCurrentItem() != null && (InventoryUtils.isSameSlot(event.getSlot(), 3, 6) && event.getCurrentItem().getType().isAir()))
                    return;
                if (InventoryUtils.isSameSlot(event.getSlot(), 6, 5) && InventoryUtils.getItem(event.getInventory(), 6, 5).hasItemMeta() && InventoryUtils.getItem(event.getInventory(), 6, 5).getItemMeta().getDisplayName().equals("Exit"))
                    event.getWhoClicked().closeInventory();
                /*
                Verify if the clicked item isn't null
                First part : Verify if the clicked item is a black stained glass pane, a green stained glass pane, a red stained glass pane or a barrier
                Second part : Verify if the ingredients are still well placed while the recipe's result is already set in the result slot (24th slot)
                Third part : Verify if there is the recipe's result in the result slot (24th slot) and if the cursor isn't null and if the player's cursor's amount + the recipe's result's amount is greater than 64
                true -> event.setCancelled(true);
                 */
                if (event.getCurrentItem() != null && ((((event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) ||
                        (event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) ||
                        (event.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) ||
                        (event.getCurrentItem().getType() == Material.BARRIER && event.getCurrentItem().hasItemMeta() && (event.getCurrentItem().getItemMeta().getDisplayName().equals("Recipe expected") || event.getCurrentItem().getItemMeta().getDisplayName().equals("Exit"))))) ||
                        (InventoryUtils.isSameSlot(event.getSlot(), 3, 6) && ((ItemStackUtils.isNotAirNorNull(event.getCursor()) &&
                                ItemStackUtils.isSameItem(event.getCursor(), InventoryUtils.getItem(event.getInventory(), 3, 6)))))))
                    event.setCancelled(true);
                if (InventoryUtils.isSameSlot(event.getSlot(), 3, 6) && ItemStackUtils.isNotAirNorNull(event.getCurrentItem()) && !(event.getClickedInventory() instanceof PlayerInventory) && (!event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().getDisplayName().equals("Recipe expected")) && (event.getCursor().getType().isAir() || ItemStackUtils.isSameItem(event.getCursor(), InventoryUtils.getItem(event.getInventory(), 3, 6))) && event.getCursor().getAmount() + event.getCurrentItem().getAmount() <= 64) {
                    if (event.isShiftClick()) {
                        event.setCancelled(true);
                        int possibleCrafts;
                        if (Bukkit.getRecipesFor(event.getCurrentItem()).stream().filter(recipe -> (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) && (Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30)
                                .map(slot -> event.getInventory().getItem(slot) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(slot))
                                .anyMatch(specifiedIngredient -> (recipe instanceof ShapedRecipe shapedRecipe ? shapedRecipe.getIngredientMap().values() : ((ShapelessRecipe) recipe).getIngredientList())
                                        .stream()
                                        .anyMatch(ingredient -> ItemStackUtils.isSameItem(ingredient, specifiedIngredient))))).findFirst().get() instanceof ShapelessRecipe shapelessRecipe) {
                            //possibleCrafts is calculated by the quotient of the smallest amount of an item in the specified ingredients that are not air over the highest amount of an item in the shapelessRecipe's ingredient list that are similar to the smallest amount of an item in the specified ingredients that are not air
                            possibleCrafts = (int) Math.floor((double) Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30)
                                    .map(slot -> event.getInventory().getItem(slot) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(slot))
                                    .filter(specifiedIngredient -> specifiedIngredient != null && specifiedIngredient.getType() != Material.AIR)
                                    .min(Comparator.comparing(ItemStack::getAmount))
                                    .get()
                                    .getAmount() / shapelessRecipe.getIngredientList()
                                    .stream()
                                    .filter(ingredient -> ingredient != null && ItemStackUtils.isSameItem(ingredient, Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30)
                                            .map(slot -> event.getInventory().getItem(slot) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(slot))
                                            .filter(specifiedIngredient -> specifiedIngredient != null && specifiedIngredient.getType() != Material.AIR)
                                            .min(Comparator.comparing(ItemStack::getAmount))
                                            .get()))
                                    .max(Comparator.comparing(ItemStack::getAmount))
                                    .stream()
                                    .findFirst()
                                    .get()
                                    .getAmount());
                            //Do a loop for how many times the recipe can be crafted (possibleCrafts)
                            for (int i = 0; i < possibleCrafts; i++) {
                                //Part where all the items' amount from crafting slots are decreased by the ingredient's amount similar to the item from crafting slots
                                List<ItemStack> ingredientList = shapelessRecipe.getIngredientList();
                                List<ItemStack> specifiedIngredients = new ArrayList<>(Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30).map(slot -> event.getInventory().getItem(slot))
                                        .filter(Objects::nonNull)
                                        .toList());
                                for (int j = 0; j < shapelessRecipe.getIngredientList().size(); j++) {
                                    ItemStack item = specifiedIngredients.stream()
                                            .findFirst()
                                            .get();
                                    ItemStack sameIngredient = ingredientList.stream()
                                            .filter(ingredient -> ItemStackUtils.isSameItem(ingredient, item))
                                            .findFirst()
                                            .get();
                                    item.setAmount(item.getAmount() - sameIngredient.getAmount());
                                    ingredientList.remove(sameIngredient);
                                    specifiedIngredients.remove(item);
                                }
                                //Part where the result is added to the inventory
                                if (event.getWhoClicked().getInventory().firstEmpty() == -1)
                                    event.getWhoClicked().getWorld().dropItemNaturally(event.getWhoClicked().getLocation(), shapelessRecipe.getResult());
                                else
                                    event.getWhoClicked().getInventory().addItem(shapelessRecipe.getResult());
                            }
                        } else if (Bukkit.getRecipesFor(event.getCurrentItem()).stream().filter(recipe -> (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) && (Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30)
                                .map(slot -> event.getInventory().getItem(slot) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(slot))
                                .anyMatch(specifiedIngredient -> (recipe instanceof ShapedRecipe shapedRecipe ? shapedRecipe.getIngredientMap().values() : ((ShapelessRecipe) recipe).getIngredientList())
                                        .stream()
                                        .anyMatch(ingredient -> ItemStackUtils.isSameItem(ingredient, specifiedIngredient))))).findFirst().get() instanceof ShapedRecipe shapedRecipe) {
                            ItemStack lowestItemStack = new ItemBuilder(Material.COAL).hideIdentifier().setAmount(64).build();
                            for (int y = 0; y < 3; y++) {
                                for (int x = 0; x < 3; x++) {
                                    if (ItemStackUtils.isNotAirNorNull(event.getInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[y][x])) && ((lowestItemStack == null ? 0 : lowestItemStack.getAmount()) >= (event.getInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[y][x]) == null ? 0 : event.getInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[y][x]).getAmount())))
                                        lowestItemStack = event.getInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[y][x]);
                                }
                            }
                            int coordinateX = 0;
                            int coordinateY = 0;
                            int possiblePos = switch (shapedRecipe.getShape().length * shapedRecipe.getShape()[0].length()) {
                                case 2 -> 6;
                                case 6 -> 2;
                                case 3 -> 3;
                                case 4 -> 4;
                                case 9 -> 1;
                                default ->
                                        throw new IllegalStateException("Unexpected value: " + shapedRecipe.getShape().length * shapedRecipe.getShape()[0].length());
                            };
                            List<ItemStack[][]> positions = new ArrayList<>(possiblePos);
                            for (int i = 0; i < possiblePos; i++) {
                                ItemStack[][] position = new ItemStack[][]{new ItemStack[3], new ItemStack[3], new ItemStack[3]};
                                for (int y = 0; y < shapedRecipe.getShape().length; y++) {
                                    for (int x = 0; x < shapedRecipe.getShape()[0].length(); x++) {
                                        position[y + coordinateY][x + coordinateX] = shapedRecipe.getIngredientMap().get(shapedRecipe.getShape()[y].charAt(x));
                                    }
                                }
                                positions.add(Arrays.stream(position).map(y -> Arrays.stream(y).map(x -> x == null ? new ItemStack(Material.AIR) : x).toArray(ItemStack[]::new)).toArray(ItemStack[][]::new));
                                if (coordinateX + 1 > 3 - shapedRecipe.getShape()[0].length()) {
                                    coordinateX = 0;
                                    coordinateY++;
                                } else coordinateX++;
                            }
                            possibleCrafts = (int) Math.floor((double) lowestItemStack.getAmount() / Arrays.stream(positions.stream()
                                            .filter(position -> Arrays.stream(position).anyMatch(row -> row[0] != null))
                                            .findFirst()
                                            .get())
                                    .findFirst()
                                    .get()[0]
                                    .getAmount());
                            for (int i = 0; i < possibleCrafts; i++) {
                                for (int y = 0; y < 3; y++) {
                                    for (int x = 0; x < 3; x++) {
                                        if (ItemStackUtils.isNotAirNorNull(event.getInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[y][x])))
                                            event.getInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[y][x]).setAmount(event.getInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[y][x]).getAmount() -
                                                    positions.stream()
                                                            .filter(position -> Arrays.stream(position).anyMatch(row -> row[0] != null))
                                                            .findFirst()
                                                            .get()[y][x].getAmount());
                                    }
                                }
                                //Part where the result is added to the inventory
                                if (event.getWhoClicked().getInventory().firstEmpty() == -1)
                                    event.getWhoClicked().getWorld().dropItemNaturally(event.getWhoClicked().getLocation(), shapedRecipe.getResult());
                                else
                                    event.getWhoClicked().getInventory().addItem(shapedRecipe.getResult());
                            }
                        }
                    } else {
                        if (Bukkit.getRecipesFor(event.getCurrentItem()).stream().filter(recipe -> (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) && (Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30)
                                .map(slot -> event.getInventory().getItem(slot) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(slot))
                                .anyMatch(specifiedIngredient -> (recipe instanceof ShapedRecipe shapedRecipe ? shapedRecipe.getIngredientMap().values() : ((ShapelessRecipe) recipe).getIngredientList())
                                        .stream()
                                        .anyMatch(ingredient -> ItemStackUtils.isSameItem(ingredient, specifiedIngredient))))).findFirst().get() instanceof ShapelessRecipe shapelessRecipe) {
                            //Decrease amount of specified ingredients
                            List<ItemStack> ingredientList = shapelessRecipe.getIngredientList();
                            List<ItemStack> specifiedIngredients = new ArrayList<>(Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30).map(slot -> event.getInventory().getItem(slot))
                                    .filter(Objects::nonNull)
                                    .toList());
                            for (int j = 0; j < shapelessRecipe.getIngredientList().size(); j++) {
                                ItemStack item = specifiedIngredients.stream()
                                        .findFirst()
                                        .get();
                                ItemStack sameIngredient = ingredientList.stream()
                                        .filter(ingredient -> ItemStackUtils.isSameItem(ingredient, item))
                                        .findFirst()
                                        .get();
                                item.setAmount(item.getAmount() - sameIngredient.getAmount());
                                ingredientList.remove(sameIngredient);
                                specifiedIngredients.remove(item);
                            }
                        } else if (Bukkit.getRecipesFor(event.getCurrentItem()).stream().filter(recipe -> (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) && (Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30)
                                .map(slot -> event.getInventory().getItem(slot) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(slot))
                                .anyMatch(specifiedIngredient -> (recipe instanceof ShapedRecipe shapedRecipe ? shapedRecipe.getIngredientMap().values() : ((ShapelessRecipe) recipe).getIngredientList())
                                        .stream()
                                        .anyMatch(ingredient -> ItemStackUtils.isSameItem(ingredient, specifiedIngredient))))).findFirst().get() instanceof ShapedRecipe shapedRecipe) {
                            int coordinateX = 0;
                            int coordinateY = 0;
                            int possiblePos = switch (shapedRecipe.getShape().length * shapedRecipe.getShape()[0].length()) {
                                case 2 -> 6;
                                case 6 -> 2;
                                case 3 -> 3;
                                case 4 -> 4;
                                case 9 -> 1;
                                default ->
                                        throw new IllegalStateException("Unexpected value: " + shapedRecipe.getShape().length * shapedRecipe.getShape()[0].length());
                            };
                            List<ItemStack[][]> positions = new ArrayList<>(possiblePos);
                            for (int i = 0; i < possiblePos; i++) {
                                ItemStack[][] position = new ItemStack[][]{new ItemStack[3], new ItemStack[3], new ItemStack[3]};
                                for (int y = 0; y < shapedRecipe.getShape().length; y++) {
                                    for (int x = 0; x < shapedRecipe.getShape()[0].length(); x++) {
                                        position[y + coordinateY][x + coordinateX] = shapedRecipe.getIngredientMap().get(shapedRecipe.getShape()[y].charAt(x));
                                    }
                                }
                                positions.add(Arrays.stream(position).map(y -> Arrays.stream(y).map(x -> x == null ? new ItemStack(Material.AIR) : x).toArray(ItemStack[]::new)).toArray(ItemStack[][]::new));
                                if (coordinateX + 1 > 3 - shapedRecipe.getShape()[0].length()) {
                                    coordinateX = 0;
                                    coordinateY++;
                                } else coordinateX++;
                            }
                            for (int y = 0; y < 3; y++) {
                                for (int x = 0; x < 3; x++) {
                                    if (ItemStackUtils.isNotAirNorNull(event.getInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[y][x])))
                                        event.getInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[y][x]).setAmount(event.getInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[y][x]).getAmount() -
                                                positions.stream()
                                                        .filter(position -> Arrays.stream(position).anyMatch(row -> row[0] != null))
                                                        .findFirst()
                                                        .get()[y][x].getAmount());
                                }
                            }
                        }
                        if (ItemStackUtils.isSameItem(event.getCursor(), event.getCurrentItem()))
                            event.getCursor().setAmount(event.getCursor().getAmount() + event.getCurrentItem().getAmount());
                    }
                }
            })
            .onUpdate(event -> {
                if (InventoryUtils.getItem(event.getInventory(), 3, 6) == null || InventoryUtils.getItem(event.getInventory(), 3, 6).getType().isAir())
                    InventoryUtils.setItem(event.getInventory(), new ItemBuilder(Material.BARRIER).setDisplayName("Recipe expected").build(), 3, 6);
                Iterator<Recipe> sourceRecipes = Bukkit.recipeIterator();
                List<List<ItemStack>> specifiedIngredients = Stream.of(0, 1, 2).map(x -> Stream.of(0, 1, 2)
                        .map(y -> event.getInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[x][y]) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[x][y]))
                        .toList()).toList();
                while (sourceRecipes.hasNext()) {
                    if (canCraft(event.getInventory(), sourceRecipes.next(), specifiedIngredients))
                        break;
                }
            }, 0, 0)
            .onClose(event -> {
                Collection<ItemStack> slots = event.getPlayer().getInventory().addItem(Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30)
                        .map(i -> event.getInventory().getItem(i)).filter(ItemStackUtils::isNotAirNorNull).toArray(ItemStack[]::new)).values();
                if (!slots.isEmpty())
                    slots.forEach(is -> event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), is));
            })
            .onDrag(event -> {
                if (event.getRawSlots().contains(23))
                    event.setCancelled(true);
            })
            .build();

    private Inventories() {
    }

    private static boolean canCraft(Inventory inventory, Recipe recipe, List<List<ItemStack>> specifiedIngredients) {
        if (recipe instanceof ShapedRecipe shapedRecipe) {
            boolean craftable = false;
            int coordinateX = 0;
            int coordinateY = 0;
            int possiblePos = switch (shapedRecipe.getShape().length * shapedRecipe.getShape()[0].length()) {
                case 2 -> 6;
                case 6 -> 2;
                case 3 -> 3;
                case 4 -> 4;
                case 9 -> 1;
                default ->
                        throw new IllegalStateException("Unexpected value: " + shapedRecipe.getShape().length * shapedRecipe.getShape()[0].length());
            };
            List<ItemStack[][]> positions = new ArrayList<>(possiblePos);
            for (int i = 0; i < possiblePos; i++) {
                ItemStack[][] position = new ItemStack[][]{new ItemStack[3], new ItemStack[3], new ItemStack[3]};
                for (int y = 0; y < shapedRecipe.getShape().length; y++) {
                    for (int x = 0; x < shapedRecipe.getShape()[0].length(); x++) {
                        position[y + coordinateY][x + coordinateX] = shapedRecipe.getIngredientMap().get(shapedRecipe.getShape()[y].charAt(x));
                    }
                }
                positions.add(Arrays.stream(position).map(y -> Arrays.stream(y).map(x -> x == null ? new ItemStack(Material.AIR) : x).toArray(ItemStack[]::new)).toArray(ItemStack[][]::new));
                if (coordinateX + 1 > 3 - shapedRecipe.getShape()[0].length()) {
                    coordinateX = 0;
                    coordinateY++;
                } else coordinateX++;
            }
            for (ItemStack[][] position : positions) {
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        if ((ItemStackUtils.isSameItem(position[y][x], specifiedIngredients.get(y).get(x)) || (position[y][x].getType().isAir() && specifiedIngredients.get(y).get(x).getType().isAir())) && specifiedIngredients.get(y).get(x).getAmount() >= position[y][x].getAmount())
                            craftable = true;
                        else {
                            craftable = false;
                            break;
                        }
                    }
                    if (!craftable)
                        break;
                }
                if (craftable)
                    break;
            }
            InventoryUtils.setItem(inventory, craftable ? shapedRecipe.getResult() : new ItemBuilder(Material.BARRIER).setDisplayName("Recipe expected").build(), 3, 6);
            InventoryUtils.fillRectangle(inventory, new ItemBuilder(craftable ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").hideIdentifier().build(), 6, 1, 6, 4);
            InventoryUtils.fillRectangle(inventory, new ItemBuilder(craftable ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").hideIdentifier().build(), 6, 6, 6, 9);
            return craftable;
        } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
            boolean craftable = false;
            List<ItemStack> specifiedIngredientsList = new ArrayList<>(specifiedIngredients.stream().flatMap(List::stream).filter(ItemStackUtils::isNotAirNorNull).toList());
            List<ItemStack> ingredientList = shapelessRecipe.getIngredientList();
            if (!specifiedIngredientsList.isEmpty() && ingredientList.size() == specifiedIngredientsList.size()) {
                specifiedIngredientsList.forEach(ingredient -> ingredientList.removeIf(itemStack -> ItemStackUtils.isSameItem(itemStack, ingredient)));
                craftable = ingredientList.isEmpty();
            }
            InventoryUtils.setItem(inventory, craftable ? recipe.getResult() : new ItemBuilder(Material.BARRIER).setDisplayName("Recipe expected").hideIdentifier().build(), 3, 6);
            InventoryUtils.fillRectangle(inventory, new ItemBuilder(craftable ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").hideIdentifier().build(), 6, 1, 6, 4);
            InventoryUtils.fillRectangle(inventory, new ItemBuilder(craftable ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").hideIdentifier().build(), 6, 6, 6, 9);
            return craftable;
        }
        return false;
    }
}
