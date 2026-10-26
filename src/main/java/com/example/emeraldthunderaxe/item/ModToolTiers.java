package com.example.emeraldthunderaxe.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.SimpleTier;

public class ModToolTiers {
    public static final Tier EMERALD_THUNDER = new SimpleTier(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, // mining level
            2031,  // durability
            9.0f,  // speed
            4.0f,  // attack damage bonus
            22,    // enchantability
            () -> Ingredient.of(net.minecraft.world.item.Items.EMERALD)
    );
}
