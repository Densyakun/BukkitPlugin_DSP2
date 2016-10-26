package org.densyakun.bukkit.dsp2.menumanager;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
public class InventoryMenu extends MenuInventory {
	public InventoryMenu(MenuManager menumanager, UUID uuid) {
		super(menumanager, 9, "メニュー", uuid);
		if (menumanager.main.playermanager.getPlayerData(uuid).getRank().isAdmin()) {
			setitem(0, Material.COMMAND, ChatColor.BOLD + "管理者機能");
		}
		if (menumanager.main.getServer().getPluginManager().getPlugin("MiniGameManager") == null) {
			ItemStack item = setitem(4, Material.BOW, ChatColor.YELLOW + "ミニゲームはメンテナンス中です");
			MaterialData data = item.getData();
			if (data instanceof Wool) {
				((Wool) data).setColor(DyeColor.RED);
			}
			item.setData(data);
		} else {
			if (org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGame(uuid) == null) {
				setitem(4, Material.BOW, ChatColor.AQUA + "ミニゲーム");
			} else {
				setitem(4, Material.WOOD_DOOR, ChatColor.RED + "ゲームから退出");
			}
		}
	}
	@Override
	public void Click(InventoryClickEvent e) {
		switch (e.getRawSlot()) {
		case 0:
			if (getMenuManager().main.playermanager.getPlayerData(e.getWhoClicked().getUniqueId()).getRank().isAdmin()) {
				e.getWhoClicked().openInventory(new InventoryAdmin(getMenuManager(), e.getWhoClicked().getUniqueId()).getInventory());
			} else {
				e.getWhoClicked().closeInventory();
				if (e.getWhoClicked() instanceof CommandSender) {
					((CommandSender) e.getWhoClicked()).sendMessage(ChatColor.RED + "管理者ではありません");
				}
			}
			break;
		case 4:
			if (getMenuManager().main.getServer().getPluginManager().getPlugin("MiniGameManager") != null) {
				if (org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGame(e.getWhoClicked().getUniqueId()) != null) {
					e.getWhoClicked().closeInventory();
					if (e.getWhoClicked() instanceof Player) {
						((Player) e.getWhoClicked()).performCommand("game leave");
					}
				} else {
					e.getWhoClicked().openInventory(new InventoryGames(getMenuManager(), e.getWhoClicked().getUniqueId()).getInventory());
				}
			}
			break;
		default:
			break;
		}
	}
}
