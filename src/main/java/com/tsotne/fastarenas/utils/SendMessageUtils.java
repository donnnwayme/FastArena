package com.tsotne.fastarenas.utils;

import com.tsotne.fastarenas.FastArenas;
import javax.annotation.Nullable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SendMessageUtils {
    private final FastArenas plugin;

    public SendMessageUtils(FastArenas plugin) {
        this.plugin = plugin;
    }

    public static void Sendmessage(@Nullable CommandSender sender, String message) {
        FastArenas plugin = (FastArenas)JavaPlugin.getPlugin(FastArenas.class);
        String type = plugin.getConfig().getString("message-type", "message").toLowerCase();
        if (sender instanceof Player player) {
            byte var6 = -1;
            switch (type.hashCode()) {
                case 1852186250:
                    if (type.equals("action_bar")) {
                        var6 = 0;
                    }
                default:
                    switch (var6) {
                        case 0:
                            player.sendActionBar(message);
                            break;
                        default:
                            player.sendMessage(message);
                    }
            }
        } else {
            plugin.getLogger().info(message);
        }
    }
}