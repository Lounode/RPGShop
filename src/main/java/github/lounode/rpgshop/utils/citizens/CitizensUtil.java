package github.lounode.rpgshop.utils.citizens;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.ClickRedirectTrait;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * comments:<br>
 * 封装一些 Citizens 函数
 *
 * @author Lounode
 * @date 2024/08/07
 */
public class CitizensUtil {
    /**
     * comments:<br>
     * 获取NPC绑定的商店
     *
     * @param npc npc
     * @return ShopID
     * @author Lounode
     * @date 2024/08/07
     */
    public static String GetShopID(NPC npc) {
        RPGShopTrait trait = npc.getTraitNullable(RPGShopTrait.class);
        if (trait != null) {
            return trait.getShopID();
        }
        return null;
    }

    /**
     * comments:<br>
     * 获取一个玩家面对着的NPC
     *
     * @param player player
     * @return {@link NPC }
     * @author Lounode
     * @date 2024/08/07
     */
    public static NPC GetNPCForward(Player player) {
        Location location = player.getLocation();
        List<NPC> search = location.getWorld().getNearbyEntities(location, 10 ,10 ,10).stream().map((e) ->{
            return CitizensAPI.getNPCRegistry().getNPC(e);
        }).filter((e) -> {
            return e != null;
        }).collect(Collectors.toList());

        search.sort(Comparator.comparingDouble(o -> o.getEntity().getLocation().distanceSquared(location)));

        Iterator var11 = search.iterator();
        if (var11.hasNext()) {
            NPC test = (NPC)var11.next();
            if (test.hasTrait(ClickRedirectTrait.class)) {
                test = ((ClickRedirectTrait)test.getTraitNullable(ClickRedirectTrait.class)).getRedirectNPC();
            }

            return test;
        }
        return null;
    }
}
