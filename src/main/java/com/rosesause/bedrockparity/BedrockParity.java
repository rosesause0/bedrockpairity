package com.rosesause.bedrockparity;

import com.rosesause.bedrockparity.setup.Registration;
import net.minecraftforge.fml.common.Mod;

@Mod("bedrockpairity")
public class BedrockParity {

    //private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "bedrockpairity";


    public BedrockParity() {
        Registration.init();
    }


}
