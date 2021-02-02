package com.rosesause.bedrockpairity;

import com.rosesause.bedrockpairity.setup.Registration;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("bedrockpairity")
public class BedrockParity {

    //private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "bedrockpairity";


    public BedrockParity() {
        Registration.init();
    }


}
