package com.example.emeraldthunderaxe.item;

import com.example.emeraldthunderaxe.entity.GreenEndermanEntity;
import com.example.emeraldthunderaxe.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EmeraldThunderAxeItem extends AxeItem {

    public EmeraldThunderAxeItem(Properties properties) {
        super(ModToolTiers.EMERALD_THUNDER, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);

        // Solo si el atacante es un jugador y el target es hostil
        if (attacker instanceof Player player && !player.level().isClientSide) {
            if (isHostile(target)) {
                spawnLightningLine(player.level(), player, target);
            }
        }

        return result;
    }

    private boolean isHostile(LivingEntity entity) {
        // Consideramos hostiles a los que implementan Enemy o son monstruos agresivos
        return entity instanceof Enemy || (entity instanceof Mob mob && mob.getTarget() != null);
    }

    private void spawnLightningLine(Level level, Player player, LivingEntity target) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        Vec3 start = player.getEyePosition();
        Vec3 direction = player.getLookAngle().normalize();

        // Línea de 8 truenos hacia adelante
        for (int i = 1; i <= 8; i++) {
            Vec3 pos = start.add(direction.scale(i * 1.8));

            // Solo spawneamos el rayo si hay espacio (evitar dentro de bloques sólidos)
            if (serverLevel.isEmptyBlock(net.minecraft.core.BlockPos.containing(pos))) {
                net.minecraft.world.entity.LightningBolt lightning = 
                    net.minecraft.world.entity.EntityType.LIGHTNING_BOLT.create(serverLevel);
                
                if (lightning != null) {
                    lightning.moveTo(pos.x, pos.y, pos.z);
                    lightning.setVisualOnly(false); // Hace daño real
                    serverLevel.addFreshEntity(lightning);
                }
            }
        }

        // Efectos visuales y de sonido
        serverLevel.playSound(null, player.blockPosition(), 
            SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.0f, 0.8f);
        
        // Partículas verdes
        for (int i = 0; i < 20; i++) {
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                player.getX() + (level.random.nextDouble() - 0.5) * 2,
                player.getY() + 1,
                player.getZ() + (level.random.nextDouble() - 0.5) * 2,
                1, 0, 0, 0, 0.1);
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            // Invocar Enderman Verde aliado
            GreenEndermanEntity enderman = ModEntities.GREEN_ENDERMAN.get().create(serverLevel);
            
            if (enderman != null) {
                // Posición frente al jugador
                Vec3 look = player.getLookAngle();
                double x = player.getX() + look.x * 2.5;
                double y = player.getY();
                double z = player.getZ() + look.z * 2.5;

                enderman.moveTo(x, y, z, player.getYRot(), 0);
                enderman.setOwner(player); // Método personalizado para hacerlo aliado
                enderman.setCustomName(net.minecraft.network.chat.Component.literal("§aEnderman Verde"));
                enderman.setCustomNameVisible(true);

                serverLevel.addFreshEntity(enderman);

                // Efectos
                serverLevel.playSound(null, player.blockPosition(),
                    SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 0.7f);
                
                for (int i = 0; i < 40; i++) {
                    serverLevel.sendParticles(ParticleTypes.PORTAL,
                        x + (level.random.nextDouble() - 0.5),
                        y + level.random.nextDouble() * 2.5,
                        z + (level.random.nextDouble() - 0.5),
                        1, 0, 0, 0, 0.1);
                }

                // Cooldown de 15 segundos
                player.getCooldowns().addCooldown(this, 300);
            }
        }

        return InteractionResult.SUCCESS;
    }
}
