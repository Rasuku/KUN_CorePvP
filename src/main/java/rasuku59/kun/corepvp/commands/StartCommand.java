package rasuku59.kun.corepvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import rasuku59.kun.corepvp.CorePvP;
import rasuku59.kun.corepvp.StartTask;

import java.util.List;

public class StartCommand implements TabExecutor {

    private final CorePvP plugin;

    public StartCommand(CorePvP plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        new StartTask(plugin).runTaskLater(plugin, CorePvP.config.getInt("countdown") * 20);
        plugin.task.runTaskTimer(plugin, 0, 20);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
