package castle.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GadgetItem {

    public static ItemStack gadgetItem() {
        ItemStack gadget = new ItemStack(Material.LEVER);
        ItemMeta gadgetMeta = gadget.getItemMeta();

        gadgetMeta.setDisplayName(
                ChatColor.translateAlternateColorCodes(
                        '&', "&b&lCastle Gadget &8(right-click)"
                )
        );

        gadget.setItemMeta(gadgetMeta);

        return gadget;
    }
}
