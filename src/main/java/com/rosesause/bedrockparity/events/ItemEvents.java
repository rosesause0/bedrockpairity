package com.rosesause.bedrockparity.events;

import com.rosesause.bedrockparity.BedrockParity;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = BedrockParity.MOD_ID)
public class ItemEvents {

    @SubscribeEvent
    public static void onUseBoneMeal(BonemealEvent event) {
        if(event.getWorld().isRemote)
            return;
        if(event.getBlock().getBlock() instanceof SugarCaneBlock) {
            boneMealSugarCane(event);
        }
        else if(event.getBlock().getBlock() instanceof FlowerBlock) {
            boneMealFlowerBlock(event);
        }
    }

    /**
     * Works like how you think id work, most of the code from {@link SugarCaneBlock}
     */
    public static void boneMealSugarCane(BonemealEvent event) {
        if (event.getWorld().isAirBlock(event.getPos().up())) {
            int i = 1;
            while(event.getWorld().getBlockState(event.getPos().down(i)).isIn(event.getBlock().getBlock()))
                i++;
            if (i <= 3) {
                int j = event.getBlock().get(SugarCaneBlock.AGE) + MathHelper.nextInt(event.getWorld().rand, 2, 5);
                if(j > 15)
                    j = 15;
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(event.getWorld(), event.getPos(), event.getBlock(), true)) {
                    event.setResult(Event.Result.ALLOW);
                    if (j == 15) {
                        event.getWorld().setBlockState(event.getPos().up(), Blocks.SUGAR_CANE.getDefaultState());
                        event.getWorld().setBlockState(event.getPos(), event.getBlock().with(SugarCaneBlock.AGE, 0), 4);
                    } else {
                        event.getWorld().setBlockState(event.getPos(), event.getBlock().with(SugarCaneBlock.AGE, j), 4);
                    }
                }
            }
        }
    }

    /**
     * Works pretty much like {@link GrassBlock#grow}
     */
    private static void boneMealFlowerBlock(BonemealEvent event) {
        BlockPos blockpos = event.getPos().up();
        World worldIn = event.getWorld();
        Random rand = new Random();
        event.setResult(Event.Result.ALLOW);
        for(int i = 0; i < 64; ++i) {
            BlockPos blockpos1 = blockpos;
            for(int j = 0; j < i / 16; ++j) {
                blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
                if (worldIn.getBlockState(blockpos1.down()).isIn(Blocks.GRASS_BLOCK) || !worldIn.getBlockState(blockpos1).hasOpaqueCollisionShape(worldIn, blockpos1)) {
                    if (event.getBlock().getBlock().getDefaultState().isValidPosition(worldIn, blockpos1))
                        worldIn.setBlockState(blockpos1, event.getBlock().getBlock().getDefaultState(), 3);
                }
            }
        }
    }
}
