package com.rosesause.bedrockparity.datagen;

import com.rosesause.bedrockparity.BedrockParity;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {


    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, BedrockParity.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }
}
