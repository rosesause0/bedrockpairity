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
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;

/**
 * Stores the potion data for the potion cauldron
 * @see Potions
 */
public class PotionCauldronTile extends TileEntity {

    private Potion potion = Potions.EMPTY;

    public PotionCauldronTile() {
        super(ParityTileEntityTypes.POTION_CAULDRON_TILE.get());
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound = super.write(compound);
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
        updateTile();
    }

    public void updateTile() {
        this.markDirty();
        if(world != null) {
            world.markBlockRangeForRenderUpdate(pos, getBlockState(), getBlockState());
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
        }
    }

    public Potion getPotion() {
        return this.potion;
    }

    public int getPotionColor() {
        StringBuilder str = new StringBuilder("effects:");
        for(int i = 0; i < potion.getEffects().size(); i ++){
            str.append("\n");
            str.append(potion.getEffects().get(i));
        }
        LogManager.getLogger().debug("\npotion:\n" + str + "\ncolor:" + PotionUtils.getPotionColor(potion));
        return PotionUtils.getPotionColor(potion);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, -1, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getNbtCompound();
        read(getBlockState(), nbt);

    }
}
