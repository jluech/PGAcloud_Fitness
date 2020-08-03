package fitness;


import java.util.List;

class FitnessEvaluation {
    private List<Integer> items_profits;
    private List<Integer> items_weights;
    private double capacity;

    FitnessEvaluation(List<Integer> all_profits, List<Integer> all_weights, double knapsack_capacity) {
        this.items_profits = all_profits;
        this.items_weights = all_weights;
        this.capacity = knapsack_capacity;
    }

    double fitEval(Individual individual) {
        double profits = 0;
        double used_weight = 0;
        double available_weight = 0;

        for (int i=0; i < individual.solution.length(); i++) {
            int bit = Integer.parseInt(individual.solution.substring(i, i+1));
            profits += (bit * this.items_profits.get(i));
            used_weight += (bit * this.items_weights.get(i));
            available_weight += this.items_weights.get(i);
        }

        double scaled_unused_weight = available_weight * Math.abs(capacity - used_weight);
        double fitness = profits - scaled_unused_weight;

        individual.fitness = fitness;
        return fitness;
    }
}
