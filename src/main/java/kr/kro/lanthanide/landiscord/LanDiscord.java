package kr.kro.lanthanide.landiscord;

import kr.kro.lanthanide.landiscord.command.DiscordCommand;
import kr.kro.lanthanide.landiscord.config.ChannelConfig;
import kr.kro.lanthanide.landiscord.event.ChatHandler;
import kr.kro.lanthanide.landiscord.event.PlayerIOHandler;
import kr.kro.lanthanide.landiscord.util.DiscordUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class LanDiscord extends JavaPlugin {
    // TODO config.yml 수정중임. 완료할것!!
    private static JDA bot = null;

    private List<ChannelConfig> channelConfig = null;

    private static LanDiscord instance;

    @Override
    public void onEnable() {
        instance = this;
        channelConfig = (List<ChannelConfig>) getConfig().getList("discord.channel", new ArrayList<ChannelConfig>());
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
        } else {
            getLogger().severe("디스코드 봇의 토큰이 설정되지 않았습니다.");
            getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        // 서버 OFF 메시지
        DiscordUtil.shutdownBot();
    }

    public static JDA getBot() {
        return LanDiscord.bot;
    }
    public List<ChannelConfig> getChannelConfig() {
        return channelConfig;
    }

}


