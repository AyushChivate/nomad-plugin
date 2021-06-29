package io.ayushchivate.github.nomad;

import net.dohaw.corelib.StringUtils;
import org.bukkit.entity.Player;
import sun.swing.StringUIClientPropertyKey;

import java.io.File;
import java.util.ArrayList;

public class TribeHandler {

    private ArrayList<Tribe> tribes = new ArrayList<>();
    NomadPlugin plugin;

    public TribeHandler(NomadPlugin plugin) {
        this.plugin = plugin;
    }

    public ArrayList<Tribe> getTribes() {
        return tribes;
    }

    public void addTribe(Tribe tribe) {
        this.tribes.add(tribe);
    }

    public void removeTribe(Tribe tribe) {
        this.tribes.remove(tribe);
    }

    public void removeAllTribes() {
        tribes.clear();
    }
    
    public boolean deleteTribe(String name) {
        return this.tribes.removeIf(tribe -> tribe.getName().equals(name));
    }

    public Tribe getPlayerTribe(Player player) {
        for (Tribe tribe : this.tribes) {
            if (tribe.containsPlayer(player)) {
                return tribe;
            }
        }
        return null;
    }

    public void loadTribes() {
        File tribeDataFolder = new File(plugin.getDataFolder(), "tribe-data");
        for (File tribeDataFile : tribeDataFolder.listFiles()) {
            TribeConfig tribeConfig = new TribeConfig(tribeDataFile);
            this.tribes.add(tribeConfig.getTribe());
        }
    }

    public void saveTribes() {
        for (Tribe tribe : this.tribes) {
            String tribeName = StringUtils.removeChatColor(StringUtils.colorString(tribe.getName()));
            File tribeDataFile = new File(plugin.getDataFolder() + File.separator + "tribe-data", tribeName + ".yml");
            TribeConfig tribeConfig = new TribeConfig(tribeDataFile);
            tribeConfig.saveTribe(tribe);
        }
    }

    public void saveTribe(String name) {
        for (Tribe tribe : this.tribes) {
            if (tribe.getName().equals(name)) {
                String tribeName = StringUtils.removeChatColor(StringUtils.colorString(tribe.getName()));
                File tribeDataFile = new File(plugin.getDataFolder() + File.separator + "tribe-data", tribeName + ".yml");
                TribeConfig tribeConfig = new TribeConfig(tribeDataFile);
                tribeConfig.saveTribe(tribe);
                return;
            }
        }
    }
}
