package AnonymousRand.anonymousrand.extremedifficultyplugin.customentities.customprojectiles;

import net.minecraft.server.v1_16_R1.*;

public class CustomEntityArrowBadEffects extends CustomEntityArrow {

    public CustomEntityArrowBadEffects(World world) {
        super(world);
    }

    @Override
    protected void a(MovingObjectPositionEntity movingobjectpositionentity) {
        super.a(movingobjectpositionentity);

        Entity entity = movingobjectpositionentity.getEntity();

        if (entity instanceof EntityPlayer) {
            EntityPlayer entityPlayer = ((EntityPlayer)entity);
            entityPlayer.addEffect(new MobEffect(MobEffects.HUNGER, 140, 10));
            entityPlayer.addEffect(new MobEffect(MobEffects.WEAKNESS, 120, 0));
            entityPlayer.addEffect(new MobEffect(MobEffects.SLOWER_MOVEMENT, 100, 1));
            entityPlayer.addEffect(new MobEffect(MobEffects.BLINDNESS, 30, 0));
            entityPlayer.addEffect(new MobEffect(MobEffects.LEVITATION, 20, 0));
        }
    }
}
