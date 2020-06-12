package ai.heuristic;

import ai.state.State;

public class OptionsKnowledgeFoundation implements Heuristic {

    private double knowledgeWeight, optionsWeight, foundationWeight;

    public OptionsKnowledgeFoundation(double knowledgeWeight, double optionsWeight, double foundationWeight){
        this.knowledgeWeight = knowledgeWeight;
        this.optionsWeight = optionsWeight;
        this.foundationWeight = foundationWeight;
    }

    @Override
    public double evaluate(State state) {
        Heuristic knowledge = new LessUnknowns();
        Heuristic options = new MoreOptions();
        Heuristic foundation = new FoundationSize();

        double knowledgeValue = knowledgeWeight * knowledge.evaluate(state);
        double optionsValue = optionsWeight * options.evaluate(state);
        double foundationValue = foundationWeight * foundation.evaluate(state);
        double noise = Math.random() * 0.001;
        return knowledgeValue + optionsValue + foundationValue + noise;
    }
}
