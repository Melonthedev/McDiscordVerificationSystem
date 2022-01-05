package wtf.melonthedev.mcdiscordverificationsystem;

import net.dv8tion.jda.api.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.melonthedev.mcdiscordverificationsystem.discord.DCBot;
import wtf.melonthedev.mcdiscordverificationsystem.minecraft.commands.DebugCommand;
import wtf.melonthedev.mcdiscordverificationsystem.minecraft.commands.SetTokenCommand;
import wtf.melonthedev.mcdiscordverificationsystem.minecraft.commands.UnverifyCommand;
import wtf.melonthedev.mcdiscordverificationsystem.minecraft.commands.VerifyCommand;
import wtf.melonthedev.mcdiscordverificationsystem.minecraft.listeners.PlayerJoinListener;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    private static Main plugin;
    private static boolean botActive = false;
    public static HashMap<String, Member> pendingVerifications = new HashMap<>();
    FileConfiguration config = getConfig();
    DCBot bot;
    String token;
    String guildName;
    String playing;


    @Override
    public void onEnable() {
        //init Stuff
        plugin = this;
        this.saveDefaultConfig();
        //this.saveConfig();
        initConfig();
        Logger logger = getLogger();
        //Discord
        if (token == null || token.equals("insert bot token here")) {
            logger.log(Level.SEVERE, "NO TOKEN PROVIDED! Set the token in 'config.yml' and restart the server or use the /settoken command.");
            logger.log(Level.WARNING, "! Continuing without bot !");
        } else {
            bot = new DCBot();
            bot.initialize(token);
        }
        //MC
        Objects.requireNonNull(this.getCommand("verify")).setExecutor(new VerifyCommand());
        Objects.requireNonNull(this.getCommand("unverify")).setExecutor(new UnverifyCommand());
        Objects.requireNonNull(this.getCommand("settoken")).setExecutor(new SetTokenCommand());
        Objects.requireNonNull(this.getCommand("debug")).setExecutor(new DebugCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        if (isBotActive()) bot.exit();
    }

    public void initConfig() {
        token = config.getString("discord.token");
        guildName = config.getString("discord.guildname");
        playing = config.getString("discord.activity");
    }

    public void verifyUser(String minecraftName, UUID minecraftUUID, String discordName, Long discordID) {
        addPlayerToVerifiedList(minecraftName, minecraftUUID, discordName, discordID);
    }

    public void addPlayerToVerifiedList(String minecraftName, UUID minecraftUUID, String discordName, Long discordID) {
        config.set("whitelist." + minecraftUUID + ".mcname", minecraftName);
        config.set("whitelist." + minecraftUUID + ".dcname", discordName);
        config.set("whitelist." + minecraftUUID + ".dcID", discordID);
        saveConfig();
    }

    public boolean isVerified(UUID uuid) {
        return config.contains("whitelist." + uuid.toString());
    }

    public static Main getPlugin() {
        return plugin;
    }

    public static boolean isBotActive() {
        return botActive;
    }

    public static void setBotActive(boolean botActive) {
        Main.botActive = botActive;
    }

    public String getGuildName() {
        return guildName;
    }

    public String getPlaying() {
        return playing;
    }

    public DCBot getBot() {
        return bot;
    }
}
