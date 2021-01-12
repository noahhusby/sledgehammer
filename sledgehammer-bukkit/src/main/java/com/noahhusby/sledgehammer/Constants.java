/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - Constants.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer;

public class Constants
{
    public static final String MODID   = "sledgehammer";
    public static final String VERSION = "0.4.2";
    public static final double SCALE = 7318261.522857145;
    public static final int scanHeight = 1024;

    public static final String responsePrefix = "<";
    public static final String commandID = "command";
    public static final String locationID = "location";
    public static final String setwarpID = "warp_position";
    public static final String testLocationID = "test_location";
    public static final String teleportID = "teleport";
    public static final String warpGUIID = "warp_gui";
    public static final String initID = "init";
    public static final String webmapID = "webmap";
    public static final String warpID = "warp";
    public static final String warpConfigID = "warp_config";
    public static final String playerUpdateID = "player_update";
    public static final String permissionCheckID = "permission_check";

    public static final int warpsPerPage = 27;
    public static final String arrowRightHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTU2YTM2MTg0NTllNDNiMjg3YjIyYjdlMjM1ZWM2OTk1OTQ1NDZjNmZjZDZkYzg0YmZjYTRjZjMwYWI5MzExIn19fQ==";
    public static final String arrowLeftHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2RjOWU0ZGNmYTQyMjFhMWZhZGMxYjViMmIxMWQ4YmVlYjU3ODc5YWYxYzQyMzYyMTQyYmFlMWVkZDUifX19";
    public static final String globeHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOThkYWExZTNlZDk0ZmYzZTMzZTFkNGM2ZTQzZjAyNGM0N2Q3OGE1N2JhNGQzOGU3NWU3YzkyNjQxMDYifX19";
    public static final String lampHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzViNTFjYzJlOTlkMDhkZDI4NzlhNzkyZjA2MmUwNzc4MzJhMDE2M2YzZDg1YzI0NGUwYmExYzM5MmFiMDlkZSJ9fX0=";
    public static final String monitorHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzA4MThhODRlNWU4ODkwOTU0YWM2Y2JlOTA5OGI4NWExNTk0NDRkNGQ4MmVhYzkwNjYzNTc4YmRkYTU0MCJ9fX0=";
    public static final String netherPortalHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjBiZmMyNTc3ZjZlMjZjNmM2ZjczNjVjMmM0MDc2YmNjZWU2NTMxMjQ5ODkzODJjZTkzYmNhNGZjOWUzOWIifX19";
    public static final String oakBlankHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRiNTMyYjVjY2VkNDZiNGI1MzVlY2UxNmVjZWQ3YmJjNWNhYzU1NTk0ZDYxZThiOGY4ZWFjNDI5OWM5ZmMifX19";
    public static final String goldenBlankHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzUxMTM3ZTExNDQzYThmYmIwNWZjZDNjY2MxYWY5YmQyMzAzOTE4ZjM1NDQ4MTg1ZTNlZDk2ZWYxODRkYSJ9fX0=";
    public static final String limePlusHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19";
    public static final String goldenExclamationHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjRkNjdhM2I0ZmJiNjcxZDg0NWE5Yzg0MmU5ZTg2MDM4YWNlZTE3Yjk1ZTZjYWNiMmIwMmNiOWMifX19";
    public static final String limeCheckmarkHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkyZTMxZmZiNTljOTBhYjA4ZmM5ZGMxZmUyNjgwMjAzNWEzYTQ3YzQyZmVlNjM0MjNiY2RiNDI2MmVjYjliNiJ9fX0=";
    public static final String redCheckmarkHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmY5ZDlkZTYyZWNhZTliNzk4NTU1ZmQyM2U4Y2EzNWUyNjA1MjkxOTM5YzE4NjJmZTc5MDY2Njk4Yzk1MDhhNyJ9fX0=";
    public static final String cyanWoolHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZhZGY3NDFhYjc2Y2QzNjIwYWQxNjEzMDAyMDJkN2I1OWEzMzA1MWU1OTY3ZTRiNjE5NGJhYzQwYmIyODBmZiJ9fX0=";
    public static final String yellowWoolHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQ5MDUyNjlhY2NhYjI0YjExOTI0ZWJhOGJkOTI5OTFiOGQ4NWNlNDI3NjAyN2ExNjM2YzkzMWI2ZDA2Yzg5ZSJ9fX0=";
    public static final String redExclamationMark = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTdmOWM2ZmVmMmFkOTZiM2E1NDY1NjQyYmE5NTQ2NzFiZTFjNDU0M2UyZTI1ZTU2YWVmMGE0N2Q1ZjFmIn19fQ==";
    public static final String pocketPortalHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7ImlkIjoiMTkwM2NhNWE3MjgzNDExODk5NjMwYTY5OTM3MTY3NmMiLCJ0eXBlIjoiU0tJTiIsInVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM5MTVkYjNmYzQwYTc5YjYzYzJjNDUzZjBjNDkwOTgxZTUyMjdjNTAyNzUwMTI4MzI3MjEzODUzM2RlYTUxOSIsInByb2ZpbGVJZCI6IjgwMThhYjAwYjJhZTQ0Y2FhYzliZjYwZWY5MGY0NWU1IiwidGV4dHVyZUlkIjoiMmM5MTVkYjNmYzQwYTc5YjYzYzJjNDUzZjBjNDkwOTgxZTUyMjdjNTAyNzUwMTI4MzI3MjEzODUzM2RlYTUxOSJ9fSwic2tpbiI6eyJpZCI6IjE5MDNjYTVhNzI4MzQxMTg5OTYzMGE2OTkzNzE2NzZjIiwidHlwZSI6IlNLSU4iLCJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzJjOTE1ZGIzZmM0MGE3OWI2M2MyYzQ1M2YwYzQ5MDk4MWU1MjI3YzUwMjc1MDEyODMyNzIxMzg1MzNkZWE1MTkiLCJwcm9maWxlSWQiOiI4MDE4YWIwMGIyYWU0NGNhYWM5YmY2MGVmOTBmNDVlNSIsInRleHR1cmVJZCI6IjJjOTE1ZGIzZmM0MGE3OWI2M2MyYzQ1M2YwYzQ5MDk4MWU1MjI3YzUwMjc1MDEyODMyNzIxMzg1MzNkZWE1MTkifSwiY2FwZSI6bnVsbH0=";
    public static final String steveHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmUyNTRjN2FlNTBmMzk5ODljZDYzYmQ1OWI1OTY2NDMzYmUxYmE4ZjE3NDUwNDAxMDkxMzI5MTVlNjRlMjcyZSJ9fX0=";
    public static final String purpleExclamationMark = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVkNGM2NDVlYjQyYmVlNDE1NjU4ZTE2NjE4ODllNjIyNjhiNWNiM2YxZTFkYTMzYWM2Y2MxZTAwYTcwOTBmZiJ9fX0=";
    public static final String redTrashCanHead = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ2NWY4MGJmMDJiNDA4ODg1OTg3YjAwOTU3Y2E1ZTllYjg3NGMzZmE4ODMwNTA5OTU5N2EzMzNhMzM2ZWUxNSJ9fX0=";
}
