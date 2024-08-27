package github.lounode.rpgshop.gui;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Button {
    @Getter
    private ItemStack skin;
    public Button (ItemStack skin) {
        this.skin = skin;
    }
    public Button () {
        ItemStack defaultSkin = new ItemStack(Material.BARRIER);
        ItemMeta meta = defaultSkin.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&4&lButton Skin ERROR"));
        defaultSkin.setItemMeta(meta);
        this.skin = defaultSkin;
    }
    @FunctionalInterface
    public interface ButtonClickCallback {
        boolean execute(ButtonClickEvent event);
    }
    private List<ButtonClickCallback> onClick = new ArrayList<>();
    public void click (ButtonClickEvent event) {
        for (ButtonClickCallback callback : onClick) {
            callback.execute(event);
        }
    }
    public boolean addClickListener(ButtonClickCallback listener) {
        return onClick.add(listener);
    }

}

