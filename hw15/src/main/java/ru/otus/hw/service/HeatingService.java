package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Ice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
public class HeatingService {

    public static final int ICE_TEMPERATURE_SEVENTY = -70;

    public static final int ICE_TEMPERATURE_TEN = -10;

    private final HeatingGateway heatingGateway;

    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    public void startGenerateIceLoop() {
        ForkJoinPool.commonPool().execute(() -> {
            List<Ice> ices = generateIces();
            heatingGateway.process(ices);
        });
    }

    private List<Ice> generateIces() {
        int count = randomGenerator.nextInt(1, 10);
        List<Ice> ices = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ices.add(generateIce());
        }
        return ices;
    }

    private Ice generateIce() {
        return new Ice(randomGenerator.nextInt(ICE_TEMPERATURE_SEVENTY, ICE_TEMPERATURE_TEN));
    }
}
