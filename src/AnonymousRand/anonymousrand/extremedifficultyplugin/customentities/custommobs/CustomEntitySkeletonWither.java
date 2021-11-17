package AnonymousRand.anonymousrand.extremedifficultyplugin.customentities.custommobs;

import AnonymousRand.anonymousrand.extremedifficultyplugin.customgoals.*;
import AnonymousRand.anonymousrand.extremedifficultyplugin.util.RemovePathfinderGoals;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomEntitySkeletonWither extends EntitySkeletonWither implements ICommonCustomMethods {

    public static JavaPlugin plugin;
    public PathfinderGoalSelector targetSelectorVanilla;
    public int attacks;
    private boolean a25, a55;

    public CustomEntitySkeletonWither(World world) {
        super(EntityTypes.WITHER_SKELETON, world);
        this.targetSelectorVanilla = super.targetSelector;
        this.a(PathType.DAMAGE_FIRE, 0.0F); /**no longer avoids fire*/
        this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.STONE_SWORD)); //makes sure that it has a sword
        this.attacks = 0;
        this.a25 = false;
        this.a55 = false;
        this.getAttributeInstance(GenericAttributes.ATTACK_KNOCKBACK).setValue(5.0); /**wither skeletons twice as fast and have extra knockback*/
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.5);
        RemovePathfinderGoals.removePathfinderGoals(this); //remove vanilla HurtByTarget and NearestAttackableTarget goals and replace them with custom ones
    }

    @Override
    public void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(0, new NewPathfinderGoalBreakBlocksAround(this, 20, 1, 1, 1, 1, true)); /**custom goal that breaks blocks around the mob periodically*/
        this.goalSelector.a(0, new NewPathfinderGoalCobwebMoveFaster(this)); /**custom goal that allows non-player mobs to still go fast in cobwebs*/
        this.goalSelector.a(0, new NewPathfinderGoalGetBuffedByMobs(this)); /**custom goal that allows this mob to take certain buffs from bats etc.*/
        this.goalSelector.a(1, new PathfinderGoalWitherSpawnBlocksEntitiesOnMob(this, org.bukkit.Material.SOUL_SOIL, 1, 1, 0, 1, -1.0)); /**custom goal that allows wither skeleton to summon soul sand in a 3 by 3 beneath itself constantly*/
        this.goalSelector.a(1, new PathfinderGoalWitherSpawnBlocksEntitiesOnMob(this, org.bukkit.Material.WITHER_ROSE, 1, 0, 0, 0, 0)); /**custom goal that allows wither skeleton to summon wither roses on itself constantly*/
        this.goalSelector.a(1, new CustomPathfinderGoalMeleeAttack(this, 1.0D, true)); /**uses the custom melee attack goal that attacks even when line of sight is broken*/
        this.targetSelector.a(1, new CustomPathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, false)); /**uses the custom goal which doesn't need line of sight to start attacking (passes to CustomPathfinderGoalNearestAttackableTarget.g() which passes to CustomIEntityAccess.customFindPlayer() which passes to CustomIEntityAccess.customFindEntity() which passes to CustomPathfinderTargetConditions.a() which removes line of sight requirement)*/
    }

    @Override
    public boolean attackEntity(Entity entity) {
        if (!super.attackEntity(entity)) {
            return false;
        } else {
            if (entity instanceof EntityHuman) {
                ((EntityHuman)entity).addEffect(new MobEffect(MobEffects.WITHER, this.attacks < 12 ? 400 : this.attacks < 35 ? 600 : 900)); /**wither skeletons apply wither for 20 seconds instead of 10 (30 seconds after 12 attacks, 45 seconds after 35 attacks0*/
            }

            return true;
        }
    }

    public double getFollowRange() { /**wither skeletons have 40 blocks detection range*/
        return 40.0;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.attacks == 25 && !this.a25) { /**after 25 attacks, wither skeletons get regen 2 and 30 max health*/
            this.a25 = true;
            this.addEffect(new MobEffect(MobEffects.REGENERATION, Integer.MAX_VALUE, 1));
            ((LivingEntity)this.getBukkitEntity()).setMaxHealth(30.0);
        }

        if (this.attacks == 55 && !this.a55) { /**after 55 attacks, wither skeletons summon a mini wither*/
            this.a55 = true;
            //todo: custom wither
        }
    }

    @Override
    public void checkDespawn() {
        if (this.getWorld().getDifficulty() == EnumDifficulty.PEACEFUL && this.L()) {
            this.die();
        } else if (!this.isPersistent() && !this.isSpecialPersistence()) {
            EntityHuman entityhuman = this.getWorld().findNearbyPlayer(this, -1.0D);

            if (entityhuman != null) {
                double d0 = Math.pow(entityhuman.getPositionVector().getX() - this.getPositionVector().getX(), 2) + Math.pow(entityhuman.getPositionVector().getZ() - this.getPositionVector().getZ(), 2); /**mobs only despawn along horizontal axes; if you are at y level 256 mobs will still spawn below you at y64 and prevent sleepingdouble d0 = entityhuman.h(this);*/
                int i = this.getEntityType().e().f();
                int j = i * i;

                if (d0 > (double)j && this.isTypeNotPersistent(d0)) {
                    this.die();
                }

                int k = this.getEntityType().e().g() + 8; /**random despawn distance increased to 40 blocks*/
                int l = k * k;

                if (this.ticksFarFromPlayer > 600 && random.nextInt(800) == 0 && d0 > (double)l && this.isTypeNotPersistent(d0)) {
                    this.die();
                } else if (d0 < (double)l) {
                    this.ticksFarFromPlayer = 0;
                }
            }

        } else {
            this.ticksFarFromPlayer = 0;
        }
    }

    @Override
    public double g(double d0, double d1, double d2) {
        double d3 = this.locX() - d0; /**for determining distance to entities, y level does not matter, eg. mob follow range, attacking (can hit player no matter the y level)*/
        double d5 = this.locZ() - d2;

        return d3 * d3 + d5 * d5;
    }

    @Override
    public double d(Vec3D vec3d) {
        double d0 = this.locX() - vec3d.x; /**for determining distance to entities, y level does not matter, eg. mob follow range, attacking (can hit player no matter the y level)*/
        double d2 = this.locZ() - vec3d.z;

        return d0 * d0 + d2 * d2;
    }

    @Override
    public int bL() { //getMaxFallHeight
        if (this.getGoalTarget() == null) {
            return 3;
        } else {
            int i = (int)(this.getHealth() * 20.0); /**mobs are willing to take 20 times the fall distance (same damage) to reach and do not stop taking falls if it is at less than 33% health*/

            return i + 3;
        }
    }

    static class PathfinderGoalWitherSpawnBlocksEntitiesOnMob extends NewPathfinderGoalSpawnBlocksEntitiesOnMob {
        public PathfinderGoalWitherSpawnBlocksEntitiesOnMob(EntityInsentient entity, org.bukkit.Material material, int delayTimer, int xRadius, int yRadius, int zRadius, double yOffset) {
            super(entity, material, delayTimer, xRadius, yRadius, zRadius, yOffset, true);
        }

        @Override
        public boolean a() { /**wither only creates soul soil bridge underneath itself if it doesn't need to get down to player*/
            if (super.a()) {
                return this.entity.getGoalTarget().locY() >= this.entity.locY();
            }

            return false;
        }
    }
}