package etc.aloe.controllers;

import etc.aloe.data.EvaluationReport;
import etc.aloe.data.ExampleSet;
import etc.aloe.data.FeatureSpecification;
import etc.aloe.data.Model;
import etc.aloe.data.Segment;
import etc.aloe.data.SegmentSet;
import etc.aloe.processes.CrossValidationPrep;
import etc.aloe.processes.CrossValidationSplit;
import etc.aloe.processes.Evaluation;
import etc.aloe.processes.FeatureExtraction;
import etc.aloe.processes.FeatureGeneration;
import etc.aloe.processes.Training;

/**
 * Class that performs cross validation and produces results.
 */
public class CrossValidationController {

    private final int folds;
    private EvaluationReport evaluationReport;
    private SegmentSet segmentSet;
    private FeatureGeneration featureGenerationImpl;
    private FeatureExtraction featureExtractionImpl;
    private Training trainingImpl;
    private Evaluation evaluationImpl;
    private CrossValidationPrep<Segment> crossValidationPrepImpl;
    private CrossValidationSplit<Segment> crossValidationSplitImpl;

    public EvaluationReport getEvaluationReport() {
        return evaluationReport;
    }

    public void setSegmentSet(SegmentSet segments) {
        this.segmentSet = segments;
    }

    public CrossValidationController(int folds) {
        this.folds = folds;
    }

    public void run() {

        //Prepare for cross validation
        CrossValidationPrep<Segment> validationPrep = this.getCrossValidationPrepImpl();
        validationPrep.randomize(segmentSet.getSegments());
        segmentSet.setSegments(validationPrep.stratify(segmentSet.getSegments(), folds));

        evaluationReport = new EvaluationReport();
        for (int foldIndex = 0; foldIndex < this.folds; foldIndex++) {

            //Split the data
            CrossValidationSplit split = this.getCrossValidationSplitImpl();
            SegmentSet trainingSegments = new SegmentSet();
            trainingSegments.setSegments(split.getTrainingForFold(segmentSet.getSegments(), foldIndex, this.folds));
            SegmentSet testingSegments = new SegmentSet();
            testingSegments.setSegments(split.getTestingForFold(segmentSet.getSegments(), foldIndex, this.folds));

            ExampleSet basicTrainingExamples = trainingSegments.getBasicExamples();
            ExampleSet basicTestingExamples = trainingSegments.getBasicExamples();

            FeatureGeneration generation = getFeatureGenerationImpl();
            FeatureSpecification spec = generation.generateFeatures(basicTrainingExamples);

            FeatureExtraction extraction = getFeatureExtractionImpl();
            ExampleSet trainingSet = extraction.extractFeatures(basicTrainingExamples, spec);
            ExampleSet testingSet = extraction.extractFeatures(basicTestingExamples, spec);

            Training training = getTrainingImpl();
            Model model = training.train(trainingSet);

            Evaluation evaluation = getEvaluationImpl();
            EvaluationReport report = evaluation.evaluate(model, testingSet);

            evaluationReport.addPartial(report);
        }

    }

    public FeatureGeneration getFeatureGenerationImpl() {
        return this.featureGenerationImpl;
    }

    public void setFeatureGenerationImpl(FeatureGeneration featureGenerator) {
        this.featureGenerationImpl = featureGenerator;
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

    public Evaluation getEvaluationImpl() {
        return this.evaluationImpl;
    }

    public void setEvaluationImpl(Evaluation evaluation) {
        this.evaluationImpl = evaluation;
    }

    public CrossValidationPrep<Segment> getCrossValidationPrepImpl() {
        return this.crossValidationPrepImpl;
    }

    public void setCrossValidationPrepImpl(CrossValidationPrep<Segment> crossValidationPrep) {
        this.crossValidationPrepImpl = crossValidationPrep;
    }

    public CrossValidationSplit<Segment> getCrossValidationSplitImpl() {
        return this.crossValidationSplitImpl;
    }

    public void setCrossValidationSplitImpl(CrossValidationSplit<Segment> crossValidationSplit) {
        this.crossValidationSplitImpl = crossValidationSplit;
    }
}
