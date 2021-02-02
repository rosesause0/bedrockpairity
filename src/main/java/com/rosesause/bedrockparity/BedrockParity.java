package com.rosesause.bedrockparity;

import com.rosesause.bedrockparity.setup.Registration;
import net.minecraftforge.fml.common.Mod;

@Mod("bedrockparity")
public class BedrockParity {

    //private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "bedrockparity";


    public BedrockParity() {
        Registration.init();
    }


}
