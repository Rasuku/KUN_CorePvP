package rasuku59.kun.corepvp;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;
import rasuku59.kun.corepvp.commands.*;

import java.util.Objects;

public final class CorePvP extends JavaPlugin implements Listener {

    public static final String COMMAND_NAME = ChatColor.AQUA + "[CorePvP] ";

    public static final String OBJECTIVE_NAME = "show_health";

    public static final String TEAM_RED = "red";
    public static final String TEAM_BLUE = "blue";

    public static FileConfiguration config;

    public static ItemStack[] redWeapons;
    public static ItemStack[] blueWeapons;

    public static Location redCorePos;
    public static Location blueCorePos;

    public static Location redSpawnPos;
    public static Location blueSpawnPos;

    public int redCoreHP;
    public int blueCoreHP;

    public Bar bar;

    public CountTask task;

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(this, this);

        this.bar = new Bar();
        bar.createBar();

        new Data();

        saveDefaultConfig();//config.ymlが存在しない場合はファイルに出力します。
        config = getConfig();

        int hp = config.getInt("coreHP");
        this.redCoreHP = hp;
        this.blueCoreHP = hp;

        Objects.requireNonNull(getCommand("ct")).setExecutor(new TeamCommand());
        Objects.requireNonNull(getCommand("randteam")).setExecutor(new RandomTeamCommand());
        Objects.requireNonNull(getCommand("setting")).setExecutor(new SettingCommand());
        Objects.requireNonNull(getCommand("start")).setExecutor(new StartCommand(this));
        Objects.requireNonNull(getCommand("end")).setExecutor(new EndCommand(this));
        Objects.requireNonNull(getCommand("setweapon")).setExecutor(new SetWeaponCommand());

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = Objects.requireNonNull(manager).getMainScoreboard();

        Objective objective = board.getObjective("show_health");
        if ( objective == null ) {
            objective = board.registerNewObjective("show_health", "health");
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName("§c❤");
        }

        Objective objective1 = board.getObjective("Stats");
        if (objective1 == null){
            objective1 = board.registerNewObjective("Stats", "dummy");
            objective1.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective1.setDisplayName("§b§lCorePvP");
        }

        Team teamRed = board.getTeam(TEAM_RED);
        if ( teamRed == null ) {
            teamRed = board.registerNewTeam(TEAM_RED);
            teamRed.setColor(ChatColor.RED);
            teamRed.setAllowFriendlyFire(false);
        }
        Team teamBlue = board.getTeam(TEAM_BLUE);
        if ( teamBlue == null ) {
            teamBlue = board.registerNewTeam(TEAM_BLUE);
            teamBlue.setColor(ChatColor.BLUE);
            teamBlue.setAllowFriendlyFire(false);
        }

        task = new CountTask(this);
    }

    @Override
    public void onDisable() {
        bar.getBossBar().removeAll();
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = Objects.requireNonNull(manager).getMainScoreboard();
        Objects.requireNonNull(board.getTeam(TEAM_RED)).unregister();
        Objects.requireNonNull(board.getTeam(TEAM_BLUE)).unregister();

        Objects.requireNonNull(board.getObjective("show_health")).unregister();
        Objects.requireNonNull(board.getObjective("Stats")).unregister();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Data.getInstance().registerPlayerData(player);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        PlayerData data = Data.getInstance().getPlayerData(player);
        switch (data.getTeam()){
            case TEAM_RED:
                event.setRespawnLocation(redSpawnPos);
                break;
            case TEAM_BLUE:
                event.setRespawnLocation(blueSpawnPos);
                break;
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
        }
        Block block = event.getBlock();
        Location location = block.getLocation();
        String team = null;
        if(task.count != 1) {
            if (redCorePos.getBlockX() == location.getBlockX() && redCorePos.getBlockY() == location.getBlockY() && redCorePos.getBlockZ() == location.getBlockZ()) {
                redCoreHP--;
                if (task.count == task.phase-1) {
                    redCoreHP--;
                }
                team = TEAM_RED;
            } else if (blueCorePos.getBlockX() == location.getBlockX() && blueCorePos.getBlockY() == location.getBlockY() && blueCorePos.getBlockZ() == location.getBlockZ()) {
                blueCoreHP--;
                if (task.count == task.phase-1) {
                    blueCoreHP--;
                }
                team = TEAM_BLUE;
            }

            if (team != null) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    PlayerData data = Data.getInstance().getPlayerData(p);
                    if (team.equals(TEAM_RED) && data.getTeam().equals(TEAM_RED)) {
                        p.sendTitle("", "§eコアが削られています！", 5, 10, 5);
                    } else if (team.equals(TEAM_BLUE) && data.getTeam().equals(TEAM_BLUE)) {
                        p.sendTitle("", "§eコアが削られています！", 5, 10, 5);
                    }

                }
            }
        }else{
            player.sendMessage(CorePvP.COMMAND_NAME + "§eフェーズ2まではコアを破壊できません。");
        }

        if (task.count < 3 && block.getType() == Material.DIAMOND_ORE) {
            player.sendMessage(CorePvP.COMMAND_NAME + "§eフェーズ3まではダイヤモンドを掘ることができません。");
            return;
        }
        if(block.getType() == Material.MELON){
            new RevivalTask(block, Material.MELON).runTaskLater(this, config.getInt("revivalMelon") * 20);
        }else if(block.getType() == Material.OAK_LOG){
            new RevivalTask(block, Material.OAK_LOG).runTaskLater(this, config.getInt("revivalOakWood") * 20);
        }else if (block.getType() == Material.COAL_ORE){
            new RevivalTask(block, Material.COAL_ORE).runTaskLater(this, config.getInt("revivalCoalOre") * 20);
        }else if(block.getType() == Material.IRON_ORE) {
            new RevivalTask(block, Material.IRON_ORE).runTaskLater(this, config.getInt("revivalIronOre") * 20);
        }else if (block.getType() == Material.DIAMOND_ORE) {
            new RevivalTask(block, Material.DIAMOND_ORE).runTaskLater(this, config.getInt("revivalDiamondOre") * 20);
        }else{
            return;
        }
        if(block.getType() == Material.MELON){
            ItemStack item = new ItemStack(Material.MELON_SLICE, 3);
            player.getInventory().addItem(item);
        }else{
            player.getInventory().addItem(block.getDrops().toArray(new ItemStack[0]));
        }
        block.setType(Material.COBBLESTONE);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(player.getGameMode() == GameMode.SURVIVAL){
            event.setCancelled(true);
        }
    }

}
