package kr.kro.lanthanide.landiscord;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class ChatHandler extends ListenerAdapter implements Listener {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getAuthor().isBot() || !e.getChannel().asTextChannel().equals(LanDiscord.getChannel())) return;
        Bukkit.getLogger().info("[Discord] <" + e.getAuthor().getName() + "> " + e.getMessage().getContentDisplay());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Component.text("<" + e.getAuthor().getName() + "> ").append(Component.text(e.getMessage().getContentDisplay(), TextColor.color(180, 180, 180))));
        }
    }

    @EventHandler
    public void onMCChat(AsyncChatEvent e) {
        LanDiscord.getChannel().sendMessage("<" + e.getPlayer().getName() + "> " + PlainTextComponentSerializer.plainText().serialize(e.message())).queue();
    }
}
