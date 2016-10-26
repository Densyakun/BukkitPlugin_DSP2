package org.densyakun.bukkit.dsp2.menumanager;
import java.util.UUID;
public class InventoryGames2vs2Join extends MenuInventory {

	public InventoryGames2vs2Join(MenuManager menumanager, int size, String name, UUID uuid) {
		super(menumanager, size, name, uuid);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	/*public Game2vs2 game;
	public InventoryGames2vs2Join(MenuManager menumanager, UUID uuid) {
		super(menumanager, 9, Game2vs2.name, uuid);
		Game game = menumanager.main.gamemanager.getPlayingGame(uuid);
		if ((game != null) && (game instanceof Game2vs2) && (game.time < ((Game2vs2) game).entrytime)) {
			setitem(2, Material.REDSTONE_BLOCK, ChatColor.BLUE + "赤チーム");
			setitem(6, Material.LAPIS_BLOCK, ChatColor.BLUE + "青チーム");
		}
	}
	@Override
	public void Click(InventoryClickEvent e) {
		switch (e.getRawSlot()) {
		case 0:
			e.getWhoClicked().closeInventory();
			e.getWhoClicked().openInventory(new InventoryGames2vs2Join(getMenuManager(), getUuid()).getInventory());
			break;
		case 2:
			e.getWhoClicked().closeInventory();
			if (e.getWhoClicked() instanceof Player) {
				Game game = getMenuManager().main.gamemanager.getPlayingGame(getUuid());
				if ((game != null) && (game instanceof Game2vs2)) {
					((Game2vs2) game).setteam((Player) e.getWhoClicked(), true);
				}
			}
			break;
		case 6:
			e.getWhoClicked().closeInventory();
			if (e.getWhoClicked() instanceof Player) {
				Game game = getMenuManager().main.gamemanager.getPlayingGame(getUuid());
				if ((game != null) && (game instanceof Game2vs2)) {
					((Game2vs2) game).setteam((Player) e.getWhoClicked(), false);
				}
			}
			break;
		default:
			break;
		}
	}*/
}
