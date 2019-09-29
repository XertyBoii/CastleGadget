import castle.commands.ItemCommand;
import castle.listeners.InteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        setupCommands();
        setupListeners();
    }

    @Override
    public void onDisable() {

    }

    private void setupCommands() {
        getCommand("gadget").setExecutor(new ItemCommand());
    }

    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new InteractEvent(this), this);
    }
}
