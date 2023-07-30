package pl.dejwideek.egiaddon;

import co.aikar.commands.PaperCommandManager;
import de.marcely.bedwars.api.BedwarsAddon;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import pl.dejwideek.egiaddon.commands.ReloadCmd;
import pl.dejwideek.egiaddon.events.GameEndEvent;
import pl.dejwideek.egiaddon.events.ItemInteractEvent;

@SuppressWarnings("ALL")
public class EGIAddon extends BedwarsAddon {

    private static EGIPlugin plugin;

    public EGIAddon(EGIPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public void registerCommands() {
        PaperCommandManager manager =
                new PaperCommandManager(plugin);

        manager.registerCommand(new ReloadCmd(plugin));
    }

    public void registerEvents() {
        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new GameEndEvent(plugin), plugin);
        manager.registerEvents(new ItemInteractEvent(plugin), plugin);
    }
}
