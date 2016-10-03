package prepose.training.datasets;

import java.util.Arrays;
import java.util.function.Consumer;

import com.google.common.base.MoreObjects;

import prepose.features.Featurable;
import prepose.features.Feature;

public class TrainingDataArray<T, F extends Featurable<T>> implements TrainingData<T> {

  private final F[] vectorizedData;

  public TrainingDataArray(final F[] vectorizedData) {

    this.vectorizedData = vectorizedData;
  }

  public F[] getVectorizedData() {

    return vectorizedData;
  }

  @Override
  public void copyColumn(final Feature feature, final double[] column) {

    for (int i = 0, len = vectorizedData.length; i < len; i++) {

      column[i] = vectorizedData[i].getFeature(feature);
    }
  }

  @Override
  public void consumeRows(final Consumer<Featurable<T>> rowConsumer) {

    for (final F f : vectorizedData) {
      rowConsumer.accept(f);
    }
  }

  @Override
  public int getNumRows() {

    return vectorizedData.length;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).toString();
  }

  @Override
  public double[] getSortedColumn(final Feature feature) {

    final double[] column = new double[vectorizedData.length];
    copyColumn(feature, column);

    Arrays.parallelSort(column);

    return column;
  }

  @Override
  public double[] getColumn(final Feature feature) {

    final double[] column = new double[vectorizedData.length];
    copyColumn(feature, column);

    return column;
  }
}
