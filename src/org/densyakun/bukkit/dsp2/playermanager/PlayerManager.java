package org.densyakun.bukkit.dsp2.playermanager;
import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.densyakun.bukkit.dsp2.Main;
import org.densyakun.bukkit.minigamemanager.Game;
import org.densyakun.bukkit.minigamemanager.MultiGame;
import org.densyakun.csvm.CSVFile;

import com.iCo6.iConomy;
import com.iCo6.system.Account;
import com.iCo6.util.Messaging;
import com.iCo6.util.Template;
public class PlayerManager implements Listener, Runnable {
	private Main main;
	private UUID ownerUUID;
	private List<UUIDandInteger> afk = new ArrayList<UUIDandInteger>();
	private int afktime;
	private String afkkick;
	private String afkkick_;
	private String afkmsg;
	private String afkbackmsg;
	private File dir;
	private CSVFile datacsv;
	private List<PlayerData> pdata = new ArrayList<PlayerData>();
	private String joinmsg;
	private String quitmsg;
	private String welcomemsg;
	private boolean adminmode = false;
	private boolean whitemode = false;
	
	private String travelermsg;
	private String rsubmitmsg;
	private String rsubmittedmsg;
	private String racceptmsg;
	private String rdismissalmsg;
	private String residentsmsg;
	private String rdemotemsg;
	private String adminpromotemsg;
	private String adminmsg;
	private String admindemotemsg;
	private String ownerpromotemsg;
	private String ownermsg;
	
