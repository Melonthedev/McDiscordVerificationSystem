package wtf.melonthedev.mcdiscordverificationsystem.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import wtf.melonthedev.mcdiscordverificationsystem.Main;
import wtf.melonthedev.mcdiscordverificationsystem.discord.listeners.ButtonListener;
import wtf.melonthedev.mcdiscordverificationsystem.discord.listeners.ReadyListener;
import wtf.melonthedev.mcdiscordverificationsystem.discord.listeners.SlashCommandListener;

import javax.security.auth.login.LoginException;
import java.util.logging.Level;

public class DCBot {

    JDA jda;
    Guild guild;

    public static void main(String[] args) {
        new DCBot().initialize(args[0]);
    }

    public void initialize(String token) {
        try {
            jda = JDABuilder.createDefault(token, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_INVITES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_BANS,
                    GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                    .enableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS, CacheFlag.EMOTE,
                            CacheFlag.MEMBER_OVERRIDES, CacheFlag.ROLE_TAGS, CacheFlag.VOICE_STATE)
                    .setActivity(Activity.playing(Main.getPlugin().getPlaying())).build();
            jda.addEventListener(new ReadyListener());
            jda.addEventListener(new ButtonListener());
            jda.addEventListener(new SlashCommandListener());
            jda.awaitReady();
            if (jda.getGuildsByName(Main.getPlugin().getGuildName(), true).size() <= 0) {
                Main.getPlugin().getLogger().log(Level.SEVERE, "NO VALID GUILD FOUND! Please check the guild in 'config.yml' and restart the server.");
                Main.getPlugin().getLogger().log(Level.WARNING, "! Continuing without bot !");
                exit();
                return;
            }
            guild = jda.getGuildsByName(Main.getPlugin().getGuildName(), true).get(0);
            Main.setBotActive(true);
        } catch (LoginException | InterruptedException e) {
            Main.getPlugin().getLogger().log(Level.SEVERE, "INVALID TOKEN PROVIDED OR LOGIN INTERRUPTED! Check the token in 'config.yml' and restart the server or use the /settoken command.");
            e.printStackTrace();
        }
    }

    public void exit() {
        Main.setBotActive(false);
        jda.shutdown();
    }

    public JDA getJDA() {
        return this.jda;
    }

    public Guild getGuild() {
        return guild;
    }
}
