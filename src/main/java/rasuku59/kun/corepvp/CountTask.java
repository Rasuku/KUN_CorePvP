package rasuku59.kun.corepvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.Objects;

public class CountTask extends BukkitRunnable {

    private final CorePvP plugin;

    private final int second;
    public final int phase;
    private int countDown;

    public int count;
    private final double time;
    private double progress;

    public CountTask(CorePvP plugin){
        this.plugin = plugin;
        this.second = CorePvP.config.getInt("time");
        this.phase = CorePvP.config.getInt("phase");
        this.countDown = CorePvP.config.getInt("countdown");


        this.count = 1;
        this.progress = 1.0;
        this.time = 1.0/(second);
    }

    @Override
    public void run() {
        if(countDown > 0){
            Bukkit.broadcastMessage("開始まであと"+ countDown);
            countDown--;
            return;
        }
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard mainBoard = Objects.requireNonNull(manager).getMainScoreboard();
        Objective objective = mainBoard.getObjective("Stats");
        Score redHP = objective.getScore("§cレッド §f残りHP:");
        redHP.setScore(plugin.redCoreHP);
        Score redPlayers = objective.getScore("§cレッド §f参加人数:");
        redPlayers.setScore(Objects.requireNonNull(mainBoard.getTeam(CorePvP.TEAM_RED)).getSize());
        Score blueHP = objective.getScore(ChatColor.BLUE + "ブルー §f残りHP:");
        blueHP.setScore(plugin.blueCoreHP);
        Score bluePlayers = objective.getScore(ChatColor.BLUE + "ブルー §f参加人数:");
        bluePlayers.setScore(Objects.requireNonNull(mainBoard.getTeam(CorePvP.TEAM_BLUE)).getSize());

        BossBar bar = plugin.bar.getBossBar();
        int sec = (int) (progress * second);
        bar.setTitle("§lフェーズ"+ count +" 残り"+ sec +"秒");
        bar.setProgress(progress);

        if(count != phase){
            progress = progress - time;
        }
        if(progress <= 0){
            count++;
            progress = 1.0;
            switch (count){
                case 2:
                    bar.setColor(BarColor.PINK);
                    Bukkit.broadcastMessage(CorePvP.COMMAND_NAME+"§eコアへの攻撃が可能になりました。");
                    break;
                case 3:
                    bar.setColor(BarColor.GREEN);
                    Bukkit.broadcastMessage(CorePvP.COMMAND_NAME+"§eダイヤモンド鉱石を掘ることができるようになりました。");
                    break;
                case 4:
                    bar.setColor(BarColor.PURPLE);
                    Bukkit.broadcastMessage(CorePvP.COMMAND_NAME+"§eコアへのダメージが二倍になりました。");
                    break;
                case 5:
                    bar.setColor(BarColor.RED);
                    Bukkit.broadcastMessage(CorePvP.COMMAND_NAME+"§eコアが崩壊を始めました。");
                    break;
            }
        }

        if(count == phase){
            bar.setTitle("§l§eコアが崩壊しています");
            if(plugin.redCoreHP > 1){
                plugin.redCoreHP--;
            }
            if(plugin.blueCoreHP > 1){
                plugin.blueCoreHP--;
            }
        }

        String message = null;
        if(plugin.redCoreHP <= 0){
            Bukkit.broadcastMessage(CorePvP.COMMAND_NAME+"§cレッドチーム§aのコアが破壊されました！");
            message = ChatColor.BLUE + "ブルーチームの勝利";
            this.cancel();
        }else if (plugin.blueCoreHP <= 0){
            Bukkit.broadcastMessage(CorePvP.COMMAND_NAME+ChatColor.BLUE+"ブルーチーム§aのコアが破壊されました！");
            message = ChatColor.RED + "レッドチームの勝利";
            this.cancel();
        }

        for (Player player:Bukkit.getOnlinePlayers()) {
            if(message != null){
                player.sendTitle(message, "§aGG", 50, 500, 50);
            }
        }
    }
}
