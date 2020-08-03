package fitness;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// Quick tutorial on how to create and start a JAR from simple Java code
// https://docs.oracle.com/javase/tutorial/deployment/jar/appman.html
// Create jar file inside "main" directory like so: jar cfe FitnessEvaluation.jar fitness.Main fitness
// View jar content like so: jar tf FitnessEvaluation.jar


public class Main {
    private static final String input_path = "/pga/input.yml";
    private static final String output_path = "/pga/output.yml";

    public static void main(String[] args) {
        // Collect individual from input file.
        List<Map<String, String>> input_data = new ArrayList<>();
        try {
            File inputFile = new File(input_path);
            Scanner inputReader = new Scanner(inputFile);
            while (inputReader.hasNextLine()) {
                String line = inputReader.nextLine();
                line = line.replace("{", "");
                line = line.replace("}", "");
                line = line.replace("\"", "");
                String[] fields = line.split(",");
                Map<String, String> data = new HashMap<>();
                for(String field : fields) {
                    String[] key_value = field.split(":");
                    String key = key_value[0].strip();
                    String value = key_value[1].strip();
                    data.put(key, value);
                }
                input_data.add(data);
            }
            inputReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Provided file was not found.");
            e.printStackTrace();
        }
        Map<String, String> ind_map = input_data.get(0);
        Individual individual = new Individual(ind_map.get("solution"), Double.parseDouble(ind_map.get("fitness")));

        System.out.println(String.format("Evaluating individual '%s'", individual.solution));

        // Parse input data and create fitness operator with it.
        // Passed in as arguments like so "capacity=150" or "profits=1,5,2,4,3"
        Map<String, String> argVals = new HashMap<>();
        for(String arg : args) {
            String[] keyVal = arg.split("=");
            String key = keyVal[0];
            String val = keyVal[1];
            argVals.put(key, val);
        }

        String profitsStr = argVals.get("profits");
        String[] profitsArr = profitsStr.split(",");
        ArrayList<Integer> profits = new ArrayList<>();
        for(String profit : profitsArr) {
            profits.add(Integer.parseInt(profit.strip()));
        }

        String[] weightsArr = argVals.get("weights").split(",");
        ArrayList<Integer> weights = new ArrayList<>();
        for(String weight : weightsArr) {
            weights.add(Integer.parseInt(weight.strip()));
        }

        FitnessEvaluation fitEval = new FitnessEvaluation(profits, weights, Double.parseDouble(argVals.get("capacity")));

        // Evaluate the fitness of the given individual.
        fitEval.fitEval(individual);
        System.out.println("Evaluated individual with fitness: " + individual.fitness);

        // Write individual with computed fitness to output file.
        try {
            List<String> content = Arrays.asList(
                    "{",
                    String.format("\t\"solution\": \"%s\",", individual.solution),
                    String.format("\t\"fitness\": %f", individual.fitness),
                    "}"
            );
            Path file = Paths.get(output_path);
            Files.write(file, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
