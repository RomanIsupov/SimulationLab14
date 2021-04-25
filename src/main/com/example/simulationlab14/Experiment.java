package main.com.example.simulationlab14;

import java.util.concurrent.ThreadLocalRandom;

public class Experiment {

    private final int totalQuantity;
    private final int eventsAmount;

    private final double average;
    private final double variance;
    private final double start;
    private final double length;

    private final double[] probabilities;
    private final double[] frequencies;

    public Experiment(double average, double variance, int totalQuantity) {
        this.average = average;
        this.variance = variance;
        this.totalQuantity = totalQuantity;

        eventsAmount = (int) Math.ceil(Math.sqrt(totalQuantity));
        probabilities = new double[eventsAmount];
        frequencies = new double[eventsAmount];

        start = average - 3 * variance;
        length = 6 * variance / eventsAmount;
        double maximumProbability = 1.0;
        for (int i = 0; i < eventsAmount; i++) {
            double center = start + length * (i + 0.5);
            probabilities[i] = length * Math.exp(-(center - average) * (center - average) /
                    (2 * variance * variance)) / (variance * Math.sqrt(2 * Math.PI));
            maximumProbability -= probabilities[i];
        }
        probabilities[eventsAmount - 1] += maximumProbability / 2;
        probabilities[0] += maximumProbability / 2;
    }

    private double summation()
    {
        double sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += ThreadLocalRandom.current().nextDouble(0.0, 1.0);
        }
        return sum - 6;
    }

    public void summationMethod() {
        resetFrequencies();
        for (int i = 0; i < totalQuantity; i++) {
            double result = variance * summation() + average - start;
            result /= length;
            if (result < 0.0000)
                result = 0.0000;
            if (result >= eventsAmount)
                result = eventsAmount - 0.5;
            frequencies[(int)Math.floor(result)] += 1.0 / totalQuantity;
        }
    }

    private double preciseSummation()
    {
        double s = summation();
        return s + (Math.pow(s, 3) + 3 * s) / 240.0;
    }

    public void preciseSummationMethod() {
        resetFrequencies();
        for (int i = 0; i < totalQuantity; i++) {
            double result = variance * preciseSummation() + average - start;
            result /= length;
            if (result < 0.0000)
                result = 0.0000;
            if (result >= eventsAmount)
                result = eventsAmount - 0.5;
            frequencies[(int)Math.floor(result)] += 1.0 / totalQuantity;
        }
    }

    private double boxMuller()
    {
        return Math.sqrt(-2.0 * Math.log(ThreadLocalRandom.current().nextDouble(0.0, 1.0))) *
                Math.cos(2 * Math.PI * ThreadLocalRandom.current().nextDouble(0.0, 1.0));
    }

    public void boxMullerMethod() {
        resetFrequencies();
        for (int i = 0; i < totalQuantity; i++) {
            double result = variance * boxMuller() + average - start;
            result /= length;
            if (result < 0.0000)
                result = 0.0000;
            if (result >= eventsAmount)
                result = eventsAmount - 0.5;
            frequencies[(int)Math.floor(result)] += 1.0 / totalQuantity;
        }
    }

    public double getAverage(double[] probabilities)
    {
        double average = 0;
        for (int i = 0; i < eventsAmount; i++) {
            average += i * probabilities[i];
        }
        return average;
    }

    public double getVariance(double[] probabilities)
    {
        double variance = 0;
        for (int i = 0; i < eventsAmount; i++) {
            variance += i * i * probabilities[i];
        }
        variance -= Math.pow(getAverage(probabilities), 2);
        return variance;
    }

    public double getChiSquared() {
        double chiSquared = 0;
        for (int i = 0; i < eventsAmount; i++) {
            chiSquared += (Math.pow(frequencies[i], 2)) / probabilities[i];
        }
        chiSquared = (chiSquared - 1) * totalQuantity;
        return chiSquared;
    }

    private void resetFrequencies() {
        for (int i = 0; i < eventsAmount; i++) {
            frequencies[i] = 0.0;
        }
    }

    public double getAverageError() {
        return  Math.abs(getAverage(frequencies) - getAverage(probabilities)) / (getAverage(probabilities) + 1);
    }

    public double getVarianceError() {
        return Math.abs(getVariance(frequencies) - getVariance(probabilities)) / getVariance(probabilities);
    }

    public double[] getFrequencies() {
        return frequencies;
    }

    public int getEventsAmount() {
        return eventsAmount;
    }

    public double getStart() {
        return start;
    }

    public double getLength() {
        return length;
    }
}
