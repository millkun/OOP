package me.millkun.registration;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class RegistrationEventHandler implements Listener {

    private final RegistrationPlugin plugin;
    private final PlayerDataManager playerDataManager;

    public RegistrationEventHandler(RegistrationPlugin plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!playerDataManager.isRegistered(uuid)) {
            player.teleport(new Location(player.getWorld(), 939.5, 40, -6484.5));
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(ChatColor.YELLOW + "Пожалуйста, зарегистрируйтесь с помощью команды /register <пароль> <пароль>");
        } else {
            player.teleport(new Location(player.getWorld(), 939.5, 40, -6484.5));
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(ChatColor.YELLOW + "Пожалуйста, войдите в систему с помощью команды /login <пароль>");
            player.sendMessage(ChatColor.YELLOW + "Если не понимаете какой пароль от Вас требуют, возможно ник, с которого Вы зашли, уже занят. Попробуйте изменить его.");
            playerDataManager.setLoginAttempted(uuid, false);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerDataManager.savePlayerLocation(player.getUniqueId(), player.getLocation());
    }
}
