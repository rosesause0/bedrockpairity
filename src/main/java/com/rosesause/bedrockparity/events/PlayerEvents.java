package com.rosesause.bedrockparity.events;

import com.rosesause.bedrockparity.BedrockParity;
import com.rosesause.bedrockparity.block.DyeCauldronBlock;
import com.rosesause.bedrockparity.block.ParityBlocks;
import com.rosesause.bedrockparity.block.PotionCauldronBlock;
import com.rosesause.bedrockparity.tile.DyeCauldronTile;
import com.rosesause.bedrockparity.tile.PotionCauldronTile;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = BedrockParity.MOD_ID)
public class PlayerEvents {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if(event.getWorld().isRemote)
            return;

        BlockState state = event.getWorld().getBlockState(event.getPos());

        //TODO change this to a switch
        if(state.getBlock() == Blocks.CAULDRON) {
            ActionResultType result = onPlayerCauldronInteract(event);
            if(result.isSuccessOrConsume())
                event.setCancellationResult(result);
        }
    }

    /**
     *
     */
    private static ActionResultType onPlayerCauldronInteract(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos).getBlockState();
        PlayerEntity player = event.getPlayer();
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        int level = state.get(CauldronBlock.LEVEL);

        if (level == 0) {
            //Fill Cauldron with Lava
            if(item == Items.LAVA_BUCKET) {
                event.setCanceled(true);
                setLavaCauldron(world, pos, player, event.getHand());
                return ActionResultType.func_233537_a_(event.getWorld().isRemote);
            }

            //You couldn't handle my strongest
            if((item == Items.POTION && PotionUtils.getPotionFromItem(stack) != Potions.WATER)){
                event.setCanceled(true);
                setPotionCauldron(world, pos, state, player, event.getHand(), level);
                return ActionResultType.func_233537_a_(event.getWorld().isRemote);
            }
        }
        else if(level > 0)
        {
            //Dye that water
            if(item.isIn(Tags.Items.DYES)) {
                event.setCanceled(true);
                setDyeCauldron(world, pos, level, player, event.getHand());
                return ActionResultType.func_233537_a_(event.getWorld().isRemote);
            }
        }
        return ActionResultType.PASS;
    }

    /**
     * Fills the cauldron with lava and sets the held item to
     */
    private static void setLavaCauldron(World world, BlockPos pos, PlayerEntity player, Hand hand) {
        if (!player.abilities.isCreativeMode)
            player.setHeldItem(hand, new ItemStack(Items.BUCKET));

        player.addStat(Stats.FILL_CAULDRON);
        world.setBlockState(pos, ParityBlocks.LAVA_CAULDRON.get().getDefaultState());
        world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    private static void setDyeCauldron(World world, BlockPos pos, int level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.abilities.isCreativeMode) {
            stack.shrink(1);
        }
        world.setBlockState(pos, ParityBlocks.DYE_CAULDRON.get().getDefaultState().with(DyeCauldronBlock.LEVEL, level));
        DyeCauldronTile dyeCauldronTile = (DyeCauldronTile) world.getTileEntity(pos);
        //Gets the color from the biome first
        dyeCauldronTile.addColorToCauldron(BiomeColors.getWaterColor(world, pos));
        //then the color from the dye
        dyeCauldronTile.addColorToCauldron(((DyeItem) stack.getItem()).getDyeColor().getColorValue());

        player.addStat(Stats.USE_CAULDRON);
        world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    private static void setPotionCauldron(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, int level) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.abilities.isCreativeMode) {
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            player.addStat(Stats.USE_CAULDRON);
            player.setHeldItem(hand, bottle);
            if (player instanceof ServerPlayerEntity) {
                ((ServerPlayerEntity) player).sendContainerToPlayer(player.container);
            }
        }

        world.setBlockState(pos, ParityBlocks.POTION_CAULDRON.get().getDefaultState());
        PotionCauldronTile potionCauldronTile = (PotionCauldronTile) world.getTileEntity(pos);
        potionCauldronTile.setPotion(PotionUtils.getPotionFromItem(stack));
        BlockState potionState = world.getBlockState(pos);
        world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        ((PotionCauldronBlock)potionState.getBlock()).setWaterLevel(world, pos, potionState, level + 1);
    }


}
