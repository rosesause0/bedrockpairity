package com.rosesause.bedrockparity.tile;

import com.rosesause.bedrockparity.block.ParityBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.EnchantingTableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
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


    private static <T extends TileEntity> TileEntityType<T> registerOverride(String name, String modid, TileEntityType<T> tile) {
        tile.setRegistryName(new ResourceLocation(modid, name));
        TileEntityType<?> old = ForgeRegistries.TILE_ENTITIES.getValue(tile.getRegistryName());
        ForgeRegistries.TILE_ENTITIES.register(tile);
        return tile;
    }

}
