package main.com.example.simulationlab14;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Listener {

    public static void main(String[] args) {
        System.out.println("Enter average, variance and number of experiments:");
        Scanner input = new Scanner(System.in);
        Experiment experiment = new Experiment(input.nextDouble(), input.nextDouble(), input.nextInt());

        System.out.println("\nSummation method:");
        experiment.summationMethod();
        printData(experiment);

        System.out.println("\nPrecise summation method:");
        experiment.preciseSummationMethod();
        printData(experiment);

        System.out.println("\nBox-Muller method:");
        experiment.boxMullerMethod();
        printData(experiment);
    }

    private static void printData(Experiment experiment) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        System.out.println("\nExperimental probabilities:");
        double[] frequencies = experiment.getFrequencies();
        double start = experiment.getStart();
        double length = experiment.getLength();

        for (int i = 0; i < experiment.getEventsAmount(); i++) {
            System.out.println(decimalFormat.format(start + i * length) + " to " +
                    decimalFormat.format(start + (i + 1) * length) + ": " + decimalFormat.format(frequencies[i]));
        }
        System.out.println("Average: " + decimalFormat.format(experiment.getAverage(frequencies) * length + start));
        System.out.println(decimalFormat.format(experiment.getAverageError() * 100) + "% Average error");
        System.out.println("Variance: " + decimalFormat.format(experiment.getVariance(frequencies) * length / experiment.getEventsAmount()));
        System.out.println(decimalFormat.format(experiment.getVarianceError() * 100) + "% Variance error");
        System.out.println("Chi squared: " + decimalFormat.format(experiment.getChiSquared()));
    }
}
