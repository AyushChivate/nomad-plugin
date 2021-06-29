package io.ayushchivate.github.nomad;

import net.dohaw.corelib.Config;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TribeConfig extends Config {

    public TribeConfig(File file) {
        super(file);
    }

    public Tribe getTribe() {

        String name = config.getString("Name");
        Material material = Material.valueOf(config.getString("Material", "APPLE"));
        UUID playerHeadOwner = material == Material.PLAYER_HEAD ? UUID.fromString(config.getString("Player Head Owner")) : null;
        int slotNumber = config.getInt("Slot Number");
        List<String> lore = config.getStringList("Lore");
        List<String> uuidStrings = config.getStringList("Players");

        List<UUID> uuids = new ArrayList<>();
        for (String uuidString : uuidStrings) {
            uuids.add(UUID.fromString(uuidString));
        }

        return new Tribe(name, material, playerHeadOwner, slotNumber, lore, uuids);
    }

    public void saveTribe(Tribe tribe) {

        List<String> uuidString = new ArrayList<>();
        List<UUID> uuids = tribe.getPlayers();

        for (UUID uuid : uuids) {
            uuidString.add(uuid.toString());
        }

        config.set("Name", tribe.getName());
        config.set("Material", tribe.getMaterial().toString());

        if (tribe.getPlayerHeadOwner() == null) {
            config.set("Player Head Owner", "None");
        } else {
            config.set("Player Head Owner", tribe.getPlayerHeadOwner().toString());
        }

        config.set("Slot Number", tribe.getSlotNumber());
        config.set("Lore", tribe.getLore());
        config.set("Players", uuidString);

        saveConfig();
    }
}
