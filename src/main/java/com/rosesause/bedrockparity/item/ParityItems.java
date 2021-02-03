package com.rosesause.bedrockparity.item;

import com.rosesause.bedrockparity.block.ParityBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static com.rosesause.bedrockparity.BedrockParity.MOD_ID;

/**
 * Registration for mod blocks and block items
 * @see net.minecraft.item.Items for vanilla
 */
public class ParityItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<BlockItem> LAVA_CAULDRON = registerBlock(ParityBlocks.LAVA_CAULDRON, ItemGroup.DECORATIONS);

    private static RegistryObject<BlockItem> registerBlock(RegistryObject<? extends Block> block, ItemGroup itemGroup) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().group(itemGroup)));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<? extends T> sup) {
        return ITEMS.register(name, sup);
    }

}
