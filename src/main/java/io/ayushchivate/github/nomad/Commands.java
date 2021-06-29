package io.ayushchivate.github.nomad;

import net.dohaw.corelib.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Commands implements CommandExecutor {

    NomadPlugin plugin;

    public Commands(NomadPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            tribeCommand(player);
        } else if (args.length == 1 && args[0].equals("leave")) {
            leaveCommand(player);
        } else if (args.length == 1 && args[0].equals("reload")) {
            reloadCommand(player);
        } else if (args.length == 2 && args[0].equals("create")) {
            try {
                createCommand(player, args[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args.length == 2 && args[0].equals("delete")) {
            deleteCommand(player, args[1]);
        }
        /* do the scoreboard thing */
        else {
            player.sendMessage(ChatColor.RED + "Invalid command.");
        }

        return true;
    }

    public void tribeCommand(Player player) {
        TribesMenu tribesMenu = new TribesMenu(this.plugin, this.plugin.getInventorySize());
        tribesMenu.initializeItems(player);
        tribesMenu.openInventory(player);
    }

    public void leaveCommand(Player player) {

        Tribe tribe = this.plugin.getTribeHandler().getPlayerTribe(player);

        if (tribe == null) {
            player.sendMessage(ChatColor.RED + "You are not in a tribe!");
            return;
        }

        tribe.removePlayer(player);
        player.sendMessage(ChatColor.GREEN + "You have successfully left the tribe.");

        player.setPlayerListName(player.getName());
    }

    public void createCommand(Player player, String name) throws IOException {

        if (!player.hasPermission("nomad.delete")) {
            player.sendMessage(ChatColor.RED + "You don't have permissions to use this command!");
            return;
        }

        File newFile = new File(plugin.getDataFolder() + File.separator + "tribe-data", name + ".yml");

        if (newFile.exists()) {
            player.sendMessage(ChatColor.RED + "There is already a tribe with this name!");
            return;
        }

        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        this.plugin.getTribeHandler().addTribe(new Tribe(name));
        this.plugin.getTribeHandler().saveTribe(name);
        player.sendMessage(ChatColor.GREEN + "Tribe created successfully.");
    }

    public void reloadCommand(Player player) {

        if (!player.hasPermission("nomad.delete")) {
            player.sendMessage(ChatColor.RED + "You don't have permissions to use this command!");
            return;
        }

        TribeHandler tribeHandler = this.plugin.getTribeHandler();
        tribeHandler.removeAllTribes();
        tribeHandler.loadTribes();
        this.plugin.loadConfigValues();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

            Tribe tribe = this.plugin.getTribeHandler().getPlayerTribe(onlinePlayer);

            if (tribe == null) {
                onlinePlayer.setPlayerListName(onlinePlayer.getName());
                return;
            }

            onlinePlayer.setPlayerListName(onlinePlayer.getName() + " [" + StringUtils.colorString(tribe.getName()) + StringUtils.colorString("&f]"));
        }
    }

    public void deleteCommand(Player player, String name) {

        if (!player.hasPermission("nomad.delete")) {
            player.sendMessage(ChatColor.RED + "You don't have permissions to use this command!");
            return;
        }

        if (plugin.getTribeHandler().deleteTribe(name)) {
            File file = new File(plugin.getDataFolder() + File.separator + "tribe-data", name + ".yml");
            file.delete();
            player.sendMessage(ChatColor.GREEN + "Tribe deleted successfully.");
        } else {
            player.sendMessage(ChatColor.RED + "This tribe does not exist!");
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

            Tribe tribe = this.plugin.getTribeHandler().getPlayerTribe(onlinePlayer);

            if (tribe == null) {
                onlinePlayer.setPlayerListName(onlinePlayer.getName());
                return;
            }

            onlinePlayer.setPlayerListName(onlinePlayer.getName() + " [" + StringUtils.colorString(tribe.getName()) + StringUtils.colorString("&f]"));
        }
    }
}
