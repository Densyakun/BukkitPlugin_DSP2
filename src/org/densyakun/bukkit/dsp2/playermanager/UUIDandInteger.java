package org.densyakun.bukkit.dsp2.playermanager;
import java.util.UUID;
public class UUIDandInteger {
	private UUID uuid;
	public byte time;
	public UUIDandInteger(UUID uuid, byte time) {
		this.uuid = uuid;
		this.time = time;
	}
	public UUID getUniqueId() {
		return uuid;
	}
}
