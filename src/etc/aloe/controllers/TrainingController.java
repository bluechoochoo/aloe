/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package etc.aloe.controllers;

import etc.aloe.data.ExampleSet;
import etc.aloe.data.FeatureSpecification;
import etc.aloe.data.Model;
import etc.aloe.data.SegmentSet;
import etc.aloe.processes.FeatureExtraction;
import etc.aloe.processes.FeatureGeneration;
import etc.aloe.processes.Training;

/**
 *
 * @author kuksenok
 */
public class TrainingController {

    private SegmentSet segmentSet;
    private FeatureSpecification featureSpecification;
    private Model model;
    private FeatureExtraction featureExtractionImpl;
    private Training trainingImpl;
    private FeatureGeneration featureGenerationImpl;

    public void setSegmentSet(SegmentSet segments) {
        this.segmentSet = segments;
    }

    public FeatureSpecification getFeatureSpecification() {
        return this.featureSpecification;
    }

    public Model getModel() {
        return this.model;
    }

    public void run() {

        System.out.println("== Training Final Model ==");

        ExampleSet basicExamples = segmentSet.getBasicExamples();

        //Generate the features
        FeatureGeneration generation = getFeatureGenerationImpl();
        this.featureSpecification = generation.generateFeatures(basicExamples);

        //Extract features
        FeatureExtraction extraction = getFeatureExtractionImpl();
        ExampleSet examples = extraction.extractFeatures(basicExamples, this.featureSpecification);

        //Train the model
        Training training = getTrainingImpl();
        this.model = training.train(examples);
    }

    public FeatureExtraction getFeatureExtractionImpl() {
        return this.featureExtractionImpl;
    }

    public void setFeatureExtractionImpl(FeatureExtraction featureExtractor) {
        this.featureExtractionImpl = featureExtractor;
    }

    public Training getTrainingImpl() {
        return this.trainingImpl;
    }

    public void setTrainingImpl(Training training) {
        this.trainingImpl = training;
    }

    public void setFeatureGenerationImpl(FeatureGeneration featureGenerationImpl) {
        this.featureGenerationImpl = featureGenerationImpl;
    }

    public FeatureGeneration getFeatureGenerationImpl() {
        return this.featureGenerationImpl;
    }
}
