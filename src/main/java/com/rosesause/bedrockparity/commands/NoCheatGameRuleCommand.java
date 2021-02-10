package com.rosesause.bedrockparity.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;

import java.util.Set;

/**
 * A gamerule command that can be accessed without cheats or op
 */
public class NoCheatGameRuleCommand {

    private static final Set<GameRules.RuleKey<?>> noCheatGameRules = new ObjectArraySet<>();

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        final LiteralArgumentBuilder<CommandSource> argumentBuilder = Commands.literal("nocheatsgamerule");
        noCheatGameRules.add(GameRules.DO_FIRE_TICK);
        noCheatGameRules.add(GameRules.DO_MOB_LOOT);
        noCheatGameRules.add(GameRules.DO_TILE_DROPS);
        noCheatGameRules.add(GameRules.NATURAL_REGENERATION);
        noCheatGameRules.add(GameRules.SHOW_DEATH_MESSAGES);
        noCheatGameRules.add(GameRules.SPAWN_RADIUS);
        noCheatGameRules.add(GameRules.DO_IMMEDIATE_RESPAWN);
        noCheatGameRules.add(ParityGameRules.PVP);
        noCheatGameRules.add(ParityGameRules.TNT_EXPLODES);

        GameRules.visitAll(new GameRules.IRuleEntryVisitor() {
            public <T extends GameRules.RuleValue<T>> void visit(GameRules.RuleKey<T> key, GameRules.RuleType<T> type) {
                if(noCheatGameRules.contains(key)) {
                    argumentBuilder.then(Commands.literal(key.getName()).executes(
                                        (source) -> NoCheatGameRuleCommand.queryGamerule(source.getSource(), key))
                                    .then(type.createArgument("value").executes(
                                        (source) -> NoCheatGameRuleCommand.setGamerule(source, key))));
                }
            }
        });
        dispatcher.register(argumentBuilder);
    }

    private static <T extends GameRules.RuleValue<T>> int setGamerule(CommandContext<CommandSource> source, GameRules.RuleKey<T> key) {
        CommandSource commandsource = source.getSource();
        T t = commandsource.getServer().getGameRules().get(key);
        t.updateValue(source, "value");
        commandsource.sendFeedback(new TranslationTextComponent("commands.gamerule.set", key.getName(), t.toString()), true);
        return t.intValue();
    }

    private static <T extends GameRules.RuleValue<T>> int queryGamerule(CommandSource source, GameRules.RuleKey<T> key) {
        T t = source.getServer().getGameRules().get(key);
        source.sendFeedback(new TranslationTextComponent("commands.gamerule.query", key.getName(), t.toString()), false);
        return t.intValue();
    }
}
