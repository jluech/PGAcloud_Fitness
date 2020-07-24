package fitness.population;


public class Individual {
    private final double DEFAULT_FITNESS = 0;

    public String solution;
    public double fitness;

    public Individual(String solution) {
        this.solution = solution;
        this.fitness = this.DEFAULT_FITNESS;
    }

    public Individual(String solution, double fitness) {
        this.solution = solution;
        this.fitness = fitness;
    }
}
