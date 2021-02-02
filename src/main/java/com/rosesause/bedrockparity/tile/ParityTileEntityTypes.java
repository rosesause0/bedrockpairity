package com.rosesause.bedrockparity.tile;

import com.rosesause.bedrockparity.block.ParityBlocks;
import com.rosesause.bedrockparity.tile.PotionCauldronTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.rosesause.bedrockparity.BedrockParity.MODID;

/**
 * Registration for mod blocks and block items
 * @see net.minecraft.tileentity.TileEntityType for vanilla
 */
public class ParityTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);

    public static final RegistryObject<TileEntityType<PotionCauldronTile>> POTION_CAULDRON_TILE = TILES.register(
            "name", () -> TileEntityType.Builder.create(PotionCauldronTile::new, ParityBlocks.POTION_CAULDRON.get()).build(null));

}