	//int firework = 0;
	public PlayerManager(Main main) {
		this.main = main;
		(dir = new File(main.getDataFolder(), "PlayerManager/")).mkdirs();
		if ((datacsv = new CSVFile(new File(dir, "Rank.csv"))).getFile().exists()) {
			try {
				List<List<String>> data = datacsv.AllRead();
				for (int a = 0; a < data.size(); a++) {
					if (4 <= data.get(a).size()) {
						PlayerData pd = new PlayerData(UUID.fromString(data.get(a).get(0)), PlayerRank.valueOf(data.get(a).get(1)));
						if (CSVFile.isArray(data.get(a).get(2))) {
							List<String> key = CSVFile.StringtoArray(data.get(a).get(2));
							List<String> value = CSVFile.StringtoArray(data.get(a).get(3));
							for (int b = 0; b < key.size() && b < value.size(); b++) {
								try {
									pd.setMetadata(key.get(b), value.get(b));
								} catch (NumberFormatException e) {
								}
							}
						}
						boolean b = true;
						for (int c = 0; c < pdata.size(); c++) {
							if (pdata.get(c).getUuid().equals(pd.getUuid())) {
								b = false;
								String[] keys = pd.getMetadataKeys();
								for (int d = 0; d < keys.length; d++) {
									pdata.get(c).setMetadata(keys[d], pd.getMetadata(keys[d]));
								}
								break;
							}
						}
						if (b) {
							pdata.add(pd);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				datacsv.getFile().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String uuidstr = main.getConfig().getString("ownerUUID", null);
		if (uuidstr != null) {
			main.getLogger().info("ownerUUID: " + (ownerUUID = UUID.fromString(uuidstr)));
		} else {
			main.getLogger().info("ownerUUID: (:null:)");
		}
		main.getLogger().info("AFK時間: " + (afktime = main.getConfig().getInt("afk-time", 60)) + "秒");
		main.getLogger().info("AFK自動Kickメッセージ: \"" + (afkkick = main.getConfig().getString("afk-kick", "Playerが放置により自動Kickされました")) + "\"");
		main.getLogger().info("AFK自動Kickメッセージ(2): \"" + (afkkick_ = main.getConfig().getString("afk-kick_", "放置により自動Kickされました")) + "\"");
		main.getLogger().info("AFKメッセージ: \"" + (afkmsg = main.getConfig().getString("afk-msg", "Playerが放置中です")) + "\"");
		main.getLogger().info("AFKからの回復メッセージ: \"" + (afkbackmsg = main.getConfig().getString("afk-back-msg", "Playerが動きました")) + "\"");
		main.getLogger().info("ログインメッセージ: \"" + (joinmsg = main.getConfig().getString("joinmsg", "Playerがログインしました")) + "\"");
		main.getLogger().info("ログアウトメッセージ: \"" + (quitmsg = main.getConfig().getString("quitmsg", "Playerがログアウトしました")) + "\"");
		main.getLogger().info("ウェルカムメッセージ: \"" + (welcomemsg = main.getConfig().getString("welcomemsg", "電車君サーバーへようこそ!")) + "\"");
		
		main.getLogger().info("旅人用メッセージ: \"" + (travelermsg = main.getConfig().getString("traveler-msg", "初めての方は旅人ランクのためブロックの破壊等はできません。住民ランクへ昇格するには登録したい住所地で/rsub [町名レターコード]を実行して下さい。(電鯖町名コードについてはこちらをご覧下さい: URL）")) + "\"");
		main.getLogger().info("住民票提出メッセージ: \"" + (rsubmitmsg = main.getConfig().getString("rsubmit-msg", "住民票を提出しました。受理されるまでお待ち下さい。生活についてはこちらをご覧下さい: URL")) + "\"");
		main.getLogger().info("住民票提出済みメッセージ: \"" + (rsubmittedmsg = main.getConfig().getString("rsubmitted-msg", "住民票は提出済みです。問題があればこちらから報告をお願いします: URL")) + "\"");
		main.getLogger().info("住民票受理メッセージ: \"" + (racceptmsg = main.getConfig().getString("raccept-msg", "住民票が受理され、住民へ昇格しました。それでは電車君サーバーをお楽しみ下さい。生活についてはこちらをご覧下さい: URL")) + "\"");
		main.getLogger().info("住民票却下メッセージ: \"" + (rdismissalmsg = main.getConfig().getString("rdismissal-msg", "申し訳ございません。住民票が受理されませんでした。担当: CHARGE メッセージ: MESSAGE")) + "\"");
		main.getLogger().info("住民用メッセージ: \"" + (residentsmsg = main.getConfig().getString("residents-msg", "住民の方々へお知らせがあります: URL")) + "\"");
		main.getLogger().info("住民降格メッセージ: \"" + (rdemotemsg = main.getConfig().getString("rdemote-msg", "住民へ降格されました。メッセージ: MESSAGE")) + "\"");
		main.getLogger().info("管理者昇格メッセージ: \"" + (adminpromotemsg = main.getConfig().getString("adminpromote-msg", "おめでとうございます。管理者へ昇格しました。管理者に向けて説明があります。こちらをご覧下さい: URL")) + "\"");
		main.getLogger().info("管理者用メッセージ: \"" + (adminmsg = main.getConfig().getString("admin-msg", "管理者の方々へお知らせがあります: URL")) + "\"");
		main.getLogger().info("管理者降格メッセージ: \"" + (admindemotemsg = main.getConfig().getString("admindemote-msg", "管理者へ降格されました。メッセージ: MESSAGE")) + "\"");
		main.getLogger().info("副鯖主昇格メッセージ: \"" + (ownerpromotemsg = main.getConfig().getString("ownerpromote-msg", "おめでとうございます。副鯖主へ昇格しました。")) + "\"");
		main.getLogger().info("副鯖主用メッセージ: \"" + (ownermsg = main.getConfig().getString("owner-msg", "副鯖主の方々へお知らせがあります: URL")) + "\"");
		main.getServer().getPluginManager().registerEvents(this, main);
		new Thread(this).start();
		main.getLogger().info("PlayerManager: 有効");
	}
	@Override
	public void run() {
		while (main.isEnabled()) {
			main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				public void run() {
					for (int a = 0; a < afk.size();) {
						afk.get(a).time++;
						if (afktime <= afk.get(a).time) {
							Player player = main.getServer().getPlayer(afk.get(a).getUniqueId());
							if (player != null) {
								if (getPlayerData(afk.get(a).getUniqueId()).getRank() == PlayerRank.Traveler) {
									main.getLogger().info(player.getDisplayName() + "を自動Kickしました");
									player.kickPlayer(afkkick_);
									afk.remove(a);
									continue;
								} else if (afktime == afk.get(a).time) {
									Player[] players = main.getServer().getOnlinePlayers().toArray(new Player[0]);
									for (int b = 0; b < players.length; b++) {
										players[b].sendMessage(afkmsg.replaceAll("Player", player.getDisplayName()));
									}
								}
							}
						}
						a++;
					}
					/*List<Player> players = new ArrayList<Player>(main.getServer().getOnlinePlayers());
					for (int a = 0; a < players.size(); a++) {
						Game game = main.gamemanager.getPlayingGame(players.get(a).getUniqueId());
						if (game != null ? ((game instanceof MultiGame) ? game.time < ((MultiGame) game).entrytime : true) : true) {
							players.get(a).setBedSpawnLocation(null);
							players.get(a).setFireTicks(0);
							players.get(a).setFoodLevel(20);
							players.get(a).setHealth(players.get(a).getMaxHealth());
							players.get(a).setSprinting(true);
							PotionEffect[] effects = players.get(a).getActivePotionEffects().toArray(new PotionEffect[0]);
							for (int b = 0; b < effects.length; b++) {
								players.get(a).removePotionEffect(effects[b].getType());
							}
							players.get(a).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 15 * 20, 96));
							if (players.get(a).getLocation().getY() < 0) {
								respawn(players.get(a));
							}
							PlayerData data = getPlayerData(players.get(a).getUniqueId());
							if (!adminmode) {
								players.get(a).getInventory().clear();
								if ((data.getRank() == PlayerRank.Owner) || (data.getRank() == PlayerRank.SubOwner) || (data.getRank() == PlayerRank.Admin)) {
									if (!data.isHide()) {
										if (0 < firework) {
											Entity entity = players.get(a).getWorld().spawnEntity(players.get(a).getLocation(), EntityType.FIREWORK);
											if (entity != null) {
												if (entity instanceof Firework) {
													FireworkMeta meta = ((Firework) entity).getFireworkMeta();
													meta.addEffect(FireworkEffect.builder().with(Type.CREEPER).withColor(PlayerRank.getColor(data.getRank())).withFade(Color.WHITE).build());
													((Firework) entity).setFireworkMeta(meta);
												}
											}
										}
									}
									if (data.getRank() == PlayerRank.Owner) {
										players.get(a).getInventory().setHelmet(new ItemStack(Material.STAINED_GLASS, 1, (short) 3));
										players.get(a).getInventory().setChestplate(new ItemStack(Material.DIAMOND_BOOTS));
										players.get(a).getInventory().setLeggings(new ItemStack(Material.DIAMOND_BOOTS));
										players.get(a).getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
									} else if (data.getRank() == PlayerRank.SubOwner) {
										players.get(a).getInventory().setHelmet(new ItemStack(Material.STAINED_GLASS, 1, (short) 11));
										players.get(a).getInventory().setChestplate(new ItemStack(Material.IRON_BOOTS));
										players.get(a).getInventory().setLeggings(new ItemStack(Material.IRON_BOOTS));
										players.get(a).getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
									} else if (data.getRank() == PlayerRank.Admin) {
										players.get(a).getInventory().setHelmet(new ItemStack(Material.STAINED_GLASS, 1, (short) 5));
										players.get(a).getInventory().setChestplate(new ItemStack(Material.LEATHER_LEGGINGS));
										players.get(a).getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
										players.get(a).getInventory().setBoots(new ItemStack(Material.LEATHER_LEGGINGS));
									} else {
										players.get(a).getInventory().setHelmet(null);
										players.get(a).getInventory().setChestplate(null);
										players.get(a).getInventory().setLeggings(null);
										players.get(a).getInventory().setBoots(null);
									}
								}
							}
						}
					}
					if (0 < firework) {
						firework--;
					}*/
				}
			});
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void stop() {
		List<List<String>> data = new ArrayList<List<String>>();
		for (int a = 0; a < pdata.size(); a++) {
			List<String> line = new ArrayList<String>();
			line.add(pdata.get(a).getUuid().toString());
			line.add(pdata.get(a).getInternalRank().name());
			String[] keys = pdata.get(a).getMetadataKeys();
			String[] values = new String[keys.length];
			for (int b = 0; b < keys.length; b++) {
				values[b] = pdata.get(a).getMetadata(keys[b]).toString();
			}
			line.add(CSVFile.ArrayToString(keys));
			line.add(CSVFile.ArrayToString(values));
			data.add(line);
		}
		try {
			datacsv.AllWrite(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public UUID getOwnerUniqueID() {
		return ownerUUID;
	}
	public PlayerData getPlayerData(UUID uuid) {
		for (int a = 0; a < pdata.size(); a++) {
			if (pdata.get(a).getUuid().equals(uuid)) {
				return pdata.get(a);
			}
		}
		PlayerData data = new PlayerData(uuid);
		pdata.add(data);
		return data;
	}
	/*public void UpdatePlayerData(PlayerData data) {
		boolean a = true;
		for (int b = 0; b < pdata.size(); b++) {
			if (pdata.get(b).getUuid().equals(data.getRank())) {
				a = false;
				pdata.set(b, data);
				break;
			}
		}
		if (a) {
			pdata.add(data);
		}
		Player player = main.getServer().getPlayer(data.getUuid());
		if (player != null) {
			namereload(player);
		}
	}*/
	public void afkupdate(UUID uuid) {
		for (int a = 0; a < afk.size(); a++) {
			if (afk.get(a).getUniqueId().equals(uuid)) {
				if (afktime <= afk.get(a).time) {
					Player player = main.getServer().getPlayer(uuid);
					if (player != null) {
						Player[] players = main.getServer().getOnlinePlayers().toArray(new Player[0]);
						for (int b = 0; b < players.length; b++) {
							players[b].sendMessage(afkbackmsg.replaceAll("Player", player.getDisplayName()));
						}
					}
				}
				afk.get(a).time = 0;
				return;
			}
		}
		afk.add(new UUIDandInteger(uuid, (byte) 0));
	}
	public void sethide(Player player, boolean hide) {
		for (int a = 0; a < pdata.size(); a++) {
			if (player.getUniqueId().equals(pdata.get(a).getUuid())) {
				pdata.get(a).setHide(hide);
				Player[] players = main.getServer().getOnlinePlayers().toArray(new Player[0]);
				for (int b = 0; b < players.length; b++) {
					if (pdata.get(a).isHide()) {
						if (getPlayerData(players[b].getUniqueId()).getRank().isAdmin()) {
							players[b].showPlayer(player);
						} else {
							players[b].hidePlayer(player);
						}
					} else {
						players[b].showPlayer(player);
					}
				}
				break;
			}
		}
	}
	public void adminmodetoggle() {
		if (adminmode = !adminmode) {
			main.getLogger().info("管理モードが有効");
		} else {
			main.getLogger().info("管理モードが無効");
		}
		Iterator<? extends Player> a = main.getServer().getOnlinePlayers().iterator();
		while (a.hasNext()) {
			Player player = a.next();
			PlayerData data = getPlayerData(player.getUniqueId());
			if (data.getRank().isAdmin()) {
				if (adminmode) {
					player.sendMessage(ChatColor.AQUA + "管理モードが有効");
				} else {
					player.sendMessage(ChatColor.RED + "管理モードが無効");
					if (player.getGameMode() == GameMode.CREATIVE) {
						player.setGameMode(GameMode.SURVIVAL);
					}
				}
			}
		}
	}
	public boolean isAdminmode() {
		return adminmode;
	}
	public void namereload(Player player) {
		PlayerData data = getPlayerData(player.getUniqueId());
		String name = new String();
		if (main.getServer().getPluginManager().getPlugin("MiniGameManager") != null && org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGame(player.getUniqueId()) != null) {
			name += ChatColor.GOLD + "[G]";
		}
		switch (data.getRank()) {
		case Owner:
			name += data.getRank().getChatColor() + "[O]" + ChatColor.WHITE;
			if (!isAdminmode() && player.getGameMode() == GameMode.CREATIVE) {
				player.setGameMode(GameMode.SURVIVAL);
			}
			break;
		case Admin:
			name += data.getRank().getChatColor() + "[A]" + ChatColor.WHITE;
			if (!isAdminmode() && player.getGameMode() == GameMode.CREATIVE) {
				player.setGameMode(GameMode.SURVIVAL);
			}
			break;
		case Residents:
			name += data.getRank().getChatColor();
			player.setGameMode(GameMode.SURVIVAL);
			break;
		default:
			player.setGameMode(GameMode.ADVENTURE);
			break;
		}
		player.setOp(data.getRank().isAdmin());
		if (data.getRank() == PlayerRank.Traveler) {
			if (main.getServer().getPluginManager().getPlugin("MiniGameManager") != null) {
				org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().addEntryBan(player.getUniqueId());
			}
		} else {
			if (main.getServer().getPluginManager().getPlugin("MiniGameManager") != null) {
				org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().removeEntryBan(player.getUniqueId());;
			}
			name += ChatColor.BOLD;
		}
		String nick = data.getMetadata("nick");
		if (nick == null) {
			name += player.getName() + ChatColor.RESET;
		} else {
			name += nick + ChatColor.RESET;
		}
		String tabname = name.substring(0, name.length() <= 16 ? name.length() : 16);
		player.setDisplayName(name);
		player.setPlayerListName(tabname.charAt(tabname.length() - 1) == '§' ? tabname.substring(0, tabname.length() - 1) : tabname);
		for (int b = 0; b < pdata.size(); b++) {
			Player c = main.getServer().getPlayer(pdata.get(b).getUuid());
			if (c != null) {
				if (!getPlayerData(player.getUniqueId()).getRank().isAdmin() && pdata.get(b).isHide()) {
					player.hidePlayer(c);
				} else {
					player.showPlayer(c);
				}
			}
		}
	}
	public void whitemodetoggle() {
		if (whitemode = !whitemode) {
			main.getLogger().info("ホワイトモードが有効");
		} else {
			main.getLogger().info("ホワイトモードが無効");
		}
		Player[] players = main.getServer().getOnlinePlayers().toArray(new Player[0]);
		for (int a = 0; a < players.length; a++) {
			if (getPlayerData(players[a].getUniqueId()).getRank().isAdmin()) {
				if (whitemode) {
					players[a].sendMessage(ChatColor.AQUA + "ホワイトモードが有効");
				} else {
					players[a].sendMessage(ChatColor.RED + "ホワイトモードが無効");
				}
			} else if (whitemode) {
				players[a].kickPlayer("ホワイトモードになりました(管理者専用)");
			}
		}
	}
	public boolean isWhitemode() {
		return whitemode;
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e) {
		namereload(e.getPlayer());
		PlayerData data = getPlayerData(e.getPlayer().getUniqueId());
		//data.setMetadata("JoinHist_" + new Date().getTime(), CSVFile.ArrayToString(new String[]{e.getPlayer().getAddress().getHostName(), e.getPlayer().getName()}));
		System.out.println("JoinHist: " + e.getPlayer().getName() + "_" + e.getPlayer().getAddress().getHostName());
		if (!data.getRank().isAdmin() && whitemode) {
			e.getPlayer().kickPlayer("ホワイトモードです(管理者専用)");
			main.traysend(main.getServer().getServerName(), e.getPlayer().getDisplayName() + "がホワイトモードによりキック", MessageType.INFO);
			e.setJoinMessage(null);
		} else {
			main.traysend(main.getServer().getServerName(), e.getPlayer().getDisplayName() + "がログイン", MessageType.INFO);
			e.setJoinMessage(joinmsg.replaceAll("Player", e.getPlayer().getDisplayName()));
			e.getPlayer().sendMessage(welcomemsg.replaceAll("Player", e.getPlayer().getDisplayName()));
			afkupdate(e.getPlayer().getUniqueId());
			if (main.getServer().getPluginManager().getPlugin("iConomy") != null) {
				Date last = new Date(e.getPlayer().getLastPlayed());
				Date now = new Date();
				if ((last.getYear() != now.getYear()) || (last.getMonth() != now.getMonth()) || (last.getDay() != now.getDay())) {
					e.getPlayer().sendMessage(ChatColor.AQUA + "ログインボーナスです");
					double prize = main.getConfig().getDouble("joinbonus", 500.0);
					new Account(e.getPlayer().getName()).getHoldings().add(prize);
					iConomy.Template.set(Template.Node.PLAYER_CREDIT);
					iConomy.Template.add("name", e.getPlayer().getName());
					iConomy.Template.add("amount", iConomy.format(prize));
					Messaging.send(e.getPlayer(), iConomy.Template.color(Template.Node.TAG_MONEY) + iConomy.Template.parse());
				}
			}
			if (main.getServer().getPluginManager().getPlugin("MiniGameManager") == null) {
				e.getPlayer().sendMessage(ChatColor.GRAY.toString() + ChatColor.BOLD + "[自動]" + ChatColor.YELLOW + "ミニゲームはメンテナンス中です");
			}
			rankmessage(e.getPlayer(), data);
		}
	}
	public void rankmessage(Player player, PlayerData data) {
		if (data.getMetadata("ownerpromotemsg") != null) {
			player.sendMessage(ChatColor.GOLD + ownerpromotemsg);
			data.removeMetadata("ownerpromotemsg");
		}
		if (data.getMetadata("adminpromotemsg") != null) {
			player.sendMessage(ChatColor.GOLD + adminpromotemsg);
			data.removeMetadata("adminpromotemsg");
		}
		if (data.getMetadata("admindemotemsg") != null) {
			player.sendMessage(ChatColor.GOLD + admindemotemsg);
			data.removeMetadata("admindemotemsg");
		}
		if (data.getMetadata("racceptmsg") != null) {
			player.sendMessage(ChatColor.GOLD + racceptmsg);
			data.removeMetadata("racceptmsg");
		}
		if (data.getMetadata("rdismissalmsg") != null) {
			player.sendMessage(ChatColor.GOLD + rdismissalmsg);
			data.removeMetadata("rdismissalmsg");
		}
		if (data.getMetadata("rdemotemsg") != null) {
			player.sendMessage(ChatColor.GOLD + rdemotemsg);
			data.removeMetadata("rdemotemsg");
		}
		if (data.getRank().isAdmin()) {
			if (data.getRank() == PlayerRank.Owner) {
				if (ownerUUID == null ? true : !data.getUuid().equals(ownerUUID)) {
					player.sendMessage(ChatColor.GOLD + ownermsg);
				}
			} else {
				player.sendMessage(ChatColor.GOLD + adminmsg);
			}
		} else if (data.getRank() == PlayerRank.Residents) {
			player.sendMessage(ChatColor.GOLD + residentsmsg);
		} else if (data.getRank() == PlayerRank.Traveler) {
			player.sendMessage(ChatColor.GOLD + travelermsg);
		}
	}
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent e) {
		e.getPlayer().leaveVehicle();
		main.traysend(main.getServer().getServerName(), e.getPlayer().getDisplayName() + "がログアウト:\n" + e.getQuitMessage(), MessageType.INFO);
		e.setQuitMessage(quitmsg.replaceAll("Player", e.getPlayer().getDisplayName()));
		/*PlayerData data = getPlayerData(e.getPlayer().getUniqueId());
		data.setMetadata("QuitPos_" + new Date().getTime(), e.getPlayer().getLocation().toString());*/
		System.out.println("QuitPos: " + e.getPlayer().getName() + "_" + e.getPlayer().getLocation());
	}
	@EventHandler
	public void PlayerRespawn(PlayerRespawnEvent e) {
		afkupdate(e.getPlayer().getUniqueId());
	}
	@EventHandler
	public void PlayerMove(PlayerMoveEvent e) {
		afkupdate(e.getPlayer().getUniqueId());
	}
	@EventHandler
	public void PlayerGameModeChange(PlayerGameModeChangeEvent e) {
		if (e.getNewGameMode() == GameMode.CREATIVE) {
			if (getPlayerData(e.getPlayer().getUniqueId()).getRank().isAdmin()) {
				if (!adminmode) {
					e.getPlayer().sendMessage(ChatColor.RED + "管理モードを有効にして下さい");
					e.setCancelled(true);
					e.getPlayer().setGameMode(GameMode.ADVENTURE);
				}
			} else {
				e.getPlayer().sendMessage(ChatColor.RED + "一般プレイヤーはゲームモードを変更できません");
				e.setCancelled(true);
				e.getPlayer().setGameMode(GameMode.ADVENTURE);
			}
		}
	}
	@EventHandler
	public void BlockBurn(BlockBurnEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void EntityBreakDoor(EntityBreakDoorEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void EntityChangeBlock(EntityChangeBlockEvent e) {
		e.setCancelled(true);
	}
	@EventHandler
	public void EntityDamageByBlock(EntityDamageByBlockEvent e) {
		if (e.getEntity() instanceof Player && getPlayerData(e.getEntity().getUniqueId()).getRank() == PlayerRank.Traveler) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void EntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && getPlayerData(e.getEntity().getUniqueId()).getRank() == PlayerRank.Traveler || e.getDamager() instanceof Player && getPlayerData(e.getDamager().getUniqueId()).getRank() == PlayerRank.Traveler) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void EntityExplode(EntityExplodeEvent e) {
		e.blockList().clear();
	}
	@EventHandler
	public void PlayerDeath(PlayerDeathEvent e) {
		PlayerData data = getPlayerData(e.getEntity().getUniqueId());
		int meta = 0;
		try {
			meta = Integer.valueOf(data.getMetadata("death"));
		} catch (NumberFormatException x) {
		}
		data.setMetadata("death", String.valueOf(meta + 1));
		if (e.getEntity().getKiller() != null) {
			data = getPlayerData(e.getEntity().getKiller().getUniqueId());
			meta = 0;
			try {
				meta = Integer.valueOf(data.getMetadata("kill"));
			} catch (NumberFormatException x) {
			}
			data.setMetadata("kill", String.valueOf(meta + 1));
		}
		e.setKeepInventory(true);
		e.setKeepLevel(true);
	}
	@EventHandler
	public void PlayerPickupItem(PlayerPickupItemEvent e) {
		if (getPlayerData(e.getPlayer().getUniqueId()).getRank() == PlayerRank.Traveler) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void PlayerDropItem(PlayerDropItemEvent e) {
		afkupdate(e.getPlayer().getUniqueId());
		main.getServer().getConsoleSender().sendMessage("アイテムがポイ捨てされました Player: " + e.getPlayer().getDisplayName() + ChatColor.RESET + " Location: " +  e.getItemDrop().getLocation());
	}
	@EventHandler
	public void BlockBreak(BlockBreakEvent e) {
		afkupdate(e.getPlayer().getUniqueId());
		if (getPlayerData(e.getPlayer().getUniqueId()).getRank() == PlayerRank.Traveler) {
			e.setCancelled(true);
		} else if (main.getServer().getPluginManager().getPlugin("MiniGameManager") != null) {
			Game game = org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGame(e.getPlayer().getUniqueId());
			if (game != null && game instanceof MultiGame && game.time < ((MultiGame) game).entrytime) {
				e.setCancelled(true);
			}
		} else if (e.getPlayer().getVehicle() instanceof Minecart) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void BlockIgnite(BlockIgniteEvent e) {
		if (e.getPlayer() != null) {
			afkupdate(e.getPlayer().getUniqueId());
			if (getPlayerData(e.getPlayer().getUniqueId()).getRank() == PlayerRank.Traveler) {
				e.setCancelled(true);
			} else if (main.getServer().getPluginManager().getPlugin("MiniGameManager") != null) {
				Game game = org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGame(e.getPlayer().getUniqueId());
				if (game != null && game instanceof MultiGame && game.time < ((MultiGame) game).entrytime) {
					e.setCancelled(true);
				}
			}
		} else {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void BlockPlace(BlockPlaceEvent e) {
		afkupdate(e.getPlayer().getUniqueId());
		if (getPlayerData(e.getPlayer().getUniqueId()).getRank() == PlayerRank.Traveler) {
			e.setCancelled(true);
		} else if (main.getServer().getPluginManager().getPlugin("MiniGameManager") != null) {
			Game game = org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGame(e.getPlayer().getUniqueId());
			if (game != null && game instanceof MultiGame && game.time < ((MultiGame) game).entrytime) {
				e.setCancelled(true);
			}
		} else if (e.getPlayer().getVehicle() instanceof Minecart) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void EntityTargetLivingEntity(EntityTargetLivingEntityEvent e) {
		if (e.getTarget() instanceof Player) {
			if (new PlayerData(e.getTarget().getUniqueId()).getRank() == PlayerRank.Traveler) {
				e.setCancelled(true);
			} else if (main.getServer().getPluginManager().getPlugin("MiniGameManager") != null) {
				Game game = org.densyakun.bukkit.minigamemanager.Main.getMinigamemanager().getPlayingGame(e.getTarget().getUniqueId());
				if (game != null && game instanceof MultiGame && game.time < ((MultiGame) game).entrytime) {
					e.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void AsyncPlayerChat(AsyncPlayerChatEvent e) {
		afkupdate(e.getPlayer().getUniqueId());
		main.traysend(main.getServer().getServerName(), e.getPlayer().getDisplayName() + " Chat:" + e.getMessage(), MessageType.INFO);
	}
	@EventHandler
	public void PlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		afkupdate(e.getPlayer().getUniqueId());
		main.traysend(main.getServer().getServerName(), e.getPlayer().getDisplayName() + " Cmd:" + e.getMessage(), MessageType.INFO);
	}
	@EventHandler
	public void InventoryOpen(InventoryOpenEvent e) {
		PlayerData data =getPlayerData(e.getPlayer().getUniqueId());
		if (data.getRank() == PlayerRank.Traveler) {
			e.setCancelled(true);
		}
	}
	/*@Override
	public void spawn(org.densyakun.bukkit.hubspawn.Main main, Player player) {
		/*Game game = main.gamemanager.getPlayingGame(player.getUniqueId());
		if (game != null) {
			if (game instanceof MultiGame) {
				((MultiGame) game).removePlayer(player.getUniqueId());
			} else {
				game.stop();
			}
		}
		firework = 5;
	}*/
	@EventHandler
	public void VehicleEnter(VehicleEnterEvent e) {
		if (e.getVehicle().getType() == EntityType.MINECART && e.getEntered() instanceof Player) {
			((Player) e.getEntered()).sendMessage(ChatColor.GREEN + "手動運転の方法:\n左クリック: ブレーキ(連打)\n右クリック: 加速(ブロックを連打・移動しているときのみ)\n手動運転区間について:\nhttp://densyakunserver.dip.jp/index.php/%E9%89%84%E9%81%93%E9%81%8B%E8%BB%A2%E6%96%B9%E5%BC%8F");
		}
	}
	@EventHandler
	public void PlayerInteract(PlayerInteractEvent e) {
		if ((e.getPlayer().getVehicle() != null) && (e.getPlayer().getVehicle() instanceof Minecart)) {
			Vector a = e.getPlayer().getVehicle().getVelocity();
			switch (e.getAction()) {
			case LEFT_CLICK_AIR:
			case LEFT_CLICK_BLOCK:
				if (a.getX() < 0) {
					a.setX(a.getX() + 0.015);
				} else if (0 < a.getX()) {
					a.setX(a.getX() - 0.015);
				}
				if (a.getY() < 0) {
					a.setY(a.getY() + 0.015);
				} else if (0 < a.getY()) {
					a.setY(a.getY() - 0.015);
				}
				if (a.getZ() < 0) {
					a.setZ(a.getZ() + 0.015);
				} else if (0 < a.getZ()) {
					a.setZ(a.getZ() - 0.015);
				}
				e.getPlayer().getVehicle().getWorld().playSound(e.getPlayer().getVehicle().getLocation(), Sound.BLAZE_HIT, 1, 0);
				break;
			case RIGHT_CLICK_AIR:
			case RIGHT_CLICK_BLOCK:
				if (a.getX() < 0) {
					a.setX(a.getX() - 0.015);
				} else if (0 < a.getX()) {
					a.setX(a.getX() + 0.015);
				}
				if (a.getY() < 0) {
					a.setY(a.getY() - 0.015);
				} else if (0 < a.getY()) {
					a.setY(a.getY() + 0.015);
				}
				if (a.getZ() < 0) {
					a.setZ(a.getZ() - 0.015);
				} else if (0 < a.getZ()) {
					a.setZ(a.getZ() + 0.015);
				}
				e.getPlayer().getVehicle().getWorld().playSound(e.getPlayer().getVehicle().getLocation(), Sound.BLAZE_HIT, 1, 0);
				break;
			default:
				break;
			}
			e.getPlayer().getVehicle().setVelocity(a);
			a = null;
		}
	}
	@EventHandler
	public void EntityDeath(EntityDeathEvent e) {
		switch (e.getEntityType()) {
		case BLAZE:
		case CAVE_SPIDER:
		case CREEPER:
		case ENDERMAN:
		case ENDER_DRAGON:
		case GHAST:
		case GIANT:
		case MAGMA_CUBE:
		case PIG_ZOMBIE:
		case SILVERFISH:
		case SKELETON:
		case SLIME:
		case SNOWMAN:
		case SPIDER:
		case WITCH:
		case WITHER:
		case WITHER_SKULL:
		case ZOMBIE:
			if (e.getEntity().getKiller() != null && new Random().nextInt(128) == 0) {
				e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), new ItemStack(Material.DIAMOND));
			}
			break;
		default:
			break;
		}
	}
}
