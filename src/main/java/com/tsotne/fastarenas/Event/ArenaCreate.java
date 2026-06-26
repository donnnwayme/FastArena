package com.tsotne.fastarenas.Event;

import com.tsotne.fastarenas.FastArenas;
import com.tsotne.fastarenas.utils.SendMessageUtils;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArenaCreate implements Listener {
    private final FastArenas plugin;
    public static Location pos1 = null;
    public static Location pos2 = null;

    public ArenaCreate(FastArenas plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        String wandname = this.plugin.getConfigManager().getWandName();
        List<String> wandlore = this.plugin.getConfigManager().getWandLore();
        Material wandmaterial = this.plugin.getConfigManager().getmaterial();
        if (item != null
                && item.getType() == wandmaterial
                && item.hasItemMeta()
                && item.getItemMeta() instanceof ItemMeta
                && item.getItemMeta().hasDisplayName()
                && item.getItemMeta().getDisplayName().equalsIgnoreCase(wandname)
                && item.getItemMeta().getLore().equals(wandlore)) {
            ItemMeta meta = item.getItemMeta();
            event.setCancelled(true);
            Block clickedBlock = event.getClickedBlock();
            if (clickedBlock == null) {
                return;
            }

            if (event.getAction().toString().contains("LEFT_CLICK_BLOCK")
                    && (player.hasPermission("fastarena.wand") || player.hasPermission("fastarena.admin") || player.isOp())) {
                pos1 = clickedBlock.getLocation();
                String pos1message = this.plugin.getConfigManager().getPosition1Message(pos1.x(), pos1.y(), pos1.z());
                SendMessageUtils.Sendmessage(player, pos1message);
            }

            if (event.getAction().toString().contains("RIGHT_CLICK_BLOCK")
                    && (player.hasPermission("fastarena.wand") || player.hasPermission("fastarena.admin") || player.isOp())) {
                pos2 = clickedBlock.getLocation();
                String pos2message = this.plugin.getConfigManager().getPosition2Message(pos2.x(), pos2.y(), pos2.z());
                SendMessageUtils.Sendmessage(player, pos2message);
            }
        }
    }
}