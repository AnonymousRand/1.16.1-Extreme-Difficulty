package AnonymousRand.anonymousrand.extremedifficultyplugin.listeners;

import AnonymousRand.anonymousrand.extremedifficultyplugin.customentities.custommobs.*;
import AnonymousRand.anonymousrand.extremedifficultyplugin.customentities.misc.CustomEntityAreaEffectCloud;
import AnonymousRand.anonymousrand.extremedifficultyplugin.customentities.misc.CustomEntityLightning;
import AnonymousRand.anonymousrand.extremedifficultyplugin.util.SpawnEntity;
import AnonymousRand.anonymousrand.extremedifficultyplugin.util.StaticPlugin;
import AnonymousRand.anonymousrand.extremedifficultyplugin.util.bukkitrunnables.RunnableLightningStorm;
import AnonymousRand.anonymousrand.extremedifficultyplugin.util.bukkitrunnables.RunnableMeteorRain;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftProjectile;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Egg;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ListenerEggs implements Listener {
    @EventHandler
    public void eggLand(ProjectileHitEvent event) { /**thrown eggs have chances to summon mobs/effects etc.*/
        if (event.getEntity() instanceof Egg) {
            new RunnableEggLand(event.getEntity());
        }
    }

    static class RunnableEggLand extends BukkitRunnable {

        private final Projectile bukkitEgg;
        private final IProjectile nmsEgg;
        private final LivingEntity bukkitPlayer;
        private final EntityLiving nmsPlayer;
        private final Location playerLoc, eggLoc;
        private final org.bukkit.World bukkitWorld;
        private final World nmsWorld;
        private double rand;
        private static final Random random = new Random();

        public RunnableEggLand(Projectile bukkitEgg) {
            this.bukkitEgg = bukkitEgg;
            this.nmsEgg = ((CraftProjectile)bukkitEgg).getHandle();

            if (bukkitEgg.getShooter() != null) {
                this.bukkitPlayer = (LivingEntity)bukkitEgg.getShooter();
                this.nmsPlayer = ((CraftLivingEntity)this.bukkitPlayer).getHandle();
                this.playerLoc = this.bukkitPlayer.getLocation();
            } else {
                this.bukkitPlayer = null;
                this.nmsPlayer = null;
                this.playerLoc = null;
                this.eggLoc = null;
                this.bukkitWorld = null;
                this.nmsWorld = null;
                return;
            }

            this.eggLoc = bukkitEgg.getLocation();
            this.bukkitWorld = bukkitEgg.getWorld();
            this.nmsWorld = ((CraftProjectile)bukkitEgg).getHandle().getWorld();
            this.run();
        }

        @Override
        public void run() {
            this.rand = random.nextDouble();

            if (rand >= 0.5) {
                MobEffect nmsMobEffect;
                int rand2 = random.nextInt(39) + 1;
                switch (rand2) {
                    case 1 -> nmsMobEffect = new MobEffect(MobEffects.ABSORBTION, 6000, 2);
                    case 2 -> nmsMobEffect = new MobEffect(MobEffects.BAD_OMEN, 36000, 0);
                    case 3 -> nmsMobEffect = new MobEffect(MobEffects.BLINDNESS, 150, 0);
                    case 4 -> nmsMobEffect = new MobEffect(MobEffects.CONDUIT_POWER, 1200, 0);
                    case 5 -> nmsMobEffect = new MobEffect(MobEffects.DOLPHINS_GRACE, 300, 0);
                    case 6 -> nmsMobEffect = new MobEffect(MobEffects.FIRE_RESISTANCE, 700, 0);
                    case 7 -> nmsMobEffect = new MobEffect(MobEffects.FASTER_DIG, 800, 1);
                    case 8 -> nmsMobEffect = new MobEffect(MobEffects.FASTER_DIG, 500, 3);
                    case 9 -> nmsMobEffect = new MobEffect(MobEffects.HEALTH_BOOST, 700, 4);
                    case 10 -> nmsMobEffect = new MobEffect(MobEffects.HUNGER, 1200, 2);
                    case 11 -> nmsMobEffect = new MobEffect(MobEffects.HARM, 1, 2);
                    case 12 -> nmsMobEffect = new MobEffect(MobEffects.HEAL, 36000, 1);
                    case 13 -> nmsMobEffect = new MobEffect(MobEffects.INVISIBILITY, 200, 0);
                    case 14 -> nmsMobEffect = new MobEffect(MobEffects.JUMP, 500, 2);
                    case 15 -> nmsMobEffect = new MobEffect(MobEffects.LEVITATION, 300, 0);
                    case 16 -> nmsMobEffect = new MobEffect(MobEffects.SLOWER_DIG, 3000, 0);
                    case 17 -> nmsMobEffect = new MobEffect(MobEffects.NIGHT_VISION, 2400, 0);
                    case 18 -> nmsMobEffect = new MobEffect(MobEffects.POISON, 300, 1);
                    case 19 -> nmsMobEffect = new MobEffect(MobEffects.POISON, 150, 2);
                    case 20 -> nmsMobEffect = new MobEffect(MobEffects.REGENERATION, 500, 1);
                    case 21 -> nmsMobEffect = new MobEffect(MobEffects.REGENERATION, 100, 3);
                    case 22 -> nmsMobEffect = new MobEffect(MobEffects.RESISTANCE, 1200, 0);
                    case 23 -> nmsMobEffect = new MobEffect(MobEffects.RESISTANCE, 600, 1);
                    case 24 -> nmsMobEffect = new MobEffect(MobEffects.RESISTANCE, 300, 2);
                    case 25 -> nmsMobEffect = new MobEffect(MobEffects.SATURATION, 8, 0);
                    case 26 -> nmsMobEffect = new MobEffect(MobEffects.SLOW_FALLING, 400, 0);
                    case 27 -> nmsMobEffect = new MobEffect(MobEffects.SLOWER_MOVEMENT, 600, 0);
                    case 28 -> nmsMobEffect = new MobEffect(MobEffects.SLOWER_MOVEMENT, 400, 2);
                    case 29 -> nmsMobEffect = new MobEffect(MobEffects.SLOWER_MOVEMENT, 280, 3);
                    case 30 -> nmsMobEffect = new MobEffect(MobEffects.FASTER_MOVEMENT, 600, 0);
                    case 31 -> nmsMobEffect = new MobEffect(MobEffects.FASTER_MOVEMENT, 400, 1);
                    case 32 -> nmsMobEffect = new MobEffect(MobEffects.FASTER_MOVEMENT, 280, 2);
                    case 33 -> nmsMobEffect = new MobEffect(MobEffects.INCREASE_DAMAGE, 280, 0);
                    case 34 -> nmsMobEffect = new MobEffect(MobEffects.INCREASE_DAMAGE, 140, 1);
                    case 35 -> nmsMobEffect = new MobEffect(MobEffects.WATER_BREATHING, 1200, 0);
                    case 36 -> nmsMobEffect = new MobEffect(MobEffects.WEAKNESS, 500, 0);
                    case 37 -> nmsMobEffect = new MobEffect(MobEffects.WEAKNESS, 250, 1);
                    case 38 -> nmsMobEffect = new MobEffect(MobEffects.WITHER, 600, 0);
                    default -> nmsMobEffect = new MobEffect(MobEffects.WITHER, 130, 2);
                }

                if (rand < 0.55) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityChickenAggressive(this.nmsWorld), 4, null, this.playerLoc, true);
                } else if (rand < 0.58) {
                    this.nmsPlayer.addEffect(nmsMobEffect);
                } else if (rand < 0.605) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.IRON_INGOT));
                } else if (rand < 0.63) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.ARROW, random.nextInt(3) + 1));
                } else if (rand < 0.65) {
                    this.bukkitWorld.spawn(this.eggLoc, ExperienceOrb.class).setExperience(random.nextInt(326) + 175);
                } else if (rand < 0.67) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityCreeper(this.nmsWorld, 15), 1, null, this.playerLoc, true);
                } else if (rand < 0.69) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityZombie(this.nmsWorld), 1, null, this.playerLoc, true);
                } else if (rand < 0.7075) {
                    ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
                    itemStack.setItemMeta(itemMeta);
                    this.bukkitWorld.dropItem(this.eggLoc, itemStack);
                } else if (rand < 0.7225) {
                    CustomEntityAreaEffectCloud newAEC = new CustomEntityAreaEffectCloud(this.nmsWorld, 5, 1, 0);
                    newAEC.addEffect(nmsMobEffect);
                    newAEC.setPosition(this.eggLoc.getX(), this.eggLoc.getY(), this.eggLoc.getZ());
                    this.nmsWorld.addEntity(newAEC);
                } else if (rand < 0.735) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.OBSIDIAN, 5));
                } else if (rand < 0.746) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityWitch(this.nmsWorld), 1, null, this.playerLoc, true);
                } else if (rand < 0.756) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityChickenAggressive(this.nmsWorld), 12, null, this.playerLoc, true);
                } else if (rand < 0.766) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityGhast(this.nmsWorld), 1, null, this.playerLoc, false);
                } else if (rand < 0.776) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityLightning(this.nmsWorld), 1, null, this.eggLoc, false);
                } else if (rand < 0.786) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.DIAMOND));
                } else if (rand < 0.796) {
                    ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(Enchantment.SWEEPING_EDGE, 1, false);
                    itemStack.setItemMeta(itemMeta);
                    this.bukkitWorld.dropItem(this.eggLoc, itemStack);
                } else if (rand < 0.806) {
                    ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
                    itemStack.setItemMeta(itemMeta);
                    this.bukkitWorld.dropItem(this.eggLoc, itemStack);
                } else if (rand < 0.816) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.GOLDEN_APPLE));
                } else if (rand < 0.824) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityEnderman(this.nmsWorld), 1, null, this.eggLoc, true);
                } else if (rand < 0.832) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityShulker(this.nmsWorld), 1, null, this.eggLoc, false);
                } else if (rand < 0.84) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.BEETROOT));
                } else if (rand < 0.848) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.SCUTE));
                } else if (rand < 0.855) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.NETHERITE_SCRAP));
                } else if (rand < 0.861) {
                    this.nmsWorld.getEntities(this.nmsEgg, this.nmsEgg.getBoundingBox().g(24.0), entity -> entity instanceof EntityMonster).forEach(entity -> {
                        if (!(entity instanceof EntityEnderDragon || entity instanceof EntityGolem || entity instanceof EntityRavager || entity instanceof EntityWither)) {
                            entity.die();
                        }
                    });
                } else if (rand < 0.867) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityPig(this.nmsWorld), 20, null, this.eggLoc, true);
                } else if (rand < 0.873) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityIllusioner(this.nmsWorld), 1, null, this.eggLoc, true);
                } else if (rand < 0.878) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.WHITE_BED));
                } else if (rand < 0.883) {
                    this.nmsWorld.createExplosion(this.nmsEgg, this.eggLoc.getX(), this.eggLoc.getY(), this.eggLoc.getZ(), 8.0F, true, Explosion.Effect.DESTROY);
                } else if (rand < 0.888) {
                    new SpawnEntity(this.nmsWorld, new CustomEntitySlimeMagmaCube(this.nmsWorld, 16), 1, null, this.eggLoc, true);
                } else if (rand < 0.893) {
                    new RunnableLightningStorm(this.nmsWorld, this.eggLoc, random.nextInt(11) + 35).runTaskTimer(StaticPlugin.plugin, 0L, random.nextInt(4) + 2);
                } else if (rand < 0.898) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityBlaze(this.nmsWorld), 3, null, this.eggLoc, true);
                } else if (rand < 0.903) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityDrowned(this.nmsWorld), 2, null, this.eggLoc, true);
                } else if (rand < 0.908) {
                    new RunnableMeteorRain(this.nmsEgg, 4, 40.0, 30).runTaskTimer(StaticPlugin.plugin, 0L, 3L);
                } else if (rand < 0.912) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityRavager(this.nmsWorld), 1, null, this.eggLoc, true);
                } else if (rand < 0.916) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.EGG, 80));
                } else if (rand < 0.92) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.IRON_BLOCK));
                } else if (rand < 0.924) {
                    new SpawnEntity(this.nmsWorld, new CustomEntitySilverfish(this.nmsWorld), 15, null, this.eggLoc, true);
                } else if (rand < 0.928) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.DIRT, 512));
                } else if (rand < 0.932) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityEvoker(this.nmsWorld), 3, null, this.eggLoc, true);
                } else if (rand < 0.9355) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityBee(this.nmsWorld), 6, null, this.eggLoc, true);
                } else if (rand < 0.9385) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityIronGolem(this.nmsWorld), 1, null, this.eggLoc, true);
                } else if (rand < 0.9415) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityGuardianElder(this.nmsWorld), 1, null, this.eggLoc, true);
                } else if (rand < 0.9445) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.BEDROCK));
                } else if (rand < 0.9475) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityLlama(this.nmsWorld), 3, null, this.eggLoc, true);
                } else if (rand < 0.9505) {
                    this.bukkitPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, Integer.MAX_VALUE, 0));
                } else if (rand < 0.9535) {
                    this.bukkitWorld.dropItem(this.eggLoc, new ItemStack(Material.GOLD_BLOCK));
                } else if (rand < 0.9565) {
                    new SpawnEntity(this.nmsWorld, new CustomEntityZombieThor(this.nmsWorld), 1, null, this.eggLoc, true);
                } else if (rand < 0.9595) {
                    new SpawnEntity(this.nmsWorld, new CustomEntitySheepAggressive(this.nmsWorld), 1, null, this.eggLoc, true);
                } else if (rand < 0.9625) {

                } else if (rand < 0.965) {

                } else if (rand < 0.9675) {

                } else if (rand < 0.97) {

                } else if (rand < 0.9725) {

                } else if (rand < 0.975) {

                } else if (rand < 0.9775) {

                } else if (rand < 0.9795) {

                } else if (rand < 0.9815) {

                } else if (rand < 0.9835) {

                } else if (rand < 0.9855) {

                } else if (rand < 0.9875) {

                } else if (rand < 0.9895) {

                } else if (rand < 0.991) {

                } else if (rand < 0.992) {

                } else if (rand < 0.993) {

                } else if (rand < 0.994) {

                } else if (rand < 0.995) {

                } else if (rand < 0.996) {

                } else if (rand < 0.99695) {

                } else if (rand < 0.9977) {

                } else if (rand < 0.9982) {

                } else if (rand < 0.9986) {

                } else if (rand < 0.999) {

                } else if (rand < 0.9994) {

                } else if (rand < 0.9997) {

                } else {

                }
            }
        }
    }
}