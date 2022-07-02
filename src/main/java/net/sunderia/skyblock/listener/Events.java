package net.sunderia.skyblock.listener;

import fr.sunderia.sunderiautils.SunderiaUtils;
import net.sunderia.skyblock.SunderiaSkyblock;
import net.sunderia.skyblock.objects.Inventories;
import net.sunderia.skyblock.objects.Items;
import net.sunderia.skyblock.objects.Skills;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.Map;

public class Events implements Listener {

    private final Map<Location, Material> placedBlocks = new HashMap<>();

    public static void onSecondEvent() {

    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player) || event.getEntityType() != EntityType.SNOWBALL || event.getEntity().getCustomName() == null || !event.getEntity().getCustomName().equals("Grenada"))
            return;
        event.getEntity().getWorld().createExplosion(event.getHitBlock() == null ? event.getHitEntity().getLocation() : event.getHitBlock().getLocation(), 1, false, false);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        //Verify if the block isn't placed
        if (!event.getBlock().hasMetadata("isPlaced")) {
            //Set xpAdded depending on the type of the block broken
            switch (event.getBlock().getType()) {
                //Mining skill
                case ICE, NETHERRACK -> Skills.addXp(Skills.MINING, event.getPlayer(), 0.5);
                case GRAVEL, STONE, COBBLESTONE -> Skills.addXp(Skills.MINING, event.getPlayer(), 1);
                case END_STONE, SAND, RED_SAND, MYCELIUM -> Skills.addXp(Skills.MINING, event.getPlayer(), 3);
                case COPPER_ORE -> Skills.addXp(Skills.MINING, event.getPlayer(), 4);
                case NETHER_QUARTZ_ORE, COAL_ORE, IRON_ORE -> Skills.addXp(Skills.MINING, event.getPlayer(), 5);
                case GOLD_ORE -> Skills.addXp(Skills.MINING, event.getPlayer(), 6);
                case GLOWSTONE, LAPIS_ORE, REDSTONE_ORE -> Skills.addXp(Skills.MINING, event.getPlayer(), 7);
                case EMERALD_ORE -> Skills.addXp(Skills.MINING, event.getPlayer(), 9);
                case DIAMOND_ORE -> Skills.addXp(Skills.MINING, event.getPlayer(), 10);
                case OBSIDIAN -> Skills.addXp(Skills.MINING, event.getPlayer(), 20);
                case ANCIENT_DEBRIS -> Skills.addXp(Skills.MINING, event.getPlayer(), 25);
                //Farming
                case CACTUS, RED_MUSHROOM_BLOCK, BROWN_MUSHROOM_BLOCK -> Skills.addXp(Skills.FARMING, event.getPlayer(), 2);
                case COCOA_BEANS, MELON, BEETROOTS, CARROTS, POTATOES, WHEAT -> Skills.addXp(Skills.FARMING, event.getPlayer(), 4);
                case PUMPKIN -> Skills.addXp(Skills.FARMING, event.getPlayer(), 4.5);
                case RED_MUSHROOM, BROWN_MUSHROOM, SUGAR_CANE -> Skills.addXp(Skills.FARMING, event.getPlayer(), 6);
                default -> {
                    //Verify if the block is a log to give xp to the player
                    if (event.getBlock().getType().name().contains("LOG")) {
                        Skills.addXp(Skills.WOODCUTTING, event.getPlayer(), 6);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            //Verify if the item picked up is a wool
            if (event.getItem().getItemStack().getType().name().contains("WOOL")) {
                Skills.addXp(Skills.FARMING, player, 2);
            } else {
                switch (event.getItem().getItemStack().getType()) {
                    //Farming
                    case LEATHER -> Skills.addXp(Skills.FARMING, player, 1);
                    case PORKCHOP, RABBIT_FOOT, RABBIT, RABBIT_HIDE, CHICKEN, FEATHER, BEEF, MUTTON -> Skills.addXp(Skills.FARMING, player, 2);
                }
            }
        }
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        double hardness = event.getBlock().getType().getHardness();
        double breakSpeed = event.getBlock().getBreakSpeed(event.getPlayer());
        System.out.println("hardness " + hardness);
        System.out.println("breakSpeed " + breakSpeed);
        System.out.println("seconds " + Math.round(hardness / breakSpeed / 20));
        System.out.println("ticks " + Math.round(hardness / breakSpeed));
    }

    @EventHandler
    public void onBlockDamageAbort(BlockDamageAbortEvent event) {

    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        //Set a metadata "isPlaced" used to verify if the block is placed thanks to this metadata
        event.getBlock().setMetadata("isPlaced", new FixedMetadataValue(SunderiaSkyblock.getInstance(), true));
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        //Metadata is removed when the block that has a metadata is pushed with a piston so we remove the metadata from the block and set the metadata to the block that has a new location modified by the piston
        event.getBlocks()
                .stream()
                .filter(block -> block.hasMetadata("isPlaced"))
                .map(Block::getState)
                .forEach(block -> {
                    block.removeMetadata("isPlaced", SunderiaUtils.getPlugin());
                    //We need to do a scheduler because metadata is set too fast while the block is not pushed to the new location
                    Bukkit.getScheduler().runTask(SunderiaUtils.getPlugin(), () -> block.getLocation().add(event.getDirection().getDirection()).getBlock().setMetadata("isPlaced", new FixedMetadataValue(SunderiaUtils.getPlugin(), true)));
                });
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        //Metadata is removed when the block that has a metadata is pushed with a piston so we remove the metadata from the block and set the metadata to the block that has a new location modified by the piston
        event.getBlocks()
                .stream()
                .filter(block -> block.hasMetadata("isPlaced"))
                .map(Block::getState)
                .forEach(block -> {
                    block.removeMetadata("isPlaced", SunderiaUtils.getPlugin());
                    //We need to do a scheduler because metadata is set too fast while the block is not pushed to the new location
                    Bukkit.getScheduler().runTask(SunderiaUtils.getPlugin(), () -> block.getLocation().add(event.getDirection().getDirection()).getBlock().setMetadata("isPlaced", new FixedMetadataValue(SunderiaUtils.getPlugin(), true)));
                });
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        //Go through all blocks that have the "isPlaced" metadata to remove that metadata because when saplings that have "isPlaced" metadata grow the logs in same position of the saplings have the "isPlaced" metadata
        event.getBlocks()
                .stream()
                .filter(blockState -> blockState.hasMetadata("isPlaced"))
                .forEach(blockState -> blockState.removeMetadata("isPlaced", SunderiaUtils.getPlugin()));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CRAFTING_TABLE && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            event.getPlayer().openInventory(Inventories.CRAFTING_GUI);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().setItem(4, Items.MINEMOBS_GUN);
    }
}
