package com.rosesause.bedrockparity.setup;

import com.rosesause.bedrockparity.block.LavaCauldronBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

/**
 * Registration for mod blocks and block items
 * @see net.minecraft.block.Blocks for vanilla
 */
public class ParityBlocks {

    public static final RegistryObject<LavaCauldronBlock> LAVA_CAULDRON =
            register("lava_cauldron", () -> new LavaCauldronBlock(AbstractBlock.Properties.create(Material.IRON, MaterialColor.STONE)
                            .setLightLevel(getLightValueCauldron())
                            .setRequiresTool()
                            .hardnessAndResistance(2.0F)
                            .notSolid()));


    /**
     * Returns the light value based on the level in the cauldron.
     */
    private static ToIntFunction<BlockState> getLightValueCauldron() {
        return (state) -> {
            return state.get(BlockStateProperties.LEVEL_0_3).intValue() * 5;
        };
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup) {
        return Registration.BLOCKS.register(name, sup);
    }

}
