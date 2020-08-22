package rasuku59.kun.corepvp;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class Bar {

    private BossBar bar;

    public Bar(){
    }

    public void addPlayer(Player player){
        this.bar.addPlayer(player);
    }

    public BossBar getBossBar(){
        return bar;
    }

    public void createBar(){
        bar = bar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
        bar.setVisible(true);
    }

}
