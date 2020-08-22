package rasuku59.kun.corepvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import rasuku59.kun.corepvp.Bar;
import rasuku59.kun.corepvp.CorePvP;
import rasuku59.kun.corepvp.CountTask;

import java.util.List;
import java.util.Objects;

public class EndCommand implements TabExecutor {

    private final CorePvP plugin;

    public EndCommand(CorePvP plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = Objects.requireNonNull(manager).getMainScoreboard();

        Objects.requireNonNull(board.getObjective("Stats")).unregister();

        Objective objective1 = board.getObjective("Stats");
        if (objective1 == null){
            objective1 = board.registerNewObjective("Stats", "dummy");
            objective1.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective1.setDisplayName("§b§lCorePvP");
        }
        plugin.task.cancel();
        plugin.task = new CountTask(plugin);

        plugin.bar.getBossBar().removeAll();
        plugin.bar = new Bar();
        plugin.bar.createBar();

        int hp = CorePvP.config.getInt("coreHP");
        plugin.redCoreHP = hp;
        plugin.blueCoreHP = hp;

        Bukkit.broadcastMessage(CorePvP.COMMAND_NAME + "§f"+sender.getName()+"が§c試合をキャンセルしました。");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
