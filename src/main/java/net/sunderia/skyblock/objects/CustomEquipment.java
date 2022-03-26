package net.sunderia.skyblock.objects;

import fr.sunderia.sunderiautils.utils.ItemStackUtils;
import net.sunderia.skyblock.SunderiaSkyblock;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class CustomEquipment {

    private ItemStack itemStack;
    private ItemStack itemStackHelmet;
    private ItemStack itemStackChestplate;
    private ItemStack itemStackLeggings;
    private ItemStack itemStackBoots;
    private ItemStack itemStackPickaxe;
    private ItemStack itemStackAxe;
    private ItemStack itemStackHoe;
    private ItemStack itemStackSword;
    private ItemStack itemStackShovel;
    private ItemStack itemStackArmor;
    private boolean resetItemStackArmorName;

    public CustomEquipment(@NotNull ItemStack itemStack, @NotNull ItemStack itemStackArmor, boolean resetItemStackArmorName){
        this.itemStack = itemStack;
        this.resetItemStackArmorName = resetItemStackArmorName;
        if(ItemStackUtils.isAnArmor(itemStackArmor)) this.itemStackArmor = itemStackArmor;
    }

    public CustomEquipment(@NotNull ItemStack itemStack, @NotNull ItemStack itemStackArmor){
        this(itemStack, itemStackArmor, false);
    }

    //Getters
    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemStack getItemStackArmor(){
        return itemStackArmor;
    }

    public boolean getResetItemStackArmorName(){
        return resetItemStackArmorName;
    }

    public ItemStack getHelmet() {
        return itemStackHelmet;
    }

    public ItemStack getChestplate() {
        return itemStackChestplate;
    }

    public ItemStack getLeggings() {
        return itemStackLeggings;
    }

    public ItemStack getBoots() {
        return itemStackBoots;
    }

    public ItemStack getHoe() {
        return itemStackHoe;
    }

    public ItemStack getAxe() {
        return itemStackAxe;
    }

    public ItemStack getSword() {
        return itemStackSword;
    }

    public ItemStack getPickaxe() {
        return itemStackPickaxe;
    }

    public ItemStack getShovel() {
        return itemStackShovel;
    }

    //Setters
    public CustomEquipment setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public CustomEquipment setItemStackArmor(ItemStack itemStackArmor){
        if(ItemStackUtils.isAnArmor(itemStackArmor)) this.itemStackArmor = itemStackArmor;
        return this;
    }

    public CustomEquipment setResetItemStackArmorName(boolean resetItemStackArmorName){
        this.resetItemStackArmorName = resetItemStackArmorName;
        return this;
    }

    public CustomEquipment setHelmet(ItemStack itemStackHelmet, Recipe recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setHelmet(ItemStack itemStackHelmet, Recipes recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setItemStackHelmet(ItemStack itemStackHelmet) {
        this.itemStackHelmet = itemStackHelmet;
        return this;
    }

    public CustomEquipment setRecipeHelmet(Recipe recipeHelmet) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeHelmet);
        return this;
    }

    public CustomEquipment setRecipeHelmet(Recipes recipeHelmet) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeHelmet.getRecipe());
        return this;
    }

    public CustomEquipment setChestplate(ItemStack itemStackHelmet, Recipes recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setChestplate(ItemStack itemStackHelmet, Recipe recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setItemStackChestplate(ItemStack itemStackChestplate) {
        this.itemStackChestplate = itemStackChestplate;
        return this;
    }

    public CustomEquipment setRecipeChestplate(Recipe recipeChestplate) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeChestplate);
        return this;
    }

    public CustomEquipment setRecipeChestplate(Recipes recipeChestplate) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeChestplate.getRecipe());
        return this;
    }

    public CustomEquipment setLeggings(ItemStack itemStackHelmet, Recipes recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setLeggings(ItemStack itemStackHelmet, Recipe recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setItemStackLeggings(ItemStack itemStackLeggings) {
        this.itemStackLeggings = itemStackLeggings;
        return this;
    }

    public CustomEquipment setRecipeLeggings(Recipe recipeLeggings) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeLeggings);
        return this;
    }

    public CustomEquipment setRecipeLeggings(Recipes recipeLeggings) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeLeggings.getRecipe());
        return this;
    }

    public CustomEquipment setBoots(ItemStack itemStackHelmet, Recipes recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setBoots(ItemStack itemStackHelmet, Recipe recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setItemStackBoots(ItemStack itemStackBoots) {
        this.itemStackBoots = itemStackBoots;
        return this;
    }

    public CustomEquipment setRecipeBoots(Recipe recipeBoots) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeBoots);
        return this;
    }

    public CustomEquipment setRecipeBoots(Recipes recipeBoots) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeBoots.getRecipe());
        return this;
    }

    public CustomEquipment setShovel(ItemStack itemStackHelmet, Recipes recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setShovel(ItemStack itemStackHelmet, Recipe recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setItemStackShovel(ItemStack itemStackShovel) {
        this.itemStackShovel = itemStackShovel;
        return this;
    }

    public CustomEquipment setRecipeShovel(Recipe recipeShovel) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeShovel);
        return this;
    }

    public CustomEquipment setRecipeShovel(Recipes recipeShovel) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeShovel.getRecipe());
        return this;
    }

    public CustomEquipment setAxe(ItemStack itemStackHelmet, Recipes recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setAxe(ItemStack itemStackHelmet, Recipe recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setItemStackAxe(ItemStack itemStackAxe) {
        this.itemStackAxe = itemStackAxe;
        return this;
    }

    public CustomEquipment setRecipeAxe(Recipe recipeAxe) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeAxe);
        return this;
    }

    public CustomEquipment setRecipeAxe(Recipes recipeAxe) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeAxe.getRecipe());
        return this;
    }

    public CustomEquipment setSword(ItemStack itemStackHelmet, Recipes recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setSword(ItemStack itemStackHelmet, Recipe recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setItemStackSword(ItemStack itemStackSword) {
        this.itemStackSword = itemStackSword;
        return this;
    }

    public CustomEquipment setRecipeSword(Recipe recipeSword) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeSword);
        return this;
    }

    public CustomEquipment setRecipeSword(Recipes recipeSword) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeSword.getRecipe());
        return this;
    }

    public CustomEquipment setPickaxe(ItemStack itemStackHelmet, Recipes recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setPickaxe(ItemStack itemStackHelmet, Recipe recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setItemStackPickaxe(ItemStack itemStackPickaxe) {
        this.itemStackPickaxe = itemStackPickaxe;
        return this;
    }

    public CustomEquipment setRecipePickaxe(Recipe recipePickaxe) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipePickaxe);
        return this;
    }

    public CustomEquipment setRecipePickaxe(Recipes recipePickaxe) {
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipePickaxe.getRecipe());
        return this;
    }

    public CustomEquipment setHoe(ItemStack itemStackHelmet, Recipes recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setHoe(ItemStack itemStackHelmet, Recipe recipeHelmet) {
        setItemStackHelmet(itemStackHelmet);
        setRecipeHelmet(recipeHelmet);
        return this;
    }

    public CustomEquipment setItemStackHoe(ItemStack itemStackHoe) {
        this.itemStackHoe = itemStackHoe;
        return this;
    }

    public CustomEquipment setRecipeHoe(Recipe recipeHoe){
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeHoe);
        return this;
    }

    public CustomEquipment setRecipeHoe(Recipes recipeHoe){
        SunderiaSkyblock.getInstance().getServer().addRecipe(recipeHoe.getRecipe());
        return this;
    }

    //Init
    public CustomEquipment initArmor(){
        initHelmet();
        initChestplate();
        initLeggings();
        initBoots();
        return this;
    }

    public CustomEquipment initTools() {
        initSword();
        initPickaxe();
        initAxe();
        initShovel();
        initHoe();
        return this;
    }

    public CustomEquipment initHelmet(){
        SunderiaSkyblock.getInstance().getServer().addRecipe(new ShapedRecipe(SunderiaSkyblock.getKey(SunderiaSkyblock.stringToKey(itemStack.getItemMeta().getDisplayName()) + "_helmet"), itemStackArmor).setIngredient('A', itemStack.getType()).shape("AAA", "A A"));
        return this;
    }

    public CustomEquipment initChestplate(){
        SunderiaSkyblock.getInstance().getServer().addRecipe(new ShapedRecipe(SunderiaSkyblock.getKey(SunderiaSkyblock.stringToKey(itemStack.getItemMeta().getDisplayName()) + "_chestplate"), itemStackArmor).setIngredient('A', itemStack.getType()).shape("A A", "AAA", "AAA"));
        return this;
    }

    public CustomEquipment initLeggings(){
        SunderiaSkyblock.getInstance().getServer().addRecipe(new ShapedRecipe(SunderiaSkyblock.getKey(SunderiaSkyblock.stringToKey(itemStack.getItemMeta().getDisplayName()) + "_leggings"), itemStackArmor).setIngredient('A', itemStack.getType()).shape("AAA", "A A", "A A"));
        return this;
    }

    public CustomEquipment initBoots(){
        SunderiaSkyblock.getInstance().getServer().addRecipe(new ShapedRecipe(SunderiaSkyblock.getKey(SunderiaSkyblock.stringToKey(itemStack.getItemMeta().getDisplayName()) + "_boots"), itemStackArmor).setIngredient('A', itemStack.getType()).shape("A A", "A A"));
        return this;
    }

    public CustomEquipment initPickaxe() {
        SunderiaSkyblock.getInstance().getServer().addRecipe(new ShapedRecipe(SunderiaSkyblock.getKey(SunderiaSkyblock.stringToKey(itemStack.getItemMeta().getDisplayName()) + "_pickaxe"), itemStackArmor).setIngredient('A', itemStack.getType()).setIngredient('B', Material.STICK).shape("AAA", " B ", " B "));
        return this;
    }

    public CustomEquipment initHoe(){
        SunderiaSkyblock.getInstance().getServer().addRecipe(new ShapedRecipe(SunderiaSkyblock.getKey(SunderiaSkyblock.stringToKey(itemStack.getItemMeta().getDisplayName()) + "_hoe"), itemStackArmor).setIngredient('A', itemStack.getType()).setIngredient('B', Material.STICK).shape("AA ", " B ", " B "));
        return this;
    }

    public CustomEquipment initSword(){
        SunderiaSkyblock.getInstance().getServer().addRecipe(new ShapedRecipe(SunderiaSkyblock.getKey(SunderiaSkyblock.stringToKey(itemStack.getItemMeta().getDisplayName()) + "_sword"), itemStackArmor).setIngredient('A', itemStack.getType()).setIngredient('B', Material.STICK).shape(" A ", " A ", " B "));
        return this;
    }

    public CustomEquipment initAxe(){
        SunderiaSkyblock.getInstance().getServer().addRecipe(new ShapedRecipe(SunderiaSkyblock.getKey(SunderiaSkyblock.stringToKey(itemStack.getItemMeta().getDisplayName()) + "_axe"), itemStackArmor).setIngredient('A', itemStack.getType()).setIngredient('B', Material.STICK).shape("AA ", "AB ", " B "));
        return this;
    }

    public CustomEquipment initShovel(){
        SunderiaSkyblock.getInstance().getServer().addRecipe(new ShapedRecipe(SunderiaSkyblock.getKey(SunderiaSkyblock.stringToKey(itemStack.getItemMeta().getDisplayName()) + "_shovel"), itemStackArmor).setIngredient('A', itemStack.getType()).setIngredient('B', Material.STICK).shape(" A ", " B ", " B "));
        return this;
    }

}
