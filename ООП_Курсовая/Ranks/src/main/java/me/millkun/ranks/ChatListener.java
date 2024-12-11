package me.millkun.ranks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatListener implements Listener {

    private final RankManager rankManager;
    private final AnimationManager animationManager;

    public ChatListener(RankManager rankManager, AnimationManager animationManager) {
        this.rankManager = rankManager;
        this.animationManager = animationManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String chatDisplayName = animationManager.getChatDisplayName(player);
        String message = event.getMessage();

        // Отключаем стандартное форматирование чата
        event.setCancelled(true);

        // Отправляем собственное форматированное сообщение
        for (Player onlinePlayer : event.getRecipients()) {
            onlinePlayer.sendMessage(chatDisplayName + message);
        }
    }
}
