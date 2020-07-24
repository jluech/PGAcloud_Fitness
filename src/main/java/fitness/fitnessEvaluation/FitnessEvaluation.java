package fitness.fitnessEvaluation;

import fitness.population.Individual;


class FitnessEvaluation {
    private int[] items_profits;
    private int[] items_weights;
    private double capacity;

    FitnessEvaluation(int[] all_profits, int[] all_weights, double knapsack_capacity) {
        this.items_profits = all_profits;
        this.items_weights = all_weights;
        this.capacity = knapsack_capacity;
    }

    double fitEval(Individual individual) {
        double profits = 0;
        double used_weight = 0;
        double available_weight = 0;

        for (int i=0; i < individual.solution.length(); i++) {
            profits += (int) individual.solution.charAt(i) * this.items_profits[i];
            used_weight += (int) individual.solution.charAt(i) * this.items_weights[i];
            available_weight += this.items_weights[i];
        }

        double scaled_unused_weight = available_weight * Math.abs(capacity - used_weight);
        double fitness = profits - scaled_unused_weight;

        individual.fitness = fitness;
        return fitness;
    }
}
