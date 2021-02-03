package com.rosesause.bedrockparity.datagen;

import com.rosesause.bedrockparity.block.ParityBlocks;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class ParityRecipes extends RecipeProvider {

    private static final Logger LOGGER = LogManager.getLogger();


    public ParityRecipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        /*ShapedRecipeBuilder.shapedRecipe(ParityBlocks.LAVA_CAULDRON.get())
                .patternLine("x x")
                .patternLine("x#x")
                .key('x', Items.IRON_INGOT)
                .key('#', Items.LAVA_BUCKET)
                .setGroup("bedrockparity")
                .addCriterion("iron", InventoryChangeTrigger.Instance.forItems(Items.IRON_INGOT))
                .build(consumer);*/
    }
}
