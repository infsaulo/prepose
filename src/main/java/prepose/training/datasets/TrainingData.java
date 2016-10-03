package prepose.training.datasets;

import java.util.function.Consumer;

import prepose.features.Featurable;
import prepose.features.Feature;

public interface TrainingData<T> {

  public void copyColumn(final Feature feature, final double[] column);

  public void consumeRows(final Consumer<Featurable<T>> rowConsumer);

  public int getNumRows();

  public double[] getSortedColumn(final Feature feature);

  public double[] getColumn(final Feature feature);
}
