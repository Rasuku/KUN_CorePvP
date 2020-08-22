package rasuku59.kun.corepvp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class StartTask extends BukkitRunnable {

    private final CorePvP plugin;

    public StartTask(CorePvP plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Bar bar = plugin.bar;
        for (Player player: Bukkit.getOnlinePlayers()) {

            bar.addPlayer(player);

            PlayerData data = Data.getInstance().getPlayerData(player);
            player.sendTitle("試合開始", "がんばれ", 20, 60, 20);
            if(data.getTeam() == null) continue;
            switch (data.getTeam()){
                case CorePvP.TEAM_RED:
                    player.teleport(CorePvP.redSpawnPos);
                    if(CorePvP.redWeapons != null){
                        player.getInventory().addItem(CorePvP.redWeapons);
                    }
                    break;
                case CorePvP.TEAM_BLUE:
                    player.teleport(CorePvP.blueSpawnPos);
                    if(CorePvP.blueWeapons != null){
                        player.getInventory().addItem(CorePvP.blueWeapons);
                    }
                    break;
            }
        }
    }
}
