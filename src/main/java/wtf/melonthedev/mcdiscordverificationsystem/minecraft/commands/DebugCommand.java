package wtf.melonthedev.mcdiscordverificationsystem.minecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.mcdiscordverificationsystem.Main;

import java.util.Objects;

public class DebugCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return true;
        if (args.length < 1) return true;
        switch (args[0]) {
            case "setguildname":
                Main.getPlugin().getConfig().set("discord.guildname", "Survivalprojekt");
                break;
            case "setactivity":
                Main.getPlugin().getConfig().set("discord.activity", "mcsurvivalprojekt.de");
                break;
            case "ctest":
                System.out.println(Objects.requireNonNull(Main.getPlugin().getConfig().getConfigurationSection("whitelist")).getValues(true));
                break;
        }
        Main.getPlugin().saveConfig();
        return false;
    }
}
