package com.rosesause.bedrockparity.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

public class ParityJukeboxBlock extends JukeboxBlock {

    public ParityJukeboxBlock() {
        super(Properties.from(Blocks.JUKEBOX));
    }

    /**
     * TODO Make this actual
     */
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if(shouldHaveParticles(stateIn, pos)) {
            for (int i = 0; i < 3; ++i) {
                int j = rand.nextInt(2) * 2 - 1;
                int k = rand.nextInt(2) * 2 - 1;
                double d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
                double d1 = (float) pos.getY() + rand.nextFloat();
                double d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) k;
                double d3 = rand.nextFloat() * (float) j;
                double d4 = ((double) rand.nextFloat() - 0.5D) * 0.125D;
                double d5 = rand.nextFloat() * (float) k;
                worldIn.addParticle(ParticleTypes.NOTE, d0, d1, d2, d3, d4, d5);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private boolean shouldHaveParticles(BlockState state, BlockPos pos) {
        if(!state.get(HAS_RECORD))
            return false;
        Map<BlockPos, ISound> map = ObfuscationReflectionHelper.getPrivateValue(WorldRenderer.class, Minecraft.getInstance().worldRenderer, "field_147593_P"); // worldRenderer.mapSoundPositions;
        ISound isound = map.get(pos);
        return Minecraft.getInstance().getSoundHandler().isPlaying(isound);
    }


    public static class JukeboxCapabilityProvider implements ICapabilityProvider {

        private final JukeboxItemHandler jukeboxHandler;

        public JukeboxCapabilityProvider(JukeboxTileEntity jukebox) {
            jukeboxHandler = new JukeboxItemHandler(jukebox);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                return LazyOptional.of(() -> jukeboxHandler).cast();
            return null;
        }

        static class JukeboxItemHandler implements IItemHandlerModifiable {

            private final JukeboxTileEntity jukebox;

            public JukeboxItemHandler(JukeboxTileEntity jukeboxIn) {
                jukebox = jukeboxIn;
            }

            @Override
            public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
                jukebox.setRecord(stack);
            }

            @Override
            public int getSlots() {
                return 1;
            }

            @Nonnull
            @Override
            public ItemStack getStackInSlot(int slot) {
                return jukebox.getRecord();
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(jukebox.getRecord().isEmpty() && !jukebox.getBlockState().get(HAS_RECORD)) {
                    if(!simulate) {
                        ((JukeboxBlock)Blocks.JUKEBOX).insertRecord(jukebox.getWorld(), jukebox.getPos(), jukebox.getBlockState(), stack);
                        jukebox.getWorld().playEvent(null, 1010, jukebox.getPos(), Item.getIdFromItem(stack.getItem()));
                        jukebox.setRecord(stack);
                    }
                    return ItemStack.EMPTY;
                }
                return stack;
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                if(!jukebox.getRecord().isEmpty() && jukebox.getBlockState().get(HAS_RECORD)) {
                    ItemStack record = jukebox.getRecord().copy();
                    if(!simulate) {
                        jukebox.getWorld().playEvent(1010, jukebox.getPos(), 0);
                        jukebox.clear();
                        BlockState state = jukebox.getBlockState().with(HAS_RECORD, false);
                        jukebox.getWorld().setBlockState(jukebox.getPos(), state, 2);
                    }
                    return record;
                }
                return ItemStack.EMPTY;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() instanceof MusicDiscItem;
            }
        }
    }

}
