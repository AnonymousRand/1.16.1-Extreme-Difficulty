package AnonymousRand.ExtremeDifficultyPlugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.entity.EntityType.*;

public class EntityDamageListeners implements Listener {

    @EventHandler
    public void entityDamage(EntityDamageEvent event) {
        if (event.getEntityType() != PLAYER && event.getEntityType() != ENDER_DRAGON && event.getEntityType() != WITHER) {  /**all non-player mobs take 15/16 less damage from these sources*/
            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.LAVA) || event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || event.getCause().equals(EntityDamageEvent.DamageCause.FALLING_BLOCK) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) || event.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING) || event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION) || event.getCause().equals(EntityDamageEvent.DamageCause.CRAMMING) || event.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)) {
                event.setDamage(event.getDamage() * 0.0625);
            }
        }

        if (event.getEntityType() == ENDER_DRAGON || event.getEntityType() == WITHER) { /**ender dragon and wither heal from these damage sources*/
            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.LAVA) || event.getCause().equals(EntityDamageEvent.DamageCause.FALL) || event.getCause().equals(EntityDamageEvent.DamageCause.FALLING_BLOCK) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) || event.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING) || event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION) || event.getCause().equals(EntityDamageEvent.DamageCause.CRAMMING) || event.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)) {
                ((LivingEntity)event.getEntity()).setMaxHealth(((LivingEntity)event.getEntity()).getMaxHealth() + event.getDamage() * 0.25); //increase max health by 25% of the damage dealt
                ((LivingEntity)event.getEntity()).setHealth(((LivingEntity)event.getEntity()).getHealth() + event.getDamage() * 0.25); //ender dragon and wither heal 25%
                event.setDamage(0.0); //no damage
            }
        }

        switch (event.getEntityType()) {
            case SKELETON: /**skeletons are immune to fire damage*/
                event.setCancelled(event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.FIRE);
            case ZOMBIE: /**zombies are immune to fire damage*/
                event.setCancelled(event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.FIRE);
        }
    }

    @EventHandler
    public void entityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != PLAYER && !(event.getDamager() instanceof Player)) { /**mobs can't damage each other*/
            event.setCancelled(true);
        }
    }
}
