package org.apache.commons.codec;

import java.util.Comparator;

public class StringEncoderComparator
  implements Comparator
{
  private final StringEncoder stringEncoder;
  
  public StringEncoderComparator()
  {
    this.stringEncoder = null;
  }
  
  public StringEncoderComparator(StringEncoder paramStringEncoder)
  {
    this.stringEncoder = paramStringEncoder;
  }
  
  public int compare(Object paramObject1, Object paramObject2)
  {
    try
    {
      int i = ((Comparable)this.stringEncoder.encode(paramObject1)).compareTo((Comparable)this.stringEncoder.encode(paramObject2));
      return i;
    }
    catch (EncoderException localEncoderException) {}
    return 0;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.codec.StringEncoderComparator
 * JD-Core Version:    0.7.0.1
 */