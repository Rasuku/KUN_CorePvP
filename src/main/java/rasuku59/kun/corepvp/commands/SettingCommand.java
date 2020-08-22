package rasuku59.kun.corepvp.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import rasuku59.kun.corepvp.CorePvP;

import java.util.*;

public class SettingCommand implements TabExecutor {

    public void setSpawnPos(Player player, String team, Location pos){
        Map<String, String> map = new HashMap<>();
        map.put(CorePvP.TEAM_RED, ChatColor.RED + "レッドチーム");
        map.put(CorePvP.TEAM_BLUE, ChatColor.BLUE + "ブルーチーム");
        switch (team){
            case CorePvP.TEAM_RED:
                CorePvP.redSpawnPos = pos;
                break;
            case CorePvP.TEAM_BLUE:
                CorePvP.blueSpawnPos = pos;
                break;
        }
        player.sendMessage(CorePvP.COMMAND_NAME + map.get(team) + ChatColor.GREEN + "のスポーン地点を変更しました。");
        player.sendMessage("(" + pos.getBlockX() + "." + pos.getBlockY() + "." + pos.getBlockZ() + ")");
        player.sendMessage(ChatColor.YELLOW + "※立っている地点が指定されます。");
    }

    public void setCorePos(Player player, String team, Location pos){
        Map<String, String> map = new HashMap<>();
        map.put(CorePvP.TEAM_RED, ChatColor.RED + "レッドチーム");
        map.put(CorePvP.TEAM_BLUE, ChatColor.BLUE + "ブルーチーム");
        switch (team){
            case CorePvP.TEAM_RED:
                CorePvP.redCorePos = pos;
                break;
            case CorePvP.TEAM_BLUE:
                CorePvP.blueCorePos = pos;
                break;
        }
        player.sendMessage(CorePvP.COMMAND_NAME + map.get(team) + ChatColor.GREEN + "のコア地点を変更しました。");
        player.sendMessage("(" + pos.getBlockX() + "." + pos.getBlockY() + "." + pos.getBlockZ() + ")");
        player.sendMessage(ChatColor.YELLOW + "※足元のブロックが指定されます。");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(CorePvP.COMMAND_NAME + ChatColor.RED + "プレイヤーが実行してください");
            return false;
        }
        if (args.length != 2)  {
            //sender.sendMessage(ChatColor.RED + "使い方: " + usageMessage);
            return false;
        }
        Location pos = ((Player) sender).getLocation();

        if(args[1].equals("spawn")){
            this.setSpawnPos((Player) sender, args[0], pos);
        }else if (args[1].equals("core")){
            this.setCorePos((Player) sender, args[0], pos.add(0, -1, 0));
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
            arguments.add("spawn");
            arguments.add("core");

            return arguments;
        }
        return null;
    }
}
