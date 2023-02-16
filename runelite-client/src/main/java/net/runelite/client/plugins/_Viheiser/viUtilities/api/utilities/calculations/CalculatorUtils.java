package net.runelite.client.plugins._Viheiser.viUtilities.api.utilities.calculations;

import java.util.concurrent.ThreadLocalRandom;

public class CalculatorUtils {
    protected static final java.util.Random random = new java.util.Random();

    public int getRandomIntBetweenRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public long randomDelay(boolean weightedDistribution, int minDelay, int maxDelay, int deviation, int targetDelay) {
        if (weightedDistribution) {
            return (long) clamp((-Math.log(Math.abs(random.nextGaussian()))) * deviation + targetDelay, minDelay, maxDelay);
        } else {
            /* generate a normal even distribution random */
            return (long) clamp(Math.round(random.nextGaussian() * deviation + targetDelay), minDelay, maxDelay);
        }
    }

    private double clamp(double val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static double random(double min, double max) {
        return Math.min(min, max) + random.nextDouble() * Math.abs(max - min);
    }

    /**
     * Returns a random integer with min as the inclusive lower bound and max as
     * the exclusive upper bound.
     *
     * @param min The inclusive lower bound.
     * @param max The exclusive upper bound.
     * @return Random integer min <= n < max.
     */
    public static int random(int min, int max) {
        int n = Math.abs(max - min);
        return Math.min(min, max) + (n == 0 ? 0 : random.nextInt(n));
    }
}
