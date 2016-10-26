package org.densyakun.bukkit.dsp2.treasurechest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.densyakun.bukkit.dsp2.Main;
public class TreasureChest implements Runnable {
	public Main main;
	public Chest chest;
	public List<Material> items = new ArrayList<Material>();
	public TreasureChest(Main main) {
		World world = (this.main = main).getServer().getWorld(main.getConfig().getString("treasure-chest-world", "kyozyuku"));
		if (world != null) {
			Block block = world.getBlockAt(main.getConfig().getInt("treasure-chest-x", 0), main.getConfig().getInt("treasure-chest-y", 0), main.getConfig().getInt("treasure-chest-z", 0));
			if (block.getState() instanceof Chest){
				chest = (Chest) block.getState();
			}
			new Thread(this).start();
		} else {
			main.getServer().getConsoleSender().sendMessage(ChatColor.RED + "ワールドが見つかりません");
		}
		List<String> conf_items = main.getConfig().getStringList("treasure-chest-items");
		for (int a = 0; a < conf_items.size(); a++) {
			String itemname = conf_items.get(a);
			Material mat = Material.matchMaterial(itemname);
			if (mat == null) {
				System.out.println("アイテム\"" + itemname + "\"は見つかりませんでした");
			} else {
				items.add(mat);
			}
		}
	}
	@Override
	public void run() {
		while (main.isEnabled() && chest != null) {
			ItemStack item = getRandomItems();
			if (item != null) {
				chest.getBlockInventory().addItem(item);
				System.out.println("[DSPTC]アイテムが出現しました: " + item.toString());
				Iterator<? extends Player> players = main.getServer().getOnlinePlayers().iterator();
				while (players.hasNext()) {
					players.next().sendMessage(ChatColor.GREEN + "[自動]宝箱にアイテムが出現しました: " + item.toString());
				}
				try {
					Thread.sleep(3600000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("[DSPTC]生成するアイテムがありません");
			}
		}
	}
	public ItemStack getRandomItems() {
		if (0 < items.size()) {
			return new ItemStack(items.get(new Random().nextInt(items.size())));
		}
		return null;
	}
}
