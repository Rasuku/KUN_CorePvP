package rasuku59.kun.corepvp.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import rasuku59.kun.corepvp.CorePvP;
import rasuku59.kun.corepvp.Data;
import rasuku59.kun.corepvp.PlayerData;

import java.util.*;

public class TeamCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 3)  {
            //sender.sendMessage(ChatColor.RED + "使い方: " + usageMessage);
            return false;
        }
        if ( !args[0].equalsIgnoreCase(CorePvP.TEAM_RED) && !args[0].equalsIgnoreCase(CorePvP.TEAM_BLUE) ) {
            sender.sendMessage(CorePvP.COMMAND_NAME + "指定されたチーム名 " + args[0] + " が無効です。");
            sender.sendMessage("red または blue を指定してください。");
            return true;
        }
        Map<String, String> map = new HashMap<>();
        map.put(CorePvP.TEAM_RED, ChatColor.RED + "レッドチーム");
        map.put(CorePvP.TEAM_BLUE, ChatColor.BLUE + "ブルーチーム");
        Player player = Bukkit.getServer().getPlayer(args[2]);
        if (player != null) {
            PlayerData data = Data.getInstance().getPlayerData(player);
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = Objects.requireNonNull(manager).getMainScoreboard();
            Team team;
            if(args[1].equals("join")){
                team = board.getTeam(args[0]);
                Objects.requireNonNull(team).addPlayer(player);
                data.setTeam(args[0]);
                player.sendMessage(CorePvP.COMMAND_NAME + map.get(args[0]) + ChatColor.GREEN + "に参加しました。");
                sender.sendMessage(CorePvP.COMMAND_NAME + player.getName() + "を" + map.get(args[0]) + ChatColor.WHITE + "に参加させました。");
            }else if (args[1].equals("kick")){
                team = board.getTeam(data.getTeam());
                data.setTeam("");
                Objects.requireNonNull(team).removePlayer(player);
                player.sendMessage(CorePvP.COMMAND_NAME + ChatColor.RED + "チームから除外されました。");
                sender.sendMessage(CorePvP.COMMAND_NAME + player.getName() + ChatColor.RED + "をチームから除外しました。");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){
            List<String> arguments = new ArrayList<>();
            arguments.add(CorePvP.TEAM_RED);
            arguments.add(CorePvP.TEAM_BLUE);

            return arguments;
        }else if (args.length == 2){
            List<String> arguments = new ArrayList<>();
            arguments.add("join");
            arguments.add("kick");

            return arguments;
        }
        return null;
    }
}
