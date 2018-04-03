package fr.cameronjames.logmein.Utils;

import fr.cameronjames.logmein.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class PlayerJoin implements Listener {

    private File saveDir;
    private Plugin plugin;


    public PlayerJoin(Plugin plugin) {
        this.plugin = plugin;
        this.saveDir = new File(plugin.getDataFolder(), "/data/");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final File file = new File(saveDir, p.getUniqueId() + ".json");
        if (!Main.getInstance().loggedIn.contains(p.getUniqueId())) {
            if (file.exists()) {
                    p.sendMessage(Main.getInstance().getConfig().getString("messages.login").replace("&", "§").replace("%player%", p.getDisplayName()));

            } else {

                p.sendMessage(Main.getInstance().getConfig().getString("messages.register").replace("&", "§").replace("%player%", p.getDisplayName()));

            }
        }

    }
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (!Main.getInstance().loggedIn.contains(p.getUniqueId())) {
            p.teleport(p);
        }
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        final Player p = e.getPlayer();
        Main.getInstance().loggedIn.remove(p.getUniqueId());
    }
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if(Main.getInstance().getConfig().getBoolean("can_talk") == false) {
            Player p = event.getPlayer();
            final File file = new File(saveDir, p.getUniqueId() + ".json");
            if (!Main.getInstance().loggedIn.contains(p.getUniqueId())) {
                event.setCancelled(true);
                if (file.exists()) {
                    p.sendMessage(Main.getInstance().getConfig().getString("messages.cant_talk_login"));
                } else {
                    p.sendMessage(Main.getInstance().getConfig().getString("messages.cant_talk_register"));
                }
            }
        }

    }
    @EventHandler
    public void onCommands(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if (!Main.getInstance().loggedIn.contains(p.getUniqueId())) {
            if ((e.getMessage().startsWith("/login")) || (e.getMessage().startsWith("/register"))) {
                return;
            }
            e.setCancelled(true);
            p.sendMessage(Main.getInstance().getConfig().getString("messages.commands_disabled").replace("&", "§"));
        }
    }



}
