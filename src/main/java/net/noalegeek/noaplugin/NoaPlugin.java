package net.noalegeek.noaplugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import fr.sunderia.sunderiautils.SunderiaUtils;
import fr.sunderia.sunderiautils.utils.ItemBuilder;
import net.noalegeek.noaplugin.listener.Events;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class NoaPlugin extends JavaPlugin {

    public static final String header = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "NoaPlugin" + ChatColor.DARK_GREEN + "] ";
    private static NoaPlugin instance;
    private ProtocolManager protocolManager;

    public static NamespacedKey getKey(String key) {
        return new NamespacedKey(getInstance(), key);
    }

    public static NoaPlugin getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        instance = this;
    }

    @Override
    public void onEnable() {
        SunderiaUtils.of(this);
        this.saveDefaultConfig();
        try {
            SunderiaUtils.registerListeners(this.getClass().getPackageName() + ".listener");
            SunderiaUtils.registerCommands(this.getClass().getPackageName() + ".commands");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().runTaskTimer(this, Events::onSecondEvent, 20, 20);
        this.getLogger().info("[NoaPlugin] Plugin is enabled.");
        Bukkit.addRecipe(new ShapedRecipe(getKey("test"), new ItemBuilder(Material.NETHERITE_BLOCK).hideIdentifier().setDisplayName("Heart of Hell").build()).shape("BAB", "AAA", "BAB").setIngredient('A', new RecipeChoice.ExactChoice(new ItemBuilder(Material.NETHERITE_INGOT).hideIdentifier().setAmount(2).build())).setIngredient('B', new RecipeChoice.ExactChoice(new ItemBuilder(Material.NETHERITE_INGOT).setAmount(5).hideIdentifier().build())));
        Bukkit.addRecipe(new ShapelessRecipe(getKey("test2"), new ItemBuilder(Material.BEDROCK).hideIdentifier().setDisplayName("Mixed Ores").build()).addIngredient(new RecipeChoice.ExactChoice(new ItemBuilder(Material.COPPER_INGOT).hideIdentifier().setAmount(9).build()))
                .addIngredient(new RecipeChoice.ExactChoice(new ItemBuilder(Material.AMETHYST_SHARD).hideIdentifier().setAmount(8).build()))
                .addIngredient(new RecipeChoice.ExactChoice(new ItemBuilder(Material.COAL).hideIdentifier().setAmount(7).build()))
                .addIngredient(new RecipeChoice.ExactChoice(new ItemBuilder(Material.IRON_INGOT).hideIdentifier().setAmount(6).build()))
                .addIngredient(new RecipeChoice.ExactChoice(new ItemBuilder(Material.GOLD_INGOT).hideIdentifier().setAmount(5).build()))
                .addIngredient(new RecipeChoice.ExactChoice(new ItemBuilder(Material.REDSTONE).hideIdentifier().setAmount(4).build()))
                .addIngredient(new RecipeChoice.ExactChoice(new ItemBuilder(Material.LAPIS_LAZULI).hideIdentifier().setAmount(3).build()))
                .addIngredient(new RecipeChoice.ExactChoice(new ItemBuilder(Material.DIAMOND).hideIdentifier().setAmount(2).build()))
                .addIngredient(new RecipeChoice.ExactChoice(new ItemBuilder(Material.EMERALD).hideIdentifier().setAmount(1).build())));
        Bukkit.addRecipe(new ShapedRecipe(getKey("test3"), new ItemBuilder(Material.PETRIFIED_OAK_SLAB).hideIdentifier().build()).shape("AAA").setIngredient('A', new RecipeChoice.ExactChoice(new ItemBuilder(Material.OAK_SLAB).setAmount(2).hideIdentifier().build())));
        Bukkit.getScheduler().runTaskTimer(this, this::saveConfig, 6000L, 6000L);
    }

    @Override
    public void onDisable() {
        getLogger().info("[NoaPlugin] Plugin is disabled.");
        this.saveConfig();
    }
}