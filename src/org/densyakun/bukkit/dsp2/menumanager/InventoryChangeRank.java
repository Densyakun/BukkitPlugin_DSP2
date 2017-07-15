package org.densyakun.bukkit.dsp2.menumanager;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.densyakun.bukkit.dsp2.Main;
import org.densyakun.bukkit.dsp2.playermanager.PlayerData;
import org.densyakun.bukkit.dsp2.playermanager.PlayerRank;

public class InventoryChangeRank extends MenuInventory {
	PlayerData playerdata;

	public InventoryChangeRank(MenuManager menumanager, UUID uuid, PlayerData playerdata) {
		super(menumanager, 9, "内部ランクを変更", uuid);
		this.playerdata = playerdata;
		if (Main.main.playermanager.getPlayerData(uuid).getRank() == PlayerRank.Owner) {
			setitem(3, Material.GOLD_BLOCK, PlayerRank.Owner.getChatColor() + PlayerRank.Owner.name());
		}
		setitem(2, Material.IRON_BLOCK, PlayerRank.Admin.getChatColor() + PlayerRank.Admin.name());
		setitem(1, Material.STONE, PlayerRank.Residents.getChatColor() + PlayerRank.Residents.name());
		setitem(0, Material.WOOD, PlayerRank.Traveler.getChatColor() + PlayerRank.Traveler.name());
	}

	@Override
	public void Click(InventoryClickEvent e) {
		switch (e.getRawSlot()) {
		case 0:
			playerdata.setRank(PlayerRank.Traveler);
			break;
		case 1:
			playerdata.setRank(PlayerRank.Residents);
			break;
		case 2:
			playerdata.setRank(PlayerRank.Admin);
			break;
		case 3:
			playerdata.setRank(PlayerRank.Owner);
			break;
		default:
			break;
		}
		Main.main.playermanager.setPlayerData(playerdata);
		e.getWhoClicked().closeInventory();
		e.getWhoClicked().sendMessage(
				ChatColor.AQUA + "内部ランクを変更: " + playerdata.getRank().getChatColor() + playerdata.getInternalRank());
	}
}
