package rasuku59.kun.corepvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import rasuku59.kun.corepvp.CorePvP;

import java.util.ArrayList;
import java.util.List;

public class SetWeaponCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(CorePvP.COMMAND_NAME + ChatColor.RED + "プレイヤーが実行してください");
            return false;
        }
        if (args.length != 2){
            return false;
        }
        if(args[0].equals(CorePvP.TEAM_RED)){
            CorePvP.redWeapons = ((Player) sender).getInventory().getContents();
            sender.sendMessage(CorePvP.COMMAND_NAME + ChatColor.RED + "レッドチーム"+ChatColor.GREEN+"のアイテムを設定しました。");
        }else if (args[0].equals(CorePvP.TEAM_BLUE)){
            CorePvP.blueWeapons = ((Player) sender).getInventory().getContents();
            sender.sendMessage(CorePvP.COMMAND_NAME + ChatColor.BLUE + "ブルーチーム"+ChatColor.GREEN+"のアイテムを設定しました。");
        }
        sender.sendMessage(ChatColor.RED + "※持っているアイテムが適用されます");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add(CorePvP.TEAM_RED);
            arguments.add(CorePvP.TEAM_BLUE);

            return arguments;
        }
        return null;
    }
}
