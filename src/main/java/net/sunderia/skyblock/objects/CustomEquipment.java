package net.sunderia.skyblock.objects;

import net.sunderia.skyblock.SunderiaSkyblock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

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

    public CustomEquipment(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    //Getters
    public ItemStack getItemStack() {
        return itemStack;
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
        return this;
    }

    public CustomEquipment initChestplate(){
        return this;
    }

    public CustomEquipment initLeggings(){
        return this;
    }

    public CustomEquipment initBoots(){
        return this;
    }

    public CustomEquipment initPickaxe() { return this; }

    public CustomEquipment initHoe(){
        return this;
    }

    public CustomEquipment initSword(){
        return this;
    }

    public CustomEquipment initAxe(){
        return this;
    }

    public CustomEquipment initShovel(){
        return this;
    }

}
