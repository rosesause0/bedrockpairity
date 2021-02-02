package com.rosesause.bedrockparity;

import com.rosesause.bedrockparity.block.ParityBlocks;
import com.rosesause.bedrockparity.item.ParityItems;
import com.rosesause.bedrockparity.tile.ParityTileEntityTypes;
import com.rosesause.bedrockparity.tile.PotionCauldronTile;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("bedrockparity")
public class BedrockParity {

    //private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "bedrockparity";


    public BedrockParity() {
        ParityBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParityItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParityTileEntityTypes.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        //CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @Mod.EventBusSubscriber(modid = BedrockParity.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegisterColors {

        @SubscribeEvent
        public static void registerBlockColors(ColorHandlerEvent.Block event) {
            event.getBlockColors().register((state, reader, pos, color) -> reader != null && pos != null ? ((PotionCauldronTile)reader.getTileEntity(pos)).getPotionColor() : -1, ParityBlocks.POTION_CAULDRON.get());
        }
    }


}
