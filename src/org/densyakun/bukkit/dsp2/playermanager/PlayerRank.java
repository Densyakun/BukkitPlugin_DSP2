package org.densyakun.bukkit.dsp2.playermanager;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum PlayerRank {
	Owner, Admin, Residents, Traveler;
	public Color getColor() {
		switch (this) {
		case Owner:
			return Color.RED;
		case Admin:
			return Color.ORANGE;
		default:
			return Color.WHITE;
		}
	}

	public ChatColor getChatColor() {
		switch (this) {
		case Owner:
			return ChatColor.RED;
		case Admin:
			return ChatColor.GOLD;
		default:
			return ChatColor.WHITE;
		}
	}

	public boolean isAdmin() {
		switch (this) {
		case Owner:
		case Admin:
			return true;
		default:
			return false;
		}
	}

	public static PlayerRank getDefault() {
		return Traveler;
	}
}
