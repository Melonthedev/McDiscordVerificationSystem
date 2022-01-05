package wtf.melonthedev.mcdiscordverificationsystem;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Messages {

    static FileConfiguration config = Main.getPlugin().getConfig();

    public static String getMessage(String key) {
        ConfigurationSection messages = config.getConfigurationSection("messages");
        if (messages == null) return key;
        String message = messages.getString(key);
        if (message == null) return key;
        return message;
    }

}
