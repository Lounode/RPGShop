package github.lounode.rpgshop.shop;

import com.google.common.base.Strings;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Shop implements Cloneable, ConfigurationSerializable {
    private String id;
    private String name;
    private int row;
    private String createTime;
    private String owner;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(String lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public String getLastEditor() {
        return lastEditor;
    }

    public void setLastEditor(String lastEditor) {
        this.lastEditor = lastEditor;
    }

    private String lastEditTime;
    private String lastEditor;
    private Inventory lastInv;
    private List<Trade> trades = new ArrayList<>();
    public Shop(String id, int row, String title) {
        this.id = id;
        this.name = title;
        this.row = row;
    }
    public boolean addTrade(Trade trade) {
        trade.setShop(this);
        int slot = trade.getSaleSlot();
        for (Trade t : trades) {
            if (t.getSaleSlot() == slot) {
                trades.set(trades.indexOf(t), trade);
                return true;
            }
        }
        return trades.add(trade);
    }


    public String getSourceTitle() {
        return name;
    }
    public void setTitle(String title) {
        this.name = title;
    }
    public String getTitle() {
        return ChatColor.translateAlternateColorCodes('&', name);
    }
    public int getSize() {
        return row * 9;
    }
    public int getRows() {
        return row;
    }
    public List<Trade> getTrades() {
        return trades;
    }
    public Trade getTrade(int slot) {
        for (Trade trade : getTrades()) {
            if (trade.getSaleSlot() == slot) {
                return trade;
            }
        }
        return null;
    }
    public NPC getBoundNPC() {
        int boundID = 0;
        NPC npc = CitizensAPI.getNPCRegistry().getById(boundID);
        return npc;
    }

    public String getID() {
        return id;
    }


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("id" , getID());
        result.put("title", getSourceTitle());
        result.put("row", getRows());

        result.put("owner", getOwner());
        result.put("create_time", getCreateTime());
        result.put("last_editor", getLastEditor());
        result.put("last_edit_time", getLastEditTime());

        result.put("trades", getTrades());

        return result;
    }

    public static Shop deserialize(Map<String, Object> args) {
        String title = (String) args.get("title");
        int row = (int) args.get("row");
        String id = (String) args.get("id");

        Shop shop = new Shop(id,row, title);

        String owner = null;
        if (args.containsKey("owner")) {
            owner = Strings.isNullOrEmpty((String) args.get("owner")) ? null : (String) args.get("owner");
        }
        shop.setOwner(owner);
        String createTime = null;
        if (args.containsKey("create_time")) {
            createTime = Strings.isNullOrEmpty((String) args.get("create_time")) ? null: (String) args.get("create_time");
        }
        shop.setCreateTime(createTime);
        String lastEditor = null;
        if (args.containsKey("last_editor")) {
            lastEditor = Strings.isNullOrEmpty((String) args.get("last_editor")) ? null : (String) args.get("last_editor");
        }
        shop.setLastEditor(lastEditor);
        String lastEditTime = null;
        if (args.containsKey("last_edit_time")) {
            lastEditTime = Strings.isNullOrEmpty((String) args.get("last_edit_time")) ? null : (String) args.get("last_edit_time");
        }
        shop.setLastEditTime(lastEditTime);

        if (args.containsKey("trades")) {
            Object raw = args.get("trades");
            //Check is 'Trade' List
            if (raw instanceof List && !((List)raw).isEmpty() && ((List)raw).get(0) instanceof Trade) {
                List<Trade> trades = (List<Trade>) args.get("trades");

                for (Trade trade : trades) {
                    shop.addTrade(trade);
                }
            }
        }



        return shop;
    }
    @Override
    public Shop clone() {
        try {
            Shop shop = (Shop) super.clone();

            shop.id = this.id;
            shop.name = this.name;
            shop.row = this.row;
            shop.createTime  = this.createTime;
            shop.owner = this.owner;
            shop.lastInv = this.lastInv;

            List<Trade> newTrades = new ArrayList<>();
            for(Trade trade : trades) {
                Trade newTrade = trade.clone();
                newTrade.setShop(shop);
                newTrades.add(newTrade);

            }
            shop.trades = newTrades;

            return shop;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

}
