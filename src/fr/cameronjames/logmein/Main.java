package fr.cameronjames.logmein;

import fr.cameronjames.logmein.Utils.EncryptionSystem;
import fr.cameronjames.logmein.Utils.PlayerJoin;
import fr.cameronjames.logmein.Utils.SerializationManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static Main instance;
    private SerializationManager serializationManager;
    public ArrayList<UUID> loggedIn = new ArrayList<>();
    public EncryptionSystem encryptionSystem;
    File saveDir = new File(this.getDataFolder(), "/data/");
    ConsoleCommandSender console = Bukkit.getConsoleSender();
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.serializationManager = new SerializationManager();
        console.sendMessage(ChatColor.GREEN + "[LogMeIn] Plugin created by CameronJamesT8. Thanks for download :)");
        console.sendMessage(ChatColor.GREEN + "[LogMeIn] The plugin have been loaded with success !");
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        getCommand("login").setExecutor(new Commands(this));
        getCommand("register").setExecutor(new Commands(this));
        getCommand("changepass").setExecutor(new Commands(this));
        onReload();

    }

    public void onReload(){


            for (Player p : Bukkit.getOnlinePlayers()) {

                File file = new File(saveDir, p.getUniqueId().toString() + ".json");
                if (file.exists()) {

                        p.sendMessage(Main.getInstance().getConfig().getString("messages.login").replace("&", "§").replace("%player%", p.getDisplayName()));

                } else {

                        p.sendMessage(Main.getInstance().getConfig().getString("messages.register").replace("&", "§").replace("%player%", p.getDisplayName()));

                }

        }
    }


    @Override
    public void onDisable() {

    }
    public static Main getInstance() {
        return instance;
    }
    public SerializationManager getSerializationManager() {
        return serializationManager;
    }
}
