package wtf.melonthedev.mcdiscordverificationsystem.discord.listeners;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import wtf.melonthedev.mcdiscordverificationsystem.Main;
import wtf.melonthedev.mcdiscordverificationsystem.Messages;
import wtf.melonthedev.mcdiscordverificationsystem.VerifyHelper;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class ReadyListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        Main.getPlugin().getLogger().log(Level.INFO, "Ready! Logged in as " + event.getJDA().getSelfUser().getName() + "#" + event.getJDA().getSelfUser().getDiscriminator());
        //Guild survivalprojekt = Main.getPlugin().getBot().getGuild();
        //survivalprojekt.updateCommands().queue();
        //survivalprojekt.upsertCommand(new CommandData("verify", "Verifiziert dich in Minecraft und im Discord.")).addOption(OptionType.STRING, "minecraftusername", "Your Minecraft Name", true).queue();
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (!event.getGuild().getName().equals(Main.getPlugin().getGuildName())) return;
        Role unverifiedRole = event.getGuild().getRoleById(804262397044064257L);
        TextChannel verifyChannel = event.getGuild().getTextChannelById(837263808556826645L);
        if (unverifiedRole == null || verifyChannel == null) return;
        event.getGuild().addRoleToMember(event.getMember(), unverifiedRole).queue();
        VerifyHelper.sendWelcomeMessage(event.getMember(), verifyChannel);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getMentionedUsers().contains(event.getJDA().getSelfUser())) event.getMessage().addReaction("👀").queue();
        if (event.isFromType(ChannelType.PRIVATE)) {
            if (event.getMessage().getAuthor().isBot()) return;
            event.getChannel().sendMessage("👀").queue();
            return;
        }
        if (event.getChannel().getIdLong() == 837263808556826645L) {
            if (event.getAuthor().isBot()) return;
            event.getMessage().delete().queueAfter(60, TimeUnit.SECONDS);
            VerifyHelper.selfDestruct(event.getChannel(), Messages.getMessage("discord.onlychatwhenverified")).queue();
            return;
        }

        //DEBUG STUFF, WILL BE REMOVED IN FINAL VERSION
        String message = event.getMessage().getContentRaw();

        Member member = event.getMember();
        Guild survivalprojekt = event.getJDA().getGuilds().get(0);
        if (member == null) {
            System.out.println("MEMBERNULL");
            return;
        }

        if (message.contains("testembed")) {
            VerifyHelper.sendWelcomeMessage(member, event.getTextChannel());
            return;
        }
        if (!message.contains(":")) return;
        if (message.split(":").length <= 1) return;
        Role role = survivalprojekt.getRolesByName(message.split(":")[0], true).get(0);
        if (role == null) {
            System.out.println("ROLENULL");
            return;
        }
        if (message.contains("addrole")) {
            survivalprojekt.addRoleToMember(member, role).queue();
        } else if (message.contains("removerole")) {
            survivalprojekt.removeRoleFromMember(member, role).queue();
        }
    }

}
