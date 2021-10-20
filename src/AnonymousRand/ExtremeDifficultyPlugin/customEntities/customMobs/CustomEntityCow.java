package AnonymousRand.ExtremeDifficultyPlugin.customEntities.customMobs;

import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class CustomEntityCow extends EntityCow {

    public CustomEntityCow(World world) {
        super(EntityTypes.COW, world);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.ticksLived == 10) { /**cows move twice as fast and have 20 health*/
            this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.4);
            this.setHealth(20.0f);
            ((LivingEntity)this.getBukkitEntity()).setMaxHealth(20.0);
        }

        Location thisLoc = new Location(this.getWorld().getWorld(), this.locX(), this.locY(), this.locZ());
        if (thisLoc.getBlock().getType() == org.bukkit.Material.COBWEB) { /**non-player mobs gain Speed 11 while in a cobweb (approx original speed)*/
            this.addEffect(new MobEffect(MobEffects.FASTER_MOVEMENT, 2, 10));
        }
    }
}