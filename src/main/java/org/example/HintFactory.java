package org.example;

import java.util.List;
import java.util.Random;

public class HintFactory {
    private static final List<HintProvider> providers = List.of(
            new HelpHintProvider(),
            new FunnyHintProvider()
    );
    private static final Random random = new Random();

    public static HintProvider getRandomHintProvider() {
        return providers.get(random.nextInt(providers.size()));
    }
}