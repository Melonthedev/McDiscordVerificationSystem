package wtf.melonthedev.mcdiscordverificationsystem.minecraft.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.mcdiscordverificationsystem.Main;
import wtf.melonthedev.mcdiscordverificationsystem.Messages;
import wtf.melonthedev.mcdiscordverificationsystem.VerifyHelper;

import java.util.UUID;

public class VerifyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Sorry but you don't have the permission to " + ChatColor.BOLD + "verify" + ChatColor.RESET + ChatColor.RED + " players. :(");
            return true;
        }
        if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + Messages.getMessage("minecraft.error.syntax") + " /verify <Player> <DiscordName> <DiscordID>");
            sender.sendMessage(ChatColor.ITALIC + "" + ChatColor.GRAY + "(You get the DiscordID if you enable Developer Mode in Discord and right click on a user and select 'Copy ID')");
            return true;
        }
        String id = args[2];
        long idL;
        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException ex) {
            sender.sendMessage(ChatColor.RED + "NumberFormatException: The provided Discord ID isn't a number (Long)");
            return true;
        }
        UUID uuid = VerifyHelper.getUUIDFromName(args[0]);
        if (uuid == null) {
            sender.sendMessage(ChatColor.RED + Messages.getMessage("minecraft.invalidminecraftaccount"));
            return true;
        }
        sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "[Verification] " + ChatColor.RESET + ChatColor.GREEN + Messages.getMessage("minecraft.verifyplayersuccess") + " '" + args[0] + "'.");
        Main.getPlugin().verifyUser(args[0], uuid, args[1], idL);
        return false;
    }


}
