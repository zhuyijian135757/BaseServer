package net.flyingfat.common.lang;

public class KeyTransformer
  implements Transformer<Object, Object>
{
  public Object transform(Object from)
  {
    if ((from instanceof Identifiable)) {
      return ((Identifiable)from).getIdentification();
    }
    return null;
  }
}
