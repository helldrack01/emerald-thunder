package com.example.emeraldthunderaxe.entity;

import com.example.emeraldthunderaxe.EmeraldThunderAxeMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, EmeraldThunderAxeMod.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<GreenEndermanEntity>> GREEN_ENDERMAN =
            ENTITY_TYPES.register("green_enderman", () ->
                    EntityType.Builder.of(GreenEndermanEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 2.9f)
                            .clientTrackingRange(8)
                            .build("green_enderman")
            );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
