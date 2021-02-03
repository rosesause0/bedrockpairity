package com.rosesause.bedrockparity;

import com.rosesause.bedrockparity.block.ParityBlocks;
import com.rosesause.bedrockparity.datagen.ParityLootTableProvider;
import com.rosesause.bedrockparity.datagen.ParityRecipes;
import com.rosesause.bedrockparity.item.ParityItems;
import com.rosesause.bedrockparity.tile.ParityTileEntityTypes;
import com.rosesause.bedrockparity.tile.PotionCauldronTile;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("bedrockparity")
public class BedrockParity {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "bedrockparity";


    public BedrockParity() {
        ParityBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParityItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParityTileEntityTypes.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        //CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @Mod.EventBusSubscriber(modid = BedrockParity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Events {

        @SubscribeEvent
        public static void registerBlockColors(ColorHandlerEvent.Block event) {
            event.getBlockColors().register((state, reader, pos, color) -> reader != null && pos != null ? ((PotionCauldronTile)reader.getTileEntity(pos)).getPotionColor() : -1, ParityBlocks.POTION_CAULDRON.get());
        }

        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            DataGenerator generator = event.getGenerator();
            ExistingFileHelper fileHelper = event.getExistingFileHelper();

            generator.addProvider(new ParityLootTableProvider(generator));
            generator.addProvider(new ParityRecipes(generator));

        }
    }


}
