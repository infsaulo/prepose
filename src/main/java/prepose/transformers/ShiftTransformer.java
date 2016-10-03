package prepose.transformers;

public class ShiftTransformer implements Transformer {

  private final double shiftValue;

  public ShiftTransformer(final double shiftValue) {
    this.shiftValue = shiftValue;
  }

  @Override
  public double applyAsDouble(final double featureValue) {

    return featureValue + shiftValue;
  }

}
