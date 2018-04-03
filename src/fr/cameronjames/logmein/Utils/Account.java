package fr.cameronjames.logmein.Utils;

import org.bukkit.entity.Player;


import java.util.UUID;

public class Account {

    private UUID uuid;
    private String password;

    public Account(UUID uuid, String password) {
        this.uuid = uuid;
        this.password = password;
    }

    public UUID getUuid() {return uuid;}
    public String getPassword() {return password;}

    public static Account createProfile(Player p, String password) {
        return new Account(p.getUniqueId(), password);
    }

}
