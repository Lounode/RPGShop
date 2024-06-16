package github.lounode.rpgshop.utils;

import com.sun.istack.internal.NotNull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SetLore {
    public ItemStack setItemLore(@NotNull ItemStack item, @NotNull List<BaseComponent> lore) {
        final net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        NBTTagList loreList = new NBTTagList();
        for (BaseComponent component : lore) {
            //loreList.add(NBTTagString.a(BaseComponent.toPlainText(component)));
            //loreList.add(BaseComponent.);
        }
        TranslatableComponent comp = new TranslatableComponent(nmsItem.getItem().getName());
        tag.set("Lore", loreList);
        nmsItem.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}
