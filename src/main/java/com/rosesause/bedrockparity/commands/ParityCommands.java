package com.rosesause.bedrockparity.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.rosesause.bedrockparity.BedrockParity.MOD_ID;

/**
 * {@link Commands}
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ParityCommands {

    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        NoCheatGameRuleCommand.register(event.getDispatcher());
        LOGGER.debug("register COMMANDS");
    }

    public static void replaceCommands(CommandEvent event) {

    }
}
