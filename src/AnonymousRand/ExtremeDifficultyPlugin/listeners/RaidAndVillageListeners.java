package AnonymousRand.ExtremeDifficultyPlugin.listeners;

import AnonymousRand.ExtremeDifficultyPlugin.customEntities.CustomEntityArrowExploding;
import AnonymousRand.ExtremeDifficultyPlugin.customEntities.CustomEntityLargeFireball;
import AnonymousRand.ExtremeDifficultyPlugin.customEntities.CustomEntityLightning;
import AnonymousRand.ExtremeDifficultyPlugin.customGoals.CustomPathfinderTargetCondition;
import AnonymousRand.ExtremeDifficultyPlugin.util.CoordsFromHypotenuse;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftVillager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.raid.RaidFinishEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

import static org.bukkit.entity.EntityType.VILLAGER;

public class RaidAndVillageListeners implements Listener {

    Random rand = new Random();

    @EventHandler
    public void villagerDamage(EntityDamageByEntityEvent event) {
        if ((event.getEntityType() == VILLAGER)) {
            EntityVillager villager = ((CraftVillager)event.getEntity()).getHandle();
            EntityPlayer player = (villager.getWorld().a(EntityPlayer.class, new CustomPathfinderTargetCondition(), villager, villager.locX(), villager.locY(), villager.locZ(), villager.getBoundingBox().grow(128.0, 128.0, 128.0))); //get closest player within 128 sphere radius of villager

            if (player != null) {
                player.addEffect(new MobEffect(MobEffects.BAD_OMEN, 1000000000, 255));
            }
        }
    }

    @EventHandler
    public void raidFinish(RaidFinishEvent event) {
        summonRaidMeteor(event.getWinners());
    }

    //temp, todo end: delete
    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) && event.hasBlock()) {
            if (event.getClickedBlock().getType().equals(Material.OAK_SIGN)) {
                summonRaidMeteor(event.getPlayer());
            }
        }
    }

    //temp, todo end: delete
    public void summonRaidMeteor(Player player) {
        CoordsFromHypotenuse coordsFromHypotenuse = new CoordsFromHypotenuse();
        Location loc;
        double hypo;
        Location pos;

            loc = player.getLocation();

            for (int i = 0; i < 400; i++) {
                hypo = rand.nextDouble() * 71; //projectiles summoned on an 71 by 71 area around player
                pos = coordsFromHypotenuse.CoordsFromHypotenuseAndAngle(player.getWorld(), new BlockPosition(loc.getX(), loc.getY(), loc.getZ()), hypo, loc.getY() + rand.nextDouble() * 20.0 + 20.0, 361.0); //gets coords for a random angle (0-360) with fixed hypotenuse to summon projectile at

                CustomEntityArrowExploding explodingArrow = new CustomEntityArrowExploding(((CraftWorld)player.getWorld()).getHandle(), new Vector(0.0, -1, 0.0), (byte)(hypo < 12.75 ? 1 : 0), player); //10% of arrows are piercing 1
                explodingArrow.setPosition(pos.getX(), pos.getY(), pos.getZ()); //summon arrow at random spot within 51 by 51 area around player and 20-40 blocks above player
                explodingArrow.getWorld().addEntity(explodingArrow);
            }

            for (int i = 0; i < 375; i++) {
                hypo = rand.nextDouble() * 71; //projectiles summoned on an 71 by 71 area around player
                pos = coordsFromHypotenuse.CoordsFromHypotenuseAndAngle(player.getWorld(), new BlockPosition(loc.getX(), loc.getY(), loc.getZ()), hypo, loc.getY() + rand.nextDouble() * 40.0 + 15.0, 361.0);

                CustomEntityLargeFireball meteorFireball = new CustomEntityLargeFireball(EntityTypes.FIREBALL, ((CraftWorld)player.getWorld()).getHandle(), 2, new Vec3D(0.0, -2.9, 0.0), pos.getX(), pos.getY(), pos.getZ());
                meteorFireball.getWorld().addEntity(meteorFireball);

                //summon wither skulls
            }

            for (int i = 0; i < 360; i += 3) { //summon a ring of lightning at a 75 block radius
                pos = coordsFromHypotenuse.CoordsFromHypotenuseAndAngle(player.getWorld(), new BlockPosition(loc.getX(), loc.getY(), loc.getZ()), 75.0, player.getWorld().getHighestBlockYAt(loc), i);
                CustomEntityLightning lightning = new CustomEntityLightning(((CraftWorld)player.getWorld()).getHandle());
                lightning.setLocation(pos.getX(), pos.getY(), pos.getZ(), 0.0f, 0.0f);
                ((CraftWorld)player.getWorld()).getHandle().addEntity(lightning);
            }
    }

    public void summonRaidMeteor(List<Player> players) {
        CoordsFromHypotenuse coordsFromHypotenuse = new CoordsFromHypotenuse();
        Location loc;
        double hypo;
        Location pos;

        for (Player player : players) {
            loc = player.getLocation();

            for (int i = 0; i < 400; i++) {
                hypo = rand.nextDouble() * 71; //projectiles summoned on an 71 by 71 area around player
                pos = coordsFromHypotenuse.CoordsFromHypotenuseAndAngle(player.getWorld(), new BlockPosition(loc.getX(), loc.getY(), loc.getZ()), hypo, loc.getY() + rand.nextDouble() * 20.0 + 20.0, 361.0); //gets coords for a random angle (0-360) with fixed hypotenuse to summon projectile at

                CustomEntityArrowExploding explodingArrow = new CustomEntityArrowExploding(((CraftWorld)player.getWorld()).getHandle(), new Vector(0.0, -1, 0.0), (byte)(hypo < 12.75 ? 1 : 0), player); //10% of arrows are piercing 1
                explodingArrow.setPosition(pos.getX(), pos.getY(), pos.getZ()); //summon arrow at random spot within 51 by 51 area around player and 20-40 blocks above player
                explodingArrow.getWorld().addEntity(explodingArrow);
            }

            for (int i = 0; i < 375; i++) {
                hypo = rand.nextDouble() * 71; //projectiles summoned on an 71 by 71 area around player
                pos = coordsFromHypotenuse.CoordsFromHypotenuseAndAngle(player.getWorld(), new BlockPosition(loc.getX(), loc.getY(), loc.getZ()), hypo, loc.getY() + rand.nextDouble() * 40.0 + 15.0, 361.0);

                CustomEntityLargeFireball meteorFireball = new CustomEntityLargeFireball(EntityTypes.FIREBALL, ((CraftWorld)player.getWorld()).getHandle(), 2, new Vec3D(0.0, -2.9, 0.0), pos.getX(), pos.getY(), pos.getZ());
                meteorFireball.getWorld().addEntity(meteorFireball);

                //summon wither skulls
            }

            for (int i = 0; i < 360; i += 3) { //summon a ring of lightning at a 75 block radius
                pos = coordsFromHypotenuse.CoordsFromHypotenuseAndAngle(player.getWorld(), new BlockPosition(loc.getX(), loc.getY(), loc.getZ()), 75.0, player.getWorld().getHighestBlockYAt(loc), i);
                CustomEntityLightning lightning = new CustomEntityLightning(((CraftWorld)player.getWorld()).getHandle());
                lightning.setLocation(pos.getX(), pos.getY(), pos.getZ(), 0.0f, 0.0f);
                ((CraftWorld)player.getWorld()).getHandle().addEntity(lightning);
            }
        }
    }
}