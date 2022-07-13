package net.sunderia.skyblock.objects;

import fr.sunderia.sunderiautils.utils.InventoryBuilder;
import fr.sunderia.sunderiautils.utils.ItemBuilder;
import fr.sunderia.sunderiautils.utils.ItemStackUtils;
import net.sunderia.skyblock.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

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
            B   BBBCB
            B   BNBCB
            B   BBBCB
            BBBBBBBBB
            RRRRERRRR
            """, Map.of('B', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build(),
            'R', new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").build(),
            'N', new ItemBuilder(Material.BARRIER).setDisplayName("Recipe expected").build(),
            'E', new ItemBuilder(Material.BARRIER).setDisplayName("Exit").build(),
            'C', new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName("Ingredients expected").build())))
            .onClick(event -> {
                //Return if the result slot (24th slot) is empty
                if (event.getCurrentItem() != null && InventoryUtils.isSameSlot(event.getSlot(), 3, 6) && event.getCurrentItem().getType().isAir())
                    return;
                //Close the inventory if the played clicked on the barrier named "Exit"
                if (InventoryUtils.isSameSlot(event.getSlot(), 6, 5) && InventoryUtils.getItem(event.getInventory(), 6, 5).hasItemMeta() && InventoryUtils.getItem(event.getInventory(), 6, 5).getItemMeta().getDisplayName().equals("Exit"))
                    event.getWhoClicked().closeInventory();
                /*
                Event is cancelled if the clicked item isn't null and
                    if the clicked item is a black stained glass pane named " ", a green stained glass pane named " ", a red stained glass pane named " " or a barrier named "Recipe expected"
                    or
                    if the slot is the result slot (24th slot) and
                        if the player's cursor is the same item as the result slot (24th slot)
                        or
                        if the click is a right click
                 */
                if (event.getCurrentItem() != null && ((((event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) ||
                        (event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE && event.getCurrentItem().hasItemMeta() && (event.getCurrentItem().getItemMeta().getDisplayName().equals(" ") ||
                                event.getCurrentItem().getItemMeta().getDisplayName().equals("Ingredients expected"))) ||
                        (event.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) ||
                        (event.getCurrentItem().getType() == Material.BARRIER && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equals("Recipe expected")))) ||
                        (InventoryUtils.isSameSlot(event.getSlot(), 3, 6) && ((ItemStackUtils.isNotAirNorNull(event.getCursor()) &&
                                ItemStackUtils.isSameItem(event.getCursor(), InventoryUtils.getItem(event.getInventory(), 3, 6))) || event.isRightClick()))))
                    event.setCancelled(true);
                /*
                Player can pick up item in the result slot (24th slot) only if the click is a left click and
                if the item isn't null or a barrier named "Recipe expected" and
                if the clicked inventory isn't the player's inventory and
                if the cursor is air or is same item as the item and
                if the cursor amount + item amount is equal or lower than 64
                 */
                /*System.out.println("isResultSlot ? " + InventoryUtils.isSameSlot(event.getSlot(), 3, 6));
                System.out.println("leftClick or shiftLeftClick ? " + (event.isLeftClick() || (event.isLeftClick() && event.isShiftClick())));
                System.out.println("result item NOT null or air ? " + ItemStackUtils.isNotAirNorNull(event.getCurrentItem()));
                System.out.println();*/
                if (InventoryUtils.isSameSlot(event.getSlot(), 3, 6) &&
                        event.isLeftClick() &&
                        ItemStackUtils.isNotAirNorNull(event.getCurrentItem()) &&
                        !(event.getClickedInventory() instanceof PlayerInventory) &&
                        (!event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().getDisplayName().equals("Recipe expected")) &&
                        (event.getCursor().getType().isAir() || ItemStackUtils.isSameItem(event.getCursor(), InventoryUtils.getItem(event.getInventory(), 3, 6))) &&
                        event.getCursor().getAmount() + event.getCurrentItem().getAmount() <= 64) {
                    //When it's a shift click, we are calculating how much crafts is possible and loop to give the items directly in the player's inventory
                    if (event.isShiftClick()) {
                        event.setCancelled(true);
                        //possibleCrafts is used later for loops
                        int possibleCrafts;
                        //Verify if it's a shapeless recipe by filtering only shaped and shapeless recipe and by filtering recipes where at least one ingredient needed is the same as one of the specifiedIngredients
                        if (Bukkit.getRecipesFor(event.getCurrentItem()).stream().filter(recipe -> (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) && (Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30)
                                .map(slot -> event.getInventory().getItem(slot) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(slot))
                                .anyMatch(specifiedIngredient -> (recipe instanceof ShapedRecipe shapedRecipe ? shapedRecipe.getIngredientMap().values() : ((ShapelessRecipe) recipe).getIngredientList())
                                        .stream()
                                        .anyMatch(ingredient -> {
                                            ItemStack specifiedIngredientClone = specifiedIngredient.clone();
                                            if(specifiedIngredientClone.hasItemMeta()) {
                                                ItemMeta specifiedIngredientMeta = specifiedIngredientClone.getItemMeta();
                                                specifiedIngredientMeta.getPersistentDataContainer().getKeys().forEach(key -> specifiedIngredientMeta.getPersistentDataContainer().remove(key));
                                                specifiedIngredientClone.setItemMeta(specifiedIngredientMeta);
                                            }
                                            ItemStack ingredientClone = ingredient.clone();
                                            if(ingredientClone.hasItemMeta()) {
                                                ItemMeta ingredientMeta = ingredientClone.getItemMeta();
                                                ingredientMeta.getPersistentDataContainer().getKeys().forEach(key -> ingredientMeta.getPersistentDataContainer().remove(key));
                                                ingredientClone.setItemMeta(ingredientMeta);
                                            }
                                            return ItemStackUtils.isSameItem(ingredientClone, specifiedIngredientClone);
                                        })))).findFirst().get() instanceof ShapelessRecipe shapelessRecipe) {
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
                                //Decrease amount of specified ingredientsz
                                List<ItemStack> ingredientList = shapelessRecipe.getIngredientList();
                                List<ItemStack> specifiedIngredients = new ArrayList<>(Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30).map(slot -> event.getInventory().getItem(slot))
                                        .filter(Objects::nonNull)
                                        .toList());
                                //We loop for every ingredient in the shapeless's ingredient list and remove the same ingredient as the first specifiedIngredient's amount to the first specifiedIngredient, then the first specifiedIngredient and the same ingredient as the first specifiedIngredient are removed from their list
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
                            //Verify if it's a shaped recipe by filtering only shaped and shapeless recipe and by filtering recipes where at least one ingredient needed is the same as one of the specifiedIngredients
                        } else if (Bukkit.getRecipesFor(event.getCurrentItem()).stream().filter(recipe -> (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) && (Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30)
                                .map(slot -> event.getInventory().getItem(slot) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(slot))
                                .anyMatch(specifiedIngredient -> (recipe instanceof ShapedRecipe shapedRecipe ? shapedRecipe.getIngredientMap().values() : ((ShapelessRecipe) recipe).getIngredientList())
                                        .stream()
                                        .anyMatch(ingredient -> {
                                            ItemStack specifiedIngredientClone = specifiedIngredient.clone();
                                            if(specifiedIngredientClone.hasItemMeta()) {
                                                ItemMeta specifiedIngredientMeta = specifiedIngredientClone.getItemMeta();
                                                specifiedIngredientMeta.getPersistentDataContainer().getKeys().forEach(key -> specifiedIngredientMeta.getPersistentDataContainer().remove(key));
                                                specifiedIngredientClone.setItemMeta(specifiedIngredientMeta);
                                            }
                                            ItemStack ingredientClone = ingredient.clone();
                                            if(ingredientClone.hasItemMeta()) {
                                                ItemMeta ingredientMeta = ingredientClone.getItemMeta();
                                                ingredientMeta.getPersistentDataContainer().getKeys().forEach(key -> ingredientMeta.getPersistentDataContainer().remove(key));
                                                ingredientClone.setItemMeta(ingredientMeta);
                                            }
                                            return ItemStackUtils.isSameItem(ingredientClone, specifiedIngredientClone);
                                        })))).findFirst().get() instanceof ShapedRecipe shapedRecipe) {
                            ItemStack lowestItemStack = new ItemBuilder(Material.BEDROCK).hideIdentifier().setAmount(64).build();
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
                            //All possible positions for the shapedRecipe are generated
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
                            //actualPosition will be equal to the position used depending on the specifiedIngredients' placements
                            List<List<ItemStack>> specifiedIngredients = Stream.of(0, 1, 2)
                                    .map(x -> Stream.of(0, 1, 2)
                                            .map(y -> event.getClickedInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[x][y]) == null ? new ItemStack(Material.AIR) : event.getClickedInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[x][y]))
                                            .toList())
                                    .toList();
                            ItemStack[][] actualPosition = null;
                            for(ItemStack[][] position : positions){
                                for(int y = 0; y < 3; y++){
                                    for(int x = 0; x < 3; x++){
                                        if(position[y][x].getType() == specifiedIngredients.get(y).get(x).getType() || (ItemStackUtils.isAirOrNull(position[y][x]) && ItemStackUtils.isAirOrNull(specifiedIngredients.get(y).get(x))))
                                            actualPosition = position;
                                        else {
                                            actualPosition = null;
                                            break;
                                        }
                                    }
                                    //actualPosition is not the correct position
                                    if(actualPosition == null)
                                        break;
                                }
                                //actualPosition is the correct position
                                if(actualPosition != null)
                                    break;
                            }
                            //possibleCrafts is calculated by the quotient of the smallest amount of an item in the specified ingredients that are not air over
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
                                        if (ItemStackUtils.isNotAirNorNull(specifiedIngredients.get(y).get(x)))
                                            specifiedIngredients.get(y)
                                                    .get(x)
                                                    .setAmount(specifiedIngredients.get(y)
                                                            .get(x)
                                                            .getAmount() - actualPosition[y][x].getAmount());
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
                        //Verify if it's a shapeless recipe by filtering only shaped and shapeless recipe and by filtering recipes where at least one ingredient needed is the same as one of the specifiedIngredients
                        if (Bukkit.getRecipesFor(event.getCurrentItem()).stream().filter(recipe -> (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) && (Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30)
                                .map(slot -> event.getInventory().getItem(slot) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(slot))
                                .anyMatch(specifiedIngredient -> (recipe instanceof ShapedRecipe shapedRecipe ? shapedRecipe.getIngredientMap().values() : ((ShapelessRecipe) recipe).getIngredientList())
                                        .stream()
                                        .anyMatch(ingredient -> {
                                            ItemStack specifiedIngredientClone = specifiedIngredient.clone();
                                            if(specifiedIngredientClone.hasItemMeta()) {
                                                ItemMeta specifiedIngredientMeta = specifiedIngredientClone.getItemMeta();
                                                specifiedIngredientMeta.getPersistentDataContainer().getKeys().forEach(key -> specifiedIngredientMeta.getPersistentDataContainer().remove(key));
                                                specifiedIngredientClone.setItemMeta(specifiedIngredientMeta);
                                            }
                                            ItemStack ingredientClone = ingredient.clone();
                                            if(ingredientClone.hasItemMeta()) {
                                                ItemMeta ingredientMeta = ingredientClone.getItemMeta();
                                                ingredientMeta.getPersistentDataContainer().getKeys().forEach(key -> ingredientMeta.getPersistentDataContainer().remove(key));
                                                ingredientClone.setItemMeta(ingredientMeta);
                                            }
                                            return ItemStackUtils.isSameItem(ingredientClone, specifiedIngredientClone);
                                        })))).findFirst().get() instanceof ShapelessRecipe shapelessRecipe) {
                            //Decrease amount of specified ingredients
                            List<ItemStack> ingredientList = shapelessRecipe.getIngredientList();
                            List<ItemStack> specifiedIngredients = new ArrayList<>(Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30).map(slot -> event.getInventory().getItem(slot))
                                    .filter(Objects::nonNull)
                                    .toList());
                            //We loop for every ingredient in the shapeless's ingredient list and remove the same ingredient as the first specifiedIngredient's amount to the first specifiedIngredient, then the first specifiedIngredient and the same ingredient as the first specifiedIngredient are removed from their list
                            for (int i = 0; i < shapelessRecipe.getIngredientList().size(); i++) {
                                ItemStack item = specifiedIngredients.get(0);
                                ItemStack sameIngredient = ingredientList.stream()
                                        .filter(ingredient -> {
                                            ItemStack specifiedIngredientClone = item.clone();
                                            if (specifiedIngredientClone.hasItemMeta()) {
                                                ItemMeta specifiedIngredientMeta = specifiedIngredientClone.getItemMeta();
                                                specifiedIngredientMeta.getPersistentDataContainer().getKeys().forEach(key -> specifiedIngredientMeta.getPersistentDataContainer().remove(key));
                                                specifiedIngredientClone.setItemMeta(specifiedIngredientMeta);
                                            }
                                            ItemStack ingredientClone = ingredient.clone();
                                            if (ingredientClone.hasItemMeta()) {
                                                ItemMeta ingredientMeta = ingredientClone.getItemMeta();
                                                ingredientMeta.getPersistentDataContainer().getKeys().forEach(key -> ingredientMeta.getPersistentDataContainer().remove(key));
                                                ingredientClone.setItemMeta(ingredientMeta);
                                            }
                                            return ItemStackUtils.isSameItem(ingredientClone, specifiedIngredientClone);
                                        })
                                        .findFirst()
                                        .get();
                                item.setAmount(item.getAmount() - sameIngredient.getAmount());
                                ingredientList.remove(sameIngredient);
                                specifiedIngredients.remove(item);
                            }
                            //Verify if it's a shaped recipe by filtering only shaped and shapeless recipe and by filtering recipes where at least one ingredient needed is the same as one of the specifiedIngredients
                        } else if (Bukkit.getRecipesFor(event.getCurrentItem()).stream().filter(recipe -> (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) && (Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30)
                                .map(slot -> event.getInventory().getItem(slot) == null ? new ItemStack(Material.AIR) : event.getInventory().getItem(slot))
                                .anyMatch(specifiedIngredient -> (recipe instanceof ShapedRecipe shapedRecipe ? shapedRecipe.getIngredientMap().values() : ((ShapelessRecipe) recipe).getIngredientList())
                                        .stream()
                                        .anyMatch(ingredient -> {
                                            ItemStack specifiedIngredientClone = specifiedIngredient.clone();
                                            if(specifiedIngredientClone.hasItemMeta()) {
                                                ItemMeta specifiedIngredientMeta = specifiedIngredientClone.getItemMeta();
                                                specifiedIngredientMeta.getPersistentDataContainer().getKeys().forEach(key -> specifiedIngredientMeta.getPersistentDataContainer().remove(key));
                                                specifiedIngredientClone.setItemMeta(specifiedIngredientMeta);
                                            }
                                            ItemStack ingredientClone = ingredient.clone();
                                            if(ingredientClone.hasItemMeta()) {
                                                ItemMeta ingredientMeta = ingredientClone.getItemMeta();
                                                ingredientMeta.getPersistentDataContainer().getKeys().forEach(key -> ingredientMeta.getPersistentDataContainer().remove(key));
                                                ingredientClone.setItemMeta(ingredientMeta);
                                            }
                                            return ItemStackUtils.isSameItem(ingredientClone, specifiedIngredientClone);
                                        })))).findFirst().get() instanceof ShapedRecipe shapedRecipe) {
                            //All possible positions for the shapedRecipe are generated
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
                            //actualPosition will be equal to the position used depending on the specifiedIngredients' placements
                            List<List<ItemStack>> specifiedIngredients = Stream.of(0, 1, 2)
                                    .map(x -> Stream.of(0, 1, 2)
                                            .map(y -> event.getClickedInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[x][y]) == null ? new ItemStack(Material.AIR) : event.getClickedInventory().getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[x][y]))
                                            .toList())
                                    .toList();
                            ItemStack[][] actualPosition = null;
                            for(ItemStack[][] position : positions){
                                for(int y = 0; y < 3; y++){
                                    for(int x = 0; x < 3; x++){
                                        if(position[y][x].getType() == specifiedIngredients.get(y).get(x).getType() || (ItemStackUtils.isAirOrNull(position[y][x]) && ItemStackUtils.isAirOrNull(specifiedIngredients.get(y).get(x))))
                                            actualPosition = position;
                                        else {
                                            actualPosition = null;
                                            break;
                                        }
                                    }
                                    //actualPosition is not the correct position
                                    if(actualPosition == null)
                                        break;
                                }
                                //actualPosition is the correct position
                                if(actualPosition != null)
                                    break;
                            }
                            //We do a loop for every crafting slot and remove position[y][x]'s amount from the specifiedIngredients.get(y).get(x)'s amount
                            for (int y = 0; y < 3; y++) {
                                for (int x = 0; x < 3; x++) {
                                    if (ItemStackUtils.isNotAirNorNull(specifiedIngredients.get(y).get(x)))
                                        specifiedIngredients.get(y)
                                                .get(x)
                                                .setAmount(specifiedIngredients.get(y)
                                                        .get(x)
                                                        .getAmount() - actualPosition[y][x].getAmount());
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
                while (sourceRecipes.hasNext()) {
                    if (canCraft(event.getInventory(), sourceRecipes.next()))
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

    private static boolean canCraft(Inventory inventory, Recipe recipe) {
        List<List<ItemStack>> specifiedIngredients = Stream.of(0, 1, 2)
                .map(x -> Stream.of(0, 1, 2)
                .map(y -> inventory.getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[x][y]) == null ? new ItemStack(Material.AIR) : inventory.getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[x][y]))
                .toList())
                .toList();
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
                int count = 0;
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 3; x++) {
                        /*
                        craftable is true if the specifiedIngredient's amount is higher or equal to positionIngredient's amount and
                             ((if the count's char's choice map isn't null and
                                if the count's char's choice map contains the specifiedIngredient) or
                             if the specifiedIngredient is same as the positionIngredient or
                             if the specifiedIngredient/positionIngredient are both null)
                        */
                        ItemStack specifiedIngredientClone = specifiedIngredients.get(y).get(x).clone();
                        if(specifiedIngredientClone.hasItemMeta()) {
                            ItemMeta specifiedIngredientMeta = specifiedIngredientClone.getItemMeta();
                            specifiedIngredientMeta.getPersistentDataContainer().getKeys().forEach(key -> specifiedIngredientMeta.getPersistentDataContainer().remove(key));
                            specifiedIngredientClone.setItemMeta(specifiedIngredientMeta);
                        }
                        ItemStack ingredientClone = position[y][x].clone();
                        if(ingredientClone.hasItemMeta()) {
                            ItemMeta ingredientMeta = ingredientClone.getItemMeta();
                            ingredientMeta.getPersistentDataContainer().getKeys().forEach(key -> ingredientMeta.getPersistentDataContainer().remove(key));
                            ingredientClone.setItemMeta(ingredientMeta);
                        }
                        if (((ItemStackUtils.isNotAirNorNull(ingredientClone) && shapedRecipe.getChoiceMap().get("abcdefghi".charAt(count)) != null && shapedRecipe.getChoiceMap().get("abcdefghi".charAt(count)).test(specifiedIngredientClone)) || ItemStackUtils.isSameItem(ingredientClone, specifiedIngredientClone) || (ingredientClone.getType().isAir() && specifiedIngredientClone.getType().isAir())) && specifiedIngredientClone.getAmount() >= ingredientClone.getAmount()) {
                            craftable = true;
                            //The variable count is updated everytime if the positionIngredient isn't null, used for getting the choice map for verification
                            if(ItemStackUtils.isNotAirNorNull(ingredientClone))
                                count++;
                        } else {
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
                specifiedIngredientsList.forEach(specifiedIngredient -> ingredientList.removeIf(ingredient -> {
                    if(!specifiedIngredient.hasItemMeta())
                        return ItemStackUtils.isSameItem(ingredient, specifiedIngredient);
                    ItemStack specifiedIngredientClone = specifiedIngredient.clone();
                    ItemMeta specifiedIngredientMeta = specifiedIngredientClone.getItemMeta();
                    specifiedIngredientMeta.getPersistentDataContainer().getKeys().forEach(key -> specifiedIngredientMeta.getPersistentDataContainer().remove(key));
                    specifiedIngredientClone.setItemMeta(specifiedIngredientMeta);
                    ItemStack ingredientClone = ingredient.clone();
                    ItemMeta ingredientMeta = ingredientClone.getItemMeta();
                    ingredientMeta.getPersistentDataContainer().getKeys().forEach(key -> ingredientMeta.getPersistentDataContainer().remove(key));
                    ingredientClone.setItemMeta(ingredientMeta);
                    return ItemStackUtils.isSameItem(ingredientClone, specifiedIngredientClone);
                }));
                craftable = ingredientList.isEmpty();
            }
            InventoryUtils.setItem(inventory, craftable ? shapelessRecipe.getResult() : new ItemBuilder(Material.BARRIER).setDisplayName("Recipe expected").hideIdentifier().build(), 3, 6);
            InventoryUtils.fillRectangle(inventory, new ItemBuilder(craftable ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").hideIdentifier().build(), 6, 1, 6, 4);
            InventoryUtils.fillRectangle(inventory, new ItemBuilder(craftable ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").hideIdentifier().build(), 6, 6, 6, 9);
            return craftable;
        }
        return false;
    }

    public static boolean canQuickCraft(Inventory inventory, Recipe recipe){
        return true;
    }
}
