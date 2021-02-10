package com.rosesause.bedrockparity.commands;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.rosesause.bedrockparity.BedrockParity.MOD_ID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = MOD_ID)
public class ParityGameRules {

    public static GameRules.RuleKey<GameRules.BooleanValue> PVP;
    public static GameRules.RuleKey<GameRules.BooleanValue> TNT_EXPLODES;
    public static GameRules.RuleKey<GameRules.BooleanValue> COMMAND_BLOCKS_ENABLED;
    //Lol fuck this
    //public static GameRules.RuleKey<GameRules.BooleanValue> SHOW_TAGS;


    public static void registerGameRules() {
        PVP          = GameRules.register("pvp",         GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
        TNT_EXPLODES = GameRules.register("tntExplodes", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
        COMMAND_BLOCKS_ENABLED = GameRules.register("commandBlocksEnabled", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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

        //TODO see if this works
        @SubscribeEvent
        public void onPlayerAttach(AttackEntityEvent event) {
            if(!event.getPlayer().getEntityWorld().getGameRules().getBoolean(PVP)) {
                if(event.getEntity() instanceof PlayerEntity)
                    event.setCanceled(true);
            }
        }
    }

}
