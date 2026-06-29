package org.mjli.mam.ponder;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import org.mjli.mam.MightAndMagic;

public class MamPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return MightAndMagic.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        MamPonderScenes.register(helper);
    }
}
