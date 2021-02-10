package com.rosesause.bedrockparity.events;

import com.rosesause.bedrockparity.BedrockParity;
import com.rosesause.bedrockparity.block.DyeCauldronBlock;
import com.rosesause.bedrockparity.block.ParityBlocks;
import com.rosesause.bedrockparity.block.PotionCauldronBlock;
import com.rosesause.bedrockparity.tileentity.DyeCauldronTile;
import com.rosesause.bedrockparity.tileentity.PotionCauldronTile;
import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.state.properties.BlockStateProperties;
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

import java.util.Random;

@Mod.EventBusSubscriber(modid = BedrockParity.MOD_ID)
public class PlayerEvents {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {


        BlockState state = event.getWorld().getBlockState(event.getPos());

        if(state.getBlock() == Blocks.CAULDRON) {
            if(event.getWorld().isRemote)
                return;
            ActionResultType result = onPlayerCauldronInteract(event);
            if(result.isSuccessOrConsume())
                event.setCancellationResult(result);
        }

        if (EnchantmentHelper.getEnchantments(event.getItemStack()).containsKey(Enchantments.FIRE_ASPECT)) {
            ActionResultType result = fireAspectInteract(event);
            if(result.isSuccessOrConsume())
                event.setCancellationResult(result);
        }
    }

/*    @SubscribeEvent
    public static void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if(event.getWorld().isRemote)
            return;
    }*/

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
                setPotionCauldron(world, pos, player, event.getHand(), level);
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
        world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
        world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    private static void setPotionCauldron(World world, BlockPos pos, PlayerEntity player, Hand hand, int level) {
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
        world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        ((PotionCauldronBlock)potionState.getBlock()).setWaterLevel(world, pos, potionState, level + 1);
    }

    /**
     *
     */
    private static ActionResultType fireAspectInteract(PlayerInteractEvent.RightClickBlock event) {
        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
        if (block == Blocks.TNT) {
            if(event.getWorld().isRemote)
                return ActionResultType.PASS;
            event.setCanceled(true);
            event.getWorld().setBlockState(event.getPos(), Blocks.AIR.getDefaultState(), 11);
            block.catchFire(event.getWorld().getBlockState(event.getPos()), event.getWorld(), event.getPos(), event.getFace(), event.getPlayer());
            return ActionResultType.SUCCESS;
        } else if (block == Blocks.CAMPFIRE && !event.getWorld().getBlockState(event.getPos()).get(BlockStateProperties.LIT)) {
            if (CampfireBlock.canBeLit(event.getWorld().getBlockState(event.getPos()))) {
                event.getWorld().playSound(event.getPlayer(), event.getPos(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, new Random().nextFloat() * 0.4F + 0.8F);
                if(event.getWorld().isRemote)
                    return ActionResultType.PASS;
                event.setCanceled(true);
                event.getWorld().setBlockState(event.getPos(), event.getWorld().getBlockState(event.getPos()).with(BlockStateProperties.LIT, Boolean.TRUE));
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

}
