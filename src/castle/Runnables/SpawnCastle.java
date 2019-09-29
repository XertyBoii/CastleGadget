package castle.Runnables;

import castle.enums.Stage;
import castle.utils.CastleLayers;
import com.mysql.fabric.xmlrpc.base.Array;
import net.minecraft.server.v1_12_R1.Block;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;

public class SpawnCastle extends BukkitRunnable {

    private Plugin plugin;

    private Location origin;
    private ArrayList<ArmorStand> entities;

    private Stage stage;
    private double raisedY = 0D;
    private int ticksShowcased = 0;

    //length and width of the castle (small blocks)
    private int length = 5;
    private int width = 5;

    public SpawnCastle(Location origin, Plugin plugin) {
        this.origin = origin;
        this.plugin = plugin;
        this.entities = new ArrayList<>();
        this.stage = Stage.IDLE;
    }

    @Override
    public void run() {
        switch(this.stage) {
            case IDLE:
                spawnEntities();
                this.stage = Stage.RISING;
                break;
            case RISING:
                update();
                break;
            case SHOWCASE:
                if (ticksShowcased >= 50) {
                    this.stage = Stage.FINSHED;
                } else {
                    ticksShowcased += 1;
                }
                break;
            case FINSHED:
                clearEntities();
                cancel();
                break;
        }
    }

    private void spawnEntities() {
        //Y offset = starting point from origin on Y axis
        double startOffsetY = -1.5D;

        //spacing = space on Y axis between each entity
        double spacing = 0.425D;

        //Util for getting all the castle layers in a nice neat class
        CastleLayers castleLayers = new CastleLayers(this.origin, this.length, this.width, spacing);

        //loops through each layer and adds all of the entities
        castleLayers.layers(startOffsetY).forEach(location -> addEntity(location, Material.COBBLESTONE));
        addKingOfTheCastle(castleLayers.kingLocation(startOffsetY));
    }

    private void addEntity(Location loc, Material hat) {
        ArmorStand as = (ArmorStand) this.origin.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);

        as.setSmall(true);
        as.setGravity(false);
        as.setHelmet(new ItemStack(hat));
        as.setSilent(true);
        as.setArms(false);
        as.setBasePlate(false);
        as.setVisible(false);

        this.entities.add(as);
    }

    private void addKingOfTheCastle(Location loc) {
        ArmorStand as = (ArmorStand) this.origin.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);

        as.setSmall(true);
        as.setGravity(false);
        as.setSilent(true);
        as.setBasePlate(false);
        as.setVisible(true);
        as.setArms(true);
        as.setCustomNameVisible(true);
        as.setCustomName(ChatColor.translateAlternateColorCodes(
                '&',"&6&lKing of the castle"));
        as.setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
        as.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        as.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        as.setBoots(new ItemStack(Material.GOLD_BOOTS));
        as.setLeftArmPose(new EulerAngle(0,1, 0));
        as.setHelmet(new ItemStack(Material.SKULL_ITEM, 1, (short) 3));

        this.entities.add(as);
    }

    private void update() {
        (new BukkitRunnable(){
            public void run() {
                if (SpawnCastle.this.raisedY < 2.3D) {
                    for(ArmorStand as : SpawnCastle.this.entities) {
                        Location loc = as.getLocation().clone();
                        loc.setY(loc.getY() + 0.0375D);
                        loc.setYaw(0.0F);
                        as.teleport(loc);

                        //to tell the difference between the king and the blocks
                        if (!as.isVisible()) {
                            //play particle for block
                            loc.getWorld().spawnParticle(Particle.ITEM_CRACK, loc, 2, 0.3, 0.5, 0.3, 0, as.getHelmet());
                        } else {
                            //play particle for king
                            loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 2, 0.3, 0.5, 0.3, 0);
                        }
                    }

                    //lil bit hacky but we need the world outside of the for loop to play the sound
                    Location first = SpawnCastle.this.entities.get(0).getLocation();
                    first.getWorld().playSound(first, Sound.BLOCK_STONE_PLACE, 1F, 1F);

                    if (SpawnCastle.this.raisedY == 0.0D) {
                        SpawnCastle.this.raisedY = SpawnCastle.this.raisedY + 0.6D;
                    } else {
                        SpawnCastle.this.raisedY = SpawnCastle.this.raisedY + 0.0375D;
                    }

                } else {
                    //finished
                    Location first = SpawnCastle.this.entities.get(0).getLocation();
                    first.getWorld().playSound(first, Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
                    SpawnCastle.this.stage = Stage.SHOWCASE;
                }
            }
        }).runTask(this.plugin);
    }

    private void clearEntities() {
        for(ArmorStand e : this.entities) {
            if (e.isValid()) {
                e.remove();
            }
        }
    }
}
