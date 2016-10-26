package org.densyakun.bukkit.dsp2.menumanager;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.densyakun.bukkit.pvp1vs1.Game1vs1;
import org.densyakun.bukkit.pvp1vs1.Map1vs1;
public class InventoryGames2vs2New extends ListInventory<Map1vs1> {
	public InventoryGames2vs2New(MenuManager menumanager, UUID uuid, Map1vs1[] e) {
		super(menumanager, Game1vs1.name + " > 新しく始める", uuid, e, Material.DIAMOND_BLOCK, true);
	}
	@Override
	public void ListClick(int n, InventoryClickEvent e) {
		e.getWhoClicked().closeInventory();
		if (e.getInventory().getItem(e.getRawSlot()) != null && e.getWhoClicked() instanceof Player) {
			((Player) e.getWhoClicked()).performCommand("game 1vs1 join " + this.e[n].mapname);
		}
	}
	@Override
	public String getName(Map1vs1 e) {
		return e.mapname;
	}
}
