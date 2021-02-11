package com.rosesause.bedrockparity.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
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

    //PARITY BLOCKS
    public static final RegistryObject<LavaCauldronBlock> LAVA_CAULDRON = register(
            "lava_cauldron",
            () -> new LavaCauldronBlock(AbstractBlock.Properties.create(Material.IRON, MaterialColor.STONE)
                            .setLightLevel(getLightValueCauldron())
                            .setRequiresTool()
                            .hardnessAndResistance(2.0F)
                            .notSolid())
    );
    public static final RegistryObject<Block> POTION_CAULDRON = register(
            "potion_cauldron",
            () -> new PotionCauldronBlock(AbstractBlock.Properties.create(Material.IRON, MaterialColor.STONE)
                    .setRequiresTool()
                    .hardnessAndResistance(2.0F)
                    .notSolid())
    );
    public static final RegistryObject<Block> DYE_CAULDRON = register(
            "dye_cauldron",
            () -> new DyeCauldronBlock(AbstractBlock.Properties.create(Material.IRON, MaterialColor.STONE)
                    .setRequiresTool()
                    .hardnessAndResistance(2.0F)
                    .notSolid())
    );

    //OVERRIDE BLOCKS
    public static final Block LIGHT_ENCHANTING_TABLE = registerOverride("enchanting_table", new EnchantingTableBlock(AbstractBlock.Properties.from(Blocks.ENCHANTING_TABLE).setLightLevel((state) -> 12)));
    public static final Block PARITY_JUKEBOX =         registerOverride("jukebox", new ParityJukeboxBlock());

    /**
     * Returns the light value based on the level in the cauldron.
     */
    private static ToIntFunction<BlockState> getLightValueCauldron() {
        return (state) -> state.get(BlockStateProperties.LEVEL_0_3) * 5;
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup) {
        return BLOCKS.register(name, sup);
    }

    private static <T extends Block> T registerOverride(String name, T b) {
        b.setRegistryName(new ResourceLocation("minecraft", name));
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
