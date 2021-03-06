package AnonymousRand.anonymousrand.extremedifficultyplugin.customentities.custommobs;

import AnonymousRand.anonymousrand.extremedifficultyplugin.customgoals.CustomPathfinderGoalNearestAttackableTarget;
import AnonymousRand.anonymousrand.extremedifficultyplugin.customgoals.NewPathfinderGoalCobwebMoveFaster;
import AnonymousRand.anonymousrand.extremedifficultyplugin.customgoals.NewPathfinderGoalGetBuffedByMobs;
import AnonymousRand.anonymousrand.extremedifficultyplugin.util.SpawnEntity;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class CustomEntityGuardian extends EntityGuardian implements ICustomMob {

    public int attacks;
    private boolean a8, a12, a40;

    public CustomEntityGuardian(World world) {
        super(EntityTypes.GUARDIAN, world);
        this.a(PathType.LAVA, 0.0F); /**no longer avoids lava*/
        this.a(PathType.DAMAGE_FIRE, 0.0F); /**no longer avoids fire*/
        this.attacks = 0;
        this.a8 = false;
        this.a12 = false;
        this.a40 = false;
    }

    @Override
    public void initPathfinder() {
        PathfinderGoalMoveTowardsRestriction pathfindergoalmovetowardsrestriction = new PathfinderGoalMoveTowardsRestriction(this, 1.0D);
        this.goalRandomStroll = new PathfinderGoalRandomStroll(this, 1.0D, 80);
        this.goalSelector.a(0, new NewPathfinderGoalCobwebMoveFaster(this)); /**custom goal that allows non-player mobs to still go fast in cobwebs*/
        this.goalSelector.a(0, new NewPathfinderGoalGetBuffedByMobs(this)); /**custom goal that allows this mob to take certain buffs from bats etc.*/
        this.goalSelector.a(4, new CustomEntityGuardian.PathfinderGoalGuardianAttack(this));
        this.goalSelector.a(5, pathfindergoalmovetowardsrestriction);
        this.goalSelector.a(7, this.goalRandomStroll);
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityPlayer.class, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityGuardian.class, 12.0F, 0.01F));
        this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));
        this.goalRandomStroll.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
        pathfindergoalmovetowardsrestriction.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
        this.targetSelector.a(0, new CustomPathfinderGoalNearestAttackableTarget<>(this, EntityLiving.class, 10, false, false, new CustomEntityGuardian.EntitySelectorGuardianTargetHumanSquid(this))); /**uses the custom goal which doesn't need line of sight to start attacking (passes to CustomPathfinderGoalNearestAttackableTarget.g() which passes to CustomIEntityAccess.customFindPlayer() which passes to CustomIEntityAccess.customFindEntity() which passes to CustomPathfinderTargetConditions.a() which removes line of sight requirement)*/
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (!this.eO() && !damagesource.isMagic() && damagesource.j() instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving)damagesource.j();

            if (!damagesource.isExplosion()) {
                entityliving.damageEntity(DamageSource.a(this), f * 0.5F); /**thorns damage increased from 2 to 50% of the damage dealt*/
                entityliving.addEffect(new MobEffect(MobEffects.SLOWER_DIG, 400, this.attacks < 55 ? 0 : 1)); /**guardians give players that hit them mining fatigue 1 (2 after 55 attacks) for 20 seconds*/
            }
        }

        if (this.goalRandomStroll != null) {
            this.goalRandomStroll.h();
        }

        return super.damageEntity(damagesource, f);
    }

    public double getFollowRange() { /**guardians have 24 block detection range (setting attribute doesn't work) (32 after 8 attacks)*/
        return this.attacks < 8 ? 24.0 : 32.0;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.attacks == 8 && !this.a8) {
            this.a8 = true;
            this.targetSelector.a(0, new CustomPathfinderGoalNearestAttackableTarget<>(this, EntityPlayer.class, true)); //updates follow range
        }

        if (this.attacks == 12 && !this.a12) { /**after 12 attacks, guardians gain regen 3 and 40 max health*/
            this.a12 = true;
            ((LivingEntity)this.getBukkitEntity()).setMaxHealth(40.0);
            this.addEffect(new MobEffect(MobEffects.REGENERATION, Integer.MAX_VALUE, 2));
        }

        if (this.attacks == 40 && !this.a40) { /**after 40 attacks, guardians summon an elder guardian*/
            this.a40 = true;
            new SpawnEntity(this.getWorld(), new CustomEntityGuardianElder(this.getWorld()), 1, null, null, this, false, true);
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

    static class PathfinderGoalGuardianAttack extends PathfinderGoal { /**guardian no longer stops attacking if player is too close*/

        private final CustomEntityGuardian guardian;
        private int b;
        private final boolean isElder;

        public PathfinderGoalGuardianAttack(CustomEntityGuardian entityguardian) {
            this.guardian = entityguardian;
            this.isElder = false;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
        }

        @Override
        public boolean a() {
            EntityLiving entityliving = this.guardian.getGoalTarget();

            return entityliving != null && entityliving.isAlive();
        }

        @Override
        public void c() {
            this.b = -10;
            this.guardian.getNavigation().o();
            this.guardian.getControllerLook().a(this.guardian.getGoalTarget(), 90.0F, 90.0F);
            this.guardian.impulse = true;
        }

        @Override
        public void d() {
            this.guardian.a(0);
            this.guardian.setGoalTarget((EntityLiving) null);
            this.guardian.goalRandomStroll.h();
        }

        @Override
        public void e() {
            EntityLiving entityliving = this.guardian.getGoalTarget();

            this.guardian.getNavigation().o();
            this.guardian.getControllerLook().a(entityliving, 90.0F, 90.0F);

            if (entityliving != null) {
                ++this.b; /**laser no longer disengages when there is a block between guardian and player*/

                if (this.b == 0) {
                    this.guardian.a(this.guardian.getGoalTarget().getId());
                    if (!this.guardian.isSilent()) {
                        this.guardian.world.broadcastEntityEffect(this.guardian, (byte) 21);
                    }
                } else if (this.b >= this.guardian.eL()) {
                    float f = 1.0F;

                    if (this.guardian.world.getDifficulty() == EnumDifficulty.HARD) {
                        f += 2.0F;
                    }

                    if (this.isElder) {
                        f += 2.0F;
                    }

                    this.guardian.attacks++;
                    entityliving.damageEntity(DamageSource.c(this.guardian, this.guardian), f);
                    entityliving.damageEntity(DamageSource.mobAttack(this.guardian), (float)this.guardian.b(GenericAttributes.ATTACK_DAMAGE));
                    this.guardian.setGoalTarget((EntityLiving)null);
                }

                if (this.b >= this.guardian.eL() / 2.5 && this.guardian.ticksLived % (this.guardian.attacks < 10 ? 4 : 3) == 0) { /**tractor beam-like effect every 4 ticks (3 after 10 attacks) for the latter 60% of the laser charging period*/
                    LivingEntity bukkitEntity = (LivingEntity)entityliving.getBukkitEntity();
                    bukkitEntity.setVelocity(new Vector((this.guardian.locX() - bukkitEntity.getLocation().getX()) / 48.0, (this.guardian.locY() - bukkitEntity.getLocation().getY()) / 48.0, (this.guardian.locZ() - bukkitEntity.getLocation().getZ()) / 48.0));

                    if (this.guardian.attacks >= 35) { /**after 35 attacks, guardians inflict poison 1 while the tractor beam is engaged*/
                        if (this.guardian.attacks >= 55) { /**after 55 attacks, guardians inflict hunger 1 and weakness 1 while the tractor beam is engaged*/
                            entityliving.addEffect(new MobEffect(MobEffects.HUNGER, 51, 0));
                            entityliving.addEffect(new MobEffect(MobEffects.WEAKNESS, 51, 0));
                        }

                        entityliving.addEffect(new MobEffect(MobEffects.POISON, 51, 0));
                    }
                }
            }

            super.e();
        }
    }

    static class EntitySelectorGuardianTargetHumanSquid implements Predicate<EntityLiving> {

        private final EntityGuardian a;

        public EntitySelectorGuardianTargetHumanSquid(EntityGuardian entityguardian) {
            this.a = entityguardian;
        }

        public boolean test(@Nullable EntityLiving entityliving) {
            return (entityliving instanceof EntityHuman || entityliving instanceof EntitySquid) && entityliving.h((Entity)this.a) > 9.0D;
        }
    }
}
