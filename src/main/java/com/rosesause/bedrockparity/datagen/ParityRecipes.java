package com.rosesause.bedrockparity.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

import java.util.function.Consumer;

public class ParityRecipes extends RecipeProvider {

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
