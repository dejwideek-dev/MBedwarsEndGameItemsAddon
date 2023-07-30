package pl.dejwideek.egiaddon.events;

import com.cryptomorin.xseries.XMaterial;
import de.marcely.bedwars.api.BedwarsAPI;
import de.marcely.bedwars.api.GameAPI;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.arena.ArenaStatus;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.dejwideek.egiaddon.EGIPlugin;
import pl.dejwideek.egiaddon.color.ColorAPI;

@SuppressWarnings("ALL")
public class ItemInteractEvent implements Listener {

    private static EGIPlugin plugin;

    public ItemInteractEvent(EGIPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        BedwarsAPI.onReady(() -> {
            Arena a = GameAPI.get().getArenaByPlayer(p);
            if(a == null) a = GameAPI.get().getArenaBySpectator(p);
            YamlDocument config = plugin.config;
            Section section = config.getSection("items");
            ColorAPI colorAPI = new ColorAPI();
            ItemStack itemInHand = p.getItemInHand();

            if(config.getBoolean("enabled")) {
                if(a != null) {
                    if(!a.getStatus().equals(ArenaStatus.END_LOBBY)) return;

                    for(Object obj : section.getKeys()) {
                        String key = (String) obj;

                        Material material = XMaterial.valueOf(section.getString(
                                key + ".material").toUpperCase()).parseMaterial();
                        byte materialData = section.getByte(key + ".material-data");
                        int amount = section.getInt(key + ".amount");
                        String displayName = colorAPI.process(section
                                .getString(key + ".displayname"));

                        if(itemInHand.getType().equals(material)
                                && itemInHand.getData().getData() == materialData
                                && itemInHand.getAmount() == amount
                                && itemInHand.getItemMeta()
                                .getDisplayName().equals(displayName)) {
                            String handler = section.getString(key + ".handler");

                            if(section.getBoolean(key + ".use-handler")) {
                                a.addSpectator(p);
                                GameAPI.get().getSpectatorItemHandler(handler).handleUse(
                                        GameAPI.get().getSpectatorByPlayer(p), null);
                            }
                            if(section.getBoolean(key + ".use-commands")) {
                                for(String s : section
                                        .getStringList(key + ".commands")) {
                                    Bukkit.dispatchCommand(p, s.replaceAll(
                                            "<player>", p.getName()));
                                }
                            }
                            return;
                        }
                    }
                }
                else return;
            }
            return;
        });
    }
}
