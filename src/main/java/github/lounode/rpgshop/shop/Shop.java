package github.lounode.rpgshop.shop;

import github.lounode.rpgshop.utils.Formater;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
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
    public String getTitle() {
        return Formater.FormatMessage(name);
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

        result.put("trades", getTrades());

        return result;
    }

    public static Shop deserialize(Map<String, Object> args) {
        String title = (String) args.get("title");
        int row = (int) args.get("row");
        String id = (String) args.get("id");

        Shop shop = new Shop(id,row, title);

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
