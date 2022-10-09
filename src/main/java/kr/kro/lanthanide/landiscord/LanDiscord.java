package kr.kro.lanthanide.landiscord;

import kr.kro.lanthanide.landiscord.command.DiscordCommand;
import kr.kro.lanthanide.landiscord.config.LanConfig;
import kr.kro.lanthanide.landiscord.event.ChatHandler;
import kr.kro.lanthanide.landiscord.event.PlayerIOHandler;
import kr.kro.lanthanide.landiscord.util.DiscordUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.io.*;

public final class LanDiscord extends JavaPlugin {
    // TODO config.yml 수정중임. 완료할것!!
    private static JDA bot = null;

    private LanConfig lanConfig;

    @Override
    public void onEnable() {
        // 컨피그 불러오기
        InputStream input;
        try {
            input = new FileInputStream(getDataFolder() + "\\config.yml");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Yaml yaml = new Yaml(new CustomClassLoaderConstructor(LanDiscord.class.getClassLoader()));
        lanConfig = yaml.loadAs(input, LanConfig.class);

        // 이벤트리스너 등록
        getServer().getPluginManager().registerEvents(new ChatHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerIOHandler(), this);
        // 커맨드 등록
        getCommand("discord").setExecutor(new DiscordCommand());

        // 봇 생성
        if (getLanConfig().getToken() != null) {
            // JDA 사용, 디스코드봇 연결
            // 봇 빌드
            bot = JDABuilder.create(getLanConfig().getToken(), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
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
            // 토큰 에러
            getLogger().severe("디스코드 봇의 토큰이 설정되지 않았습니다.");
            getPluginLoader().disablePlugin(this);
        }

        // 서버 시작 메시지 출력

    }

    @Override
    public void onDisable() {
        // 서버 OFF 메시지
        DiscordUtil.shutdownBot();
    }

    public static JDA getBot() {
        return LanDiscord.bot;
    }
    public LanConfig getLanConfig() {
        return lanConfig;
    }

}


