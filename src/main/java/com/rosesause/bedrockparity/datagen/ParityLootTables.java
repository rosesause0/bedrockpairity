package com.rosesause.bedrockparity.datagen;

import com.rosesause.bedrockparity.block.ParityBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.rosesause.bedrockparity.BedrockParity.MOD_ID;

public class ParityLootTables {

    public static class Blocks extends BlockLootTables {

        @Override
        protected void addTables() {
            this.registerDropping(ParityBlocks.LAVA_CAULDRON.get(), Items.CAULDRON);
            this.registerDropping(ParityBlocks.POTION_CAULDRON.get(), Items.CAULDRON);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return StreamSupport
                    .stream(ForgeRegistries.BLOCKS.spliterator(), false)
                    .filter(
                            entry -> entry.getRegistryName() != null &&
                                    entry.getRegistryName().getNamespace().equals(MOD_ID)
                    ).collect(Collectors.toSet());
        }

    }
}
