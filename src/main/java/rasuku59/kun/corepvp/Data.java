package rasuku59.kun.corepvp;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Data {

    private static Data instance;

    private final Map<String, PlayerData> playerData = new HashMap<>();

    public Data(){
        Data.instance = this;
    }

    public static Data getInstance() {
        return Data.instance;
    }

    public void registerPlayerData(Player player){
        playerData.put(player.getName(), new PlayerData(player));
    }

    public PlayerData getPlayerData(Player player){
        return playerData.get(player.getName());
    }

}
