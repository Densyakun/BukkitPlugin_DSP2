package org.densyakun.bukkit.dsp2.treasurechest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
	public Chest chest;
	public List<Material> items = new ArrayList<Material>();
	public boolean full = false;
	public long fullmsg = 0;

	public TreasureChest() {
		String worldname = Main.main.getConfig().getString("treasure-chest-world", "kyozyuku");
		World world = (Main.main).getServer().getWorld(worldname);
		if (world != null) {
			Block block = world.getBlockAt(Main.main.getConfig().getInt("treasure-chest-x", 0),
					Main.main.getConfig().getInt("treasure-chest-y", 0), Main.main.getConfig().getInt("treasure-chest-z", 0));
			if (block.getState() instanceof Chest) {
				chest = (Chest) block.getState();
			}
			new Thread(this).start();
		} else {
			Main.main.getServer().getConsoleSender().sendMessage(ChatColor.RED + "ワールド" + worldname + "が見つかりません");
		}
		/*
		 * List<String> conf_items =
		 * main.getConfig().getStringList("treasure-chest-items"); for (int a =
		 * 0; a < conf_items.size(); a++) { String itemname = conf_items.get(a);
		 * Material mat = Material.matchMaterial(itemname); if (mat == null) {
		 * System.out.println("アイテム\"" + itemname + "\"は見つかりませんでした"); } else {
		 * items.add(mat); } }
		 */
	}

	@Override
	public void run() {
		for (int sec = 0; Main.main.isEnabled() && chest != null; sec++) {
			if (sec == 3600) {
				ItemStack item = getRandomItems();
				if (item != null) {
					addItem(item);
				} else {
					System.out.println("[DSPTC]生成するアイテムがありません");
					break;
				}
				sec = 0;
			}
			if (full) {
				long time = new Date().getTime();
				if (fullmsg <= time - 180000) {
					cfullcheck();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public ItemStack getRandomItems() {
		if (0 < items.size()) {
			return new ItemStack(items.get(new Random().nextInt(items.size())));
		}
		return null;
	}

	public HashMap<Integer, ItemStack> addItem(ItemStack item) {
		HashMap<Integer, ItemStack> a = chest.getBlockInventory().addItem(item);
		int amount = item.getAmount();
		Iterator<ItemStack> b = a.values().iterator();
		while (b.hasNext()) {
			amount -= b.next().getAmount();
		}
		if (amount != 0) {
			System.out.println("[DSPTC]アイテムが出現しました: " + item.getType() + " x" + amount);
			Iterator<? extends Player> players = Main.main.getServer().getOnlinePlayers().iterator();
			while (players.hasNext()) {
				players.next().sendMessage(ChatColor.GREEN + "[自動]宝箱にアイテムが出現しました: " + item.getType() + " x" + amount);
			}
		}
		cfullcheck();
		return a;
	}

	public void join(Player player) {
		cfullcheck();
	}

	public boolean cfullcheck() {
		if (chest.getBlockInventory().firstEmpty() == -1) {
			full = true;
			cfullmsg();
			return true;
		} else {
			full = false;
		}
		return false;
	}

	private void cfullmsg() {
		fullmsg = new Date().getTime();
		Iterator<? extends Player> players = Main.main.getServer().getOnlinePlayers().iterator();
		while (players.hasNext()) {
			players.next().sendMessage(ChatColor.GREEN + "[自動]宝箱がいっぱいです！誰か取りに来て！");
		}
	}

	public static boolean isAddable(Material type) {
		return !(type == Material.ARROW || type == Material.BONE || type == Material.BOW || type == Material.EGG || type == Material.FEATHER || type == Material.SEEDS || type == Material.SULPHUR || type == Material.POISONOUS_POTATO || type == Material.ROTTEN_FLESH || type == Material.SPIDER_EYE || type == Material.STRING || type == Material.WOOD_AXE || type == Material.WOOD_HOE || type == Material.WOOD_PICKAXE || type == Material.WOOD_SPADE || type == Material.WOOD_SWORD);
	}
}
