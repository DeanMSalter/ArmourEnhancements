package McEssence.ArmourEnhancements;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin {
    Config config;

    @Override
    public void onEnable() {
        File f = new File(this.getDataFolder() + "/");
        if(!f.exists()) {
            f.mkdir();
        }
        getConfig().options().copyDefaults(true);
        saveConfig();
        config = new Config(this);
        if (!config.getEnabled()){
            Bukkit.getLogger().info(ChatColor.RED + " Disabled" + this.getName() + " As not enabled in config");
            return;
        }

        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled" + this.getName());
        this.getCommand("reload").setExecutor(new Commands());
        Bukkit.getScheduler().runTaskTimer(this,() -> {
            if (!config.getEnabled()){
                return;
            }
            HashMap<String, HashMap<String, Material>> armourSets = config.getArmourSets();
            HashMap<String, ArrayList> armourSetEffects = config.getArmourSetEffects();
            for(Player player : Bukkit.getOnlinePlayers()){
                PlayerInventory inventory = player.getInventory();
                ItemStack helmet = inventory.getHelmet();
                ItemStack chestplate = inventory.getChestplate();
                ItemStack leggings = inventory.getLeggings();
                ItemStack boots = inventory.getBoots();

                HashMap<String, ItemStack> armourPieces = new HashMap<String, ItemStack>();
                armourPieces.put("HELMET", helmet);
                armourPieces.put("CHESTPLATE", chestplate);
                armourPieces.put("LEGGINGS", leggings);
                armourPieces.put("BOOTS", boots);

                for (Map.Entry<String, HashMap<String, Material>> armourSetMap : armourSets.entrySet()) {
                    boolean matchesDesiredArmour = true;
                    String armourSetName = armourSetMap.getKey();
                    HashMap<String, Material> armourSet = armourSetMap.getValue();
                    for (Map.Entry<String, ItemStack> armourPieceMap : armourPieces.entrySet()) {
                        String armourPieceName = armourPieceMap.getKey();
                        ItemStack armourPiece = armourPieceMap.getValue();
                        if (!checkArmourPiece(armourPiece, armourSet.get(armourPieceName))) {
                            matchesDesiredArmour = false;
                            break;
                        }
                    }
                    if (matchesDesiredArmour) {
                        ArrayList<PotionEffect> armourEffects = armourSetEffects.get(armourSetName);
                        for (PotionEffect effect : armourEffects) {
                            player.addPotionEffect(effect);
                        }
                    }
                }

            }
        },20, 20);

    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.GREEN + "Disabled " + this.getName());
    }

    private Boolean checkArmourPiece(ItemStack armourPiece, Material desiredArmourPiece){
        if (armourPiece == null && desiredArmourPiece != Material.AIR) {
            return false;
        }

        if (armourPiece != null && (armourPiece.getType() != desiredArmourPiece)) {
            return false;
        }

        return true;
    }

}
