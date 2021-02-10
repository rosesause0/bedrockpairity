package com.rosesause.bedrockparity.block;

import com.rosesause.bedrockparity.tileentity.DyeCauldronTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class DyeCauldronBlock extends CauldronBlock {

    public DyeCauldronBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(LEVEL, 3));
    }

    /**
     * Nothing on collision
     */
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {

    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DyeCauldronTile();
    }

    /**
     * Fills bucket with lava and when so it does this fun thing that turns the block back into a vanilla old cauldron.
     * Also when a water bucket is used it extinguishes the lava.
     */
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack itemstack = player.getHeldItem(handIn);
        if (!itemstack.isEmpty()) {
            int i = state.get(LEVEL);
            Item item = itemstack.getItem();

            if (item == Items.WATER_BUCKET) {
                if (i < 3 && !worldIn.isRemote) {
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                        } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.BUCKET))) {
                            player.dropItem(new ItemStack(Items.BUCKET), false);
                        }
                    }

                    player.addStat(Stats.USE_CAULDRON);
                    this.setWaterLevel(worldIn, pos, state, 0);
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                }
                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item instanceof DyeItem) {
                if (i > 0 && !worldIn.isRemote) {
                    DyeCauldronTile dyeCauldronTile = (DyeCauldronTile) worldIn.getTileEntity(pos);
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    dyeCauldronTile.addColorToCauldron(((DyeItem) item).getDyeColor().getColorValue());

                    player.addStat(Stats.USE_CAULDRON);
                    worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item instanceof DyeableArmorItem) {
                if (i > 0 && !worldIn.isRemote) {
                    DyeCauldronTile dyeCauldronTile = (DyeCauldronTile) worldIn.getTileEntity(pos);
                    ItemStack armor = dyeCauldronTile.addColorToItem(itemstack);

                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(handIn, armor);
                    } else if (!player.inventory.addItemStackToInventory(armor)) {
                        player.dropItem(armor, false);
                    } else if (player instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity)player).sendContainerToPlayer(player.container);
                    }

                    player.addStat(Stats.USE_CAULDRON);
                    worldIn.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if(i-1 == 0)
                        worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                    else
                        this.setWaterLevel(worldIn, pos, state, i-1);
                }
                return ActionResultType.func_233537_a_(worldIn.isRemote);
            }
        }
        return ActionResultType.PASS;
    }

    /**
     * dye rain
     */
    @Override
    public void fillWithRain(World worldIn, BlockPos pos) {

    }


}
