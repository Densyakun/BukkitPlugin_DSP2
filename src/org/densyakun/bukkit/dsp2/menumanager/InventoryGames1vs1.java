package org.densyakun.bukkit.dsp2.menumanager;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.densyakun.bukkit.pvp1vs1.Game1vs1;
import org.densyakun.bukkit.pvp1vs1.Map1vs1;
public class InventoryGames1vs1 extends ListInventory<Game1vs1> {
	public InventoryGames1vs1(MenuManager menumanager, UUID uuid, Game1vs1[] e) {
		super(menumanager, "PvPGames > " + Game1vs1.name, uuid, e, Material.DIAMOND_BLOCK, true);
		if (menumanager.main.getServer().getPluginManager().getPlugin("MiniGameManager") != null && org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGame(uuid) == null) {
			setitem(2, Material.WORKBENCH, ChatColor.YELLOW + "新規にゲームを始める");
			setitem(6, Material.POWERED_RAIL, ChatColor.GOLD.toString() + ChatColor.BOLD + "クイック参加");
		}
	}
	@Override
	public void ClickButton(InventoryClickEvent e) {
		switch (e.getRawSlot()) {
		case 2:
			if (getMenuManager().main.getServer().getPluginManager().getPlugin("MiniGameManager") != null) {
				if (org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGame(e.getWhoClicked().getUniqueId()) == null) {
					e.getWhoClicked().openInventory(new InventoryGames2vs2New(getMenuManager(), e.getWhoClicked().getUniqueId(), org.densyakun.bukkit.pvp1vs1.Main.main.getEnabledMaps().toArray(new Map1vs1[0])).getInventory());
				} else {
					e.getWhoClicked().closeInventory();
					e.getWhoClicked().sendMessage(ChatColor.RED + "ゲーム中です");
				}
			}
			break;
		case 6:
			e.getWhoClicked().closeInventory();
			if (e.getWhoClicked() instanceof Player) {
				((Player) e.getWhoClicked()).performCommand("game 1vs1 join");
			}
			break;
		default:
			break;
		}
	}
	@Override
	public void ListClick(int n, InventoryClickEvent e) {
		e.getWhoClicked().closeInventory();
		if (getMenuManager().main.getServer().getPluginManager().getPlugin("MiniGameManager") != null) {
			if (org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGame(e.getWhoClicked().getUniqueId()) == null) {
				if (e.getInventory().getItem(e.getRawSlot()) != null && e.getWhoClicked() instanceof Player && !this.e[n].addPlayer((Player) e.getWhoClicked())) {
					e.getWhoClicked().sendMessage(ChatColor.RED + "満員です");
				}
			} else {
				e.getWhoClicked().sendMessage(ChatColor.RED + "ゲーム中です");
			}
		}
	}
	@Override
	public String getName(Game1vs1 e) {
		String playersname = new String();
		List<Player> players = e.getPlayers();
		for (int a = 0; a < players.size(); a++) {
			if (a != 0) {
				playersname += "\n";
			}
			playersname += players.get(a).getDisplayName();
		}
		return Game1vs1.name + "参加する(" + players.size() + "/" + e.getMaxplayers() + "): \n" + playersname;
	}
}
