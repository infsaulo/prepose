package prepose.transformers;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.function.DoubleUnaryOperator;

import prepose.features.Featurable;
import prepose.features.Feature;
import prepose.stats.SimpleSummaryStats;
import prepose.training.datasets.TrainingData;

public class CombinedTransformer {

  private static final AbsTransformer absTransformer = AbsTransformer.getInstance();
  private static final LogTransformer logTransformer = LogTransformer.getInstance();
  private static final ShiftTransformer shiftTransformer = new ShiftTransformer(
      Optional.ofNullable(System.getProperty("transformer.feature.shift_value"))
          .map(Double::parseDouble).orElse(100.0));
  private static final DoubleUnaryOperator composedTransformer =
      absTransformer.andThen(shiftTransformer).andThen(logTransformer);


  public static void transformFeatures(final Featurable<?> featuresValue,
      final Set<Feature> features, final double[] meanFeatures, final double[] stdFeatures) {

    for (final Feature feature : features) {
      final double mean = meanFeatures[feature.getIndex()];
      final double std = stdFeatures[feature.getIndex()];
      final StandardizationTransformer stdTransformer = new StandardizationTransformer(mean, std);
      double featureValue = composedTransformer.applyAsDouble(featuresValue.getFeature(feature));
      featureValue = stdTransformer.applyAsDouble(featureValue);
      featuresValue.setFeature(feature, featureValue);
    }
  }

  public static void transformFeatures(final Executor executor, final TrainingData<?> trainingData,
      final Set<Feature> features, final double[] meanFeatures, final double[] stdFeatures) {

    final CountDownLatch transformationLatch1 = new CountDownLatch(features.size());
    for (final Feature feature : features) {
      executor.execute(() -> {
        final double[] featureValues = trainingData.getColumn(feature);
        for (int rowIndex = 0; rowIndex < featureValues.length; rowIndex++) {

          featureValues[rowIndex] = composedTransformer.applyAsDouble(featureValues[rowIndex]);
        }
        transformationLatch1.countDown();
      });
    }

    try {
      transformationLatch1.await();
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }

    final CountDownLatch transformationLatch2 = new CountDownLatch(features.size());
    // Get the features mean and variance to standardization
    for (final Feature feature : features) {

      executor.execute(() -> {

        final SimpleSummaryStats featureStats = new SimpleSummaryStats();
        double mean = 0;
        double std = 0;

        final double[] featureValues = trainingData.getColumn(feature);
        for (int rowIndex = 0; rowIndex < featureValues.length; rowIndex++) {
          featureStats.addValue(featureValues[rowIndex]);
        }

        mean = featureStats.getMean();
        std = Math.sqrt(featureStats.getVariance());
        meanFeatures[feature.getIndex()] = mean;
        stdFeatures[feature.getIndex()] = std;
        final StandardizationTransformer stdTransformer = new StandardizationTransformer(mean, std);
        for (int rowIndex = 0; rowIndex < featureValues.length; rowIndex++) {
          featureValues[rowIndex] = stdTransformer.applyAsDouble(featureValues[rowIndex]);
        }

        transformationLatch2.countDown();
      });
    }

    try {
      transformationLatch2.await();
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }
}
