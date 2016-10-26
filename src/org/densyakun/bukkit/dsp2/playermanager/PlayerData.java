package org.densyakun.bukkit.dsp2.playermanager;
import java.util.HashMap;
import java.util.UUID;

import org.densyakun.bukkit.dsp2.Main;
public class PlayerData {
	private UUID uuid;
	private PlayerRank rank;
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
		if (uuid.equals(Main.getMain().playermanager.getOwnerUniqueID())) {
			return PlayerRank.Owner;
		}
		return rank;
	}
	public PlayerRank getInternalRank() {
		return rank;
	}
	public void setRank(PlayerRank rank) {
		if (!uuid.equals(Main.getMain().playermanager.getOwnerUniqueID())) {
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
			Main main = Main.getMain();
			main.playermanager.rankmessage(main.getServer().getPlayer(uuid), this);
		}
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
