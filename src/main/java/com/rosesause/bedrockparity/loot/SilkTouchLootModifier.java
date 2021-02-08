package com.rosesause.bedrockparity.loot;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SilkTouchLootModifier extends LootModifier {

    private final Item silkItem;

    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected SilkTouchLootModifier(ILootCondition[] conditionsIn, Item silkItemIn) {
        super(conditionsIn);
        silkItem = silkItemIn;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.clear();
        generatedLoot.add(new ItemStack(silkItem));
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<SilkTouchLootModifier> {

        @Override
        public SilkTouchLootModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {

            Item silkItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation((JSONUtils.getString(json, "silkItem"))));
            return new SilkTouchLootModifier(conditionsIn, silkItem);
        }

        @Override
        public JsonObject write(SilkTouchLootModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("silkItem", ForgeRegistries.ITEMS.getKey(instance.silkItem).toString());
            return json;
        }
    }
}
