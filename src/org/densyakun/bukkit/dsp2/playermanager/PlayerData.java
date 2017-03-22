package org.densyakun.bukkit.dsp2.playermanager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.densyakun.bukkit.dsp2.Main;

public class PlayerData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UUID uuid;
	private PlayerRank rank;
	private String nick;
	private boolean hide = false;
	private HashMap<String, String> metadata = new HashMap<String, String>();

	public PlayerData(UUID uuid) {
		this(uuid, PlayerRank.getDefault());
	}

	public PlayerData(UUID uuid, PlayerRank rank) {
		this.uuid = uuid;
		this.rank = rank;
	}

	public UUID getUuid() {
		return uuid;
	}

	public PlayerRank getRank() {
		if (uuid.equals(Main.main.playermanager.getOwnerUniqueID())) {
			return PlayerRank.Owner;
		}
		return rank;
	}

	public PlayerRank getInternalRank() {
		return rank;
	}

	public void setRank(PlayerRank rank) {
		if (!uuid.equals(Main.main.playermanager.getOwnerUniqueID())) {
			if (rank == PlayerRank.Owner) {
				setMetadata("ownerpromotemsg", "");
				removeMetadata("adminpromotemsg");
				removeMetadata("admindemotemsg");
			}
			if (rank == PlayerRank.Admin) {
				if (this.rank.isAdmin()) {
					setMetadata("admindemotemsg", "");
					removeMetadata("ownerpromotemsg");
					removeMetadata("adminpromotemsg");
				} else {
					setMetadata("adminpromotemsg", "");
					removeMetadata("ownerpromotemsg");
					removeMetadata("admindemotemsg");
				}
			}
			this.rank = rank;
			Player player = Main.main.getServer().getPlayer(uuid);
			if (player != null) {
				Main.main.playermanager.namereload(player);
				Main.main.playermanager.rankmessage(player, this);
			}
		}
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return nick;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

	public String[] getMetadataKeys() {
		return metadata.keySet().toArray(new String[0]);
	}

	public String getMetadata(String key) {
		return metadata.get(key);
	}

	public void setMetadata(String key, String value) {
		metadata.put(key, value);
	}

	public String removeMetadata(String key) {
		return metadata.remove(key);
	}
}
