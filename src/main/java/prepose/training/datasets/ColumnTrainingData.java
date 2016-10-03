package prepose.training.datasets;

import prepose.features.Featurable;
import prepose.features.Feature;

public class ColumnTrainingData<T, F extends Featurable<T>> extends TrainingDataArray<T, F> {

  private final double[][] columns;
  private final int numRows;

  public ColumnTrainingData(final F[] vectorizedData, final double[][] columns) {

    super(vectorizedData);

    this.columns = columns;
    this.numRows = columns[0].length;
  }

  @Override
  public void copyColumn(final Feature feature, final double[] column) {

    System.arraycopy(columns[feature.getIndex()], 0, column, 0, numRows);
  }

  @Override
  public int getNumRows() {

    return numRows;
  }

  @Override
  public double[] getSortedColumn(final Feature feature) {

    return columns[feature.getIndex()];
  }

  @Override
  public double[] getColumn(final Feature feature) {

    return columns[feature.getIndex()];
  }
}
