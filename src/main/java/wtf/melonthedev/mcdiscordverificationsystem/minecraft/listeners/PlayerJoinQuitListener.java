package wtf.melonthedev.mcdiscordverificationsystem.minecraft.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import wtf.melonthedev.mcdiscordverificationsystem.Main;
import wtf.melonthedev.mcdiscordverificationsystem.Messages;
import wtf.melonthedev.mcdiscordverificationsystem.VerifyHelper;

import java.util.Objects;
import java.util.logging.Level;

public class PlayerJoinQuitListener implements Listener {

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
            Main.getPlugin().getLogger().log(Level.INFO,
                    event.getPlayer().getName() + "/" +
                    event.getAddress() +
                    " tried to log in.");
            //Bukkit.broadcastMessage(ChatColor.YELLOW + event.getPlayer().getName() + " " + Messages.getMessage("minecraft.triedtojoin"));
            event.disallow( PlayerLoginEvent.Result.KICK_OTHER, ChatColor.GREEN + Messages.getMessage("minecraft.notverified"));
            //event.getPlayer().kickPlayer(ChatColor.GREEN + "You are not verified yet." +
            //        "\nJoin our Discordserver to verify: " +
            //        "\n" + ChatColor.AQUA + "discord.mcsurvivalprojekt.de" + ChatColor.GOLD +
            //        "\nIt just makes sure no bots are joining.");
            return;
        }
        VoiceChannel channel = Main.getPlugin().getBot().getGuild().getVoiceChannelById(Messages.getID("onlinecountervoicechannelid"));
        if (channel == null) return;
        channel.getManager().setName("Online: " + (Bukkit.getOnlinePlayers().size() + 1)).queue();


        Role ingameRole = Main.getPlugin().getBot().getGuild().getRoleById(944217967003070484L);
        if (ingameRole == null) return;
        Member member = Main.getPlugin().getMemberFromMinecraft(event.getPlayer().getUniqueId());
        if (member == null) return;
        Main.getPlugin().getBot().getGuild().addRoleToMember(member, ingameRole).queue();
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        VoiceChannel channel = Main.getPlugin().getBot().getGuild().getVoiceChannelById(Messages.getID("onlinecountervoicechannelid"));
        if (channel == null) return;
        channel.getManager().setName("Online: " + (Bukkit.getOnlinePlayers().size() - 1)).queue();

        Role ingameRole = Main.getPlugin().getBot().getGuild().getRoleById(944217967003070484L);
        if (ingameRole == null) return;
        Member member = Main.getPlugin().getMemberFromMinecraft(event.getPlayer().getUniqueId());
        if (member == null) return;
        Main.getPlugin().getBot().getGuild().removeRoleFromMember(member, ingameRole).queue();
    }

}
