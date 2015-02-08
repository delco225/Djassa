package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class NotFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  private final IOFileFilter filter;
  
  public NotFileFilter(IOFileFilter paramIOFileFilter)
  {
    if (paramIOFileFilter == null) {
      throw new IllegalArgumentException("The filter must not be null");
    }
    this.filter = paramIOFileFilter;
  }
  
  public boolean accept(File paramFile)
  {
    return !this.filter.accept(paramFile);
  }
  
  public boolean accept(File paramFile, String paramString)
  {
    return !this.filter.accept(paramFile, paramString);
  }
  
  public String toString()
  {
    return super.toString() + "(" + this.filter.toString() + ")";
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.NotFileFilter
 * JD-Core Version:    0.7.0.1
 */