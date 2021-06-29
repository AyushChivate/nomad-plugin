package io.ayushchivate.github.nomad;

import net.dohaw.corelib.CoreLib;
import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class NomadPlugin extends JavaPlugin implements Listener {

    private TribeHandler tribeHandler;
    private int inventorySize;
    private DefaultConfig defaultConfig;

    @Override
    public void onEnable() {
        CoreLib.setInstance(this);

        JPUtils.registerEvents(this);

        JPUtils.validateFiles("config.yml");
        JPUtils.validateFilesOrFolders(new HashMap<String, Object>() {{
            put("tribe-data", getDataFolder());
        }}, true);

        loadConfigValues();
        this.tribeHandler = new TribeHandler(this);

        tribeHandler.loadTribes();

        JPUtils.registerCommand("tribe", new Commands(this));
    }

    @Override
    public void onDisable() {
        tribeHandler.saveTribes();
    }

    public int getInventorySize() {
        return this.inventorySize;
    }

    public TribeHandler getTribeHandler() {
        return this.tribeHandler;
    }

    public void loadConfigValues() {
        this.defaultConfig = new DefaultConfig();
        this.inventorySize = defaultConfig.getInventorySize();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        Tribe tribe = this.tribeHandler.getPlayerTribe(player);

        if (tribe == null) {
            return;
        }

        player.setPlayerListName(player.getName() + " [" + StringUtils.colorString(tribe.getName()) + StringUtils.colorString("&f]"));
    }
}
