package kr.kro.lanthanide.landiscord;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class LanDiscord extends JavaPlugin {
    // TODO config.yml 수정중임. 완료할것!!
    private static JDA bot = null;
    private static TextChannel channel;

    @Override
    public void onEnable() {
        // 이벤트리스너 등록
        getServer().getPluginManager().registerEvents(new ChatHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerIOHandler(), this);
        // 커맨드 등록
        getCommand("discord").setExecutor(new DiscordCommand());
        // 기본 콘피그를 dataFolder 에 옮김
        saveDefaultConfig();


        // 디스코드 봇 작업
        if (getConfig().getString("discord.token") != null) {
            // JDA 사용, 디스코드봇 연결
            // 봇 빌드
            bot = JDABuilder.create(getConfig().getString("discord.token"), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                    .setActivity(Activity.watching(getServer().getOnlinePlayers().size() + "명의 플레이어"))
                    .addEventListeners(new ChatHandler())
                    .build();
            // 봇 생성 완료까지 대기
            try {
                bot.awaitReady();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getLogger().info("봇을 시작했습니다.");

            // 채널 설정
            if (getConfig().getString("discord.channel") != null) {
                // discord.channel 존재
                channel = bot.getTextChannelById(getConfig().getLong("discord.channel"));

                getLogger().info(channel.getName() + " 채널을 감지했습니다.");
            } else {
                getLogger().severe("디스코드 채널의 id 가 설정되지 않았습니다.");
            }

            // 서버 ON 메시지
            channel.sendMessage("디스코드가 연결되었습니다.").queue();
        } else {
            getLogger().severe("디스코드 봇의 토큰이 설정되지 않았습니다.");
        }
    }

    @Override
    public void onDisable() {
        // 서버 OFF 메시지
        shutdownBot();
    }

    public static TextChannel getChannel() {
        return channel;
    }

    public static void shutdownBot() {
        if (channel != null && bot != null) {
            // 디스코드로 송출
            channel.sendMessage("디스코드 연결이 끊겼습니다.").queue();

            try {
                // 봇 셧다운
                bot.shutdown();
                Logger.getLogger("LanDiscord").info("봇을 종료했습니다.");
            } catch (NoClassDefFoundError e) {
                // 플러그인 파일 변경 오류
                Logger.getLogger("LanDiscord").severe("봇 종료 중 라이브러리 오류가 발생했습니다. 서버를 완전히 재기동해주세요.");
            }
        }
    }

    public static void sendWebhookEmbed(String name, String message) {
        // 웹훅 작업
        channel.createWebhook(name).queue(webhook -> {
            try (WebhookClient client = WebhookClientBuilder.fromJDA(webhook).build()) {
                WebhookEmbed embed = new WebhookEmbedBuilder()
                        .setColor(0x55ff88)
                        .setDescription(message)
                        .build();
                client.send(embed);
            }
        });
    }

    public static void sendWebHookString(String name, String message) {
        channel.createWebhook(name).queue(webhook -> {
            try (WebhookClient client = WebhookClientBuilder.fromJDA(webhook).build()){
                client.send(message);
            }
        });
    }
}


