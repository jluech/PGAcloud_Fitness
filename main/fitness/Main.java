package fitness;

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
// Create jar file inside "main" directory like so: jar cfe FitnessEvaluation.jar fitness.Main fitness
// View jar content like so: jar tf FitnessEvaluation.jar


public class Main {
    private static final String input_path = "/pga/input.yml";
    private static final String output_path = "/pga/output.yml";

    public static void main(String[] args) {
        System.out.println("Hello Fitness Evaluation");
        System.out.println(String.join(" ", args));

        System.out.println("Input: "+input_path);
        System.out.println("Output: "+output_path);  // TODO: remove?

        // Collect individual from input file.
        Map<String, String> input_data = new HashMap<>();
        try {
            File inputFile = new File(input_path);
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
        Individual individual = new Individual(input_data.get("solution"), Double.parseDouble(input_data.get("fitness")));

        // Parse input data and create fitness operator with it.
        // Passed in like so "-Dcapacity=150" or "-Dprofits=1,5,2,4,3"
        String profitsStr = System.getProperty("profits");
        int[] profits = Arrays.stream(profitsStr.split(", ")).mapToInt(Integer::parseInt).toArray();
        String weightsStr = System.getProperty("weights");
        int[] weights = Arrays.stream(weightsStr.split(", ")).mapToInt(Integer::parseInt).toArray();
        FitnessEvaluation fitEval = new FitnessEvaluation(profits, weights, Double.parseDouble(System.getProperty("capacity")));

        // Evaluate the fitness of the given individual.
        fitEval.fitEval(individual);

        // Write individual with computed fitness to output file.
        try {
            FileWriter inputWriter = new FileWriter(output_path, true);
            inputWriter.append("{\n");
            inputWriter.append(String.format("\tsolution: %s\n", individual.solution));
            inputWriter.append(String.format("\tfitness: %f\n", individual.fitness));
            inputWriter.append("}\n");
            inputWriter.close();
        } catch (IOException e) {
            System.out.println("Could not write individual to file.");
            e.printStackTrace();
        }
    }
}
