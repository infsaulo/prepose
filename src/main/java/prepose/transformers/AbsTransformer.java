package prepose.transformers;

public class AbsTransformer implements Transformer {

  private static final AbsTransformer instance = new AbsTransformer();

  private AbsTransformer() {}

  public static AbsTransformer getInstance() {

    return instance;
  }

  @Override
  public double applyAsDouble(final double featureValue) {

    return Math.abs(featureValue);
  }

}
