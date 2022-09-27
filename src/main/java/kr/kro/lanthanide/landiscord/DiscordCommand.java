package kr.kro.lanthanide.landiscord;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DiscordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args.length) {
            case 1:
                if (args[0].equals("shutdown")) {
                    sender.sendMessage("봇을 강제로 종료합니다.");
                    LanDiscord.getBot().shutdown();
                }
            default:
                return false;
        }
    }
}
