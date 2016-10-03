package prepose.features;

public interface Featurable<T> {

  public void setFeature(final Feature feature, final double value);

  public double getFeature(final Feature feature);

  public double[] getVectorized();

  public T get();
}
