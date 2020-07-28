package fitness;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

// Quick tutorial on how to create and start a JAR from simple Java code
// https://docs.oracle.com/javase/tutorial/deployment/jar/appman.html
// Create jar file like so: jar cfmv FitnessEvaluation.jar Manifest.txt src/main/java/fitness
// View jar content like so: jar tf FitnessEvaluation.jar


public class Main {
    private static final String input_path = "/pga/fitness/input.yml";
    private static final String output_path = "/pga/fitness/output.yml";

    public static void main(String[] args) {
        System.out.println("Hello Fitness Evaluation");
        System.out.println(String.join(" ", args));

        System.out.println("Input: "+input_path);
        System.out.println("Output: "+output_path);

        // Collect individual from input file.
        Individual individual = null;
        Gson gson = new Gson();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(input_path));
            individual = gson.fromJson(reader, Individual.class);
            reader.close();
        } catch (IOException e) {
            System.out.println("Provided file was not found.");
            e.printStackTrace();
        }

        if (individual == null) {
            throw new IllegalArgumentException("Could not read and initialize individual!");
        }

        // Parse input data and create fitness operator with it.
        // Passed in like so "-Dcapacity=150" or "-Dprofits=1,5,2,4,3"
        String profitsStr = System.getProperty("profits");
        int[] profits = Arrays.stream(profitsStr.split(",")).mapToInt(Integer::parseInt).toArray();
        String weightsStr = System.getProperty("weights");
        int[] weights = Arrays.stream(weightsStr.split(",")).mapToInt(Integer::parseInt).toArray();
        FitnessEvaluation fitEval = new FitnessEvaluation(profits, weights, Double.parseDouble(System.getProperty("capacity")));

        // Evaluate the fitness of the given individual.
        fitEval.fitEval(individual);

        // Write individual with computed fitness to output file.
        try {
            FileWriter writer = new FileWriter(output_path);
            String individualString = gson.toJson(individual, Individual.class);
            writer.append(individualString);
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not write result to file.");
            e.printStackTrace();
        }
    }
}
