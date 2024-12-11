package me.millkun.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private Commands plugin;
    private RankManager rankManager;
    private WarpManager warpManager;

    public CommandHandler(Commands plugin, RankManager rankManager, WarpManager warpManager) {
        this.plugin = plugin;
        this.rankManager = rankManager;
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эта команда доступна только игрокам.");
            return true;
        }

        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();
        String rank = rankManager.getRank(uuid);

        switch (command.getName().toLowerCase()) {
            case "gamemode":
            case "gm":
                return handleGameModeCommand(player, rank, args);
            case "setwarp":
                return handleSetWarpCommand(player, rank, args);
            case "delwarp":
                return handleDelWarpCommand(player, rank, args);
            case "warp":
                return handleWarpCommand(player, args);
            case "warps":
                return handleWarpsCommand(player);
            case "spawn":
                return handleSpawnCommand(player);
            case "sun":
                return handleWeatherCommand(player, rank, "sun");
            case "rain":
                return handleWeatherCommand(player, rank, "rain");
            case "thunder":
                return handleWeatherCommand(player, rank, "thunder");
            case "day":
                return handleTimeCommand(player, rank, "day");
            case "night":
                return handleTimeCommand(player, rank, "night");
            default:
                return false;
        }
    }

    private boolean handleGameModeCommand(Player player, String rank, String[] args) {
        if (args.length != 1) {
            player.sendMessage("Использование: /gamemode <mode>");
            return true;
        }

        String mode = args[0].toLowerCase();
        if (rank.equals("Творец") || rank.equals("Мастер")) {
            switch (mode) {
                case "1":
                case "creative":
                    player.setGameMode(org.bukkit.GameMode.CREATIVE);
                    player.sendMessage("Вы установили режим креатива.");
                    return true;
                case "2":
                case "adventure":
                    player.setGameMode(org.bukkit.GameMode.ADVENTURE);
                    player.sendMessage("Вы установили режим приключения.");
                    return true;
                case "3":
                case "spectator":
                    if (rank.equals("Творец")) {
                        player.setGameMode(org.bukkit.GameMode.SPECTATOR);
                        player.sendMessage("Вы установили режим наблюдателя.");
                    } else {
                        player.sendMessage("У вас нет доступа к этому режиму.");
                    }
                    return true;
                default:
                    player.sendMessage("Неверный режим. Доступные режимы: creative, adventure, spectator.");
                    return true;
            }
        } else {
            player.sendMessage("У вас нет доступа к этой команде.");
            return true;
        }
    }

    private boolean handleSetWarpCommand(Player player, String rank, String[] args) {
        if (rank.equals("Творец") || rank.equals("Мастер") || rank.equals("Техник")) {
            if (args.length != 1) {
                player.sendMessage("Использование: /setwarp <название>");
                return true;
            }
            String warpName = args[0];
            Location location = player.getLocation();
            warpManager.setWarp(warpName, location);
            player.sendMessage("Варп " + warpName + " установлен.");
            return true;
        } else {
            player.sendMessage("У вас нет доступа к этой команде.");
            return true;
        }
    }

    private boolean handleDelWarpCommand(Player player, String rank, String[] args) {
        if (rank.equals("Творец") || rank.equals("Мастер") || rank.equals("Техник")) {
            if (args.length != 1) {
                player.sendMessage("Использование: /delwarp <название>");
                return true;
            }
            String warpName = args[0];
            if (warpManager.deleteWarp(warpName)) {
                player.sendMessage("Варп " + warpName + " удален.");
            } else {
                player.sendMessage("Варп с таким названием не существует.");
            }
            return true;
        } else {
            player.sendMessage("У вас нет доступа к этой команде.");
            return true;
        }
    }

    private boolean handleWarpCommand(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage("Использование: /warp <название>");
            return true;
        }
        String warpName = args[0];
        Location warpLocation = warpManager.getWarp(warpName);
        if (warpLocation != null) {
            player.teleport(warpLocation);
            player.sendMessage("Вы телепортированы на варп " + warpName + ".");
        } else {
            player.sendMessage("Варп с таким названием не существует.");
        }
        return true;
    }

    private boolean handleWarpsCommand(Player player) {
        if (warpManager.getWarps().isEmpty()) {
            player.sendMessage("На сервере нет установленных варпов.");
        } else {
            player.sendMessage("Список варпов на сервере:");
            for (String warpName : warpManager.getWarps().keySet()) {
                player.sendMessage(warpName);
            }
        }
        return true;
    }

    private boolean handleSpawnCommand(Player player) {
        Location spawnLocation = new Location(plugin.getServer().getWorld("world"), 830.5, 68, -6479.5);
        player.teleport(spawnLocation);
        player.sendMessage("Вы телепортированы на спавн.");
        return true;
    }

    private boolean handleWeatherCommand(Player player, String rank, String weatherType) {
        if (rank.equals("Творец") || rank.equals("Мастер")) {
            switch (weatherType) {
                case "sun":
                    player.getWorld().setStorm(false);
                    player.sendMessage("Погода изменена на солнечную.");
                    break;
                case "rain":
                    player.getWorld().setStorm(true);
                    player.sendMessage("Погода изменена на дождливую.");
                    break;
                case "thunder":
                    player.getWorld().setStorm(true);
                    player.getWorld().setThundering(true);
                    player.sendMessage("Погода изменена на грозу.");
                    break;
            }
            return true;
        } else {
            player.sendMessage("У вас нет доступа к этой команде.");
            return true;
        }
    }

    private boolean handleTimeCommand(Player player, String rank, String time) {
        if (rank.equals("Творец") || rank.equals("Мастер")) {
            switch (time) {
                case "day":
                    player.getWorld().setTime(1000);
                    player.sendMessage("Время изменено на день.");
                    break;
                case "night":
                    player.getWorld().setTime(13000);
                    player.sendMessage("Время изменено на ночь.");
                    break;
            }
            return true;
        } else {
            player.sendMessage("У вас нет доступа к этой команде.");
            return true;
        }
    }
}
