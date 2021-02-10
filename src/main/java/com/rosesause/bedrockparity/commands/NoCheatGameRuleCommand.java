package com.rosesause.bedrockparity.commands;

import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.impl.GameRuleCommand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;

import java.util.Set;

import static com.rosesause.bedrockparity.BedrockParity.MOD_ID;

/**
 *
 */
public class NoCheatGameRuleCommand {

    private static Set<GameRules.RuleKey<?>> noCheatGameRules = new ObjectArraySet<>();

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        final LiteralArgumentBuilder<CommandSource> literalargumentbuilder = Commands.literal("nocheatsgamerule");
        noCheatGameRules.add(GameRules.DO_FIRE_TICK);
        noCheatGameRules.add(GameRules.DO_MOB_LOOT);
        noCheatGameRules.add(GameRules.DO_TILE_DROPS);
        noCheatGameRules.add(GameRules.NATURAL_REGENERATION);
        noCheatGameRules.add(GameRules.SHOW_DEATH_MESSAGES);
        noCheatGameRules.add(GameRules.SPAWN_RADIUS);
        noCheatGameRules.add(GameRules.DO_IMMEDIATE_RESPAWN);
        noCheatGameRules.add(GameRules.SPAWN_RADIUS);
        noCheatGameRules.add(ParityGameRules.PVP);
        noCheatGameRules.add(ParityGameRules.TNT_EXPLODES);
        GameRules.visitAll(new GameRules.IRuleEntryVisitor() {
            public <T extends GameRules.RuleValue<T>> void visit(GameRules.RuleKey<T> key, GameRules.RuleType<T> type) {
                if(noCheatGameRules.contains(key)) {
                    literalargumentbuilder.then(Commands.literal(key.getName()).executes((p_223483_1_) -> {
                        return NoCheatGameRuleCommand.func_223486_b(p_223483_1_.getSource(), key);
                    }).then(type.createArgument("value").executes((p_223482_1_) -> {
                        return NoCheatGameRuleCommand.func_223485_b(p_223482_1_, key);
                    })));
                }
            }
        });
        dispatcher.register(literalargumentbuilder);
    }

    private static <T extends GameRules.RuleValue<T>> int func_223485_b(CommandContext<CommandSource> p_223485_0_, GameRules.RuleKey<T> p_223485_1_) {
        CommandSource commandsource = p_223485_0_.getSource();
        T t = commandsource.getServer().getGameRules().get(p_223485_1_);
        t.updateValue(p_223485_0_, "value");
        commandsource.sendFeedback(new TranslationTextComponent("commands.gamerule.set", p_223485_1_.getName(), t.toString()), true);
        return t.intValue();
    }

    private static <T extends GameRules.RuleValue<T>> int func_223486_b(CommandSource p_223486_0_, GameRules.RuleKey<T> p_223486_1_) {
        T t = p_223486_0_.getServer().getGameRules().get(p_223486_1_);
        p_223486_0_.sendFeedback(new TranslationTextComponent("commands.gamerule.query", p_223486_1_.getName(), t.toString()), false);
        return t.intValue();
    }
}
