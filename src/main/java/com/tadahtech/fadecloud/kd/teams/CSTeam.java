package com.tadahtech.fadecloud.kd.teams;

import com.google.common.collect.Lists;
import com.tadahtech.fadecloud.kd.KingdomDefense;
import com.tadahtech.fadecloud.kd.game.Game;
import com.tadahtech.fadecloud.kd.info.PlayerInfo;
import com.tadahtech.fadecloud.kd.map.Island;
import com.tadahtech.fadecloud.kd.map.LocationType;
import com.tadahtech.fadecloud.kd.threads.Tickable;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Timothy Andis
 */
public abstract class CSTeam extends Tickable {

    protected TeamType type;
    protected Loadout loadout;
    protected List<UUID> players;

    protected final Random random = new Random();
    protected Island island;
    private boolean lost;

    public CSTeam(TeamType type, Island island, Loadout loadout) {
        this.type = type;
        this.island = island;
        this.loadout = loadout;
        this.players = Lists.newArrayList();
    }

    public TeamType getType() {
        return type;
    }

    public abstract void onOtherDamage(EntityDamageByEntityEvent event, PlayerInfo info);

    public abstract void onDamage(EntityDamageByEntityEvent event, PlayerInfo info);

    public abstract void onHit(EntityDamageByEntityEvent event, PlayerInfo info);

    public void onRespawn(Player player) {
        LocationType type;
        switch (this.type) {
            case CREEPER:
                type = LocationType.CREEPER_SPAWN;
                break;
            case ENDERMAN:
                type = LocationType.ENDERMAN_SPAWN;
                break;
            case SKELETON:
                type = LocationType.SKELETON_SPAWN;
                break;
            case ZOMBIE:
                type = LocationType.ZOMBIE_SPAWN;
                break;
            default: return;
        }
        Location location = KingdomDefense.getInstance().getGame().getMap().getLocation(type).get();
        player.teleport(location);
        player.setHealth(player.getMaxHealth());
        KingdomDefense.getInstance().getInfoManager().get(player).setCurrentTeam(this);
        this.loadout.load(player);
    }

    public void add(Player player) {
        if(this.players == null) {
            this.players = Lists.newArrayList();
        }
        this.players.add(player.getUniqueId());
    }

    public abstract void applyEffects(Player player);

    public List<Player> getBukkitPlayers() {
        Game game = KingdomDefense.getInstance().getGame();
        return game.getPlayers().stream()
          .filter(info -> info.getCurrentTeam().equals(this))
          .map(PlayerInfo::getBukkitPlayer)
          .collect(Collectors.toList());
    }

    public Island getIsland() {
        return island;
    }

    public abstract LocationType getLocationType();

    public int getSize() {
        return players.size();
    }

    public void loadout(Player bukkitPlayer) {
        loadout.load(bukkitPlayer);
    }

    public abstract ItemStack getMenuIcon();

    public boolean hasLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public Location getRespawn() {
        LocationType type;
        switch (this.type) {
            case CREEPER:
                type = LocationType.CREEPER_SPAWN;
                break;
            case ENDERMAN:
                type = LocationType.ENDERMAN_SPAWN;
                break;
            case SKELETON:
                type = LocationType.SKELETON_SPAWN;
                break;
            case ZOMBIE:
                type = LocationType.ZOMBIE_SPAWN;
                break;
            default: return getBukkitPlayers().get(0).getWorld().getSpawnLocation();
        }
        return KingdomDefense.getInstance().getGame().getMap().getLocation(type).get();
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public enum TeamType {

        CREEPER,
        ZOMBIE,
        SKELETON,
        ENDERMAN,;

        public String getName() {
            switch (this) {
                case CREEPER:
                    return ChatColor.DARK_GREEN + "Creeper";
                case ENDERMAN:
                    return ChatColor.DARK_PURPLE + "Enderman";
                case SKELETON:
                    return ChatColor.DARK_GRAY + "Skeleton";
                case ZOMBIE:
                    return ChatColor.DARK_AQUA + "Zombie";
            }
            return "None";
        }

        public int getId() {
            switch (this) {
                case ENDERMAN:
                    return 4;
                case SKELETON:
                    return 3;
                case ZOMBIE:
                    return 2;
                case CREEPER:
                    return 1;
            }
            return -1;
        }

        public static TeamType fromId(int id) {
            switch (id) {
                case 1:
                    return CREEPER;
                case 2:
                    return ZOMBIE;
                case 3:
                    return SKELETON;
                case 4:
                    return ENDERMAN;
            }
            return null;
        }

        public String fancy() {
            switch (this) {
                case CREEPER: return ChatColor.GREEN + "Creeper";
                case ENDERMAN: return ChatColor.LIGHT_PURPLE + "Enderman";
                case ZOMBIE: return ChatColor.DARK_GREEN + "Zombie";
                case SKELETON: return ChatColor.GRAY + "Skeleton";
            }
            return "";
        }
    }

}
