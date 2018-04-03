package fr.cameronjames.logmein;

import fr.cameronjames.logmein.Utils.Account;
import fr.cameronjames.logmein.Utils.FileUtils;
import fr.cameronjames.logmein.Utils.SerializationManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Commands implements CommandExecutor {
    private File saveDir;
    private Plugin plugin;
    public Commands(Plugin plugin) {
        this.plugin = plugin;
        this.saveDir = new File(plugin.getDataFolder(), "/data/");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player){
            Player p = (Player)sender;
            // LOGIN COMMAND
            if(command.getName().equalsIgnoreCase("login")){
                if(args.length == 1){
                    if(!Main.getInstance().loggedIn.contains(p.getUniqueId())) {
                        String password = Main.getInstance().encryptionSystem.cryptWithMD5(args[0]);
                        final File file = new File(saveDir, p.getUniqueId() + ".json");
                        if (file.exists()) {
                            final SerializationManager profileSerializationManager = Main.getInstance().getSerializationManager();
                            final String json = FileUtils.loadContent(file);
                            final Account account = profileSerializationManager.deserialize(json);
                            String actualPassword = account.getPassword();
                            if (password.equalsIgnoreCase(actualPassword)) {
                                Main.getInstance().loggedIn.add(p.getUniqueId());
                                p.sendMessage(Main.getInstance().getConfig().getString("messages.success_login").replace("&", "§").replace("%player%", p.getDisplayName()));
                                return true;
                            } else {
                                p.sendMessage(Main.getInstance().getConfig().getString("messages.password_not_match").replace("&", "§").replace("%player%", p.getDisplayName()));
                                return true;
                            }
                        } else {
                            p.sendMessage(Main.getInstance().getConfig().getString("messages.no_account").replace("&", "§").replace("%player%", p.getDisplayName()));
                            return true;
                        }
                    }else{
                        p.sendMessage(Main.getInstance().getConfig().getString("messages.already_logged").replace("&", "§").replace("%player%", p.getDisplayName()));
                        return true;
                    }
                }else{
                    p.sendMessage(Main.getInstance().getConfig().getString("messages.needpasslogin").replace("&", "§").replace("%player%", p.getDisplayName()));
                    return true;
                }
            }
            // END OF LOGIN COMMAND
            // REGISTER COMMAND
            if(command.getName().equalsIgnoreCase("register")){
                if(args.length == 2){
                    if(args[0].equals(args[1])){
                        if(args[0].length() >= Main.getInstance().getConfig().getInt("password_lenght")){
                            String password = Main.getInstance().encryptionSystem.cryptWithMD5(args[0]);
                            final File file = new File(saveDir, p.getUniqueId() + ".json");
                            if (!file.exists()) {
                                final File dossier = new File(plugin.getDataFolder(), "/data/");
                                if (!dossier.exists()) {
                                    dossier.mkdirs();
                                }
                                final SerializationManager SerializationManager = Main.getInstance().getSerializationManager();
                                final Account profile = Account.createProfile(p, password);
                                final String json = SerializationManager.serialize(profile);
                                FileUtils.saveText(file, json);
                                p.sendMessage(Main.getInstance().getConfig().getString("messages.register_success").replace("&", "§").replace("%player%", p.getDisplayName()));
                                return true;
                            }else{
                                p.sendMessage(Main.getInstance().getConfig().getString("messages.already_registered").replace("&", "§").replace("%player%", p.getDisplayName()));
                                return true;
                            }

                        }else{
                            String lenght = String.valueOf(Main.getInstance().getConfig().getInt("password_lenght"));
                            p.sendMessage(Main.getInstance().getConfig().getString("messages.password_lenght").replace("&", "§").replace("%lenght%", lenght).replace("%player%", p.getDisplayName()));
                            return true;
                        }
                    }else{
                        p.sendMessage(Main.getInstance().getConfig().getString("messages.register_not_match").replace("&", "§").replace("%player%", p.getDisplayName()));
                        return true;
                    }
                }else{
                    p.sendMessage(Main.getInstance().getConfig().getString("messages.register_need_2args").replace("&", "§").replace("%player%", p.getDisplayName()));
                    return true;
                }
            }
            // END OF REGISTER COMMAND
            if(command.getName().equalsIgnoreCase("changepass")){
                if(args.length == 2){
                    String password1 = Main.getInstance().encryptionSystem.cryptWithMD5(args[0]);
                    String password2 = Main.getInstance().encryptionSystem.cryptWithMD5(args[1]);
                    final File file = new File(saveDir, p.getUniqueId() + ".json");
                    if(file.exists()) {
                        final SerializationManager profileSerializationManager = Main.getInstance().getSerializationManager();
                        final String json = FileUtils.loadContent(file);
                        final Account account = profileSerializationManager.deserialize(json);
                        String actualPassword = account.getPassword();
                        if (password1.equalsIgnoreCase(actualPassword)) {
                            if(args[1].length() >= Main.getInstance().getConfig().getInt("password_lenght")) {
                                String newPass = Main.getInstance().getSerializationManager().serialize(new Account(p.getUniqueId(), password2));
                                FileUtils.saveText(file, newPass);
                                p.sendMessage(Main.getInstance().getConfig().getString("messages.success_changepass").replace("&", "§").replace("%player%", p.getDisplayName()));
                                Main.getInstance().loggedIn.remove(p.getUniqueId());
                                return true;
                            }else{
                                String lenght = String.valueOf(Main.getInstance().getConfig().getInt("password_lenght"));
                                p.sendMessage(Main.getInstance().getConfig().getString("messages.password_lenght").replace("&", "§").replace("%lenght%", lenght).replace("%player%", p.getDisplayName()));
                                return true;
                            }
                        } else {
                            p.sendMessage(Main.getInstance().getConfig().getString("messages.password_not_match_change").replace("&", "§").replace("%player%", p.getDisplayName()));
                            return true;
                        }
                    }else{
                        p.sendMessage(Main.getInstance().getConfig().getString("messages.no_account").replace("&", "§").replace("%player%", p.getDisplayName()));
                    }
                }else{
                    p.sendMessage(Main.getInstance().getConfig().getString("messages.needpasschangepass").replace("&", "§").replace("%player%", p.getDisplayName()));
                    return true;
                }
            }

        }
        return false;
    }
}
