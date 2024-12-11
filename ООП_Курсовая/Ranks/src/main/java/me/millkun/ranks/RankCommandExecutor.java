package me.millkun.ranks;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RankCommandExecutor implements org.bukkit.command.CommandExecutor {

    private final RankManager rankManager;

    public RankCommandExecutor(RankManager rankManager) {
        this.rankManager = rankManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setrank")) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "У Вас нет прав на использование этой команды");
                return false;
            }

            if (args.length != 2) {
                sender.sendMessage(ChatColor.RED + "/setrank <player> <rank>");
                return false;
            }

            Player target = sender.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Игрок не найден");
                return false;
            }

            String rank = args[1];
            rankManager.setPlayerRank(target, rank);
            sender.sendMessage(ChatColor.GREEN + "Ранг " + rank + " был установлен игроку " + target.getName());
            return true;
        }
        return false;
    }
}