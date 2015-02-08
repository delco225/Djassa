package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class SizeFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  private final boolean acceptLarger;
  private final long size;
  
  public SizeFileFilter(long paramLong)
  {
    this(paramLong, true);
  }
  
  public SizeFileFilter(long paramLong, boolean paramBoolean)
  {
    if (paramLong < 0L) {
      throw new IllegalArgumentException("The size must be non-negative");
    }
    this.size = paramLong;
    this.acceptLarger = paramBoolean;
  }
  
  public boolean accept(File paramFile)
  {
    boolean bool;
    if (paramFile.length() < this.size) {
      bool = true;
    }
    while (this.acceptLarger) {
      if (!bool)
      {
        return true;
        bool = false;
      }
      else
      {
        return false;
      }
    }
    return bool;
  }
  
  public String toString()
  {
    if (this.acceptLarger) {}
    for (String str = ">=";; str = "<") {
      return super.toString() + "(" + str + this.size + ")";
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.SizeFileFilter
 * JD-Core Version:    0.7.0.1
 */