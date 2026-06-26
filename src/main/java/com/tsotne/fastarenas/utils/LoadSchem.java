package com.tsotne.fastarenas.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockState;
import com.tsotne.fastarenas.FastArenas;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

public class LoadSchem {
    private final FastArenas plugin;

    public LoadSchem(FastArenas plugin) {
        this.plugin = plugin;
    }

    public static void loadSchematic(FastArenas plugin, String arenaName, @Nullable Player player) {
        FileConfiguration config = plugin.getConfig();
        File schemFile = new File(new File(plugin.getDataFolder(), "arena"), arenaName + ".schem");
        String arenanotfound = plugin.getConfigManager().getArenaNotFound();
        if (!schemFile.exists()) {
            Bukkit.broadcastMessage(arenanotfound);
        } else {
            String worldName = config.getString("arena." + arenaName + ".world");
            if (worldName == null) {
                String worldmessage = plugin.getConfigManager().getworldnotspecified();
                SendMessageUtils.Sendmessage(player, worldmessage);
            } else {
                World bukkitWorld = Bukkit.getWorld(worldName);
                if (bukkitWorld == null) {
                    String worldmessage = plugin.getConfigManager().getworldnotloaded();
                    SendMessageUtils.Sendmessage(player, worldmessage);
                } else {
                    List<Double> pos1List = config.getDoubleList("arena." + arenaName + ".pos1");
                    List<Double> pos2List = config.getDoubleList("arena." + arenaName + ".pos2");
                    if (pos1List.size() == 3 && pos2List.size() == 3) {
                        double minX = Math.min((Double)pos1List.get(0), (Double)pos2List.get(0));
                        double minY = Math.min((Double)pos1List.get(1), (Double)pos2List.get(1));
                        double minZ = Math.min((Double)pos1List.get(2), (Double)pos2List.get(2));
                        new Location(bukkitWorld, minX, minY, minZ);
                        ClipboardFormat format = ClipboardFormats.findByFile(schemFile);
                        if (format == null) {
                            String arenaNotFound = plugin.getConfigManager().getArenaNotFound();
                            SendMessageUtils.Sendmessage(player, arenaNotFound);
                        } else {
                            try {
                                ClipboardReader reader = format.getReader(new FileInputStream(schemFile));

                                try {
                                    Clipboard clipboard = reader.read();
                                    com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(bukkitWorld);
                                    EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(weWorld).fastMode(true).limitUnlimited().build();

                                    try {
                                        editSession.disableHistory();
                                        Region region = clipboard.getRegion();
                                        BlockVector3 origin = clipboard.getOrigin();

                                        for (BlockVector3 pos : region) {
                                            BlockState block = clipboard.getBlock(pos);
                                            if (!plugin.getConfigManager().isBlacklisted(block.getBlockType().getName())) {
                                                BlockVector3 finalPos = pos.subtract(origin).add(BlockVector3.at(minX, minY, minZ));
                                                editSession.smartSetBlock(finalPos, block);
                                            }
                                        }

                                        editSession.flushQueue();
                                    } catch (Throwable var30) {
                                        if (editSession != null) {
                                            try {
                                                editSession.close();
                                            } catch (Throwable var29) {
                                                var30.addSuppressed(var29);
                                            }
                                        }

                                        throw var30;
                                    }

                                    if (editSession != null) {
                                        editSession.close();
                                    }
                                } catch (Throwable var31) {
                                    if (reader != null) {
                                        try {
                                            reader.close();
                                        } catch (Throwable var28) {
                                            var31.addSuppressed(var28);
                                        }
                                    }

                                    throw var31;
                                }

                                if (reader != null) {
                                    reader.close();
                                }
                            } catch (WorldEditException | IOException var32) {
                                String arenainvalid = plugin.getConfigManager().getarenaloadingerror();
                                SendMessageUtils.Sendmessage(player, arenainvalid);
                            }

                            Boolean items = config.getBoolean("arena." + arenaName + ".clearitems", false);
                            Boolean crystal = config.getBoolean("arena." + arenaName + ".clearcrystal", false);
                            Boolean players = config.getBoolean("arena." + arenaName + ".tpplayers", false);
                            String spawnName = config.getString(
                                    "arena." + arenaName + ".spawn",
                                    config.getString("default-spawn", "default")
                            );
                            plugin.getServer().getScheduler().runTask(plugin, task -> clear(bukkitWorld, pos1List, pos2List, items, crystal, players, spawnName));
                        }
                    } else {
                        String posinvalid = plugin.getConfigManager().getpositioninvalid();
                        SendMessageUtils.Sendmessage(player, posinvalid);
                    }
                }
            }
        }
    }

    private static void clear(World world, List<Double> start, List<Double> end, Boolean items, Boolean crystal, Boolean players, String spawnName) {
        if (items || crystal || players) {
            if (world != null) {
                if (start != null && end != null) {
                    if (start.size() >= 3 && end.size() >= 3) {
                        double minX = Math.min((Double)start.get(0), (Double)end.get(0));
                        double minY = Math.min((Double)start.get(1), (Double)end.get(1));
                        double minZ = Math.min((Double)start.get(2), (Double)end.get(2));
                        double maxX = Math.max((Double)start.get(0), (Double)end.get(0));
                        double maxY = Math.max((Double)start.get(1), (Double)end.get(1));
                        double maxZ = Math.max((Double)start.get(2), (Double)end.get(2));
                        BoundingBox box = BoundingBox.of(new Location(world, minX, minY, minZ), new Location(world, maxX, maxY, maxZ));
                        FastArenas fastArenas = (FastArenas)JavaPlugin.getPlugin(FastArenas.class);
                        Location spawn = fastArenas.getConfigManager().getSpawn(spawnName);
                        String message = fastArenas.getConfigManager().getspawnmessage();

                        for (Entity entity : world.getNearbyEntities(box)) {
                            if (items && entity instanceof Item) {
                                entity.remove();
                            } else {
                                if (crystal && entity.getType().equals(EntityType.ENDER_CRYSTAL)) {
                                    entity.remove();
                                }

                                if (players && spawn != null && entity.getType().equals(EntityType.PLAYER)) {
                                    entity.teleport(spawn);
                                    entity.sendMessage(message);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}