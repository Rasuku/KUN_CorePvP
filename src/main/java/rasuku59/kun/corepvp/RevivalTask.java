package rasuku59.kun.corepvp;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class RevivalTask extends BukkitRunnable {

    private final Block block;
    private final Material material;

    public RevivalTask(Block block, Material material){
        this.block = block;
        this.material = material;
    }
    @Override
    public void run() {
        block.setType(material);
    }
}
