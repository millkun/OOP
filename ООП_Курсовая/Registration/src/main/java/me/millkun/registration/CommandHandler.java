package me.millkun.registration;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandHandler implements CommandExecutor {

    private final RegistrationPlugin plugin;
    private final PlayerDataManager playerDataManager;

    public CommandHandler(RegistrationPlugin plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду может использовать только игрок.");
            return false;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if (command.getName().equalsIgnoreCase("register")) {
            return handleRegisterCommand(player, uuid, args);
        } else if (command.getName().equalsIgnoreCase("login")) {
            return handleLoginCommand(player, uuid, args);
        }
        return false;
    }

    private boolean handleRegisterCommand(Player player, UUID uuid, String[] args) {
        if (playerDataManager.isRegistered(uuid)) {
            player.sendMessage(ChatColor.RED + "Вы уже зарегистрированы.");
            return false;
        }

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "/register <password> <password>");
            return false;
        }

        String password1 = args[0];
        String password2 = args[1];

        if (!password1.equals(password2)) {
            player.sendMessage(ChatColor.RED + "Пароли не совпадают.");
            return false;
        }

        playerDataManager.registerPlayer(uuid, password1, "Пионер");
        player.sendMessage(ChatColor.GREEN + "Вы успешно зарегистрировались!");
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(new Location(player.getWorld(), 830.5, 68, -6479.5));
        return true;
    }

    private boolean handleLoginCommand(Player player, UUID uuid, String[] args) {
        if (!playerDataManager.isRegistered(uuid)) {
            player.sendMessage(ChatColor.RED + "Сначала зарегистрируйтесь.");
            return false;
        }

        if (playerDataManager.hasLoginAttempted(uuid)) {
            player.sendMessage(ChatColor.RED + "Вы уже вошли в систему.");
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "/login <password>");
            return false;
        }

        String password = args[0];
        if (playerDataManager.validatePassword(uuid, password)) {
            player.sendMessage(ChatColor.GREEN + "Вы успешно вошли в систему!");
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(playerDataManager.getPlayerLocation(uuid).orElse(new Location(player.getWorld(), 830.5, 68, -6479.5)));
            playerDataManager.setLoginAttempted(uuid, true);
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "Неверный пароль.");
            return false;
        }
    }
}
