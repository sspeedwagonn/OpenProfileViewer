package net.civicraft.openProfileViewer;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;

//slash command for profile :D
public class ProfileCommand extends ListenerAdapter {
    public void onSlashCommand(SlashCommandInteractionEvent e) {
        if (e.getName().equals("profile")) {
            e.deferReply().queue(); //temporary, based on profile card loading time
            Player player = Bukkit.getPlayer(Objects.requireNonNull(e.getOption("player")).getAsString());
            if (player == null || !player.hasPlayedBefore()) {
                e.reply("Player not found! Have they joined the server?").queue();
            } else {

            }
        }
    }
}
