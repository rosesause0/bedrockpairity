package com.rosesause.bedrockparity.item;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.rosesause.bedrockparity.BedrockParity.MOD_ID;

public class ParityPotions {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, MOD_ID);

    public static final RegistryObject<Potion> DECAY = POTIONS.register("decay", () -> new Potion("decay", new EffectInstance(Effects.WITHER, 3600, 1)));

}
