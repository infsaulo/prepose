package prepose.transformers;

public class StandardizationTransformer implements Transformer {

  private final double mean;
  private final double std;

  public StandardizationTransformer(final double mean, final double std) {

    this.mean = mean;
    this.std = std;
  }

  @Override
  public double applyAsDouble(final double featureValue) {

    final double standardizedValue = (featureValue - mean) / std;
    return standardizedValue;
  }

}
