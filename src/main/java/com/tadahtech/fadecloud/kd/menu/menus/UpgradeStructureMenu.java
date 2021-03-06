package com.tadahtech.fadecloud.kd.menu.menus;

import com.tadahtech.fadecloud.kd.KingdomDefense;
import com.tadahtech.fadecloud.kd.info.PlayerInfo;
import com.tadahtech.fadecloud.kd.items.ItemBuilder;
import com.tadahtech.fadecloud.kd.map.structures.Structure;
import com.tadahtech.fadecloud.kd.map.structures.strucs.DefenseStructure;
import com.tadahtech.fadecloud.kd.map.structures.strucs.Guardian;
import com.tadahtech.fadecloud.kd.menu.Button;
import com.tadahtech.fadecloud.kd.menu.Menu;
import com.tadahtech.fadecloud.kd.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

/**
 * Created by Timothy Andis (TadahTech) on 7/30/2015.
 */
public class UpgradeStructureMenu extends Menu {

    private Structure structure;

    public UpgradeStructureMenu(Structure structure) {
        super(ChatColor.DARK_PURPLE + "Upgrade " + structure.getBaseName());
        this.structure = structure;
    }

    @Override
    protected Button[] setUp() {
        Button[] buttons = new Button[27];
        buttons = pane(buttons);
        if (structure instanceof Guardian) {
            ItemStack upgrade = new ItemBuilder(new ItemStack(Material.EMERALD_BLOCK))
              .name(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Upgrade")
              .lore(" ", ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to Upgrade to " + (structure.getLevel() + 1),
                ChatColor.RED + "Cost: " + structure.getNextCost())
              .build();
            Guardian structure = (Guardian) this.structure;
            if(structure.getLevel() == 4) {
                upgrade = new ItemBuilder(new ItemStack(Material.EMERALD_BLOCK))
                  .name(ChatColor.AQUA.toString() + ChatColor.BOLD + "Fully Upgraded")
                  .build();
            }
            ItemBuilder builder = new ItemBuilder(new ItemStack(Material.EMERALD));
            builder.name(ChatColor.GOLD + "Healing Cooldown");
            builder.lore(ChatColor.GREEN.toString() + structure.getCooldown() + " " + Utils.plural("second", structure.getCooldown()),
              ChatColor.GRAY + "Next: " +
                ChatColor.GREEN.toString() + (structure.getCooldown() - 5) + Utils.plural("second", structure.getCooldown() - 5));
            ItemStack health = new ItemBuilder(new Wool(DyeColor.LIME).toItemStack())
              .amount(1)
              .name(ChatColor.GOLD + "Health Per Cycle")
              .lore(ChatColor.GREEN.toString() + structure.getHealthPerTick() + " hearts",
                ChatColor.GRAY + "Next: " +
                  ChatColor.GREEN.toString() + (structure.getHealthPerTick() * 1.5) + "hearts")
              .build();
            buttons[11] = new Button(builder.build(), () -> {
            });
            buttons[13] = new Button(health, () -> {
            });
            buttons[17] = new Button(upgrade, player -> {
                PlayerInfo info = KingdomDefense.getInstance().getInfoManager().get(player);
                if (!info.hasEnough(structure.getNextCost())) {
                    player.closeInventory();
                    info.sendMessage(ChatColor.RED + "You cannot afford this!");
                    return;
                }
                if (!info.remove(structure.getNextCost())) {
                    player.closeInventory();
                    info.sendMessage(ChatColor.RED + "You cannot afford this!");
                    return;
                }
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                if(structure.getLevel() == 5) {
                    return;
                }
                structure.setCooldown(structure.getCooldown() - 5);
                structure.setHealthPerTick(structure.getHealthPerTick() * 1.5);
                structure.setLevel(structure.getLevel() + 1);
                info.sendMessage(structure.getName() + ChatColor.AQUA + " tower upgraded!.");
            });
            return buttons;
        }
        buttons = pane(buttons);
        DefenseStructure structure = (DefenseStructure) this.structure;
        ItemBuilder builder = new ItemBuilder(new ItemStack(Material.EMERALD));
        builder.name(ChatColor.GOLD + "Fire Rate");
        builder.lore(ChatColor.GREEN.toString() + structure.getFireRate() + " projectiles per second",
          ChatColor.GRAY + "Next: " + ChatColor.GREEN.toString() + (structure.getFireRate() * 2) + " projectiles per second");

        ItemStack damage = new ItemBuilder(new ItemStack(Material.ARROW))
          .name(ChatColor.GOLD + "Damage")
          .lore(ChatColor.GREEN.toString() + structure.getDamage() + " hearts",
            ChatColor.GRAY + "Next: " +
              ChatColor.GREEN + (structure.getDamage() * 1.5) + " hearts")
          .build();

        buttons[11] = new Button(builder.build(), () -> {
        });
        buttons[13] = new Button(damage, () -> {
        });

        ItemStack upgrade = new ItemBuilder(new ItemStack(Material.EMERALD_BLOCK))
          .name(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Upgrade! ")
          .lore(" ", ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to Upgrade to Level " + (structure.getLevel() + 1),
            ChatColor.RED + "Cost: " + structure.getNextCost())
          .build();
        if(structure.getLevel() == 4) {
            upgrade = new ItemBuilder(new ItemStack(Material.EMERALD_BLOCK))
              .name(ChatColor.AQUA.toString() + ChatColor.BOLD + "Fully Upgraded")
              .build();
        }
        buttons[17] = new Button(upgrade, player -> {
            PlayerInfo info = KingdomDefense.getInstance().getInfoManager().get(player);
            if (!info.hasEnough(structure.getNextCost())) {
                player.closeInventory();
                info.sendMessage(ChatColor.RED + "You cannot afford this!");
                return;
            }
            if (!info.remove(structure.getNextCost())) {
                player.closeInventory();
                info.sendMessage(ChatColor.RED + "You cannot afford this!");
                return;
            }
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            if(structure.getLevel() == 5) {
                return;
            }
            structure.setFireRate(structure.getFireRate() * 2);
            structure.setDamage(structure.getDamage() * 1.5);
            structure.setLevel(structure.getLevel() + 1);
            player.closeInventory();
            info.sendMessage(structure.getName() + ChatColor.YELLOW + " tower upgraded!.");
        });
        return buttons;
    }

    @Override
    public void onClose(Player player) {
        remove(player.getUniqueId());
    }
}
