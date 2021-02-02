package com.rosesause.bedrockparity.setup;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

/**
 * Registration for mod blocks and block items
 * @see net.minecraft.item.Items for vanilla
 */
public class ParityItems {

    public static final RegistryObject<BlockItem> LAVA_CAULDRON = registerBlock(PairityBlocks.LAVA_CAULDRON, ItemGroup.DECORATIONS);

    private static RegistryObject<BlockItem> registerBlock(RegistryObject<? extends Block> block, ItemGroup itemGroup) {
        return Registration.ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().group(itemGroup)));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<? extends T> sup) {
        return Registration.ITEMS.register(name, sup);
    }

}
