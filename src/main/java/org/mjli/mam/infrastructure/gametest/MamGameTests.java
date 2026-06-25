package org.mjli.mam.infrastructure.gametest;

import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.infrastructure.gametest.tests.TestVerdantPath;

@EventBusSubscriber(modid = MightAndMagic.MODID)
public class MamGameTests {

    private static final Class<?>[] TEST_CLASSES = {
            TestVerdantPath.class,
    };

    @SubscribeEvent
    public static void registerTests(RegisterGameTestsEvent event) {
        for (Class<?> cls : TEST_CLASSES) {
            event.register(cls);
        }
    }
}
