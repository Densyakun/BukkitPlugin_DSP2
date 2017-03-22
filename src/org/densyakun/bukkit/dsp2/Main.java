package org.densyakun.bukkit.dsp2;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.densyakun.bukkit.dsp2.menumanager.MenuManager;
import org.densyakun.bukkit.dsp2.playermanager.PlayerData;
import org.densyakun.bukkit.dsp2.playermanager.PlayerManager;
import org.densyakun.bukkit.dsp2.treasurechest.TreasureChest;

public class Main extends JavaPlugin implements Listener {
	public static final String param_is_not_enough = "パラメータが足りません";
	public static final String param_wrong_cmd = "パラメータが間違っています";
	public static final String cmd_player_only = "このコマンドはプレイヤーのみ実行できます";
	
	public static Main main;
	String prefix;
	TrayIcon tray;
	
	public PlayerManager playermanager;
	public MenuManager menumanager;
	public TreasureChest treasurechest;
	//public CityManager citymanager;
	@Override
	public void onLoad() {
		main = this;
	}
	@Override
	public void onEnable() {
		try {
			SystemTray.getSystemTray()
					.add(tray = new TrayIcon(ImageIO.read(new File("./server-icon.png")), getServer().getServerName()));
		} catch (AWTException | IOException e) {
			e.printStackTrace();
		}
		playermanager = new PlayerManager();
		menumanager = new MenuManager();
		treasurechest = new TreasureChest();
		//citymanager = new CityManager();
		load();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getName() + ": 有効");
	}

	public void load() {
		saveDefaultConfig();
		playermanager.load();
	}

	public void save() {
		playermanager.save();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("dsp")) {
			if (args.length == 0) {
				sender.sendMessage(prefix + ChatColor.GOLD + param_is_not_enough);
				sender.sendMessage(ChatColor.GREEN + "/dsp (map|kit|join|stats)");
			} else if (args[0].equalsIgnoreCase("admin")) {
				if (sender.isOp()) {
					if (args.length == 1) {
						sender.sendMessage(prefix + ChatColor.GOLD + param_is_not_enough);
						sender.sendMessage(ChatColor.GREEN + "/dsp admin (load|save|hide|show|mode|whitemode|white|player)");
					} else if (args[1].equalsIgnoreCase("load")) {
						load();
						sender.sendMessage(ChatColor.AQUA + "ロードしました");
					} else if (args[1].equalsIgnoreCase("save")) {
						save();
						sender.sendMessage(ChatColor.AQUA + "セーブしました");
					} else if (args[1].equalsIgnoreCase("hide")) {
						if (sender instanceof Player) {
							playermanager.sethide((Player) sender, true);
							sender.sendMessage(ChatColor.AQUA + "一般プレイヤーから姿を隠しました");
						} else {
							sender.sendMessage(prefix + ChatColor.GOLD + cmd_player_only);
						}
					} else if (args[1].equalsIgnoreCase("show")) {
						if (sender instanceof Player) {
							playermanager.sethide((Player) sender, false);
							sender.sendMessage(ChatColor.AQUA + "姿を表示しました");
						} else {
							sender.sendMessage(prefix + ChatColor.GOLD + cmd_player_only);
						}
					} else if (args[1].equalsIgnoreCase("mode")) {
						playermanager.adminmodetoggle();
					} else if (args[1].equalsIgnoreCase("whitemode") || args[1].equalsIgnoreCase("white")) {
						playermanager.whitemodetoggle();
					} else if (args[1].equalsIgnoreCase("player")) {
						if (args.length == 2) {
							sender.sendMessage(prefix + ChatColor.GOLD + param_is_not_enough);
							sender.sendMessage(ChatColor.GREEN + "/dsp admin player (perfcmd)");
						}/* else if (args[2].equalsIgnoreCase("data")) {
							if (args.length < 4) {
								sender.sendMessage(ChatColor.RED + "パラメーターが足りません");
							} else {
							}
						} else if (args[2].equalsIgnoreCase("history") || args[1].equalsIgnoreCase("hist")) {
							if (args.length < 4) {
								if (sender instanceof ConsoleCommandSender) {
									getServer().getConsoleSender().sendMessage(ChatColor.RED + "パラメーターが足りません");
								} else {
									sender.sendMessage(ChatColor.RED + "パラメーターが足りません");
								}
							} else {

							}
						}*/ else if (args[2].equalsIgnoreCase("perfcmd")) {
							if (args.length < 5) {
								sender.sendMessage(prefix + ChatColor.GOLD + param_is_not_enough);
								sender.sendMessage(ChatColor.GREEN + "/dsp admin player perfcmd (cmd)");
							} else {
								Player player = getServer().getPlayer(args[3]);
								if (player != null) {
									String cmd = args[4];
									for (int a = 5; a < args.length; a++) {
										cmd += " " + args[a];
									}
									player.performCommand(cmd);
									sender.sendMessage(ChatColor.AQUA + player.getDisplayName() + ChatColor.AQUA + "がコマンド" + cmd + "を実行しました");
								} else {
									sender.sendMessage(ChatColor.RED + "プレイヤーが見つかりません");
								}
							}
						} else {
							sender.sendMessage(prefix + ChatColor.GOLD + param_wrong_cmd);
							sender.sendMessage(ChatColor.GREEN + "/dsp admin player (perfcmd)");
						}
					} else {
						sender.sendMessage(prefix + ChatColor.GOLD + param_wrong_cmd);
						sender.sendMessage(ChatColor.GREEN + "/dsp admin (load|save|hide|show|mode|whitemode|white|player)");
					}
				}
			}/* else if (args[0].equalsIgnoreCase("rsub")) {
				if (sender instanceof HumanEntity) {
					PlayerData playerdata = playermanager.getPlayerData(((HumanEntity) sender).getUniqueId());
					String rsub = playerdata.getMetadata("rsub");
					if (rsub == null) {
						
						sender.sendMessage(ChatColor.AQUA + "提出しました");
					} else {
						sender.sendMessage(ChatColor.RED + "すでに提出しています");
					}
				} else {
					sender.sendMessage(prefix + ChatColor.GOLD + cmd_player_only);
				}
			}*/ else {
				sender.sendMessage(prefix + ChatColor.GOLD + param_is_not_enough);
				sender.sendMessage(ChatColor.GREEN + "/dsp (map|kit|join|stats)");
			}
		} else if (label.equalsIgnoreCase("menu")) {
			if (sender instanceof HumanEntity) {
				menumanager.OpenMenu((HumanEntity) sender);
			} else {
				sender.sendMessage(prefix + ChatColor.GOLD + cmd_player_only);
			}
		} else if (label.equalsIgnoreCase("nick")) {
			if (sender instanceof Player) {
				PlayerData playerdata = playermanager.getPlayerData(((HumanEntity) sender).getUniqueId());
				if (args.length == 0) {
					playerdata.removeMetadata("nick");
					playermanager.setPlayerData(playerdata);
					playermanager.namereload((Player) sender);
					sender.sendMessage(ChatColor.AQUA + "ニックネームを初期化しました");
					sender.sendMessage(ChatColor.GOLD + "ニックネームを設定するには、/nick (name) を実行して下さい");
				} else {
					playerdata.setMetadata("nick", args[0]);
					playermanager.setPlayerData(playerdata);
					sender.sendMessage(ChatColor.AQUA + "ニックネームを変更しました");
				}
			} else {
				sender.sendMessage(prefix + ChatColor.GOLD + cmd_player_only);
			}
		}
		return true;
	}

	@Override
	public void onDisable() {
		Player[] players = getServer().getOnlinePlayers().toArray(new Player[0]);
		for (int a = 0; a < players.length; a++) {
			players[a].kickPlayer("再起動またはサーバー停止のため自動キックされました");
		}
		save();
		SystemTray.getSystemTray().remove(tray);
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getName() + ": 無効");
	}

	public void traysend(String title, String msg, MessageType type) {
		tray.displayMessage(title, msg, type);
	}

	@EventHandler
	public void ItemDespawn(ItemDespawnEvent e) {
		if (TreasureChest.isAddable(e.getEntity().getItemStack().getType())) {
			Iterator<ItemStack> i = treasurechest.addItem(e.getEntity().getItemStack()).values().iterator();
			while (i.hasNext()) {
				ItemStack item = i.next();
				e.getLocation().getWorld().dropItem(e.getLocation(), item);
			}
		}
	}
}
