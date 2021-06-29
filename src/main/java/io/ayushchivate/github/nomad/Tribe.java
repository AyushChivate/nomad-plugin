package io.ayushchivate.github.nomad;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tribe {

    private String name;
    private Material material;
    private UUID playerHeadOwner;
    private int slotNumber;
    private List<String> lore;
    private List<UUID> players;

    public Tribe(String name) {
        this.name = name;
        this.slotNumber = -1;
        this.material = Material.APPLE;
        this.playerHeadOwner = null;
        this.lore = new ArrayList<>();
        this.players = new ArrayList<>();
    }
    public Tribe(String name, Material material, UUID playerHeadOwner, int slotNumber, List<String> lore, List<UUID> players) {
        this.name = name;
        this.slotNumber = slotNumber;
        this.material = material;
        this.playerHeadOwner = playerHeadOwner;
        this.lore = lore;
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public Material getMaterial() {
        return material;
    }

    public UUID getPlayerHeadOwner() {
        return playerHeadOwner;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public List<String> getLore() {
        return lore;
    }

    public boolean containsPlayer(Player player) {
        return this.players.contains(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        this.players.remove(player.getUniqueId());
    }

    public void addPlayer(Player player) {
        this.players.add(player.getUniqueId());
    }
}
