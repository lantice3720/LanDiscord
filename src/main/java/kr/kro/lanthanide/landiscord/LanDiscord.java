package kr.kro.lanthanide.landiscord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class LanDiscord extends JavaPlugin {
    private static JDA bot = null;
    private static TextChannel channel;

    @Override
    public void onEnable() {

        // 이벤트리스너 등록
        getServer().getPluginManager().registerEvents(new ChatHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerIOHandler(), this);

        getCommand("discord").setExecutor(new DiscordCommand());

        if (getConfig().getString("discord.token") != null) {
            // JDA 사용, 디스코드봇 연결

            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        // 봇 빌드
                        bot = JDABuilder.create(getConfig().getString("discord.token"), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                                .addEventListeners(new ChatHandler())
                                .build();
                        bot.awaitReady();
                        getLogger().info("봇을 시작했습니다.");

                        // 채널, 길드 설정
                        if (getConfig().getString("discord.channel") == null) {
                            getLogger().severe("디스코드 채널의 id 가 설정되지 않았습니다.");
                        } else {
                            // discord.channel 존재
                            channel = bot.getTextChannelById(getConfig().getLong("discord.channel"));

                            getLogger().info(channel.getName() + " 채널을 감지했습니다.");
                        }

                        // 서버 ON 메시지
                        channel.sendMessage("디스코드가 연결되었습니다.").queue();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.runTask(this);
        } else {
            getLogger().severe("디스코드 봇의 토큰이 설정되지 않았습니다.");
        }

        // 기본 콘피그를 dataFolder 에 옮김
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // 서버 OFF 메시지
        if (channel != null && bot != null) {
            // 디스코드로 송출
            channel.sendMessage("디스코드 연결이 끊겼습니다.").queue();

            try {
                // 봇 셧다운
                bot.shutdown();
                getLogger().info("봇을 종료했습니다.");
            } catch (NoClassDefFoundError e) {
                // 플러그인 파일 변경 오류
                getLogger().severe("봇 종료 중 라이브러리 오류가 발생했습니다. 서버를 완전히 재기동해주세요.");
            }
        }
    }

    public static JDA getBot() {
        return bot;
    }

    public static TextChannel getChannel() {
        return channel;
    }
}


