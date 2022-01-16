package McEssence.ArmourEnhancements;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Config{
    private final Main main;
    public Config(Main mainTemp){
        main = mainTemp;
    }

    public Boolean getEnabled(){
        return main.getConfig().getBoolean("general.enabled");
    }

    public HashMap<String, HashMap<String, Material>> getArmourSets(){
        HashMap<String, HashMap<String, Material>> armourSets = new HashMap<>();
        try {
            ConfigurationSection armourSetsConfig = main.getConfig().getConfigurationSection("armourSets");
            for (String armourSetName : armourSetsConfig.getKeys(false)) {
                MemorySection armourSet = (MemorySection) armourSetsConfig.get(armourSetName);
                HashMap<String, Material> armourPieces = new HashMap<String, Material>();
                armourPieces.put("HELMET", Material.getMaterial((String) armourSet.get("HELMET")));
                armourPieces.put("CHESTPLATE", Material.getMaterial((String) armourSet.get("CHESTPLATE")));
                armourPieces.put("LEGGINGS", Material.getMaterial((String) armourSet.get("LEGGINGS")));
                armourPieces.put("BOOTS", Material.getMaterial((String) armourSet.get("BOOTS")));
                armourSets.put(armourSetName, armourPieces);
            }
        }catch(Exception e){
            Bukkit.getLogger().info(ChatColor.RED + "Error when loading armour sets from config");
        }
        return armourSets;
    }

    public HashMap<String, ArrayList> getArmourSetEffects(){
        HashMap<String, ArrayList> armourSetEffects = new HashMap<>();
        try {
            ConfigurationSection armourSetsEffectsConfig = main.getConfig().getConfigurationSection("effects");
            for (String armourSetsEffectsName : armourSetsEffectsConfig.getKeys(false)) {
                MemorySection armourSetsEffects = (MemorySection) armourSetsEffectsConfig.get(armourSetsEffectsName);
                ArrayList<PotionEffect> armourSetEffectsList = new ArrayList<PotionEffect>();
                Map<String, Object> effects = armourSetsEffects.getValues(false);
                for (Map.Entry<String, Object> effectMap : effects.entrySet()) {
                  MemorySection effectValuesSection = (MemorySection) effectMap.getValue();
                  Map<String, Object> effectValues = effectValuesSection.getValues(true);
                  armourSetEffectsList.add(new PotionEffect(PotionEffectType.getByName((String) effectValues.get("EFFECTNAME")), (Integer) effectValues.get("DURATION"), (Integer) effectValues.get("LEVEL")));
                  armourSetEffects.put(armourSetsEffectsName, armourSetEffectsList);
                }
//                for(Object effect : effects) {
//
//                }

            }
        }catch(Exception e){
            Bukkit.getLogger().info(ChatColor.RED + "Error when loading armour set effects from config");
        }
        return armourSetEffects;
    }

    public String getGlowstickItemString(){
        return main.getConfig().getString("general.glowstickItem");
    }

    public Material getGlowstickMaterial(){
        return Material.getMaterial(getGlowstickItemString());
    }

    public int getThrowDistanceMultiplier(){
        return main.getConfig().getInt("general.throwDistanceMultiplier");
    }

    public int getGlowstickDurationSeconds(){
        return main.getConfig().getInt("general.glowstickDurationSeconds");
    }
}
