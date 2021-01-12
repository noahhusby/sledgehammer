package com.noahhusby.sledgehammer.gui.inventories.warp.config.manage;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.chat.ChatHandler;
import com.noahhusby.sledgehammer.data.location.Point;
import com.noahhusby.sledgehammer.data.warp.Warp;
import com.noahhusby.sledgehammer.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIChild;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIRegistry;
import com.noahhusby.sledgehammer.gui.inventories.warp.config.ManageGroupInventoryController;
import com.noahhusby.sledgehammer.gui.inventories.warp.config.confirmation.ConfirmationController;
import com.noahhusby.sledgehammer.network.S2P.S2PWarpConfigPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageWarpInventory extends GUIChild {

    private final WarpConfigPayload payload;
    private final Warp cur;

    public ManageWarpInventory(WarpConfigPayload payload, Warp cur) {
        this.payload = payload;
        this.cur = cur;
    }

    @Override
    public void init() {
        for(int x = 0; x < 27; x++) {
            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);

            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(ChatColor.RESET+"");
            meta.setDisplayName(null);
            glass.setItemMeta(meta);

            inventory.setItem(x, glass);
        }

        {
            String headId = cur.getHeadID();
            if(headId.equals("")) headId = Constants.cyanWoolHead;
            ItemStack item = SledgehammerUtil.getSkull(headId, ChatColor.BLUE
                    + "" + ChatColor.BOLD + cur.getName());

            ItemMeta meta = item.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.DARK_GRAY + "Server: " + cur.getServer());
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.GRAY + "ID: " + cur.getId());
            meta.setLore(lore);
            item.setItemMeta(meta);

            inventory.setItem(4, item);
        }

        inventory.setItem(18, SledgehammerUtil.getSkull(Constants.arrowLeftHead, ChatColor.RED + ""
            + ChatColor.BOLD + "Back"));
        inventory.setItem(26, SledgehammerUtil.getSkull(Constants.limeCheckmarkHead, ChatColor.GREEN + "" + ChatColor.BOLD + "Save"));

        {
            ItemStack item = new ItemStack(Material.NAME_TAG, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Change Name");
            item.setItemMeta(meta);
            inventory.setItem(11, item);
        }

        inventory.setItem(12, SledgehammerUtil.getSkull(Constants.pocketPortalHead, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Move Location"));

        {
            byte pinnedColor = 7;
            String title = ChatColor.WHITE + "" + ChatColor.BOLD + "Not Pinned";

            if(payload.isLocal()) {
                switch (cur.getPinnedMode()) {
                    case LOCAL:
                        pinnedColor = 4;
                        title = ChatColor.GOLD + "" + ChatColor.BOLD + "Pinned Locally";
                        break;
                    case GLOBAL:
                        pinnedColor = 14;
                        title = ChatColor.RED + "" + ChatColor.BOLD + "Pinned Globally";
                        break;
                }
            } else {
                if(cur.getPinnedMode() == Warp.PinnedMode.LOCAL || cur.getPinnedMode() == Warp.PinnedMode.GLOBAL) {
                    pinnedColor = 4;
                    title = ChatColor.GOLD + "" + ChatColor.BOLD + "Pinned";
                }
            }

            ItemStack item = new ItemStack(Material.WOOL, 1, pinnedColor);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(title);

            List<String> lore = new ArrayList<>();
            if(payload.isLocal() && !payload.isAdmin() && cur.getPinnedMode() == Warp.PinnedMode.GLOBAL) {
                lore.add(ChatColor.RED + "You don't have permission to change the mode!");
            } else {
                lore.add(ChatColor.GREEN + "Click to change!");
            }

            meta.setLore(lore);

            item.setItemMeta(meta);
            inventory.setItem(13, item);
        }

        inventory.setItem(14, SledgehammerUtil.getSkull(Constants.steveHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Change Head"));
        inventory.setItem(15, SledgehammerUtil.getSkull(Constants.redTrashCanHead, ChatColor.RED + "" + ChatColor.BOLD + "Delete Warp"));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getCurrentItem() == null) return;
        if(e.getCurrentItem().getItemMeta() == null) return;
        if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;
        if(e.getSlot() == 18) {
            GUIRegistry.register(new ManageGroupInventoryController(getPlayer(), payload));
            return;
        }

        if(e.getSlot() == 11) {
            GUIRegistry.register(new ChangeNameController(getPlayer(), payload, cur));
            return;
        }

        if(e.getSlot() == 12) {
            JSONObject data = new JSONObject();
            data.put("warpId", cur.getId());
            Point point = new Point(String.valueOf(player.getLocation().getX()),
                    String.valueOf(player.getLocation().getY()),
                    String.valueOf(player.getLocation().getZ()),
                    String.valueOf(player.getLocation().getPitch()),
                    String.valueOf(player.getLocation().getYaw()));
            data.put("point", point.getJSON());

            SledgehammerNetworkManager.getInstance().send(new S2PWarpConfigPacket(
                    S2PWarpConfigPacket.ProxyConfigAction.WARP_UPDATE_LOCATION,
                    getPlayer(), payload.getSalt(), data));
        }

        if(e.getSlot() == 13) {
            if(e.getCurrentItem().getDurability() == 7) {
                cur.setPinnedMode(Warp.PinnedMode.LOCAL);
                GUIRegistry.register(new ManageWarpInventoryController(getController(), payload, cur));
            } else if(e.getCurrentItem().getDurability() == 4) {
                if(payload.isLocal() && payload.isAdmin()) {
                    cur.setPinnedMode(Warp.PinnedMode.GLOBAL);
                    GUIRegistry.register(new ManageWarpInventoryController(getController(), payload, cur));
                } else if(!payload.isLocal()) {
                    cur.setPinnedMode(Warp.PinnedMode.NONE);
                    GUIRegistry.register(new ManageWarpInventoryController(getController(), payload, cur));
                }
            } else if(e.getCurrentItem().getDurability() == 14) {
                if(payload.isAdmin()) {
                    cur.setPinnedMode(Warp.PinnedMode.NONE);
                    GUIRegistry.register(new ManageWarpInventoryController(getController(), payload, cur));
                }
            }
        }

        if(e.getSlot() == 14) {
            getController().close();
            ChatHandler.getInstance().startEntry(getPlayer(), ChatColor.BLUE + "Enter the Base64 head code from " +
                    ChatColor.GOLD + "minecraft-heads.com", (success, text) -> {
                        if(success) {
                            cur.setHeadID(text);
                        }
                GUIRegistry.register(new ManageWarpInventoryController(player, payload, cur));
            });
        }

        if(e.getSlot() == 15) {
            JSONObject data = new JSONObject();
            data.put("warpId", cur.getId());

            SledgehammerNetworkManager.getInstance().send(new S2PWarpConfigPacket(
                    S2PWarpConfigPacket.ProxyConfigAction.REMOVE_WARP,
                    getPlayer(), payload.getSalt(), data));
        }

        if(e.getSlot() == 26) {
            SledgehammerNetworkManager.getInstance().send(
                    new S2PWarpConfigPacket(S2PWarpConfigPacket.ProxyConfigAction.UPDATE_WARP, getPlayer(),
                            ((ManageWarpInventoryController) controller).getPayload().getSalt(), cur.toJson()));
            GUIRegistry.register(new ConfirmationController(getPlayer(), payload, ConfirmationController.Type.HEAD_UPDATE));
        }
    }


}
