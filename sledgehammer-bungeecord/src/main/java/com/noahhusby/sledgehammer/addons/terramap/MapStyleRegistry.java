package com.noahhusby.sledgehammer.addons.terramap;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.P2CMapStylePacket;

/**
 * Handles loading map styles
 * @author SmylerMC
 *
 */
public class MapStyleRegistry {

	public static final String FILENAME = "terramap_user_styles.json";
	
	private static File configMapsFile;
	private static Map<String, P2CMapStylePacket> availableMaps = new HashMap<String, P2CMapStylePacket>();

	/**
	 * @return the map styles that are available
	 */
	public static Map<String, P2CMapStylePacket> getMaps() {
		return availableMaps;
	}
	
	/**
	 * Loads map styles from the config file
	 */
	public static void loadFromConfigFile() {
		if(configMapsFile == null) {
			Sledgehammer.logger.warning("Map config file was null! Did not load.");
			return;
		}
		availableMaps = new HashMap<String, P2CMapStylePacket>();
		if(!configMapsFile.exists()) {
			try {
				Sledgehammer.logger.info("Map config file did not exist, creating a blank one.");
				MapStyleFile mapFile = new MapStyleFile(new MapFileMetadata(0, "Add custom map styles here. See an example at styles.terramap.thesmyler.fr (open in your browser, do not add http or https prefix)"));
				GsonBuilder builder = new GsonBuilder();
				builder.setPrettyPrinting();
				Files.write(builder.create().toJson(mapFile), configMapsFile, Charset.defaultCharset());
			} catch (IOException e) {
				Sledgehammer.logger.warning("Failed to create map style config file! " + e.getLocalizedMessage());
			}
		} else {
			try {
				availableMaps.putAll(loadFromFile(configMapsFile));
			} catch (Exception e) {
				Sledgehammer.logger.warning("Failed to read map style config file! " + e.getLocalizedMessage());
			}
		}
	}
	
	private static P2CMapStylePacket readFromSaved(String id, SavedMapStyle saved, long version, String comment) {
		return new P2CMapStylePacket(
				id,
				version,
				saved.url,
				saved.name,
				saved.copyright,
				saved.min_zoom,
				saved.max_zoom,
				saved.display_priority,
				saved.allow_on_minimap,
				comment
			);
	}
	
	private static Map<String, P2CMapStylePacket> loadFromFile(File file) throws IOException {
		String json = String.join("", java.nio.file.Files.readAllLines(file.toPath()));
		Map<String, P2CMapStylePacket> maps =  loadFromJson(json);
		return maps;
	}

	private static Map<String, P2CMapStylePacket> loadFromJson(String json) {
		Gson gson = new Gson();
		MapStyleFile savedStyles = gson.fromJson(json, MapStyleFile.class);
		Map<String, P2CMapStylePacket> styles = new HashMap<String, P2CMapStylePacket>();
		for(String id: savedStyles.maps.keySet()) {
			P2CMapStylePacket style = readFromSaved(id, savedStyles.maps.get(id), savedStyles.metadata.version, savedStyles.metadata.comment);
			styles.put(id, style);
		}
		return styles;
	}
	
	/**
	 * Set the map styles config file
	 * 
	 * @param file
	 */
	public static void setConfigMapFile(File file) {
		configMapsFile = file;
	}
	
	static class SavedMapStyle {

		String url;
		Map<String, String> name;
		Map<String, String> copyright;
		int min_zoom;
		int max_zoom;
		int display_priority;
		boolean allow_on_minimap;

	}

	static class MapStyleFile {

		Map<String, SavedMapStyle> maps;
		MapFileMetadata metadata;
		
		MapStyleFile(MapFileMetadata metadata) {
			this.metadata = metadata;
			this.maps = new HashMap<String, SavedMapStyle>();
		}
		
		MapStyleFile() {
			this(new MapFileMetadata());
		}

	}

	static class MapFileMetadata {
		
		long version;
		String comment;
		
		MapFileMetadata(long version, String comment) {
			this.comment = comment;
			this.version = version;
		}
		
		MapFileMetadata() {
			this(0, "");
		}

	}
}
