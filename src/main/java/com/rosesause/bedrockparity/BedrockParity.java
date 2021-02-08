package com.rosesause.bedrockparity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.rosesause.bedrockparity.block.ParityBlocks;
import com.rosesause.bedrockparity.block.ParityJukeboxBlock;
import com.rosesause.bedrockparity.datagen.ParityLootTableProvider;
import com.rosesause.bedrockparity.datagen.ParityRecipes;
import com.rosesause.bedrockparity.item.ParityItems;
import com.rosesause.bedrockparity.loot.ParityLootModifiers;
import com.rosesause.bedrockparity.loot.SilkTouchLootModifier;
import com.rosesause.bedrockparity.tile.DyeCauldronTile;
import com.rosesause.bedrockparity.tile.ParityTileEntityTypes;
import com.rosesause.bedrockparity.tile.PotionCauldronTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.data.DataGenerator;
import net.minecraft.tileentity.EnchantingTableTileEntity;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IRegistryDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Mod("bedrockparity")
public class BedrockParity {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "bedrockparity";


    public BedrockParity() {
        ParityBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParityItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParityTileEntityTypes.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ParityLootModifiers.GLM.register(FMLJavaModLoadingContext.get().getModEventBus());
        //CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    @Mod.EventBusSubscriber(modid = BedrockParity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {

        public static final ResourceLocation JUKEBOX_ITEMHANDLER = new ResourceLocation(MOD_ID, "jukebox_item_handler");

        @SubscribeEvent
        public static void attachTileCapabilites(AttachCapabilitiesEvent<TileEntity> event) {
            if(event.getObject() instanceof JukeboxTileEntity) {
                event.addCapability(
                        JUKEBOX_ITEMHANDLER,
                        new ParityJukeboxBlock.JukeboxCapabilityProvider((JukeboxTileEntity)event.getObject())
                );
            }
        }
    }

    @Mod.EventBusSubscriber(modid = BedrockParity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Events {

        @SubscribeEvent
        public static void blockColorEvent(ColorHandlerEvent.Block event) {
            event.getBlockColors().register((state, reader, pos, color) -> reader != null && pos != null ? ((PotionCauldronTile)reader.getTileEntity(pos)).getPotionColor() : -1, ParityBlocks.POTION_CAULDRON.get());
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

        private static int whiteLeaves(BlockState state, IBlockReader reader, BlockPos pos ) {
            if(reader == null || pos == null)
                return FoliageColors.getDefault();
            if(reader.getBlockState(pos.up()).getBlock() instanceof SnowBlock) {
                return 16777215;
            }
            return BiomeColors.getFoliageColor((IBlockDisplayReader) reader, pos);
        }

        //TODO move this to ParityBlocks
        @SubscribeEvent
        public static void blocks(RegistryEvent.Register<Block> e) {
            TileEntityType.ENCHANTING_TABLE.validBlocks = ImmutableSet.of(ParityBlocks.LIGHT_ENCHANTING_TABLE);
            TileEntityType.JUKEBOX.validBlocks = ImmutableSet.of(ParityBlocks.PARITY_JUKEBOX);
        }

        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            DataGenerator generator = event.getGenerator();
            ExistingFileHelper fileHelper = event.getExistingFileHelper();

            //generator.addProvider(new ParityLootTableProvider(generator));
            //generator.addProvider(new ParityRecipes(generator));

        }

    }


}
