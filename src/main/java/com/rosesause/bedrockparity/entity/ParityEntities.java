package com.rosesause.bedrockparity.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.rosesause.bedrockparity.BedrockParity.MOD_ID;

public class ParityEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MOD_ID);

    public static final RegistryObject<EntityType<BabySquidEntity>> BABY_SQUID = ENTITIES.register("baby_squid", () -> EntityType.Builder.create(BabySquidEntity::new, EntityClassification.WATER_CREATURE)
            .size(0.8F, 0.8F)
            .trackingRange(8)
            .build("baby_squid"));
}
