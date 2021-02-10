package com.rosesause.bedrockparity.datagen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.rosesause.bedrockparity.BedrockParity.MOD_ID;


/**
 *
 */
public class ParityLootTableProvider extends LootTableProvider {


    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>>
            lootTableGenerators = ImmutableList.of(
            Pair.of(ParityLootTables.Blocks::new, LootParameterSets.BLOCK)
    );


    public ParityLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return lootTableGenerators;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        final Set<ResourceLocation> modLootTableIds =
                LootTables
                        .getReadOnlyLootTables()
                        .stream()
                        .filter(lootTable -> lootTable.getNamespace().equals(MOD_ID))
                        .collect(Collectors.toSet());

        for (ResourceLocation id : Sets.difference(modLootTableIds, map.keySet()))
            validationtracker.addProblem("Missing mod loot table: " + id);

        map.forEach((id, lootTable) ->
                LootTableManager.validateLootTable(validationtracker, id, lootTable));
    }

    @Override
    public String getName() {
        return MOD_ID +" LootTables";
    }
}
