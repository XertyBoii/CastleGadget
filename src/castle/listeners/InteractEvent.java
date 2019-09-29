package castle.listeners;

import castle.Runnables.SpawnCastle;
import castle.items.GadgetItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class InteractEvent implements Listener {

    private Plugin plugin;

    public InteractEvent(Plugin p) {
        this.plugin = p;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        ItemStack itemInHand = e.getPlayer().getItemInHand();

        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if (itemInHand != null) {
            if (itemInHand.hasItemMeta()) {
                if (itemInHand.getItemMeta().equals(GadgetItem.gadgetItem().getItemMeta()))
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("Spawning castle at your location");
                    new SpawnCastle(e.getClickedBlock().getLocation(), this.plugin)
                            .runTaskTimer(this.plugin, 20, 2);
                    return;
             }
        }
    }
}
