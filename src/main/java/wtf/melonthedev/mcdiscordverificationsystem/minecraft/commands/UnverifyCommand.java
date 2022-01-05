package wtf.melonthedev.mcdiscordverificationsystem.minecraft.commands;

import net.dv8tion.jda.api.entities.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.mcdiscordverificationsystem.Main;
import wtf.melonthedev.mcdiscordverificationsystem.Messages;
import wtf.melonthedev.mcdiscordverificationsystem.VerifyHelper;

import java.util.UUID;

public class UnverifyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Sorry but you don't have the permission to " + ChatColor.BOLD + "unverify" + ChatColor.RESET + ChatColor.RED + " players. :(");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + Messages.getMessage("minecraft.error.syntax") + " /unverify <Player>");
            return true;
        }
        UUID uuid = VerifyHelper.getUUIDFromName(args[0]);
        if (uuid == null) {
            sender.sendMessage(ChatColor.RED + Messages.getMessage("minecraft.error.invalidplayername"));
            return true;
        }
        String dcId = Main.getPlugin().getConfig().getString("whitelist." + uuid + ".dcID");
        if (dcId != null) {
            Role unverifiedRole = Main.getPlugin().getBot().getGuild().getRoleById(804262397044064257L);
            if (unverifiedRole != null)
                Main.getPlugin().getBot().getGuild().addRoleToMember(dcId, unverifiedRole).queue();
        }
        Main.getPlugin().getConfig().set("whitelist." + uuid, null);
        Main.getPlugin().saveConfig();
        sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "[Verification] " + ChatColor.RESET + ChatColor.GREEN + Messages.getMessage("minecraft.unverifyplayersuccess") + " '" + args[0] + "'.");
        Player player = Bukkit.getPlayer(args[0]);
        if (player != null) player.kickPlayer(ChatColor.RED + "You were unverified.\nYou can verify again by following the steps you get after rejoining.");
        return false;
    }
}
