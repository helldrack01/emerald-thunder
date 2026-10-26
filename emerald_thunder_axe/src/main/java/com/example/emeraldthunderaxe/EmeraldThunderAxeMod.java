package com.example.emeraldthunderaxe;

import com.example.emeraldthunderaxe.entity.ModEntities;
import com.example.emeraldthunderaxe.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(EmeraldThunderAxeMod.MOD_ID)
public class EmeraldThunderAxeMod {
    public static final String MOD_ID = "emeraldthunderaxe";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public EmeraldThunderAxeMod(IEventBus modEventBus) {
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        NeoForge.EVENT_BUS.register(this);

        LOGGER.info("Emerald Thunder Axe loaded!");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Common setup logic
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // Client setup (renderers etc.)
    }
}
