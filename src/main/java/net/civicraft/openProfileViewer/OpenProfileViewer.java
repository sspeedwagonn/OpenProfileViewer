package net.civicraft.openProfileViewer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bukkit.plugin.java.JavaPlugin;

public final class OpenProfileViewer extends JavaPlugin {
    static OpenProfileViewer instance;

    @Override
    public void onEnable() {
        instance = this;
        registerDiscordBot();
        saveDefaultConfig();
    }

    private void registerDiscordBot() {
        String token = getConfig().getString("discord.token");
        if (token == null || token.isEmpty()) {
            getLogger().severe("[OpenProfileViewer] Discord bot token is null or empty!");
            return;
        }

        String activity = getConfig().getString("discord.activity");
        String activityMessage = getConfig().getString("discord.activity-message");
        String status = getConfig().getString("discord.status", "ONLINE");

        JDABuilder builder = JDABuilder.createDefault(token);

        //activity manager
        if (activity != null && !activity.equalsIgnoreCase("NONE")) {
            Activity.ActivityType activityType;
            try {
                activityType = Activity.ActivityType.valueOf(activity.toUpperCase());
            } catch (IllegalArgumentException e) {
                getLogger().warning("[OpenProfileViewer] Invalid activity type '" + activity + "'. Defaulting to Playing!");
                activityType = Activity.ActivityType.PLAYING;
                getConfig().set("discord.activity", "Playing");
            }

            //activity message manager
            if (activityMessage != null && !activityMessage.equalsIgnoreCase("NONE")) {
                if (activityMessage.length() > 128) {
                    activityMessage = activityMessage.substring(0, 125) + "...";
                    getLogger().warning("[OpenProfileViewer] Activity message shortened to 128 characters. Consider editing your activity message to meet the 128-character limit!");
                    getConfig().set("discord.activity-message", activityMessage);
                }
                builder.setActivity(Activity.of(activityType, activityMessage));
            } else {
                builder.setActivity(Activity.of(activityType, ""));
            }
        }

        //status manager
        try {
            OnlineStatus onlineStatus = OnlineStatus.valueOf(status.toUpperCase().replace(" ", "_"));
            builder.setStatus(onlineStatus);
        } catch (IllegalArgumentException e) {
            getLogger().warning("[OpenProfileViewer] Invalid status '" + status + "'. Defaulting to Online!");
            builder.setStatus(OnlineStatus.ONLINE);
            getConfig().set("discord.status", "ONLINE");
        }

        saveConfig();//save any changes made by checks
        JDA bot = builder.build();
        bot.updateCommands().addCommands(Commands.slash("profile", "Returns the profile of a player").addOption(OptionType.STRING, "player", "Player's username", true)).queue();
    }

    public static OpenProfileViewer getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
