package com.tadahtech.fadecloud.kd.items;

import com.tadahtech.fadecloud.kd.KingdomDefense;
import com.tadahtech.fadecloud.kd.csc.packets.response.GameInfoResponsePacket;
import com.tadahtech.fadecloud.kd.game.Game;
import com.tadahtech.fadecloud.kd.info.PlayerInfo;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Timothy Andis (TadahTech) on 8/1/2015.
 */
public class HubItem extends ModSpecialItem {

    public HubItem() {
        super(new ItemBuilder(new ItemStack(Material.BED))
          .name(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Click to return to the Hub")
          .build());
    }

    @Override
    public void onClick(Player player) {
        Game game = KingdomDefense.getInstance().getGame();
        PlayerInfo info = KingdomDefense.getInstance().getInfoManager().get(player);
        game.removePlayer(info);
        new GameInfoResponsePacket().write();
    }

    @Override
    protected boolean alwaysAllow() {
        return true;
    }
}
