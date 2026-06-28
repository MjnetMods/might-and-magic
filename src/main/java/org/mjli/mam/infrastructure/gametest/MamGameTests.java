package org.mjli.mam.infrastructure.gametest;

import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.infrastructure.gametest.tests.TestManaPool;
import org.mjli.mam.infrastructure.gametest.tests.TestPetalApothecary;
import org.mjli.mam.infrastructure.gametest.tests.TestRecipes;
import org.mjli.mam.infrastructure.gametest.tests.TestVerdantFlowers;
import org.mjli.mam.infrastructure.gametest.tests.TestVerdantPath;

@EventBusSubscriber(modid = MightAndMagic.MODID)
public class MamGameTests {

    private static final Class<?>[] TEST_CLASSES = {
            TestVerdantPath.class,
            TestVerdantFlowers.class,
            TestManaPool.class,
            TestRecipes.class,
            TestPetalApothecary.class,
    };

    @SubscribeEvent
    public static void registerTests(RegisterGameTestsEvent event) {
        for (Class<?> cls : TEST_CLASSES) {
            event.register(cls);
        }
    }
}
