package pl.dejwideek.egiaddon.events;

import com.cryptomorin.xseries.XMaterial;
import de.marcely.bedwars.api.arena.Arena;
import de.marcely.bedwars.api.event.arena.RoundEndEvent;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.dejwideek.egiaddon.EGIPlugin;
import pl.dejwideek.egiaddon.color.ColorAPI;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class GameEndEvent implements Listener {

    private static EGIPlugin plugin;

    public GameEndEvent(EGIPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGameEnd(RoundEndEvent e) {
        YamlDocument config = plugin.config;
        ColorAPI colorAPI = new ColorAPI();

        if(config.getBoolean("enabled")) {
            Arena a = e.getArena();
            for(Player p : a.getPlayers()) {
                int contentIndex = 0;
                for(ItemStack content : p.getInventory().getContents()) {
                    p.getInventory().setItem(
                            contentIndex, new ItemStack(
                                    XMaterial.AIR.parseMaterial()));
                    contentIndex++;
                }

                Section section = config.getSection("items");
                for(Object obj : section.getKeys()) {
                    String key = (String) obj;

                    Material material = XMaterial.valueOf(
                                    section.getString(key + ".material")
                                            .toUpperCase())
                            .parseMaterial();
                    byte materialData = section.getByte(
                            key + ".material-data");
                    int amount = section.getInt(key + ".amount");
                    String displayName =
                            colorAPI.process(section
                                    .getString(key + ".displayname"));
                    ArrayList<String> lore = new ArrayList<>();
                    int slot = section.getInt(key + ".slot");

                    for(String s : section.getStringList(
                            key + ".lore")) {
                        lore.add(colorAPI.process(s));
                    }

                    ItemStack item = new ItemStack(material, amount, materialData);
                    ItemMeta meta = item.getItemMeta();

                    meta.setDisplayName(displayName);
                    meta.setLore(lore);
                    item.setItemMeta(meta);

                    p.getInventory().setItem(slot, item);
                }
            }
            for(Player p : a.getSpectators()) {
                for(ItemStack content : p.getInventory().getContents()) {
                    content.setType(XMaterial.AIR.parseMaterial());
                }

                Section section = config.getSection("items");
                for(Object obj : section.getKeys()) {
                    String key = (String) obj;

                    Material material = XMaterial.valueOf(
                                    section.getString(key + ".material")
                                            .toUpperCase())
                            .parseMaterial();
                    byte materialData = section.getByte(
                            key + ".material-data");
                    int amount = section.getInt(key + ".amount");
                    String displayName =
                            colorAPI.process(section
                                    .getString(key + ".displayname"));
                    ArrayList<String> lore = new ArrayList<>();
                    int slot = section.getInt(key + ".slot");

                    for(String s : section.getStringList(
                            key + ".lore")) {
                        lore.add(colorAPI.process(s));
                    }

                    ItemStack item = new ItemStack(material, amount, materialData);
                    ItemMeta meta = item.getItemMeta();

                    meta.setDisplayName(displayName);
                    meta.setLore(lore);
                    item.setItemMeta(meta);

                    p.getInventory().setItem(slot, item);
                }
            }
        }
    }
}
