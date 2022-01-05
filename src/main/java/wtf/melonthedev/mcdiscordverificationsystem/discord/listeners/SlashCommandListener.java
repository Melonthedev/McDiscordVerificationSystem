package wtf.melonthedev.mcdiscordverificationsystem.discord.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import wtf.melonthedev.mcdiscordverificationsystem.Main;
import wtf.melonthedev.mcdiscordverificationsystem.Messages;
import wtf.melonthedev.mcdiscordverificationsystem.VerifyHelper;

import java.awt.*;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.getName().equals("verify") || event.getOption("minecraftusername") == null || event.getMember() == null) return;
        String username = Objects.requireNonNull(event.getOption("minecraftusername")).getAsString();
        if (event.getChannel().getIdLong() != 837263808556826645L) {
            event.reply("*🙄 you don't need this command anymore.*").setEphemeral(true).addActionRow(Button.success("iknow", "I know 😞"),
                    Button.danger("idk", "HAH IT WAS A PRANK 😂"), Button.link("https://youtu.be/dQw4w9WgXcQ", "I will give up 😐")).queue();
            return;
        }
        Role unverifiedRole = Objects.requireNonNull(event.getGuild()).getRoleById(804262397044064257L);
        if (!event.getMember().getRoles().contains(unverifiedRole)) {
            VerifyHelper.sendAlreadyVerifiedMessage(event);
            return;
        }
        if (username.contains(" ") || username.length() < 3 || username.length() > 16 || VerifyHelper.containsSpecialChars(username) || !VerifyHelper.isValidMinecraftAccount(username)) {
            MessageEmbed message = new EmbedBuilder()
                    .setColor(Color.RED).setTitle(Messages.getMessage("discord.invalidinput"))
                    .setFooter("VerifySystem by Melonthedev", Objects.requireNonNull(event.getGuild()).getIconUrl())
                    .setTimestamp(new Date().toInstant())
                    .setDescription(username).addField(Messages.getMessage("discord.pleaseuse"), Messages.getMessage("discord.invalidinputdescription"), true).build();
            event.replyEmbeds(message).setEphemeral(true).queue();
            Main.getPlugin().getLogger().log(Level.WARNING, "Discord User "
                    + event.getUser().getName() + "#" + event.getUser().getDiscriminator() + " tried to verify himself with an invalid MC Account: " + username);
            return;
        }
        if (Main.getPlugin().getConfig().contains("whitelist." + VerifyHelper.getUUIDFromName(username))) {
            MessageEmbed message = new EmbedBuilder().setTitle(Messages.getMessage("discord.mcnamealreadyverified")).setColor(Color.RED).setTimestamp(new Date().toInstant())
                    .setDescription("🤔")
                    .setFooter("VerifySystem by Melonthedev", Objects.requireNonNull(event.getGuild()).getIconUrl()).build();
            event.replyEmbeds(message).setEphemeral(true).queue();
            return;
        }
        if (Main.pendingVerifications.containsValue(event.getMember())) {
            MessageEmbed message = new EmbedBuilder().setTitle(Messages.getMessage("discord.inputupdated")).setColor(Color.GREEN).setTimestamp(new Date().toInstant())
                    .setDescription(Messages.getMessage("discord.nearlyverifieddescription"))
                    .setFooter("VerifySystem by Melonthedev", Objects.requireNonNull(event.getGuild()).getIconUrl()).build();
            event.replyEmbeds(message).setEphemeral(true).queue();
            Main.pendingVerifications.put(username.toLowerCase(), event.getMember());
            return;
        }
        MessageEmbed message = new EmbedBuilder().setTitle(Messages.getMessage("discord.nearlyverified")).setColor(Color.GREEN).setTimestamp(new Date().toInstant())
                .setDescription(Messages.getMessage("discord.nearlyverifieddescription"))
                .setFooter("VerifySystem by Melonthedev", Objects.requireNonNull(event.getGuild()).getIconUrl()).build();
        event.replyEmbeds(message).setEphemeral(true).queue();
        Main.pendingVerifications.put(username.toLowerCase(), event.getMember());
    }
}
