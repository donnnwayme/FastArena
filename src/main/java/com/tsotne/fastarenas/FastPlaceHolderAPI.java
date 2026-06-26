package com.tsotne.fastarenas;

import com.tsotne.fastarenas.autoreset.AutoResetArena;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FastPlaceHolderAPI extends PlaceholderExpansion {
    private final FastArenas plugin;

    public FastPlaceHolderAPI(FastArenas plugin) {
        this.plugin = plugin;
    }

    @NotNull
    public String getIdentifier() {
        return "fastarena";
    }

    @NotNull
    public String getAuthor() {
        return "geocotne";
    }

    @NotNull
    public String getVersion() {
        return "1.0";
    }

    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (identifier.startsWith("timer_")) {
            String raw = identifier.substring("timer_".length());
            String[] parts = raw.split("_");
            String arenaName = parts[0];
            String timePart = parts.length > 1 ? parts[1].toLowerCase() : "";
            AutoResetArena arena = this.plugin.getautoResetManager().getArena(arenaName);
            if (arena == null) {
                return "Unknown arena";
            } else {
                long millis = arena.getMillisUntilReset();
                long seconds = millis / 1000L;
                long minutes = seconds / 60L;
                long hours = minutes / 60L;
                long s = seconds % 60L;
                long m = minutes % 60L;
                switch (timePart) {
                    case "hour":
                        return String.valueOf(hours);
                    case "minute":
                        return String.valueOf(m);
                    case "second":
                        return String.valueOf(s);
                    case "":
                        StringBuilder sb = new StringBuilder();
                        if (hours > 0L) {
                            sb.append(hours).append("h ");
                        }

                        if (m > 0L) {
                            sb.append(m).append("m ");
                        }

                        if (s > 0L || sb.isEmpty()) {
                            sb.append(s).append("s");
                        }

                        return sb.toString().trim();
                    default:
                        return "Invalid format";
                }
            }
        } else {
            return null;
        }
    }
}