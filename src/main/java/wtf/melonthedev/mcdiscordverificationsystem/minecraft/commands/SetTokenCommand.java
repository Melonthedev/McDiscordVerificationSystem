package wtf.melonthedev.mcdiscordverificationsystem.minecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.mcdiscordverificationsystem.Main;

public class SetTokenCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Sorry but you don't have the permission to set the " + ChatColor.BOLD + "holy token " + ChatColor.RESET + ChatColor.RED + ". :(");
            return true;
        }
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(ChatColor.RED + "Sorry but only the console can set the token. Don't be sad but the token is something really dangerous. :(");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Syntaxerror: /settoken <String: token>");
            return true;
        }
        Main.getPlugin().getConfig().set("discord.token", args[0]);
        Main.getPlugin().saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Sucess! Reloading the server...");
        Bukkit.reload();
        return false;
    }
}
