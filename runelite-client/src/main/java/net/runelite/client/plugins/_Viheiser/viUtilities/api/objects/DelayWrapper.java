package net.runelite.client.plugins._Viheiser.viUtilities.api.objects;

import lombok.Getter;

public class DelayWrapper {
    public DelayWrapper(boolean weightedDistribution, int minDelay, int maxDelay, int deviation, int target){
        this.weightedDistribution = weightedDistribution;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        this.deviation = deviation;
        this.target = target;
    }

    public void update(boolean weightedDistribution, int minDelay, int maxDelay, int deviation, int target){
        this.weightedDistribution = weightedDistribution;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
        this.deviation = deviation;
        this.target = target;
    }
    @Getter
    private boolean weightedDistribution;
    @Getter
    private int minDelay;
    @Getter
    private int maxDelay;
    @Getter
    private int deviation;
    @Getter
    private int target;
}
