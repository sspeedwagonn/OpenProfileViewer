package net.civicraft.openProfileViewer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;

public final class OpenProfileViewer extends JavaPlugin {

    @Override
    public void onEnable() {
        registerDiscordBot(getConfig().getString("discord-token"));
    }

    private void registerDiscordBot(String token) {
        if (token == null || token.isEmpty()) {
            getLogger().severe("[OpenProfileViewer] Discord bot token is null or empty!");
            return;
        }

        JDA bot = JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();
        bot.updateCommands().addCommands(
                Commands.slash("profile", "Returns the profile of a player")
                        .addOption(OptionType.STRING, "player", "Player's username"))
                .queue();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
