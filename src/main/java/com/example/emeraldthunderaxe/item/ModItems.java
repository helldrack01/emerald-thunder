package com.example.emeraldthunderaxe.item;

import com.example.emeraldthunderaxe.EmeraldThunderAxeMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(EmeraldThunderAxeMod.MOD_ID);

    public static final DeferredItem<Item> EMERALD_THUNDER_AXE = ITEMS.register("emerald_thunder_axe",
            () -> new EmeraldThunderAxeItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(2031) // Like netherite
                    .attributes(AxeItem.createAttributes(ModToolTiers.EMERALD_THUNDER, 6.0f, -3.0f))
            ));

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EmeraldThunderAxeMod.MOD_ID);

    public static final Supplier<CreativeModeTab> EMERALD_THUNDER_TAB = CREATIVE_MODE_TABS.register("emerald_thunder_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.emeraldthunderaxe"))
                    .icon(() -> new ItemStack(EMERALD_THUNDER_AXE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(EMERALD_THUNDER_AXE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
