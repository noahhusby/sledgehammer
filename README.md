# Sledgehammer
A set of tools for multi-server Build the Earth networks

### Documentation has been moved to the [wiki](https://github.com/noahhusby/Sledgehammer/wiki)

[![sledgehammer-bungeecord Actions Status](https://github.com/noahhusby/sledgehammer/workflows/sledgehammer-bungeecord/badge.svg)](https://github.com/noahhusby/sledgehammer/actions)
[![sledgehammer-bukkit Actions Status](https://github.com/noahhusby/sledgehammer/workflows/sledgehammer-bukkit/badge.svg)](https://github.com/noahhusby/sledgehammer/actions)

## Features
* Region Assigning - /tpll will automatically teleport between servers based upon region configuration
* Global Warps - Set warps and teleport across the network
* Warp Map - Online map vizualizer with warps. Check out [Sledgehammer Map](https://github.com/noahhusby/Sledgehammer-Map/releases)
* Border Teleportation (WIP) - Teleport automatically between servers when passing through region borders

#### Future Plans
* Border Duplication - Duplicate buildings on border for a seamless teleportation experience

## Installation
Go to the [releases](https://github.com/noahhusby/Sledgehammer/releases) page and download the latest bungeecord plugin and bukkit plugin.

## Building
### Bungeecord
* Clone this repo, or download as a zip
* Open the `Sledgehammer [Bungeecord]` in your preferred IDE
* Build using: Maven Package

### Bukkit
* Clone this repo, or download as a zip
* Open the `Sledgehammer [Bukkit]` folder in IntelliJ
* Import it as a gradle project
* Run `./gradlew build` to build
