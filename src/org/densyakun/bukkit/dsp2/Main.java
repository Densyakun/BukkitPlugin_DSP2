package org.densyakun.bukkit.dsp2;
import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.densyakun.bukkit.dsp2.menumanager.MenuManager;
import org.densyakun.bukkit.dsp2.playermanager.PlayerData;
import org.densyakun.bukkit.dsp2.playermanager.PlayerManager;
import org.densyakun.bukkit.dsp2.treasurechest.TreasureChest;
public class Main extends JavaPlugin implements Listener {
	private static Main main;
	private TrayIcon tray;
	
	public PlayerManager playermanager;
	public MenuManager menumanager;
	public TreasureChest treasurechest;
	//public CityManager citymanager;
	@Override
	public void onEnable() {
		main = this;
		playermanager = new PlayerManager(this);
		menumanager = new MenuManager(this);
		treasurechest = new TreasureChest(this);
		//citymanager = new CityManager(this);
		load();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getName() + ": 有効");
	}
	public void load() {
		saveDefaultConfig();
		try {
			SystemTray.getSystemTray().add(tray = new TrayIcon(ImageIO.read(new File("./server-icon.png")), getServer().getServerName()));
		} catch (AWTException | IOException e) {
			e.printStackTrace();
		}
	}
	public void save() {
		playermanager.stop();
		SystemTray.getSystemTray().remove(tray);
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		/*String argsa = label;
		for (int a = 0; a < args.length; a++) {
			argsa += " " + args[a];
		}
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Command: " + ChatColor.RESET + sender.getName() + "/" + argsa);*/
		if (label.equalsIgnoreCase("dsp")) {
			if (args.length == 0) {
				if (sender instanceof ConsoleCommandSender) {
					getServer().getConsoleSender().sendMessage(ChatColor.RED + "パラメーターが必要です");
				} else {
					sender.sendMessage(ChatColor.RED + "パラメーターが必要です");
				}
			} else if (args[0].equalsIgnoreCase("admin")) {
				if (sender.isOp()) {
					if (args.length == 1) {
						if (sender instanceof ConsoleCommandSender) {
							getServer().getConsoleSender().sendMessage(ChatColor.RED + "パラメーターが足りません");
						} else {
							sender.sendMessage(ChatColor.RED + "パラメーターが足りません");
						}
					} else if (args[1].equalsIgnoreCase("load")) {
						load();
						if (sender instanceof ConsoleCommandSender) {
							getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "ロードしました");
						} else {
							sender.sendMessage(ChatColor.AQUA + "ロードしました");
						}
					} else if (args[1].equalsIgnoreCase("save")) {
						save();
						if (sender instanceof ConsoleCommandSender) {
							getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "セーブしました");
						} else {
							sender.sendMessage(ChatColor.AQUA + "セーブしました");
						}
					} else if (args[1].equalsIgnoreCase("reload")) {
						save();
						load();
						if (sender instanceof ConsoleCommandSender) {
							getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "リロードしました");
						} else {
							sender.sendMessage(ChatColor.AQUA + "リロードしました");
						}
					} else if (args[1].equalsIgnoreCase("hide")) {
						if (sender instanceof Player) {
							playermanager.sethide((Player) sender, true);
							sender.sendMessage(ChatColor.AQUA + "一般プレイヤーから姿を隠しました");
						} else {
							if (sender instanceof ConsoleCommandSender) {
								getServer().getConsoleSender().sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
							} else {
								sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
							}
						}
					} else if (args[1].equalsIgnoreCase("show")) {
						if (sender instanceof Player) {
							playermanager.sethide((Player) sender, false);
							sender.sendMessage(ChatColor.AQUA + "姿を表示しました");
						} else {
							if (sender instanceof ConsoleCommandSender) {
								getServer().getConsoleSender().sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
							} else {
								sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
							}
						}
					} else if (args[1].equalsIgnoreCase("adminmode") || args[1].equalsIgnoreCase("mode")) {
						playermanager.adminmodetoggle();
					} else if (args[1].equalsIgnoreCase("whitemode") || args[1].equalsIgnoreCase("white")) {
						playermanager.whitemodetoggle();
					} else if (args[1].equalsIgnoreCase("player")) {
						if (args.length == 2) {
							if (sender instanceof ConsoleCommandSender) {
								getServer().getConsoleSender().sendMessage(ChatColor.RED + "パラメーターが足りません");
							} else {
								sender.sendMessage(ChatColor.RED + "パラメーターが足りません");
							}
						} else if (args[2].equalsIgnoreCase("data")) {
							if (args.length < 4) {
								if (sender instanceof ConsoleCommandSender) {
									getServer().getConsoleSender().sendMessage(ChatColor.RED + "パラメーターが足りません");
								} else {
									sender.sendMessage(ChatColor.RED + "パラメーターが足りません");
								}
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
						} else if (args[2].equalsIgnoreCase("cmdperform") || args[2].equalsIgnoreCase("cmdperf")) {
							if (args.length < 5) {
								if (sender instanceof ConsoleCommandSender) {
									getServer().getConsoleSender().sendMessage(ChatColor.RED + "パラメーターが足りません");
								} else {
									sender.sendMessage(ChatColor.RED + "パラメーターが足りません");
								}
							} else {
								Player player = getServer().getPlayer(args[3]);
								if (player != null) {
									String cmd = args[4];
									for (int a = 5; a < args.length; a++) {
										cmd += " " + args[a];
									}
									player.performCommand(cmd);
									if (sender instanceof ConsoleCommandSender) {
										getServer().getConsoleSender().sendMessage(ChatColor.AQUA + player.getDisplayName() + ChatColor.AQUA + "がコマンド" + cmd + "を実行しました");
									} else {
										sender.sendMessage(ChatColor.AQUA + player.getDisplayName() + ChatColor.AQUA + "がコマンド" + cmd + "を実行しました");
									}
								} else {
									if (sender instanceof ConsoleCommandSender) {
										getServer().getConsoleSender().sendMessage(ChatColor.RED + "プレイヤーが見つかりません");
									} else {
										sender.sendMessage(ChatColor.RED + "プレイヤーが見つかりません");
									}
								}
							}
						} else {
							if (sender instanceof ConsoleCommandSender) {
								getServer().getConsoleSender().sendMessage(ChatColor.RED + "パラメーターが間違っています");
							} else {
								sender.sendMessage(ChatColor.RED + "パラメーターが間違っています");
							}
						}
					} else {
						if (sender instanceof ConsoleCommandSender) {
							getServer().getConsoleSender().sendMessage(ChatColor.RED + "パラメーターが間違っています");
						} else {
							sender.sendMessage(ChatColor.RED + "パラメーターが間違っています");
						}
					}
				} else {
					if (sender instanceof ConsoleCommandSender) {
						getServer().getConsoleSender().sendMessage(ChatColor.RED + "管理者専用です");
					} else {
						sender.sendMessage(ChatColor.RED + "管理者専用です");
					}
				}
			} else if (args[0].equalsIgnoreCase("menu")) {
				if (sender instanceof HumanEntity) {
					menumanager.OpenMenu((HumanEntity) sender);
				} else {
					if (sender instanceof ConsoleCommandSender) {
						getServer().getConsoleSender().sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
					} else {
						sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
					}
				}
			} else if (args[0].equalsIgnoreCase("rsub")) {
				if (sender instanceof HumanEntity) {
					PlayerData playerdata = playermanager.getPlayerData(((HumanEntity) sender).getUniqueId());
					String rsub = playerdata.getMetadata("rsub");
					if (rsub == null) {
						
						sender.sendMessage(ChatColor.RED + "提出しました");
					} else {
						sender.sendMessage(ChatColor.RED + "すでに提出しています");
					}
				} else {
					if (sender instanceof ConsoleCommandSender) {
						getServer().getConsoleSender().sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
					} else {
						sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーのみ実行できます");
					}
				}
			} else {
				if (sender instanceof ConsoleCommandSender) {
					getServer().getConsoleSender().sendMessage(ChatColor.RED + "パラメーターが間違っています");
				} else {
					sender.sendMessage(ChatColor.RED + "パラメーターが間違っています");
				}
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
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getName() + ": 無効");
	}
	public static Main getMain() {
		return main;
	}
	public void traysend(String title, String msg, MessageType type) {
		tray.displayMessage(title, msg, type);
	}
}
