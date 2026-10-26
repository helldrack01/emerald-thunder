package com.example.emeraldthunderaxe.entity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import com.example.emeraldthunderaxe.EmeraldThunderAxeMod;

@EventBusSubscriber(modid = EmeraldThunderAxeMod.MOD_ID)
public class ModEntityAttributes {

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GREEN_ENDERMAN.get(), GreenEndermanEntity.createAttributes().build());
    }
}
