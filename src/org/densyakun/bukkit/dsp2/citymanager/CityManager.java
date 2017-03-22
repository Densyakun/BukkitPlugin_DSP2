package org.densyakun.bukkit.dsp2.citymanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CityManager {
	private static Map<String, List<UUID>> localpublicentities = new HashMap<String, List<UUID>>();
	private static Map<String, List<UUID>> rsubmits = new HashMap<String, List<UUID>>();

	public static Map<String, List<UUID>> getLocalPublicEntities() {
		return localpublicentities;
	}

	public static boolean registLocalPublicEntities(String localpe, UUID uuid) {
		List<UUID> uuids = localpublicentities.get(localpe);
		if (uuids != null) {
			if (!uuids.contains(uuid)) {
				uuids.add(uuid);
				// localpublicentities.put(localpe, uuids);
			}
			return true;
		}
		return false;
	}

	public static boolean removeLocalPublicEntities(String localpe, UUID uuid) {
		List<UUID> uuids = localpublicentities.get(localpe);
		if (uuids != null) {
			for (int a = 0; a < uuids.size(); a++) {
				if (uuids.get(a).equals(uuid)) {
					uuids.remove(uuid);
					if (uuids.size() == 0) {
						localpublicentities.remove(localpe);
					}
					// localpublicentities.put(localpe, uuids);
					return true;
				}
			}
		}
		return false;
	}

	public static Map<String, List<UUID>> getRSubmits() {
		return rsubmits;
	}

	public static void rsubmit() {

	}

	public static void raccept() {

	}
}
