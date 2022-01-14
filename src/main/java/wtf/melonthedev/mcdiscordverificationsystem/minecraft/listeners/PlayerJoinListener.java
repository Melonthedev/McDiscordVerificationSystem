package wtf.melonthedev.mcdiscordverificationsystem.minecraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import wtf.melonthedev.mcdiscordverificationsystem.Main;
import wtf.melonthedev.mcdiscordverificationsystem.Messages;
import wtf.melonthedev.mcdiscordverificationsystem.VerifyHelper;

import java.util.Objects;
import java.util.logging.Level;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        String name = event.getPlayer().getName().toLowerCase();
        if (Main.pendingVerifications.containsKey(name)) {
            Main.getPlugin().verifyUser(name, event.getPlayer().getUniqueId(), Main.pendingVerifications.get(name).getUser().getName(), Main.pendingVerifications.get(name).getIdLong());
            VerifyHelper.sendVerificationSuccessMessage(Main.pendingVerifications.get(name));
            Main.pendingVerifications.remove(name);
            return;
        }
        if (!Main.getPlugin().isVerified(event.getPlayer().getUniqueId())) {
            Main.getPlugin().getLogger().log(Level.INFO, event.getPlayer().getName() + "/" + Objects.requireNonNull(event.getPlayer().getAddress()).getAddress().getHostAddress() +
                    " tried to log in.");
            //Bukkit.broadcastMessage(ChatColor.YELLOW + event.getPlayer().getName() + " " + Messages.getMessage("minecraft.triedtojoin"));
            event.getPlayer().kickPlayer(ChatColor.GREEN + Messages.getMessage("minecraft.notverified"));
            //event.getPlayer().kickPlayer(ChatColor.GREEN + "You are not verified yet." +
            //        "\nJoin our Discordserver to verify: " +
            //        "\n" + ChatColor.AQUA + "discord.mcsurvivalprojekt.de" + ChatColor.GOLD +
            //        "\nIt just makes sure no bots are joining.");
        }
    }

}
