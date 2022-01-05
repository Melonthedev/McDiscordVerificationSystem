package wtf.melonthedev.mcdiscordverificationsystem;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.RestAction;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class VerifyHelper {

    private static final HashMap<String, UUID> cachedUsers = new HashMap<>();

    public static UUID getUUIDFromName(String mcName) {
        if (cachedUsers.containsKey(mcName)) return cachedUsers.get(mcName);
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + mcName);
            HttpURLConnection con;
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) content.append(inputLine);
            in.close();
            JsonElement element = JsonParser.parseString(content.toString());
            if (element == null) return null;
            if (!(element instanceof JsonObject)) return null;
            String uuidString = element.getAsJsonObject().get("id").getAsString();
            char[] c = uuidString.toCharArray();
            uuidString = "" + c[0] + "" + c[1] + "" + c[2]+"" +  c[3] + "" + c[4] + "" + c[5] + c[6] + "" + c[7] + "-"
                    + c[8] + c[9] + c[10]+ c[11] + "-"
                    + c[12] + c[13] + c[14]+ c[15] + "-"
                    + c[16] + c[17] + c[18]+ c[19] + "-"
                    + c[20] + c[21] + c[22]+ c[23] + c[24] + c[25] + c[26]+ c[27] + c[28] + c[29] + c[30]+ c[31];
            UUID uuid = UUID.fromString(uuidString);
            cachedUsers.put(mcName, uuid);
            return uuid;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isValidMinecraftAccount(String username) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            HttpURLConnection con;
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            return con.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean containsSpecialChars(String input) {
        String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^`{|}";
        boolean containsSpecialCharacters = false;
        for (int i = 0; i < input.length(); i++)
        {
            char ch = input.charAt(i);
            if (specialCharactersString.contains(Character.toString(ch))) {
                containsSpecialCharacters = true;
                break;
            }
        }
        return containsSpecialCharacters;
    }

    public static void sendVerificationSuccessMessage(Member member) {
        TextChannel verifyChannel = Main.getPlugin().bot.getJDA().getTextChannelById(837263808556826645L);
        MessageEmbed message = new EmbedBuilder().setTitle(Messages.getMessage("discord.verified")).setColor(Color.GREEN).setTimestamp(new Date().toInstant())
                .setDescription(member.getAsMention() + " " + Messages.getMessage("discord.verifieddescription"))
                .setFooter("VerifySystem by Melonthedev", Objects.requireNonNull(verifyChannel).getGuild().getIconUrl()).build();
        member.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessageEmbeds(message)
                .setActionRow(Button.success("getaccess", "✔ " + Messages.getMessage("discord.buttongetaccess")))
                .queue());
    }

    public static void sendWelcomeMessage(Member member, TextChannel channel) {
        MessageEmbed embed = new EmbedBuilder().setColor(Color.CYAN).setTitle(Messages.getMessage("discord.welcome") + Main.getPlugin().getGuildName() + "!")
                .setDescription(member.getAsMention() + " " + Messages.getMessage("discord.welcomedescription"))
                .setTimestamp(new Date().toInstant())
                .setFooter("VerifySystem by Melonthedev", channel.getGuild().getIconUrl())
                .setThumbnail(member.getEffectiveAvatarUrl())
                .build();
        channel.sendMessageEmbeds(embed).queue();
    }

    public static void sendAlreadyVerifiedMessage(SlashCommandEvent event) {
        MessageEmbed message = new EmbedBuilder().setTitle("You are already verified!").setColor(Color.GREEN).setTimestamp(new Date().toInstant())
                .setDescription("I dont even know why you are here...")
                .setFooter("VerifySystem by Melonthedev", Main.getPlugin().getBot().getGuild().getIconUrl()).build();
        event.replyEmbeds(message).setEphemeral(true).queue();
    }

    public static RestAction<Void> selfDestruct(MessageChannel channel, String content) {
        return channel.sendMessage(content)
                .delay(60, TimeUnit.SECONDS)
                .flatMap(Message::delete);
    }


}
