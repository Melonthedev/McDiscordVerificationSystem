package wtf.melonthedev.mcdiscordverificationsystem.discord.listeners;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import wtf.melonthedev.mcdiscordverificationsystem.Main;
import wtf.melonthedev.mcdiscordverificationsystem.VerifyHelper;

import java.util.Objects;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getComponent() == null || event.getButton() == null)
            return;
        switch (event.getComponentId()) {
            case "iknow":
                event.editButton(Button.success("iknow", "Yeah and now stop! 🙄").withDisabled(true)).queue();
                break;
            case "idk":
                event.editButton(Button.danger("idk", "😡🤬🤬😡😤 YOU BETTER STOP TROLLING ME!").withDisabled(true)).queue();
                break;
            case "stopeyes":
                event.editButton(event.getButton().withDisabled(true)).queue();
                event.getChannel().sendMessage("Okay ¯\\_(ツ)_/¯").queue();
                Main.getPlugin().getConfig().set("discord.stopeyes." + event.getUser().getId(), true);
                Main.getPlugin().saveConfig();
                break;
            case "starteyes":
                event.editButton(event.getButton().withDisabled(true)).queue();
                event.getChannel().sendMessage("👍").queue();
                Main.getPlugin().getConfig().set("discord.stopeyes." + event.getUser().getId(), null);
                Main.getPlugin().saveConfig();
                break;
        }
        if (!event.getComponentId().equals("getaccess"))
            return;
        if (!(event.getChannelType() == ChannelType.PRIVATE))
            return;
        event.editButton(event.getButton().withDisabled(true)).queue();
        Guild guild = Main.getPlugin().getBot().getGuild();
        if (guild == null) return;
        if (guild.getMember(event.getUser()) == null) return;
        Role unverifiedRole = guild.getRoleById(804262397044064257L);
        if (unverifiedRole == null) {
            event.reply("SORRY! Something went wrong with removing your role. Please send me a message: Melonthedev#1848").queue();
            return;
        }
        guild.removeRoleFromMember(Objects.requireNonNull(guild.getMember(event.getUser())), unverifiedRole).queue();
    }
}
