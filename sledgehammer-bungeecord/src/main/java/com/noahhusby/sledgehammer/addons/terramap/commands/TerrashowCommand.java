package com.noahhusby.sledgehammer.addons.terramap.commands;

import java.util.ArrayList;
import java.util.List;

import com.noahhusby.sledgehammer.addons.terramap.PlayerDisplayPreferences;
import com.noahhusby.sledgehammer.addons.terramap.TerramapAddon;
import com.noahhusby.sledgehammer.addons.terramap.TerramapVersion;
import com.noahhusby.sledgehammer.addons.terramap.TerramapVersion.ReleaseType;
import com.noahhusby.sledgehammer.addons.terramap.commands.TranslationContextBuilder.TranslationContext;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.players.PlayerManager;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.TabExecutor;

/**
 * Implements the /terrashow command,
 * that lets player control whether or not they should visible to others on the map.
 * This command is critical for privacy reasons.
 * 
 * @author SmylerMC
 *
 */
public class TerrashowCommand extends Command implements TabExecutor {

	public static final String CMD_NAME = "terrashow";
	public static final String USAGE = "/terrashow <show|hide|status> [playername (optional)]";

	private final TranslationContextBuilder translation;

	public TerrashowCommand() {
		super(CMD_NAME, TerramapAddon.TERRASHOW_BASE_PERMISSION_NODE);
		this.translation = new TranslationContextBuilder()
				.setMinimumVersion(new TerramapVersion(1, 0, 0, ReleaseType.BETA, 6, 3))
				.addTranslation("terramap.commands.terrashow.usage", "/terrashow <show|hide|status> [player name (optional)]")
				.addTranslation("terramap.commands.terrashow.too_few_parameters", "Too few parameters: /terrashow <show|hide|status> [player name (optional)]")
				.addTranslation("terramap.commands.terrashow.too_many_parameters", "Too many parameters: /terrashow <show|hide|status> [player name (optional)]")
				.addTranslation("terramap.commands.terrashow.console_player_name", "Player name is required when executing from the server console: /terrashow <show|hide|status> [player name (optional)]")
				.addTranslation("terramap.commands.terrashow.cannot_change_own_visibility", "You do not have the permission to change your visibility")
				.addTranslation("terramap.commands.terrashow.cannot_change_others_visibility", "You do not have the permission to change others' visibility")
				.addTranslation("terramap.commands.terrashow.noplayer", "Player does not exist: /terrashow <show|hide|status> [player name (optional)]")
				.addTranslation("terramap.commands.terrashow.invalid_action", "Invalid action: /terrashow <show|hide|status> [player name (optional)]")
				.addTranslation("terramap.commands.terrashow.getvisible", "%1$s is currently visible on the map")
				.addTranslation("terramap.commands.terrashow.setvisible", "%1$s is now visible on the map")
				.addTranslation("terramap.commands.terrashow.gethidden", "%1$s is currently hidden on the map")
				.addTranslation("terramap.commands.terrashow.sethidden", "%1$s is now hidden on the map");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {

		TranslationContext context = this.translation.createContext(sender);

		ProxiedPlayer player = null;
		ProxiedPlayer senderPlayer = sender instanceof ProxiedPlayer? (ProxiedPlayer) sender: null;

		if(args.length <= 0) {
			context.sendError("terramap.commands.terrashow.too_few_parameters");
			return;
		}

		if(args.length > 2) {
			context.sendError("terramap.commands.terrashow.too_many_parameters");
			return;
		}

		if(args.length == 2) {
			player = PlayerManager.getInstance().getPlayer(args[1]);
		} else if(sender instanceof ProxiedPlayer) {
			player = (ProxiedPlayer)sender;
		} else {
			context.sendError("terramap.commands.terrashow.console_player_name");
			return;
		}

		if(player != null && player.equals(senderPlayer)) {
			if(senderPlayer != null && !this.canPlayerHideSelf(senderPlayer)) {
				context.sendError("terramap.commands.terrashow.cannot_change_own_visibility");
				return;
			}
		} else {
			if(senderPlayer != null && !this.canPlayerHideOthers(senderPlayer)) {
				context.sendError("terramap.commands.terrashow.cannot_change_others_visibility");
				return;
			}
		}

		if(player == null) {
			context.sendError("terramap.commands.terrashow.noplayer");
			return;
		}

		SledgehammerPlayer shPlayer = PlayerManager.getInstance().getPlayer(player);
		switch(args[0]) {
		case "status":
			String key = PlayerDisplayPreferences.shouldDisplayPlayer(shPlayer) ? "terramap.commands.terrashow.getvisible": "terramap.commands.terrashow.gethidden";
			if(context.doesSupportFormatting()) {
				BaseComponent message = ChatHelper.makeTitleTextComponent(new TextElement("", ChatColor.WHITE));
				message.addExtra(context.getBaseTextComponent(key, player.getDisplayName()));
				senderPlayer.sendMessage(message);
			} else {
				context.sendRawMessage(key, player.getDisplayName());
			}
			break;
		case "show":
			PlayerDisplayPreferences.setShouldDisplayPlayer(shPlayer, true);
			key = "terramap.commands.terrashow.setvisible";
			if(context.doesSupportFormatting()) {
				BaseComponent message = ChatHelper.makeTitleTextComponent(new TextElement("", ChatColor.WHITE));
				message.addExtra(context.getBaseTextComponent(key, player.getDisplayName()));
				senderPlayer.sendMessage(message);
			} else {
				context.sendRawMessage(key, player.getDisplayName());
			}
			break;
		case "hide":
			PlayerDisplayPreferences.setShouldDisplayPlayer(shPlayer, false);
			key = "terramap.commands.terrashow.sethidden";
			if(context.doesSupportFormatting()) {
				BaseComponent message = ChatHelper.makeTitleTextComponent(new TextElement("", ChatColor.WHITE));
				message.addExtra(context.getBaseTextComponent(key, player.getDisplayName()));
				senderPlayer.sendMessage(message);
			} else {
				context.sendRawMessage(key, player.getDisplayName());
			}
			break;
		default:
			context.sendError("terramap.commands.terrashow.invalid_action");
			return;
		}

	}
	
	private boolean canPlayerHideOthers(ProxiedPlayer player) {
		return player.hasPermission(TerramapAddon.TERRASHOW_OTHERS_PERMISSION_NODE) || PermissionHandler.getInstance().isAdmin(player);
	}
	
	private boolean canPlayerHideSelf(ProxiedPlayer player) {
		return player.hasPermission(TerramapAddon.TERRASHOW_SELF_PERMISSION_NODE) || PermissionHandler.getInstance().isAdmin(player);
	}

	@Override
	public Iterable<String> onTabComplete(CommandSender arg0, String[] args) {
		if(args.length == 1) {
			List<String> l = new ArrayList<>();
			l.add("show");
			l.add("hide");
			l.add("status");
			this.filterListWithMatching(l, args[0]);
			return l;
		} else if(args.length == 2){
			List<String> l = new ArrayList<>();
			for(SledgehammerPlayer p: PlayerManager.getInstance().getPlayers()) l.add(p.getName());
			this.filterListWithMatching(l, args[1]);
			return l;
		}
		return new ArrayList<String>();
	}
	
	private void filterListWithMatching(List<String> list, String needle) {
		for(int i=0; i<list.size();) {
			String str = list.get(i);
			if(str == null || !str.startsWith(needle)) list.remove(i);
			else i++;
		}
	}

}
