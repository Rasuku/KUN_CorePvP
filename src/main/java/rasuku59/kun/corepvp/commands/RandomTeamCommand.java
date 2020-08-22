package rasuku59.kun.corepvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

public class RandomTeamCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put(CorePvP.TEAM_RED, ChatColor.RED + "レッドチーム");
        map.put(CorePvP.TEAM_BLUE, ChatColor.BLUE + "ブルーチーム");

        List<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(list);

        int i = 0;

        for (Player player : list) {
            PlayerData data = Data.getInstance().getPlayerData(player);
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = Objects.requireNonNull(manager).getMainScoreboard();
            String key;
            if (i % 2 == 0){
                key = CorePvP.TEAM_RED;
            }else{
                key = CorePvP.TEAM_BLUE;
            }
            Team team;
            if(args[0].equals("join")){
                team = board.getTeam(key);
                Objects.requireNonNull(team).addPlayer(player);
                data.setTeam(key);
                player.sendMessage(CorePvP.COMMAND_NAME + map.get(key) + ChatColor.WHITE + "に参加しました。");
                sender.sendMessage(CorePvP.COMMAND_NAME + player.getName() + "を" + map.get(key) + ChatColor.WHITE + "に参加させました。");
            }else if (args[0].equals("kick")){
                team = board.getTeam(data.getTeam());
                data.setTeam("");
                Objects.requireNonNull(team).removePlayer(player);
                player.sendMessage(CorePvP.COMMAND_NAME + ChatColor.RED + "チームから除外されました。");
                sender.sendMessage(CorePvP.COMMAND_NAME + player.getName() + ChatColor.RED + "をチームから除外しました。");
            }
            i++;
        }


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){
            List<String> arguments = new ArrayList<>();
            arguments.add("join");
            arguments.add("kick");

            return arguments;
        }
        return null;
    }
}
