package com.rosesause.bedrockparity.mixin;

import com.rosesause.bedrockparity.commands.ParityGameRules;
import net.minecraft.tileentity.CommandBlockLogic;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandBlockLogic.class)
public class MixinCommandBlockLogic {


    @Inject(at = @At("HEAD"), method = "trigger(Lnet/minecraft/world/World;)Z", cancellable = true)
    private void trigger(World worldIn, CallbackInfoReturnable<Boolean> cir) {
        if(!worldIn.getGameRules().getBoolean(ParityGameRules.COMMANDBLOCKS_ENABLED)) {
            cir.setReturnValue(true);
        }
    }
}
