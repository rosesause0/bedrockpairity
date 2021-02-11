package com.rosesause.bedrockparity.mixin;


import com.rosesause.bedrockparity.block.ParityBlocks;
import com.rosesause.bedrockparity.tileentity.PotionCauldronTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.SwampHutPiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

@Mixin(SwampHutPiece.class)
public abstract class MixinSwampHitPiece extends ScatteredStructurePiece {

    //Ignore this
    protected MixinSwampHitPiece(IStructurePieceType structurePieceTypeIn, Random rand, int xIn, int yIn, int zIn, int widthIn, int heightIn, int depthIn) {
        super(structurePieceTypeIn, rand, xIn, yIn, zIn, widthIn, heightIn, depthIn);
    }

    @Inject(at = @At("TAIL"), method = "func_230383_a_(Lnet/minecraft/world/ISeedReader;Lnet/minecraft/world/gen/feature/structure/StructureManager;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/MutableBoundingBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)Z")
    public void func_230383_a_(ISeedReader seedReader, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        int i = random.nextInt(3);
        //if i == 0 it stays a caudron
        if(i > 0) {
            setBlockState(seedReader, ParityBlocks.POTION_CAULDRON.get().getDefaultState().with(BlockStateProperties.LEVEL_0_3, i), 4, 2, 6, boundingBox);
            Iterator<Potion> itemIterator = ForgeRegistries.POTION_TYPES.iterator(); // Gets the registry iterator
            ArrayList<Potion> potions = new ArrayList<>();
            for (Iterator<Potion> it = itemIterator; it.hasNext(); ) // Iterates through registry iterator
            {
                potions.add(it.next()); // Adds each element to a List (easier to get a random element from)
            }
            Random rand = new Random();
            Potion randomPotion = potions.get(rand.nextInt(potions.size() - 1)); // Chooses a random element from the List of Blocks
            BlockPos blockpos = new BlockPos(this.getXWithOffset(4, 6), this.getYWithOffset(2), this.getZWithOffset(4, 6));
            ((PotionCauldronTile) seedReader.getTileEntity(blockpos)).setPotion(randomPotion);
        }
    }

}