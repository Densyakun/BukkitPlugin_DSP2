package org.densyakun.bukkit.dsp2.citymanager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.densyakun.bukkit.dsp2.Main;
public class CityManager {
	private Main main;
	private Map<String, List<UUID>> localpublicentities = new HashMap<String, List<UUID>>();
	private Map<String, List<UUID>> rsubmits = new HashMap<String, List<UUID>>();
	public CityManager(Main main) {
		this.main = main;
	}
	public Map<String, List<UUID>> getLocalPublicEntities() {
		return localpublicentities;
	}
	public boolean registLocalPublicEntities(String localpe, UUID uuid) {
		List<UUID> uuids = localpublicentities.get(localpe);
		if (uuids != null) {
			if (!uuids.contains(uuid)) {
				uuids.add(uuid);
				//localpublicentities.put(localpe, uuids);
			}
			return true;
		}
		return false;
	}
	public boolean removeLocalPublicEntities(String localpe, UUID uuid) {
		List<UUID> uuids = localpublicentities.get(localpe);
		if (uuids != null) {
			for (int a = 0; a < uuids.size(); a++) {
				if (uuids.get(a).equals(uuid)) {
					uuids.remove(uuid);
					if (uuids.size() == 0) {
						localpublicentities.remove(localpe);
					}
					//localpublicentities.put(localpe, uuids);
					return true;
				}
			}
		}
		return false;
	}
	public Map<String, List<UUID>> getRSubmits() {
		return rsubmits;
	}
	public void rsubmit() {
		
	}
	public void raccept() {
		
	}
}
