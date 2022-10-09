package kr.kro.lanthanide.landiscord.util;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import kr.kro.lanthanide.landiscord.LanDiscord;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.WebhookAction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

public class DiscordUtil {

    /**
     * 봇을 강제로 종료합니다. 플러그인 파일 변조가 발생할 경우 에러가 발생합니다.
     */
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

    /**
     * 채널에 웹훅 메시지를 전송합니다.
     * @param channel
     * @param message
     * @throws IOException
     */
    public static void sendWebhookMessage(@NotNull TextChannel channel, @NotNull WebhookMessage message) throws IOException {
        // 웹훅 작업
        // TODO 웹훅 생성 없이 보내는 방법 연구
        // BUG 알 수 없는 웹훅 에러? 발생중. fatal 은 아닌듯함.
        channel.createWebhook(message.getUsername()).setAvatar(Icon.from(new URL(message.getAvatarUrl()).openStream())).queue(webhook -> {
            try (WebhookClient client = WebhookClientBuilder.fromJDA(webhook).build()) {
                client.send(message);
                webhook.delete().queue();
            }
        });
    }

    public static void sendWebHookString(TextChannel channel, String name, String message, @Nullable Icon avatar) {
        channel.createWebhook(name).setAvatar(avatar).queue(webhook -> {
            try (WebhookClient client = WebhookClientBuilder.fromJDA(webhook).build()) {
                client.send(message);
                webhook.delete().queue();
            }
        });
    }

}
