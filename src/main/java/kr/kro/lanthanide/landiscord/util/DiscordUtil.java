package kr.kro.lanthanide.landiscord.util;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import kr.kro.lanthanide.landiscord.LanDiscord;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

public class DiscordUtil {

    public static void shutdownBot() {
        if (LanDiscord.getBot() != null) {
            // 디스코드로 송출

            try {
                // 봇 셧다운
                LanDiscord.getBot().shutdown();
                Logger.getLogger("LanDiscord").info("봇을 종료했습니다.");
            } catch (NoClassDefFoundError e) {
                // 플러그인 파일 변경 오류
                Logger.getLogger("LanDiscord").severe("봇 종료 중 라이브러리 오류가 발생했습니다. 서버를 완전히 재기동해주세요.");
            }
        }
    }

    public static void sendWebhookMessage(@NotNull TextChannel channel, @NotNull WebhookMessage message) throws IOException {
        // 웹훅 작업
        WebhookAction webhookAction;
        if (message.getUsername() == null) {
            return;
        } else if (message.getAvatarUrl() != null) {
            webhookAction = channel.createWebhook(message.getUsername()).setAvatar(Icon.from(new URL(message.getAvatarUrl()).openStream()));
        } else {
            webhookAction = channel.createWebhook(message.getUsername()).setAvatar(null);
        }
        // 디버그코드
        webhookAction = channel.createWebhook(message.getUsername());
        // 디버스코드 end
        webhookAction.queue(webhook -> {
            try (WebhookClient client = WebhookClientBuilder.fromJDA(webhook).build()) {
                client.send(message);
            }
        });
    }

    public static void sendWebHookString(TextChannel channel, String name, String message, @Nullable Icon avatar) {
        channel.createWebhook(name).setAvatar(avatar).queue(webhook -> {
            try (WebhookClient client = WebhookClientBuilder.fromJDA(webhook).build()) {
                client.send(message);
            }
        });
    }

}
