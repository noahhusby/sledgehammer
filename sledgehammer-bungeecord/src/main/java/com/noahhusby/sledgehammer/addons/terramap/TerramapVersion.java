package com.noahhusby.sledgehammer.addons.terramap;

import java.util.Arrays;
import java.util.Map;

import com.noahhusby.sledgehammer.Sledgehammer;

import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Represents a version of Terramap.
 * The format for Terramap version number are as follows:</br>
 * [marjor target].[minor target].[build target](-[release type][release build](.[release revision]))(-dev)</br>
 * major target, minor target, build target, release build, and release revision are integers. </br>
 * Release type is one of: alpha, beta, rc or is absent. </br>
 * Parts in parentheses can be omitted (will assume 0 for numbers and normal release for release type.
 * -dev is only present on dev builds that are not available to the public.
 * In all places this class would be use, a null version means Terramap is not installed
 * 
 * A ${version} version means the mod is running in a development environment.
 * 
 * This file was originally written for Terramap.
 * 
 * @author SmylerMC
 *
 */
public class TerramapVersion implements Comparable<TerramapVersion> {

	public final int majorTarget;
	public final int minorTarget;
	public final int buildTarget;

	public final ReleaseType releaseType;

	public final int build;
	public final int revision;

	public final boolean devBuild;
	public final boolean devRun;

	/**
	 * Creates a new version object
	 * 
	 * @param majorTarget
	 * @param minorTarget
	 * @param buildTarget
	 * @param releaseType
	 * @param build
	 * @param revision
	 * @param devBuild
	 */
	public TerramapVersion(
			int majorTarget,
			int minorTarget,
			int buildTarget,
			ReleaseType releaseType,
			int build,
			int revision,
			boolean devBuild) {
		super();
		this.majorTarget = majorTarget;
		this.minorTarget = minorTarget;
		this.buildTarget = buildTarget;
		this.releaseType = releaseType;
		this.build = build;
		this.revision = revision;
		this.devBuild = devBuild;
		this.devRun = false;
	}
	
	/**
	 * 
	 * @param majorTarget
	 * @param minorTarget
	 * @param buildTarget
	 * @param type
	 * @param build
	 * @param revision
	 */
	public TerramapVersion(int majorTarget, int minorTarget, int buildTarget, ReleaseType type, int build, int revision) {
		this(majorTarget, minorTarget, buildTarget, type, build, revision, false);
	}
	
	public TerramapVersion(int majorTarget, int minorTarget, int buildTarget, ReleaseType type, int build) {
		this(majorTarget, minorTarget, buildTarget, type, build, 0, false);
	}
	
	public TerramapVersion(int majorTarget, int minorTarget, int buildTarget) {
		this(majorTarget, minorTarget, buildTarget, ReleaseType.RELEASE, 0, 0, false);
	}

	/**
	 * Creates a version object by parsing a version String
	 * 
	 * @param versionString - version to parse
	 * @throws InvalidVersionString if the given string is not a valid version number.
	 * See other constructors to safely create version numbers if you don't need to do it by parsing a version String.
	 */
	public TerramapVersion(String versionString) throws InvalidVersionString {
		if("${version}".equals(versionString)) {
			this.majorTarget = this.minorTarget = this.buildTarget = this.build = this.revision = 0;
			this.devBuild = false;
			this.devRun = true;
			this.releaseType = ReleaseType.DEV;
		} else {
			String[] parts = versionString.split("-");
			if(parts.length > 3) {
				throw new InvalidVersionString("Invalid version string " + versionString);
			}
			if(parts.length > 0) {
				if("dev".equals(parts[parts.length - 1])) {
					this.devBuild = true;
					parts = (String[]) Arrays.copyOfRange(parts, 0, parts.length - 1);
				} else {
					this.devBuild = false;
				}
				String[] target = parts[0].split("\\.");
				if(target.length != 3) throw new InvalidVersionString("Invalid target version " + parts[0]);
				devRun = false;
				try {
					this.majorTarget = Integer.parseInt(target[0]);
				} catch(NumberFormatException e) {
					throw new InvalidVersionString("Invalid target major version: " + target[0]);
				}
				try {
					this.minorTarget = Integer.parseInt(target[1]);
				} catch(NumberFormatException e) {
					throw new InvalidVersionString("Invalid target minor version: " + target[1]);
				}
				try {
					this.buildTarget = Integer.parseInt(target[2]);
				} catch(NumberFormatException e) {
					throw new InvalidVersionString("Invalid target build version: " + target[2]);
				}
				if(parts.length > 1) {
					for(ReleaseType type: ReleaseType.values()) {
						if(type.equals(ReleaseType.RELEASE)) continue;
						if(parts[1].startsWith(type.name)) {
							this.releaseType = type;
							parts[1] = parts[1].substring(type.name.length());
							String[] build = parts[1].split("\\.");
							if(build.length > 0) {
								try {
									this.build = Integer.parseInt(build[0]);
								} catch(NumberFormatException e) {
									throw new InvalidVersionString("Invalid build version: " + build[0]);
								}
								if(build.length > 1) {
									try {
										this.revision = Integer.parseInt(build[1]);
									} catch(NumberFormatException e) {
										throw new InvalidVersionString("Invalid revision version: " + build[1]);
									}
								} else {
									this.revision = 0;
								}
							} else {
								throw new InvalidVersionString("Invalid target version number: " + parts[1]);
							}
							return;
						}
					}
					throw new InvalidVersionString("Invalid target release type: " + parts[1]);
				} else {
					this.releaseType = ReleaseType.RELEASE;
					this.build = this.minorTarget;
					this.revision = this.buildTarget;
				}
			} else {
				throw new InvalidVersionString("Empty version string");
			}
		}
	}
	
	/**
	 * Returns a version String for this version
	 */
	@Override
	public String toString() {
		if(this.isDev()) {
			return "${version}";
		}
		String str = "";
		str += this.majorTarget;
		str += "." + this.minorTarget;
		str += "." + this.buildTarget;
		if(!this.releaseType.equals(ReleaseType.RELEASE)) {
			str += "-" + this.releaseType.name;
			str += this.build;
			if(this.revision != 0) {
				str += "." + this.revision;
			}
		}
		if(this.devBuild) {
			str += "-dev";
		}
		return str;
	}
	
	/**
	 * 
	 * @return true if this version is running in a dev environment (i.e? it does not have a version number)
	 */
	public boolean isDev() {
		return this.devRun || this.releaseType.equals(ReleaseType.DEV);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + build;
		result = prime * result + buildTarget;
		result = prime * result + (devBuild ? 1231 : 1237);
		result = prime * result + (devRun ? 1231 : 1237);
		result = prime * result + majorTarget;
		result = prime * result + minorTarget;
		result = prime * result + ((releaseType == null) ? 0 : releaseType.hashCode());
		result = prime * result + revision;
		return result;
	}

	/**
	 * True if the other object is a version equal to this one
	 */
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(!(other instanceof TerramapVersion)) return false;
		return this.compareTo((TerramapVersion) other) == 0;
	}

	/**
	 * Returns a positive integer if this version is newer than the other,
	 * 0 if they are the same and a negative number otherwise.
	 */
	@Override
	public int compareTo(TerramapVersion other) {
		
		if(other == null) {
			// Null means not installed, so we are always ahead
			return Integer.MAX_VALUE;
		}
		
		if(this.isDev()) {
			if(other.isDev()) return 0;
			else return Integer.MAX_VALUE;
		}
		
		int majorComp = this.majorTarget - other.majorTarget;
		if(majorComp != 0) return majorComp;
		
		int minorComp = this.minorTarget - other.minorTarget;
		if(minorComp != 0) return minorComp;
		
		int buildComp = this.buildTarget - other.buildTarget;
		if(buildComp != 0) return buildComp;
		
		int typeComp = this.releaseType.priority - this.releaseType.priority;
		if(typeComp != 0) return typeComp;
		
		int rComp = this.build - other.build;
		if(rComp != 0) return rComp;
		
		return this.revision - other.revision;
		
	}
	
	/**
	 * Compares this version with another version object
	 * 
	 * @param other version, can be null to specify that Terramap is not installed. Null will always be the oldest.
	 * 
	 * @return true if this version is newer or equal to the other, false otherwise
	 */
	public boolean isNewerOrSame(TerramapVersion other) {
		return this.compareTo(other) >= 0;
	}
	
	/**
	 * Compares this version with another version object
	 * 
	 * @param other version, can be null to specify that Terramap is not installed. Null will always be the oldest.
	 * 
	 * @return true if this version is strictly newer than the other, false otherwise
	 */
	public boolean isNewer(TerramapVersion other) {
		return this.compareTo(other) > 0;
	}
	
	/**
	 * Compares this version with another version object
	 * 
	 * @param other version, can be null to specify that Terramap is not installed. Null will always be the oldest.
	 * 
	 * @return true if this version is older or equal to the other, false otherwise
	 */
	public boolean isOlderOrSame(TerramapVersion other) {
		return this.compareTo(other) <= 0;
	}
	
	/**
	 * Compares this version with another version object
	 * 
	 * @param other version, can be null to specify that Terramap is not installed. Null will always be the oldest.
	 * 
	 * @return true if this version is strictly older than the other, false otherwise
	 */
	public boolean isOlder(TerramapVersion other) {
		return this.compareTo(other) < 0;
	}
	
	/**
	 * Get's the given player's Terramap version.
	 * The player needs to have logged onto a Forge server at least once for this to work.
	 * If it's not the case then this will return null, even though that may be wrong.
	 * 
	 * @return the player Terramap version, or null if they don't have it
	 */
	public static TerramapVersion getClientVersion(ProxiedPlayer player) {
		Map<String, String> modList = player.getModList();
		TerramapVersion version = null;
		String remoteVersion = modList.get(TerramapAddon.TERRAMAP_MODID);
		if(remoteVersion != null) {
			try {
				version = new TerramapVersion(remoteVersion);
			} catch(InvalidVersionString e) {
				Sledgehammer.logger.warning("Failed to parse a client's Terramap version: " + remoteVersion + " : " + e.getLocalizedMessage());
			}
		}
		return version;
	}

	/**
	 * Different types of Terramap releases
	 * 
	 * @author SmylerMC
	 *
	 */
	public static enum ReleaseType {

		RELEASE("", 4),
		RELEASE_CANDIDATE("rc", 3),
		BETA("beta", 2),
		ALPHA("alpha", 1),
		DEV("dev", 0); // Running from dev environment, no actual version number

		public final String name;
		public final int priority;

		private ReleaseType(String name, int priority) {
			this.name = name;
			this.priority = priority;
		}

	}

	public class InvalidVersionString extends Exception {

		private static final long serialVersionUID = 1L;

		public InvalidVersionString(String message) {
			super(message);
		}

	}

}
