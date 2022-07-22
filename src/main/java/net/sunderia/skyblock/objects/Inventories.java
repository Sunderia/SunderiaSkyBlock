package net.sunderia.skyblock.objects;

import fr.sunderia.sunderiautils.utils.InventoryBuilder;
import fr.sunderia.sunderiautils.utils.ItemBuilder;
import fr.sunderia.sunderiautils.utils.ItemStackUtils;
import net.sunderia.skyblock.SunderiaSkyblock;
import net.sunderia.skyblock.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
            """, Map.of('B', new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).hideIdentifier().setDisplayName(" ").build(),
            'R', new ItemBuilder(Material.RED_STAINED_GLASS_PANE).hideIdentifier().setDisplayName(" ").build(),
            'N', addPersistentDataContainer(new ItemBuilder(Material.BARRIER).hideIdentifier().setDisplayName(ChatColor.RESET + "" + ChatColor.RED + "Recipe expected"), SunderiaSkyblock.getKey("recipeExpected"), PersistentDataType.BYTE, (byte) 1).build(),
            'E', addPersistentDataContainer(new ItemBuilder(Material.BARRIER).hideIdentifier().setDisplayName(ChatColor.RESET + "" + ChatColor.RED + "Exit"), SunderiaSkyblock.getKey("exit"), PersistentDataType.BYTE, (byte) 1).build(),
            'C', addPersistentDataContainer(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).hideIdentifier().setDisplayName(ChatColor.RESET + "" + ChatColor.RED + "Quick Crafting Slot"), SunderiaSkyblock.getKey("quickCraftingSlot"), PersistentDataType.BYTE, (byte) 1).build())))
            .onOpen(event -> {

            })
            .onClick(event -> {
                //Return if the result slot (24th slot) is empty
                if (event.getSlot() == 23 && ItemStackUtils.isAirOrNull(event.getCurrentItem()))
                    return;
                //Close the inventory if the played clicked on the barrier named "Exit"
                if (event.getSlot() == 49 && hasPersistentDataContainer(event.getCurrentItem(), SunderiaSkyblock.getKey("exit"), PersistentDataType.BYTE))
                    event.getWhoClicked().closeInventory();
                /*
                Event is cancelled if the clicked item isn't null and
                    if the clicked item is a black stained glass pane named " ", a green stained glass pane named " ", a red stained glass pane named " " or a barrier named "Recipe expected"
                    or
                    if the slot is the result slot (24th slot) and
                        if the player's cursor is the same item as the result slot (24th slot)
                        or
                        if the click is a right click and if the clicked inventory isn't instanceof PlayerInventory
                 */
                if (event.getCurrentItem() != null && ((((event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) ||
                        (event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE && event.getCurrentItem().hasItemMeta() && (event.getCurrentItem().getItemMeta().getDisplayName().equals(" ") ||
                                hasPersistentDataContainer(event.getCurrentItem(), SunderiaSkyblock.getKey("quickCraftingSlot"), PersistentDataType.BYTE))) ||
                        (event.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE && event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName().equals(" ")) ||
                        (event.getCurrentItem().getType() == Material.BARRIER && hasPersistentDataContainer(event.getCurrentItem(), SunderiaSkyblock.getKey("recipeExpected"), PersistentDataType.BYTE)))) ||
                        (Stream.of(23, 16, 25, 34)
                                .anyMatch(slot -> slot == event.getSlot()) && ((ItemStackUtils.isNotAirNorNull(event.getCursor()) &&
                                ItemStackUtils.isSameItem(event.getCursor(), event.getCurrentItem())) || (!(event.getClickedInventory() instanceof PlayerInventory) && event.isRightClick())))))
                    event.setCancelled(true);
                /*
                Player can pick up item in the result slot (24th slot) only if the click is a left click and
                if the item isn't null or a barrier that haven't the PersistentDataContainer "recipeExpected" and
                if the clicked inventory isn't the player's inventory and
                if the cursor is air or is same item as the item and
                if the cursor amount + item amount is equal or lower than 64
                 */
                if (event.getSlot() == 23 &&
                        event.isLeftClick() &&
                        ItemStackUtils.isNotAirNorNull(event.getCurrentItem()) &&
                        !(event.getClickedInventory() instanceof PlayerInventory) &&
                        (!hasPersistentDataContainer(event.getCurrentItem(), SunderiaSkyblock.getKey("recipeExpected"), PersistentDataType.BYTE)) &&
                        (event.getCursor().getType().isAir() || ItemStackUtils.isSameItem(event.getCursor(), event.getCurrentItem())) &&
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
                                //Decrease amount of specified ingredients
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
                            for (ItemStack[][] position : positions) {
                                for (int y = 0; y < 3; y++) {
                                    for (int x = 0; x < 3; x++) {
                                        if (position[y][x].getType() == specifiedIngredients.get(y).get(x).getType() || (ItemStackUtils.isAirOrNull(position[y][x]) && ItemStackUtils.isAirOrNull(specifiedIngredients.get(y).get(x))))
                                            actualPosition = position;
                                        else {
                                            actualPosition = null;
                                            break;
                                        }
                                    }
                                    //actualPosition is not the correct position
                                    if (actualPosition == null)
                                        break;
                                }
                                //actualPosition is the correct position
                                if (actualPosition != null)
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
                            for (ItemStack[][] position : positions) {
                                for (int y = 0; y < 3; y++) {
                                    for (int x = 0; x < 3; x++) {
                                        if (position[y][x].getType() == specifiedIngredients.get(y).get(x).getType() || (ItemStackUtils.isAirOrNull(position[y][x]) && ItemStackUtils.isAirOrNull(specifiedIngredients.get(y).get(x))))
                                            actualPosition = position;
                                        else {
                                            actualPosition = null;
                                            break;
                                        }
                                    }
                                    //actualPosition is not the correct position
                                    if (actualPosition == null)
                                        break;
                                }
                                //actualPosition is the correct position
                                if (actualPosition != null)
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
                Stream.of(16, 25, 34)
                        .filter(slot -> ItemStackUtils.isAirOrNull(event.getInventory().getItem(slot)))
                        .forEach(slot -> event.getInventory().setItem(slot, addPersistentDataContainer(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).hideIdentifier().setDisplayName(ChatColor.RESET + "" + ChatColor.RED + "Quick Crafting Slot"), SunderiaSkyblock.getKey("quickCraftingSlot"), PersistentDataType.BYTE, (byte) 1).build()));
                if (ItemStackUtils.isAirOrNull(event.getInventory().getItem(23)))
                    event.getInventory().setItem(23, addPersistentDataContainer(new ItemBuilder(Material.BARRIER).hideIdentifier().setDisplayName(ChatColor.RESET + "" + ChatColor.RED + "Recipe expected"), SunderiaSkyblock.getKey("recipeExpected"), PersistentDataType.BYTE, (byte) 1).build());
                for (Recipe recipe : StreamSupport.stream(Spliterators.spliteratorUnknownSize(Bukkit.recipeIterator(), 0), false)
                        .filter(recipe -> recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe)
                        .toList()) {
                    if (Stream.of(16, 25, 34)
                            .noneMatch(slot -> hasPersistentDataContainer(event.getInventory().getItem(slot), SunderiaSkyblock.getKey("quickCraftingSlot"), PersistentDataType.BYTE)))
                        break;
                    if (Stream.of(16, 25, 34)
                            .map(slot -> event.getInventory().getItem(slot))
                            .noneMatch(itemStack -> ItemStackUtils.isSameItem(removeAllLore(itemStack, true), recipe.getResult())))
                        canQuickCraft(recipe, event.getInventory());
                }
                for (Recipe recipe : StreamSupport.stream(Spliterators.spliteratorUnknownSize(Bukkit.recipeIterator(), 0), false)
                        .filter(recipe -> recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe)
                        .toList()) {
                    if (canCraft(event.getInventory(), recipe))
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
        boolean craftable = false;
        List<List<ItemStack>> specifiedIngredients = Stream.of(0, 1, 2)
                .map(x -> Stream.of(0, 1, 2)
                        .map(y -> inventory.getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[x][y]) == null ? new ItemStack(Material.AIR) : inventory.getItem(new int[][]{{10, 11, 12}, {19, 20, 21}, {28, 29, 30}}[x][y]))
                        .toList())
                .toList();
        if (!specifiedIngredients.isEmpty()) {
            if (recipe instanceof ShapedRecipe shapedRecipe) {
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
                            ItemStack specifiedIngredientClone = removeAllPersitentDataContainer(specifiedIngredients.get(y).get(x), true);
                            ItemStack ingredientClone = removeAllPersitentDataContainer(position[y][x], true);
                            if (((ItemStackUtils.isNotAirNorNull(ingredientClone) && shapedRecipe.getChoiceMap().get("abcdefghi".charAt(count)) != null && shapedRecipe.getChoiceMap().get("abcdefghi".charAt(count)).test(specifiedIngredientClone)) || ItemStackUtils.isSameItem(ingredientClone, specifiedIngredientClone) || (ingredientClone.getType().isAir() && specifiedIngredientClone.getType().isAir())) && specifiedIngredientClone.getAmount() >= ingredientClone.getAmount()) {
                                craftable = true;
                                //The variable count is updated everytime if the positionIngredient isn't null, used for getting the choice map for verification
                                if (ItemStackUtils.isNotAirNorNull(ingredientClone))
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
            } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                List<ItemStack> specifiedIngredientsList = new ArrayList<>(specifiedIngredients.stream().flatMap(List::stream).filter(ItemStackUtils::isNotAirNorNull).toList());
                List<ItemStack> ingredientList = shapelessRecipe.getIngredientList();
                if (!specifiedIngredientsList.isEmpty() && ingredientList.size() == specifiedIngredientsList.size()) {
                    specifiedIngredientsList.forEach(specifiedIngredient -> ingredientList.removeIf(ingredient -> {
                        if (!specifiedIngredient.hasItemMeta())
                            return ItemStackUtils.isSameItem(ingredient, specifiedIngredient);
                        return ItemStackUtils.isSameItem(removeAllPersitentDataContainer(specifiedIngredient, true), removeAllPersitentDataContainer(ingredient, true));
                    }));
                    craftable = ingredientList.isEmpty();
                }
            }
        }
        inventory.setItem(23, craftable ? recipe.getResult() : addPersistentDataContainer(new ItemBuilder(Material.BARRIER).hideIdentifier().setDisplayName(ChatColor.RESET + "" + ChatColor.RED + "Recipe expected"), SunderiaSkyblock.getKey("recipeExpected"), PersistentDataType.BYTE, (byte) 1).build());
        InventoryUtils.fillRectangle(inventory, new ItemBuilder(craftable ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").hideIdentifier().build(), 6, 1, 6, 4);
        InventoryUtils.fillRectangle(inventory, new ItemBuilder(craftable ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").hideIdentifier().build(), 6, 6, 6, 9);
        return craftable;
    }

    public static boolean canQuickCraft(Recipe recipe, Inventory inventory) {
        boolean quickCraftable = false;
        List<ItemStack> specifiedIngredients = new ArrayList<>();
        List<ItemStack> neededIngredients = new ArrayList<>();
        List<ItemStack> choices = new ArrayList<>();
        //Get the ingredients placed in the craftingSlots and the contents in the inventory and make these lists irreducible
        Stream.concat(Stream.of(10, 11, 12, 19, 20, 21, 28, 29, 30)
                        .filter(slot -> inventory.getItem(slot) != null)
                        .map(inventory::getItem), Arrays.stream(inventory.getViewers().get(0).getInventory().getContents()))
                .filter(ItemStackUtils::isNotAirNorNull)
                .map(itemStack -> removeAllPersitentDataContainer(itemStack, true))
                .forEach(specifiedIngredient -> {
                    specifiedIngredients.stream()
                            .filter(listItemStack -> ItemStackUtils.isSameItem(listItemStack, specifiedIngredient))
                            .forEach(listItemStack -> {
                                listItemStack.setAmount(listItemStack.getAmount() + specifiedIngredient.getAmount());
                                specifiedIngredient.setAmount(0);
                            });
                    if (specifiedIngredient.getAmount() > 0)
                        specifiedIngredients.add(specifiedIngredient);
                });
        //If there is no specifiedIngredients, we know that the recipe isn't quickCraftable
        if (!specifiedIngredients.isEmpty()) {
            //Loop for every recipeChoice from the choice map/list and transform this into a List<ItemStack>
            for (List<ItemStack> itemStackList : (recipe instanceof ShapedRecipe shapedRecipe ? shapedRecipe.getChoiceMap()
                    .values()
                    .stream()
                    .filter(Objects::nonNull)
                    .toList() : ((ShapelessRecipe) recipe).getChoiceList()).stream()
                    .map(recipeChoice -> {
                        //Transform recipeChoice by his choices list and transform List<Material> to List<ItemStack> and return always a List<ItemStack>
                        return recipeChoice instanceof RecipeChoice.MaterialChoice ? ((RecipeChoice.MaterialChoice) recipeChoice).getChoices().stream()
                                .map(ItemStack::new)
                                .toList() : ((RecipeChoice.ExactChoice) recipeChoice).getChoices();
                    })
                    //Filter only choices lists that have a size higher than 1
                    .toList()) {
                //If itemStackList have one element, we add this element to the neededIngredients
                if (itemStackList.size() == 1)
                    choices.add(removeAllPersitentDataContainer(itemStackList.get(0), true));
                else {
                    //If itemStackList have more than one element and contained one of the specifiedIngredients, we transform that list to the specifiedIngredient that's the same as one of the ItemStack from itemStackList
                    Optional<ItemStack> sameItemStack = itemStackList.stream()
                            .filter(itemStack -> specifiedIngredients.stream()
                                    .anyMatch(specifiedIngredient -> ItemStackUtils.isSameItem(itemStack, specifiedIngredient)))
                            .findFirst();
                    if (sameItemStack.isPresent()) choices.add(sameItemStack.get());
                    else return false;
                }
            }
            //Make choices list irreducible into neededIngredients
            choices.forEach(neededIngredient -> {
                neededIngredients.stream()
                        .filter(listItemStack -> ItemStackUtils.isSameItem(listItemStack, neededIngredient))
                        .forEach(listItemStack -> {
                            listItemStack.setAmount(listItemStack.getAmount() + neededIngredient.getAmount());
                            neededIngredient.setAmount(0);
                        });
                if (neededIngredient.getAmount() > 0)
                    neededIngredients.add(neededIngredient);
            });
            //Verify if the recipe is craftable depending on the specifiedIngredients and the neededIngredients
            if (neededIngredients.stream()
                    .allMatch(neededIngredient -> specifiedIngredients.stream()
                            .anyMatch(specifiedIngredient -> ItemStackUtils.isSameItem(neededIngredient, specifiedIngredient)))) {
                for (ItemStack specifiedIngredient : specifiedIngredients.stream()
                        .filter(specifiedItem -> neededIngredients.stream()
                                .anyMatch(neededItem -> ItemStackUtils.isSameItem(specifiedItem, neededItem)))
                        .toList()) {
                    //There should be always only one neededIngredient that's the same as the specifiedIngredient because a type of material should be present only one time
                    ItemStack neededIngredient = neededIngredients.stream()
                            .filter(itemStack -> ItemStackUtils.isSameItem(itemStack, specifiedIngredient))
                            .findFirst()
                            .get();
                    if (specifiedIngredient.getAmount() - neededIngredient.getAmount() >= 0) {
                        specifiedIngredient.setAmount(specifiedIngredient.getAmount() - neededIngredient.getAmount());
                        quickCraftable = true;
                    } else {
                        quickCraftable = false;
                        break;
                    }
                }
            }
        }
        ItemStack recipeResult = recipe.getResult().clone();
        ItemMeta recipeResultMeta = recipeResult.hasItemMeta() ? recipeResult.getItemMeta() : Bukkit.getItemFactory().getItemMeta(recipeResult.getType());
        recipeResultMeta.setLore(Stream.concat(recipeResultMeta.hasLore() ? recipeResultMeta.getLore()
                        .stream() : Stream.of(), Stream.concat(Stream.of(ChatColor.BLUE + "Needed ingredients:"), neededIngredients.stream()
                        .map(neededIngredient -> ChatColor.RESET + "" + ChatColor.GRAY + neededIngredient.getAmount() + "x " + (ItemStackUtils.isCustomItem(neededIngredient) ? neededIngredient.getItemMeta()
                                .getDisplayName() : fr.sunderia.sunderiautils.utils.StringUtils.capitalizeWord(neededIngredient.getType()
                                .name()
                                .replace("_", " ")
                                .toLowerCase(Locale.ROOT))))))
                .toList());
        recipeResult.setItemMeta(recipeResultMeta);
        //Get the first quickCraftingSlots (17th slot, 26th slot and 35th slot) that haven't the PersitentDataContainer "quickCraftingSlot" and set the recipe's result if he's quickCraftable or the red glass pannel if the recipe isn't quickCraftable
        inventory.setItem(Stream.of(16, 25, 34)
                .filter(slot -> hasPersistentDataContainer(inventory.getItem(slot), SunderiaSkyblock.getKey("quickCraftingSlot"), PersistentDataType.BYTE))
                .findFirst()
                .get(), quickCraftable ? recipeResult : addPersistentDataContainer(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).hideIdentifier().setDisplayName(ChatColor.RESET + "" + ChatColor.RED + "Quick Crafting Slot"), SunderiaSkyblock.getKey("quickCraftingSlot"), PersistentDataType.BYTE, (byte) 1).build());
        return quickCraftable;
    }

    //Temporary function
    public static <T, Z> ItemBuilder addPersistentDataContainer(ItemBuilder itemBuilder, NamespacedKey namespacedKey, PersistentDataType<T, Z> persistentDataType, Z value) {
        ItemStack itemStack = itemBuilder.build();
        ItemMeta itemMeta = itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        itemMeta.getPersistentDataContainer().set(namespacedKey, persistentDataType, value);
        itemStack.setItemMeta(itemMeta);
        return new ItemBuilder(itemStack);
    }

    //Temporary function
    public static <T, Z> boolean hasPersistentDataContainer(ItemStack itemStack, NamespacedKey namespacedKey, PersistentDataType<T, Z> persistentDataType) {
        return ItemStackUtils.isNotAirNorNull(itemStack) && itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(namespacedKey, persistentDataType);
    }

    //Temporary function
    public static ItemStack removeAllPersitentDataContainer(ItemStack itemStack, boolean clone) {
        ItemStack item = clone ? itemStack.clone() : itemStack;
        if (item.hasItemMeta()) {
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.getPersistentDataContainer().getKeys().forEach(key -> itemMeta.getPersistentDataContainer().remove(key));
            item.setItemMeta(itemMeta);
        }
        return item;
    }

    //Temporary function
    public static ItemStack removeAllLore(ItemStack itemStack, boolean clone) {
        ItemStack item = clone ? itemStack.clone() : itemStack;
        if (item.hasItemMeta() && !item.getItemMeta().getLore().isEmpty()) {
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setLore(new ArrayList<>());
            item.setItemMeta(itemMeta);
        }
        return item;
    }
}
