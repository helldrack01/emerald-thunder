package com.example.emeraldthunderaxe.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class GreenEndermanEntity extends EnderMan {

    private UUID ownerUUID;
    @Nullable
    private Player cachedOwner;

    public GreenEndermanEntity(EntityType<? extends EnderMan> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 9.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.1D, 6.0F, 2.5F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.9D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 12, true, false,
                entity -> {
                    if (!(entity instanceof Enemy)) return false;
                    if (entity instanceof GreenEndermanEntity) return false;
                    // Ataca a quien esté atacando al dueño o a enemigos cercanos
                    return true;
                }));
    }

    public void setOwner(Player player) {
        this.ownerUUID = player.getUUID();
        this.cachedOwner = player;
    }

    @Nullable
    public Player getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        }
        if (this.ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(this.ownerUUID);
            if (entity instanceof Player player) {
                this.cachedOwner = player;
                return player;
            }
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();

        // Aura verde
        if (this.level().isClientSide && this.tickCount % 2 == 0) {
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER,
                    this.getX() + (this.random.nextDouble() - 0.5) * 0.9,
                    this.getY() + this.random.nextDouble() * 2.7,
                    this.getZ() + (this.random.nextDouble() - 0.5) * 0.9,
                    0, 0.04, 0);
        }

        // Seguir al dueño si se aleja mucho
        Player owner = getOwner();
        if (owner != null && !this.level().isClientSide && this.tickCount % 20 == 0) {
            if (this.distanceToSqr(owner) > 1024) { // 32 bloques
                this.teleportTowards(owner);
            }

            // Atacar automáticamente al objetivo del dueño
            if (owner.getLastHurtMob() != null && owner.getLastHurtMob().isAlive()
                    && owner.getLastHurtMob() instanceof Enemy) {
                this.setTarget(owner.getLastHurtMob());
            }
            if (owner.getLastHurtByMob() != null && owner.getLastHurtByMob().isAlive()
                    && owner.getLastHurtByMob() instanceof Enemy) {
                this.setTarget(owner.getLastHurtByMob());
            }
        }
    }

    private void teleportTowards(LivingEntity target) {
        for (int attempt = 0; attempt < 20; attempt++) {
            double x = target.getX() + (this.random.nextDouble() - 0.5) * 6.0;
            double y = target.getY() + this.random.nextInt(3);
            double z = target.getZ() + (this.random.nextDouble() - 0.5) * 6.0;

            if (this.randomTeleport(x, y, z, true)) {
                this.level().playSound(null, this.xo, this.yo, this.zo,
                        SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                break;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && source.getEntity() == getOwner()) {
            return false; // Inmune al daño del dueño
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (entity == getOwner()) return true;
        if (entity instanceof GreenEndermanEntity other && other.getOwner() != null
                && other.getOwner() == this.getOwner()) {
            return true;
        }
        return super.isAlliedTo(entity);
    }

    // Goal simple de seguimiento
    private static class FollowOwnerGoal extends Goal {
        private final GreenEndermanEntity enderman;
        private final double speedModifier;
        private final float startDistance;
        private final float stopDistance;
        private Player owner;
        private int timeToRecalcPath;

        public FollowOwnerGoal(GreenEndermanEntity enderman, double speed, float startDist, float stopDist) {
            this.enderman = enderman;
            this.speedModifier = speed;
            this.startDistance = startDist;
            this.stopDistance = stopDist;
        }

        @Override
        public boolean canUse() {
            Player owner = enderman.getOwner();
            if (owner == null || !owner.isAlive()) return false;
            if (enderman.distanceToSqr(owner) < (stopDistance * stopDistance)) return false;
            this.owner = owner;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (owner == null || !owner.isAlive()) return false;
            double dist = enderman.distanceToSqr(owner);
            return dist > (stopDistance * stopDistance) && dist < (startDistance * startDistance * 16);
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
        }

        @Override
        public void tick() {
            enderman.getLookControl().setLookAt(owner, 10.0F, (float) enderman.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                enderman.getNavigation().moveTo(owner, this.speedModifier);
            }
        }
    }
}
