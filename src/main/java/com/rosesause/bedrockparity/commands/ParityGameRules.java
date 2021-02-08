package com.rosesause.bedrockparity.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.rosesause.bedrockparity.BedrockParity;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;

import static com.rosesause.bedrockparity.BedrockParity.MOD_ID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = MOD_ID)
public class ParityGameRules {
    private static final Logger LOGGER = LogManager.getLogger();

    public static GameRules.RuleKey<GameRules.BooleanValue> PVP;
    public static GameRules.RuleKey<GameRules.BooleanValue> TNT_EXPLODES;
    public static GameRules.RuleKey<GameRules.BooleanValue> SHOW_TAGS;


    public static void registerGameRules() {
        PVP          = GameRules.register("pvp", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
        TNT_EXPLODES = GameRules.register("tnt_explodes", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
        //SHOW_TAGS    = GameRules.register(MOD_ID + ":show_tags", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
    }

    public static class GameRuleEvents {

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onEntityJoin(EntityJoinWorldEvent event) {
            Entity entity = event.getEntity();
            if(!event.getWorld().getGameRules().getBoolean(TNT_EXPLODES) && entity instanceof TNTEntity) {
                event.setCanceled(true);
                event.getWorld().addEntity(new ItemEntity(event.getWorld(), entity.getPosX(), entity.getPosY(), entity.getPosZ(), new ItemStack(Blocks.TNT)));
            }
            else if(!event.getWorld().getGameRules().getBoolean(TNT_EXPLODES) && entity instanceof TNTMinecartEntity) {
                event.setCanceled(true);
                event.getWorld().addEntity(new ItemEntity(event.getWorld(), entity.getPosX(), entity.getPosY(), entity.getPosZ(), new ItemStack(Blocks.TNT)));
                event.getWorld().addEntity(new ItemEntity(event.getWorld(), entity.getPosX(), entity.getPosY(), entity.getPosZ(), new ItemStack(Items.MINECART)));
            }
        }
    }

}
