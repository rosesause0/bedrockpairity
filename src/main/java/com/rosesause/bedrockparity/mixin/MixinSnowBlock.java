package com.rosesause.bedrockparity.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(SnowBlock.class)
public class MixinSnowBlock {

    /**
     * @author rosesause
     * TODO replace true with config
     */
    @Overwrite
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (worldIn.getLightFor(LightType.BLOCK, pos) > 11 || (worldIn.getBiome(pos).getTemperature() > 1.0f && true)) {
            Block.spawnDrops(state, worldIn, pos);
            worldIn.removeBlock(pos, false);
        }
    }

}
