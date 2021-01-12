/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - SledgehammerUtil.java
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

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SledgehammerUtil {
    public static Player getPlayerFromName(String name) {
        return Bukkit.getServer().getPlayer(name);
    }

    public static boolean compare(String a, String b) {
        if(a == null || b == null) return false;
        return a.trim().toLowerCase().equals(b.trim().toLowerCase());
    }

    public static ItemStack getSkull(String h, String n) {
        try {
            ItemStack head = SkullCreator.itemFromBase64(h);
            ItemMeta meta = head.getItemMeta();
            meta.setDisplayName(n);
            head.setItemMeta(meta);
            return head;
        } catch (StringIndexOutOfBoundsException | NullPointerException e){
            return getSkull(Constants.steveHead, n);
        }
    }

    public static boolean isPlayerAvailable(String p) {
        return isPlayerAvailable(getPlayerFromName(p));
    }

    public static boolean isPlayerAvailable(Player p) {
        if(p == null) return false;
        return p.isOnline();
    }

    public static class NumberHeads {
        public static ItemStack getHead(int val, String display) {
            String id = "";
            switch (val) {
                default:
                case 1:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E1MTZmYmFlMTYwNThmMjUxYWVmOWE2OGQzMDc4NTQ5ZjQ4ZjZkNWI2ODNmMTljZjVhMTc0NTIxN2Q3MmNjIn19fQ==";
                    break;
                case 2:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDY5OGFkZDM5Y2Y5ZTRlYTkyZDQyZmFkZWZkZWMzYmU4YTdkYWZhMTFmYjM1OWRlNzUyZTlmNTRhZWNlZGM5YSJ9fX0=";
                    break;
                case 3:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQ5ZTRjZDVlMWI5ZjNjOGQ2Y2E1YTFiZjQ1ZDg2ZWRkMWQ1MWU1MzVkYmY4NTVmZTlkMmY1ZDRjZmZjZDIifX19";
                    break;
                case 4:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJhM2Q1Mzg5ODE0MWM1OGQ1YWNiY2ZjODc0NjlhODdkNDhjNWMxZmM4MmZiNGU3MmY3MDE1YTM2NDgwNTgifX19";
                    break;
                case 5:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFmZTM2YzQxMDQyNDdjODdlYmZkMzU4YWU2Y2E3ODA5YjYxYWZmZDYyNDVmYTk4NDA2OTI3NWQxY2JhNzYzIn19fQ==";
                    break;
                case 6:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2FiNGRhMjM1OGI3YjBlODk4MGQwM2JkYjY0Mzk5ZWZiNDQxODc2M2FhZjg5YWZiMDQzNDUzNTYzN2YwYTEifX19";
                    break;
                case 7:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjk3NzEyYmEzMjQ5NmM5ZTgyYjIwY2M3ZDE2ZTE2OGIwMzViNmY4OWYzZGYwMTQzMjRlNGQ3YzM2NWRiM2ZiIn19fQ==";
                    break;
                case 8:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWJjMGZkYTlmYTFkOTg0N2EzYjE0NjQ1NGFkNjczN2FkMWJlNDhiZGFhOTQzMjQ0MjZlY2EwOTE4NTEyZCJ9fX0=";
                    break;
                case 9:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZhYmM2MWRjYWVmYmQ1MmQ5Njg5YzA2OTdjMjRjN2VjNGJjMWFmYjU2YjhiMzc1NWU2MTU0YjI0YTVkOGJhIn19fQ==";
                    break;
                case 10:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2FmM2ZkNDczYTY0OGI4NDdjY2RhMWQyMDc0NDc5YmI3NjcyNzcxZGM0MzUyMjM0NjhlZDlmZjdiNzZjYjMifX19";
                    break;
                case 11:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDhjYWI1M2IwMjA5OGU2ODFhNDZkMWQ3ZjVmZjY5MTc0NmFkZjRlMWZiM2FmZTM1MTZkZDJhZjk0NDU2OSJ9fX0=";
                    break;
                case 12:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmZkODNiNWJhYWU0Y2I4NTY5NGExNGQ2ZDEzMzQxZWY3MWFhM2Q5MmQzN2RlMDdiZWE3N2IyYzlkYzUzZSJ9fX0=";
                    break;
                case 13:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjFlNTk4NWJlNDg4NmY5ZjE2ZTI0NDdjM2Y0NjEwNTNiNDUxMzQyZDRmYjAxNjZmYjJmODhkZjc0MjIxMzZiNCJ9fX0=";
                    break;
                case 14:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTY4MTQ1NjQzOGFlOWIyZDRkMmJmYWI5Y2YzZmZhOTM1NGVlYmRiM2YwMmNlMjk1NzkyOTM0OGU1Yjg1ZmY5NSJ9fX0=";
                    break;
                case 15:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzE5YzRkYjczNjViMWI4OGIxMjllNzA0MTg0MjEzZmUwNzhkODhiYzNkNGFlM2Q1MjI5MGY2MWQ5NTVkNTEifX19";
                    break;
                case 16:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY1ZGQwNzliOThmZGFjNDNhMTlhNzk1YmE0NmZkOTdmMjNlYTc3NTdkOTJhZDBhNjlhZGM5NzMyODllNWEifX19";
                    break;
                case 17:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmU1MWJmZThlYmZhYTU4NWE3ODdlMWNiNzcyYzdmZDdkOWE5Mjg2ZDk1ZWZhNTRkNjZmYTgyNzRmMTg4ZiJ9fX0=";
                    break;
                case 18:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjZkZTlhNWEyZDhhMjM3MDcwMTliOWVmNjFkMTY2Mjg2MGUwYjE2NTNkZjZjMjc2MTZiZTJjNzZmY2QxODc1In19fQ==";
                    break;
                case 19:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJkNDU5MDRkMzRiNjM2YjJmNjQyNjFiM2Q4YmNlZDI1ODI4YzJiOGM0ODIzYjdlMTgzZWU4YTZmMWEyODRkIn19fQ==";
                    break;
                case 20:
                    id = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzRiOTNjNzIxNTE5ZTE0OTY0OWI3ZTRhZmI2ZDc2Y2ZjODE0NjA4YWU5Yzk1ZTdjM2RiNGJmNGJkYWFjZjMxZSJ9fX0=";
                    break;
            }
            ItemStack skull = SkullCreator.itemFromBase64(id);
            ItemMeta meta = skull.getItemMeta();
            meta.setDisplayName(display);
            skull.setItemMeta(meta);
            return skull;
        }
    }

    public static class JsonUtils {
        public static JSONObject toObject(String s) {
            try {
                return (JSONObject) new JSONParser().parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static JSONArray toArray(Object o) {
            return toArray((String) o);
        }

        public static JSONArray toArray(String s) {
            try {
                return (JSONArray) new JSONParser().parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static int fromBoolean(boolean b) {
            return b ? 1 : 0;
        }

        public static boolean fromBooleanValue(long l) {
            return new Long(l).intValue() != 0;
        }

        public static int toInt(Object val) {
            int x = 0;
            if(val instanceof Long) {
                x = ((Long) val).intValue();
            } else {
                x = (int) val;
            }
            return x;
        }
    }
}
