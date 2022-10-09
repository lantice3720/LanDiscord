package kr.kro.lanthanide.landiscord.event;

import club.minnced.discord.webhook.receive.ReadonlyMessage;
import club.minnced.discord.webhook.receive.ReadonlyUser;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import io.papermc.paper.event.player.AsyncChatEvent;
import kr.kro.lanthanide.landiscord.LanDiscord;
import kr.kro.lanthanide.landiscord.util.DiscordUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class ChatHandler extends ListenerAdapter implements Listener {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getAuthor().isBot() || JavaPlugin.getPlugin(LanDiscord.class).getLanConfig().getChannel().stream().filter(channelConfig1 -> Objects.equals(channelConfig1.getId(), e.getChannel().getId())).toList().isEmpty())
            return;
        Bukkit.getLogger().info("[Discord] <" + e.getAuthor().getName() + "> " + e.getMessage().getContentDisplay());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Component.text("<" + e.getAuthor().getName() + "> ").append(Component.text(e.getMessage().getContentDisplay(), TextColor.color(180, 180, 180))));
        }
    }

    @EventHandler
    public void onMCChat(AsyncChatEvent e) {
        // forEach 로 알맞은 채널에 웹훅 메시지 출력
        JavaPlugin.getPlugin(LanDiscord.class).getLanConfig().getChannel().forEach(channelConfig -> {
            if (channelConfig.getUsage("webhook").contains("serverChat")) {
                try {
                    // 웹훅 전송
                    DiscordUtil.sendWebhookMessage(LanDiscord.getBot().getTextChannelById(channelConfig.getId()),
                            new WebhookMessageBuilder().append(PlainTextComponentSerializer.plainText().serialize(e.message()))
                                    .setAvatarUrl("https://mc-heads.net/head/" + e.getPlayer().getUniqueId())
                                    .setUsername(e.getPlayer().getName())
                                    .build());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                // TEST
                DiscordUtil.sendWebHookString(LanDiscord.getBot().getTextChannelById(channelConfig.getId()), e.getPlayer().getName(), PlainTextComponentSerializer.plainText().serialize(e.message()), null);
            }
        });
        // 봇 메시지 출력
        JavaPlugin.getPlugin(LanDiscord.class).getLanConfig().getChannel().forEach(channelConfig -> {
            if (channelConfig.getUsage("bot").contains("serverChat")) {
                LanDiscord.getBot().getTextChannelById(channelConfig.getId()).sendMessage("<" + e.getPlayer().getName() + "> " + PlainTextComponentSerializer.plainText().serialize(e.message())).queue();
            }
        });
    }
}
