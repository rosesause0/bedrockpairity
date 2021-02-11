package com.rosesause.bedrockparity;

import com.mojang.brigadier.CommandDispatcher;
import com.rosesause.bedrockparity.block.ParityBlocks;
import com.rosesause.bedrockparity.block.ParityJukeboxBlock;
import com.rosesause.bedrockparity.commands.ParityGameRules;
import com.rosesause.bedrockparity.datagen.ParityLootTableProvider;
import com.rosesause.bedrockparity.datagen.ParityRecipes;
import com.rosesause.bedrockparity.item.ParityItems;
import com.rosesause.bedrockparity.item.ParityPotions;
import com.rosesause.bedrockparity.loot.ParityLootModifiers;
import com.rosesause.bedrockparity.tileentity.DyeCauldronTile;
import com.rosesause.bedrockparity.tileentity.ParityTileEntityTypes;
import com.rosesause.bedrockparity.tileentity.PotionCauldronTile;
import net.minecraft.block.Block;
import net.minecraft.block.SnowBlock;
import net.minecraft.command.CommandSource;
import net.minecraft.command.impl.GameRuleCommand;
import net.minecraft.data.DataGenerator;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod("bedrockparity")
public class BedrockParity {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "bedrockparity";

    public BedrockParity() {
        LOGGER.debug("hey parity here");
        ParityBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParityItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParityTileEntityTypes.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParityLootModifiers.GLM.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParityPotions.POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(new ParityGameRules.GameRuleEvents());
    }

    public static IEventBus getModEventBus() {
        return FMLJavaModLoadingContext.get().getModEventBus();
    }

    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ParityGameRules.registerGameRules();
        });
    }

    @Mod.EventBusSubscriber(modid = BedrockParity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {

        public static final ResourceLocation JUKEBOX_ITEM_HANDLER = new ResourceLocation(MOD_ID, "jukebox_item_handler");

        @SubscribeEvent
        public static void attachTileCapabilities(AttachCapabilitiesEvent<TileEntity> event) {
            if(event.getObject() instanceof JukeboxTileEntity) {
                event.addCapability(
                        JUKEBOX_ITEM_HANDLER,
                        new ParityJukeboxBlock.JukeboxCapabilityProvider((JukeboxTileEntity)event.getObject())
                );
            }
        }

        @SubscribeEvent
        public static void init(final FMLServerAboutToStartEvent event){
            CommandDispatcher<CommandSource> dispatcher = event.getServer().getCommandManager().getDispatcher();
            GameRuleCommand.register(dispatcher);
        }

    }

    @Mod.EventBusSubscriber(modid = BedrockParity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Events {

        @SubscribeEvent
        public static void blockColorEvent(ColorHandlerEvent.Block event) {
            event.getBlockColors().register((state, reader, pos, color) -> {
                if (reader != null && pos != null) {
                    return ((PotionCauldronTile) reader.getTileEntity(pos)).getPotionColor();
                }
                return -1;
            }, ParityBlocks.POTION_CAULDRON.get());
            event.getBlockColors().register((state, reader, pos, color) -> reader != null && pos != null ? ((DyeCauldronTile)reader.getTileEntity(pos)).getDyeColor() : -1, ParityBlocks.DYE_CAULDRON.get());

            //TODO make look better ig
            /*List<String> blockList = Lists.newArrayList(
                    "minecraft:spruce_leaves",
                    "minecraft:birch_leaves",
                    "minecraft:oak_leaves",
                    "minecraft:jungle_leaves",
                    "minecraft:acacia_leaves",
                    "minecraft:dark_oak_leaves");
            for(String id : blockList) {
                Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id));
                if(b == null || b.delegate == null)
                    return;

                event.getBlockColors().register((state, reader, pos, color) -> whiteLeaves(state, reader, pos), b);
            }*/
        }

        private static int whiteLeaves(IBlockReader reader, BlockPos pos) {
            if(reader == null || pos == null)
                return FoliageColors.getDefault();
            if(reader.getBlockState(pos.up()).getBlock() instanceof SnowBlock) {
                return 16777215;
            }
            return BiomeColors.getFoliageColor((IBlockDisplayReader) reader, pos);
        }

        @SubscribeEvent
        public static void blocks(RegistryEvent.Register<Block> e) {
            ParityTileEntityTypes.editTileEntities();
        }

        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            DataGenerator generator = event.getGenerator();
            //ExistingFileHelper fileHelper = event.getExistingFileHelper();

            generator.addProvider(new ParityLootTableProvider(generator));
            generator.addProvider(new ParityRecipes(generator));
        }



    }


}
