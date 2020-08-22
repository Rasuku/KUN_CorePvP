package rasuku59.kun.corepvp;

import org.bukkit.entity.Player;

public class PlayerData {

    private final Player player;
    private String team;

    public PlayerData(Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setTeam(String team){
        this.team = team;
    }

    public String getTeam(){
        return team;
    }
}
