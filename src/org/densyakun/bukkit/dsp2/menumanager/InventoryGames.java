package org.densyakun.bukkit.dsp2.menumanager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.densyakun.bukkit.minigamemanager.Game;
import org.densyakun.bukkit.pvp1vs1.Game1vs1;
public class InventoryGames extends MenuInventory {
	public InventoryGames(MenuManager menumanager, UUID uuid) {
		super(menumanager, 9, "メニュー > PvPGames", uuid);
		if (menumanager.main.getServer().getPluginManager().getPlugin("MiniGameManager") != null && org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGame(uuid) == null) {
			if (menumanager.main.getServer().getPluginManager().getPlugin("PvPFFA") == null) {
				ItemStack item = setitem(3, Material.WOOL, ChatColor.YELLOW + "PvPFFAはメンテナンス中です");
				MaterialData data = item.getData();
				if (data instanceof Wool) {
					((Wool) data).setColor(DyeColor.RED);
				}
				item.setData(data);
			} else {
				//setitem(3, Material.WOOD_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + GameFFA.name);
			}
			if (menumanager.main.getServer().getPluginManager().getPlugin("PvP1vs1") == null) {
				ItemStack item = setitem(4, Material.WOOL, ChatColor.YELLOW + "PvP1vs1はメンテナンス中です");
				MaterialData data = item.getData();
				if (data instanceof Wool) {
					((Wool) data).setColor(DyeColor.RED);
				}
				item.setData(data);
			} else {
				setitem(4, Material.IRON_SWORD, ChatColor.RED.toString() + ChatColor.BOLD + Game1vs1.name);
			}
			if (menumanager.main.getServer().getPluginManager().getPlugin("PvP2vs2") == null) {
				ItemStack item = setitem(5, Material.WOOL, ChatColor.YELLOW + "PvP2vs2はメンテナンス中です");
				MaterialData data = item.getData();
				if (data instanceof Wool) {
					((Wool) data).setColor(DyeColor.RED);
				}
				item.setData(data);
			} else {
				//setitem(5, Material.GOLD_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + Game2vs2.name);
			}
			if (menumanager.main.getServer().getPluginManager().getPlugin("PvPSkyWars") == null) {
				ItemStack item = setitem(6, Material.WOOL, ChatColor.YELLOW + "PvPSkyWarsはメンテナンス中です");
				MaterialData data = item.getData();
				if (data instanceof Wool) {
					((Wool) data).setColor(DyeColor.RED);
				}
				item.setData(data);
			} else {
				//setitem(6, Material.GOLD_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + GameSkyWars.name);
			}
			setitem(8, Material.SIGN, ChatColor.YELLOW + "他のミニゲームは現在メンテナンス中です");
		}
		/*List<String>lore = new ArrayList<String>();
		lore.add(Game2vs2.name + "は2vs2PvPが出来ます");
		lore.add("このゲームではPvP用アイテムを使用します");
		lore.add("PvP用アイテムは一番持っていない人と同じ物が使えます");
		lore.add("勝利するとRPをゲットできます");
		setitem(4, Material.IRON_SWORD, ChatColor.BLUE + Game2vs2.name, lore);*/
	}
	@Override
	public void Open(InventoryOpenEvent e) {
		if (getMenuManager().main.getServer().getPluginManager().getPlugin("MiniGameManager") == null || org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGame(getUuid()) != null) {
			e.setCancelled(true);
		}
	}
	@Override
	public void Click(InventoryClickEvent e) {
		switch (e.getRawSlot()) {
		case 4:
			if (getMenuManager().main.getServer().getPluginManager().getPlugin("MiniGameManager") != null && org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGame(e.getWhoClicked().getUniqueId()) == null) {
				List<Game> games = org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGames();
				List<Game1vs1> multi = new ArrayList<Game1vs1>();
				for (int a = 0; a < games.size(); a++) {
					if (games.get(a) instanceof Game1vs1) {
						multi.add((Game1vs1) games.get(a));
					}
				}
				e.getWhoClicked().openInventory(new InventoryGames1vs1(getMenuManager(), e.getWhoClicked().getUniqueId(), multi.toArray(new Game1vs1[0])).getInventory());
			}
			break;
		default:
			break;
		}
	}
}
