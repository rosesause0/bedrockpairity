package com.rosesause.bedrockparity.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;


/**
 * @see net.minecraft.item.DyeableArmorItem
 */
public class DyeCauldronTile extends TileEntity {

    private int dyeColor;

    public DyeCauldronTile() {
        super(ParityTileEntityTypes.DYE_CAULDRON_TILE.get());
        this.dyeColor = -1;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.dyeColor = nbt.getInt("dyeColor");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("dyeColor", dyeColor);
        return compound;
    }

    public ItemStack addColorToItem(ItemStack stack) {
        ItemStack armor = stack.copy();
        DyeableArmorItem armorItem = (DyeableArmorItem) armor.getItem();
        int armorColor = armorItem.getColor(armor);

        armorItem.setColor(armor, addColor(armorColor, dyeColor));
        return armor;
    }

    public float[] getColorValues(int color) {
        return new float[]{((color & 16711680) >> 16) / 255.0F, ((color & '\uff00') >> 8) / 255.0F, ((color & 255)) / 255.0F};
    }

    private int addColor(int color, int addColor) {
        float[] colorValues = getColorValues(addColor);
        int i = 0;
        int j = 0;
        int[] aint = new int[3];

        if(color != -1) {
            int k = color;
            float r = (float)(k >> 16 & 255) / 255.0F;
            float g = (float)(k >> 8 & 255) / 255.0F;
            float b = (float)(k & 255) / 255.0F;
            i = (int)((float)i + Math.max(r, Math.max(g, b)) * 255.0F);
            aint[0] = (int)((float)aint[0] + r * 255.0F);
            aint[1] = (int)((float)aint[1] + g * 255.0F);
            aint[2] = (int)((float)aint[2] + b * 255.0F);
            ++j;
        }

        int r = (int)(colorValues[0] * 255.0F);
        int g =  (int)(colorValues[1] * 255.0F);
        int b = (int)(colorValues[2] * 255.0F);
        i += Math.max(r, Math.max(g, b));
        aint[0] += r;
        aint[1] += g;
        aint[2] += b;
        j++;

        int j1 = aint[0] / j;
        int k1 = aint[1] / j;
        int l1 = aint[2] / j;
        float f3 = (float)i / (float)j;
        float f4 = (float)Math.max(j1, Math.max(k1, l1));
        j1 = (int)((float)j1 * f3 / f4);
        k1 = (int)((float)k1 * f3 / f4);
        l1 = (int)((float)l1 * f3 / f4);
        int j2 = (j1 << 8) + k1;
        return (j2 << 8) + l1;
    }

    /**
     * @see net.minecraft.item.IDyeableArmorItem
     */
    public void addColorToCauldron(int color) {
        this.dyeColor = addColor(color, dyeColor);
        sendUpdates();
    }

    private void sendUpdates() {
        this.markDirty();
        world.markBlockRangeForRenderUpdate(pos, getBlockState(), getBlockState());
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
    }

    public int getDyeColor() {
        return this.dyeColor;
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
        this.dyeColor = nbt.getInt("dyeColor");
        //im sure this is fine................ :|
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
    }
}
