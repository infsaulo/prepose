package prepose.transformers;

import java.util.function.DoubleUnaryOperator;

public interface Transformer extends DoubleUnaryOperator {

  @Override
  public double applyAsDouble(final double featureValue);

}
