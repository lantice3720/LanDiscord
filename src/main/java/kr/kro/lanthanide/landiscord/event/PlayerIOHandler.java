package kr.kro.lanthanide.landiscord.event;

import kr.kro.lanthanide.landiscord.LanDiscord;
import kr.kro.lanthanide.landiscord.config.ChannelConfig;
import kr.kro.lanthanide.landiscord.util.DiscordUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PlayerIOHandler implements Listener {
    
    // TODO ChatHandler 와 PlayerIOHandler 매우 겹침. 수정필요
    private List<ChannelConfig> channelConfig = null;
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // forEach 로 알맞은 채널에 웹훅 메시지 출력
        JavaPlugin.getPlugin(LanDiscord.class).getChannelConfig().forEach(channelConfig -> {
            if(channelConfig.getWebhookUsage().contains("playerIO")) {
                // TODO 이름 null 로 변경, LanDiscord 에서 메서드 수정, 필요하다 생각되면 메서드 새 클래스로 분리
                DiscordUtil.sendWebHookString(LanDiscord.getBot().getTextChannelById(channelConfig.getId()), "LanDev", e.getPlayer().getName() + "이 접속했습니다.", null);
            }
        });
        // 봇 메시지 출력
        JavaPlugin.getPlugin(LanDiscord.class).getChannelConfig().forEach(channelConfig -> {
            if(channelConfig.getBotUsage().contains("playerIO")) {
                LanDiscord.getBot().getTextChannelById(channelConfig.getId()).sendMessage(e.getPlayer().getName() + " 이 접속했습니다.").queue();
            }
        });
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent e) {
        // forEach 로 알맞은 채널에 웹훅 메시지 출력
        JavaPlugin.getPlugin(LanDiscord.class).getChannelConfig().forEach(channelConfig -> {
            if(channelConfig.getWebhookUsage().contains("playerIO")) {
                // TODO 이름 null 로 변경, LanDiscord 에서 메서드 수정, 필요하다 생각되면 메서드 새 클래스로 분리
                DiscordUtil.sendWebHookString(LanDiscord.getBot().getTextChannelById(channelConfig.getId()), "LanDev", e.getPlayer().getName() + "이 퇴장했습니다.", null);
            }
        });
        // 봇 메시지 출력
        JavaPlugin.getPlugin(LanDiscord.class).getChannelConfig().forEach(channelConfig -> {
            if(channelConfig.getBotUsage().contains("playerIO")) {
                LanDiscord.getBot().getTextChannelById(channelConfig.getId()).sendMessage(e.getPlayer().getName() + " 이 퇴장했습니다.").queue();
            }
        });
    }
}
