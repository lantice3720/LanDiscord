package kr.kro.lanthanide.landiscord;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerIOHandler implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        LanDiscord.getChannel().sendMessage(e.getPlayer().getName() + "이(가) 게임에 참여했습니다.").queue();
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent e) {
        LanDiscord.getChannel().sendMessage(e.getPlayer().getName() + "이(가) 게임을 나갔습니다.").queue();
    }
}
