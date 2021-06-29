package io.ayushchivate.github.nomad;

import net.dohaw.corelib.JPUtils;
import net.dohaw.corelib.StringUtils;
import net.dohaw.corelib.helpers.ItemStackHelper;
import net.dohaw.corelib.menus.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class TribesMenu extends Menu implements Listener {

    NomadPlugin plugin;

    public TribesMenu(JavaPlugin plugin, int numSlots) {
        super(plugin, null, "Tribes", numSlots);
        this.plugin = (NomadPlugin) plugin;
        JPUtils.registerEvents(this);
    }

    @Override
    public void initializeItems(Player player) {
        List<Tribe> tribes = this.plugin.getTribeHandler().getTribes();
        for (Tribe tribe : tribes) {

            int slotNumber = tribe.getSlotNumber();

            if (slotNumber == -1) {
                player.sendMessage(ChatColor.RED + "The server owner must configure all of the tribes!");
                plugin.getLogger().severe("You must configure all tribes.");
                continue;
            }

            if (tribe.getMaterial() == Material.PLAYER_HEAD) {
                inv.setItem(slotNumber, createGuiItem(ItemStackHelper.getPlayerHead(tribe.getPlayerHeadOwner()), tribe.getName(), tribe.getLore()));

            } else {
                inv.setItem(tribe.getSlotNumber(), createGuiItem(tribe.getMaterial(), tribe.getName(), tribe.getLore()));
            }
        }
        this.fillerMat = Material.BLACK_STAINED_GLASS_PANE;
        fillMenu(false);
    }

    @EventHandler
    @Override
    protected void onInventoryClick(InventoryClickEvent e) {

        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        Inventory clickedInventory = e.getClickedInventory();
        Inventory topInventory = player.getOpenInventory().getTopInventory();

        if (clickedInventory == null) return;
        if (!topInventory.equals(inv) || !clickedInventory.equals(topInventory)) return;
        if (clickedItem == null && e.getCursor() == null) return;

        e.setCancelled(true);

        if (clickedItem == null || clickedItem.getItemMeta() == null || clickedItem.getType() == fillerMat) {
            return;
        }

        if (this.plugin.getTribeHandler().getPlayerTribe(player) != null) {
            player.sendMessage(ChatColor.RED + "You must leave your tribe before joining another one! (/tribe leave)");
            return;
        }

        String displayName = StringUtils.removeChatColor(clickedItem.getItemMeta().getDisplayName());

        Tribe tribe = getTribeClicked(displayName);

        if (tribe == null) return;

        tribe.addPlayer(player);

        player.setPlayerListName(player.getName() + " [" + StringUtils.colorString(tribe.getName()) + StringUtils.colorString("&f]"));

        player.closeInventory();

        player.sendMessage(ChatColor.GREEN + "You have successfully joined a tribe!");

    }

    public Tribe getTribeClicked(String displayName) {

        for (Tribe tribe : this.plugin.getTribeHandler().getTribes()) {
            String uncoloredTribeName = StringUtils.removeChatColor(StringUtils.colorString(tribe.getName()));
            if (uncoloredTribeName.equals(displayName)) {
                return tribe;
            }
        }
        return null;
    }


}
