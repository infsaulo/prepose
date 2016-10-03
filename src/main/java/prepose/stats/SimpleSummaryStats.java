package prepose.stats;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.SecondMoment;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

import com.google.common.base.MoreObjects;


public class SimpleSummaryStats {

  private double min;
  private double max;
  private double sum;

  private final SecondMoment secondMoment;
  private final Mean mean;
  private final Variance variance;

  public SimpleSummaryStats() {
    this.min = Double.NaN;
    this.max = Double.NaN;
    this.secondMoment = new SecondMoment();
    this.mean = new Mean(secondMoment);
    this.variance = new Variance(secondMoment);
  }

  public SimpleSummaryStats(final double min, final double max, final double sum,
      final SecondMoment secondMoment) {
    this.min = min;
    this.max = max;
    this.sum = sum;
    this.secondMoment = secondMoment.copy();
    this.mean = new Mean(secondMoment);
    this.variance = new Variance(secondMoment);
  }

  public void addValue(final double value) {
    if (value < min) {
      min = value;
    } else if (value > max) {
      max = value;
    } else if (Double.isNaN(min)) {
      min = value;
      max = value;
    }
    sum += value;
    secondMoment.increment(value);
  }

  public double getSum() {
    return sum;
  }

  public double getMin() {
    return min;
  }

  public double getMax() {
    return max;
  }

  public double getMean() {
    return mean.getResult();
  }

  public double getVariance() {
    return variance.getResult();
  }

  public long getN() {
    return secondMoment.getN();
  }

  public SecondMoment getSecondMoment() {
    return secondMoment;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("min", min).add("max", max).add("sum", sum)
        .add("secondMoment", secondMoment.getResult()).add("mean", mean.getResult())
        .add("variance", variance.getResult()).add("n", getN()).toString();
  }
}
