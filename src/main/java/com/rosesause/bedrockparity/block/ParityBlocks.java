package com.rosesause.bedrockparity.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.EnchantingTableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static com.rosesause.bedrockparity.BedrockParity.MOD_ID;

/**
 * Registration for mod blocks and block items
 * @see net.minecraft.block.Blocks for vanilla
 */
public class ParityBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    //Regular blocks
    public static final RegistryObject<LavaCauldronBlock> LAVA_CAULDRON =
            register("lava_cauldron", () -> new LavaCauldronBlock(AbstractBlock.Properties.create(Material.IRON, MaterialColor.STONE)
                            .setLightLevel(getLightValueCauldron())
                            .setRequiresTool()
                            .hardnessAndResistance(2.0F)
                            .notSolid()));
    public static final RegistryObject<PotionCauldronBlock> POTION_CAULDRON =
            register("potion_cauldron", () -> new PotionCauldronBlock(AbstractBlock.Properties.create(Material.IRON, MaterialColor.STONE)
                    .setRequiresTool()
                    .hardnessAndResistance(2.0F)
                    .notSolid()));
    public static final RegistryObject<DyeCauldronBlock> DYE_CAULDRON =
            register("dye_cauldron", () -> new DyeCauldronBlock(AbstractBlock.Properties.create(Material.IRON, MaterialColor.STONE)
                    .setRequiresTool()
                    .hardnessAndResistance(2.0F)
                    .notSolid()));

    //OVERRIDES
    public static final Block LIGHT_ENCHANTING_TABLE =
            registerOverride("enchanting_table", "minecraft", new EnchantingTableBlock(AbstractBlock.Properties.from(Blocks.ENCHANTING_TABLE)
                    .setLightLevel((state) -> 12)));
    public static final Block PARITY_JUKEBOX = registerOverride("jukebox", "minecraft", new ParityJukeboxBlock());

    /**
     * Returns the light value based on the level in the cauldron.
     */
    private static ToIntFunction<BlockState> getLightValueCauldron() {
        return (state) -> {
            return state.get(BlockStateProperties.LEVEL_0_3).intValue() * 5;
        };
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup) {
        return BLOCKS.register(name, sup);
    }

    //TODO fix this?? maybe...
    private static <T extends Block> T registerOverride(String name, String modid, T b) {
        b.setRegistryName(new ResourceLocation(modid, name));
        Block old = ForgeRegistries.BLOCKS.getValue(b.getRegistryName());
        ForgeRegistries.BLOCKS.register(b);
        ForgeRegistries.ITEMS.register(new BlockItem(b, new Item.Properties().group(old.asItem().getGroup())) {
            @Override
            public String getCreatorModId(ItemStack itemStack) {
                return MOD_ID;
            }
        }.setRegistryName(b.getRegistryName()));
        return b;
    }

}
