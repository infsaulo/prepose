package prepose.transformers;

public class LogTransformer implements Transformer {

  private static LogTransformer instance = new LogTransformer();

  private LogTransformer() {}

  public static LogTransformer getInstance() {

    return instance;
  }

  @Override
  public double applyAsDouble(final double featureValue) {

    return Math.log(featureValue);
  }

}
