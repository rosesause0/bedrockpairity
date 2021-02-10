package com.rosesause.bedrockparity.mixin;

import com.rosesause.bedrockparity.commands.ParityGameRules;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.network.play.client.CUpdateCommandBlockPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetHandler.class)
public class MixinServerPlayNetHandler {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(at = @At("HEAD"), method = "processUpdateCommandBlock(Lnet/minecraft/network/play/client/CUpdateCommandBlockPacket;)V", cancellable = true)
    private void processUpdateCommandBlock(CUpdateCommandBlockPacket packetIn, CallbackInfo callback) {
        if(!player.getServerWorld().getGameRules().getBoolean(ParityGameRules.COMMANDBLOCKS_ENABLED)) {
            this.player.sendMessage(new TranslationTextComponent("advMode.notEnabled"), Util.DUMMY_UUID);
            callback.cancel();
        }
    }
}
