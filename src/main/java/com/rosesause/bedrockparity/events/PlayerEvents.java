package com.rosesause.bedrockparity.events;

import com.rosesause.bedrockparity.BedrockParity;
import com.rosesause.bedrockparity.setup.ParityBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = BedrockParity.MODID)
public class PlayerEvents {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack itemMain = event.getPlayer().getHeldItem(Hand.MAIN_HAND);
        ItemStack itemOff = event.getPlayer().getHeldItem(Hand.OFF_HAND);
        BlockState state = event.getWorld().getBlockState(event.getPos());
        PlayerEntity player = event.getPlayer();
        LOGGER.info("CLICK");
        //Cauldron
        if(state.getBlock() == Blocks.CAULDRON) {
            LOGGER.info("CAULDRON");
            int level = state.get(CauldronBlock.LEVEL);
            if (level == 0) {
                if(hasItemInHands(player, new ItemStack(Items.LAVA_BUCKET)))
                {
                    LOGGER.info("CAULDRON Empty");
                    //event.setResult(Event.Result.DENY);
                    event.setCanceled(true);

                    Hand handIn = whichHand(player, new ItemStack(Items.LAVA_BUCKET));
                    setLavaCauldron(event.getWorld(), event.getPos(), event.getPlayer(), handIn);
                    event.setCancellationResult(ActionResultType.func_233537_a_(event.getWorld().isRemote));
                }

            }
        }

    }

    /**
     *
     */
    private static void setLavaCauldron(World world, BlockPos pos, PlayerEntity player, Hand hand) {
        if (!player.abilities.isCreativeMode)
            player.setHeldItem(hand, new ItemStack(Items.BUCKET));

        player.addStat(Stats.FILL_CAULDRON);
        world.setBlockState(pos, ParityBlocks.LAVA_CAULDRON.get().getDefaultState());
        world.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }


    /**
     *
     */
    private static boolean hasItemInHands(PlayerEntity player, ItemStack stack)
    {
        return player.getHeldItem(Hand.MAIN_HAND).isItemEqual(stack) || player.getHeldItem(Hand.OFF_HAND).isItemEqual(stack);
    }

    /**
     *
     */
    private static Hand whichHand(PlayerEntity player, ItemStack stack) {
        return player.getHeldItem(Hand.MAIN_HAND).isItemEqual(stack) ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }



}
