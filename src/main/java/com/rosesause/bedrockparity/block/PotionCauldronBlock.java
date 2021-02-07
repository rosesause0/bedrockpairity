package com.rosesause.bedrockparity.block;

import com.rosesause.bedrockparity.tile.PotionCauldronTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
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

/**
 *
 */
public class PotionCauldronBlock extends CauldronBlock {

    public PotionCauldronBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(LEVEL, Integer.valueOf(3)));
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
        return new PotionCauldronTile();
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
                    worldIn.playSound((PlayerEntity) null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                }

                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == Items.GLASS_BOTTLE) {
                if (i > 0 && !worldIn.isRemote) {
                    PotionCauldronTile potionCauldronTile = (PotionCauldronTile) worldIn.getTileEntity(pos);
                    ItemStack potion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), potionCauldronTile.getPotion());
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(handIn, potion);
                    } else if (!player.inventory.addItemStackToInventory(potion)) {
                        player.dropItem(potion, false);
                    } else if (player instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity)player).sendContainerToPlayer(player.container);
                    }

                    player.addStat(Stats.USE_CAULDRON);
                    worldIn.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if(i-1 == 0)
                        worldIn.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                    else
                        this.setWaterLevel(worldIn, pos, state, i-1);
                }
                return ActionResultType.func_233537_a_(worldIn.isRemote);
            } else if (item == Items.POTION) {
                PotionCauldronTile potionCauldronTile = (PotionCauldronTile) worldIn.getTileEntity(pos);
                if(PotionUtils.getPotionFromItem(itemstack) == potionCauldronTile.getPotion()) {
                    if (i < 3 && !worldIn.isRemote) {
                        if (!player.abilities.isCreativeMode) {
                            ItemStack itemstack3 = new ItemStack(Items.GLASS_BOTTLE);
                            player.addStat(Stats.USE_CAULDRON);
                            player.setHeldItem(handIn, itemstack3);
                            if (player instanceof ServerPlayerEntity) {
                                ((ServerPlayerEntity)player).sendContainerToPlayer(player.container);
                            }
                        }

                        potionCauldronTile.setPotion(PotionUtils.getPotionFromItem(itemstack));

                        worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        this.setWaterLevel(worldIn, pos, state, i + 1);
                    }

                    return ActionResultType.func_233537_a_(worldIn.isRemote);
                }
            } else if (item == Items.ARROW) {
                if (i > 0 && !worldIn.isRemote) {
                    PotionCauldronTile potionCauldronTile = (PotionCauldronTile) worldIn.getTileEntity(pos);
                    //TODO make it a config option to do full stack
                    ItemStack potion = PotionUtils.addPotionToItemStack(new ItemStack(Items.TIPPED_ARROW), potionCauldronTile.getPotion());
                    if (!player.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }
                    if (itemstack.isEmpty()) {
                        player.setHeldItem(handIn, potion);
                    } else if (!player.inventory.addItemStackToInventory(potion)) {
                        player.dropItem(potion, false);
                    } else if (player instanceof ServerPlayerEntity) {
                        ((ServerPlayerEntity)player).sendContainerToPlayer(player.container);
                    }

                    player.addStat(Stats.USE_CAULDRON);
                    worldIn.playSound((PlayerEntity) null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
     * potion rain
     */
    @Override
    public void fillWithRain(World worldIn, BlockPos pos) {

    }
}
