package com.noahhusby.sledgehammer.addons.terramap.commands;

import java.util.HashMap;
import java.util.Map;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.addons.terramap.TerramapVersion;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Handles Terramap's translations.
 * The idea is to create a context specific to a client or console when needed.
 * Texts in this context will either been sent in plain English if the client does not have the mod installed,
 * or sent as translation key and parameters if the client has the capability to translate it by itself.
 * 
 * @author SmylerMC
 *
 */
public class TranslationContextBuilder {
	
	private Map<String, String> texts = new HashMap<>();
	private TerramapVersion minTranslationVersion = null;
	
	/**
	 * Adds a translation key => value pair to this context builder.
	 * 
	 * @param key - The key
	 * @param text - The English text
	 * @return this
	 */
	public TranslationContextBuilder addTranslation(String key, String text) {
		this.texts.put(key, text);
		return this;
	}
	
	/**
	 * Clients with a version newer or equal to this one will be sent the translation key instead of the translated text.
	 * 
	 * @param version - minimum client version that supports translation in this context. null if none do.
	 */
	public TranslationContextBuilder setMinimumVersion(TerramapVersion version) {
		this.minTranslationVersion = version;
		return this;
	}
	
	/**
	 * Creates a translation context for the given client using this builder's settings.
	 * 
	 * @param sender
	 * @return a {@link TranslationContext} for the given client
	 */
	public TranslationContext createContext(CommandSender sender) {
		return new TranslationContext(sender);
	}
	
	public class TranslationContext {
		
		private final ProxiedPlayer remotePlayer;
		private final TerramapVersion remoteVersion;
		private final boolean remoteSupportsTranslation;
		
		private TranslationContext(CommandSender remote) {
			if(remote instanceof ProxiedPlayer) {
				this.remotePlayer  = (ProxiedPlayer)remote;
				this.remoteVersion = TerramapVersion.getClientVersion(this.remotePlayer);
			} else {
				this.remoteVersion = null;
				this.remotePlayer = null;
			}
			if(TranslationContextBuilder.this.minTranslationVersion == null) {
				this.remoteSupportsTranslation = false;
			} else {
				this.remoteSupportsTranslation = TranslationContextBuilder.this.minTranslationVersion.isOlderOrSame(this.remoteVersion);
			}
		}
		
		/**
		 * @return whether or not the client supports translation in this context
		 */
		public boolean doesSupportTranslation() {
			return this.remoteSupportsTranslation;
		}
		
		/**
		 * Indicates whether or not the client supports formatting.
		 * A player supports text formatting, the proxy's console does not.
		 * 
		 * @return whether or not the client supports text formatting codes
		 */
		public boolean doesSupportFormatting() {
			return this.remotePlayer != null; //i.e. not console
		}
		
		/**
		 * Indicate whether or not the given key would be translated by this context.
		 * It will only be so if this context has translated text for this key,
		 * and the client cannot translate it by itself.
		 * 
		 * @param key - The key to translate
		 * @return whether or not the given key will be translated
		 */
		public boolean willTranslateHere(String key) {
			return !this.remoteSupportsTranslation && TranslationContextBuilder.this.texts.containsKey(key);
		}
		
		/**
		 * Gets the unformatted text for this key.
		 * 
		 * @param key - The key to translate
		 * @return a translated but unformatted text if possible, the key otherwise.
		 */
		public String getString(String key) {
			if(this.willTranslateHere(key)) return TranslationContextBuilder.this.texts.get(key);
			return key;
		}
		
		/**
		 * Gets a text component for this key and format parameters.
		 * This component will either be a {@link TranslatableComponent} or a translated {@link TextComponent}},
		 * depending the client capability to do translation by itself.
		 * 
		 * @param key - The key to translate
		 * @param objects - format parameters
		 * @return a {@link BaseComponent} for the given text, translated depending on the context
		 */
		public BaseComponent getBaseTextComponent(String key, Object... objects) {
			if(!this.willTranslateHere(key)) return new TranslatableComponent(key, objects);
			return new TextComponent(String.format(TranslationContextBuilder.this.texts.getOrDefault(key, key), objects));
		}
		
		/**
		 * Send an error message to the client.
		 * It will be a red chat message for a player, and a {@link java.util.logging.Logger#warning(String)} message for the console.
		 * 
		 * @param key - The translation key
		 * @param objects - The formatting parameters
		 */
		public void sendError(String key, Object... objects) {
			BaseComponent base = this.getBaseTextComponent(key, objects);
			if(this.remotePlayer != null) {
				base.setColor(ChatColor.RED);
				this.remotePlayer.sendMessage(base);
			} else {
				String text = this.getString(key);
				if(this.willTranslateHere(key)) text = String.format(text, objects);
				Sledgehammer.logger.warning(text);
			}
		}
		
		/**
		 * Send a message to the client.
		 * It will be a plain white chat message for a player, and a {@link java.util.logging.Logger#info(String)} message for the console.
		 * 
		 * @param key - The translation key
		 * @param objects - The formatting parameters
		 */
		public void sendRawMessage(String key, Object... objects) {
			BaseComponent base = this.getBaseTextComponent(key, objects);
			if(this.remotePlayer != null) {
				this.remotePlayer.sendMessage(base);
			} else {
				String text = this.getString(key);
				if(this.willTranslateHere(key)) text = String.format(text, objects);
				Sledgehammer.logger.info(text);
			}
		}
		
	}

}
