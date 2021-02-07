package com.rosesause.bedrockparity.tile;

import com.rosesause.bedrockparity.block.ParityBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.rosesause.bedrockparity.BedrockParity.MOD_ID;

/**
 * Registration for mod blocks and block items
 * @see net.minecraft.tileentity.TileEntityType for vanilla
 */
public class ParityTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);

    public static final RegistryObject<TileEntityType<PotionCauldronTile>> POTION_CAULDRON_TILE = TILES.register(
            "potion_cauldron", () -> TileEntityType.Builder.create(PotionCauldronTile::new, ParityBlocks.POTION_CAULDRON.get()).build(null));
    public static final RegistryObject<TileEntityType<DyeCauldronTile>> DYE_CAULDRON_TILE = TILES.register(
            "dye_cauldron", () -> TileEntityType.Builder.create(DyeCauldronTile::new, ParityBlocks.DYE_CAULDRON.get()).build(null));

}
