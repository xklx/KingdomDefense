package com.tadahtech.fadecloud.kd.teams.skeleton;

import com.tadahtech.fadecloud.kd.KingdomDefense;
import com.tadahtech.fadecloud.kd.info.PlayerInfo;
import com.tadahtech.fadecloud.kd.items.ItemBuilder;
import com.tadahtech.fadecloud.kd.teams.CSTeam.TeamType;
import com.tadahtech.fadecloud.kd.items.ModSpecialItem;
import com.tadahtech.fadecloud.kd.utils.CustomFirework;
import com.tadahtech.fadecloud.kd.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Timothy Andis (TadahTech) on 7/29/2015.
 */
public class SkeletonItem extends ModSpecialItem {

    private final FireworkEffect effect = FireworkEffect.builder()
      .withColor(Color.BLACK, Color.GRAY)
      .withFlicker()
      .withTrail()
      .with(Type.BALL)
      .build();

    public SkeletonItem() {
        super(ItemBuilder.wrap(new ItemStack(Material.ARROW, 1, (byte) 4))
          .name(ChatColor.GRAY.toString() + ChatColor.BOLD + "Arrow Storm")
          .lore(ChatColor.GRAY + "Right click to summon an arrow storm around you.")
          .build());
    }

    @Override
    public void onClick(Player player) {
        PlayerInfo info = KingdomDefense.getInstance().getInfoManager().get(player);
        int level = info.getLevel(TeamType.SKELETON);
        int radius = level * 3;
        List<Location> circle = Utils.circle(player.getEyeLocation(), radius, 1, true, false, 10);
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
        for(Location location : circle) {
            location.getWorld().strikeLightningEffect(location);
            CustomFirework.spawn(location, effect);
            location.getWorld().spawn(location, Arrow.class);
        }
    }

}
