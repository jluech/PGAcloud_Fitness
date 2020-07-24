package fitness.fitnessEvaluation;

import fitness.population.Individual;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Quick tutorial on how to create and start a JAR from simple Java code
// https://docs.oracle.com/javase/tutorial/deployment/jar/appman.html


public class Main {
    public static void main(String[] args) {
        System.out.println("Hello Fitness Evaluation");

        // passed in like so "java -jar -Dfile_path=/path/to/file.yml jar-name.jar"
        String input_file_path = System.getProperty("file_path");
        System.out.println(input_file_path);

        // Collect input data from input file.
        Map<String, String> input_data = new HashMap<>();
        try {
            File inputFile = new File(input_file_path);
            Scanner inputReader = new Scanner(inputFile);
            while (inputReader.hasNextLine()) {
                String line = inputReader.nextLine();
                String[] data = line.split(": ");
                input_data.put(data[0], data[1]);
            }
            inputReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Provided file was not found.");
            e.printStackTrace();
        }

        // Parse input data and create fitness operator with it.
        String profitsStr = input_data.get("all_profits");
        int[] profits = Arrays.stream(profitsStr.split(",")).mapToInt(Integer::parseInt).toArray();
        String weightsStr = input_data.get("all_weights");
        int[] weights = Arrays.stream(weightsStr.split(",")).mapToInt(Integer::parseInt).toArray();
        FitnessEvaluation fitEval = new FitnessEvaluation(profits, weights, Double.parseDouble(input_data.get("capacity")));

        // Evaluate the fitness of the given individual.
        Individual individual = new Individual(input_data.get("solution"));
        double fitness = fitEval.fitEval(individual);

        // Write computed fitness back to file.
        try {
            FileWriter inputWriter = new FileWriter(input_file_path, true);
            inputWriter.append(String.format("fitness: %f\n", fitness));
            inputWriter.close();
        } catch (IOException e) {
            System.out.println("Could not write fitness to file.");
            e.printStackTrace();
        }
    }
}
