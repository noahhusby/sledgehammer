package com.noahhusby.sledgehammer.addons.terramap;

import java.util.List;

import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;

/**
 * Manages player display preferences
 * 
 * @author SmylerMC
 *
 */
public final class PlayerDisplayPreferences {

	private final static String SHOW_ATTRIBUTE = "TerraShow";
	private final static String HIDE_ATTRIBUTE = "TerraHide";
	
	/**
	 * Indicates if the player should be visible on the map to others
	 * 
	 * @param player - the player
	 * @return a boolean
	 */
	public static boolean shouldDisplayPlayer(SledgehammerPlayer player) {
		return !player.checkAttribute("TERRA_HIDE", false);
	}

	/**
	 * Set whether or not a player should be visible on the map to others
	 * 
	 * @param player - the player
	 * @param yesNo
	 */
	public static void setShouldDisplayPlayer(SledgehammerPlayer player, boolean yesNo) {
		player.getAttributes().put("TERRA_HIDE", !yesNo);
	}

}