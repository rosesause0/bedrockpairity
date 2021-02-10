package com.rosesause.bedrockparity.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

/**
 * Stores the potion data for the potion cauldron
 * @see Potions
 */
public class PotionCauldronTile extends TileEntity {

    private Potion potion;

    public PotionCauldronTile() {
        super(ParityTileEntityTypes.POTION_CAULDRON_TILE.get());
        this.potion = Potions.EMPTY;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putString("Potion", ForgeRegistries.POTION_TYPES.getKey(potion).toString());
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.potion = PotionUtils.getPotionTypeFromNBT(nbt);
    }

    public void setPotion(Potion potionIn) {
        this.potion = potionIn;
        this.markDirty();
        sendUpdates();
    }

    private void sendUpdates() {
        this.markDirty();
        world.markBlockRangeForRenderUpdate(pos, getBlockState(), getBlockState());
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
    }

    public Potion getPotion() {
        return this.potion;
    }

    public int getPotionColor() {
        return PotionUtils.getPotionColor(potion);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        this.write(nbt);
        return new SUpdateTileEntityPacket(this.pos, -1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getNbtCompound();
        read(getBlockState(), nbt);
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
    }
}
