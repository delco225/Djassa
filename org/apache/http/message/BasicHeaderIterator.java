package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class BasicHeaderIterator
  implements HeaderIterator
{
  protected final Header[] allHeaders;
  protected int currentIndex;
  protected String headerName;
  
  public BasicHeaderIterator(Header[] paramArrayOfHeader, String paramString)
  {
    this.allHeaders = ((Header[])Args.notNull(paramArrayOfHeader, "Header array"));
    this.headerName = paramString;
    this.currentIndex = findNext(-1);
  }
  
  protected boolean filterHeader(int paramInt)
  {
    return (this.headerName == null) || (this.headerName.equalsIgnoreCase(this.allHeaders[paramInt].getName()));
  }
  
  protected int findNext(int paramInt)
  {
    int i = paramInt;
    if (i < -1) {}
    boolean bool;
    do
    {
      return -1;
      int j = -1 + this.allHeaders.length;
      for (bool = false; (!bool) && (i < j); bool = filterHeader(i)) {
        i++;
      }
    } while (!bool);
    return i;
  }
  
  public boolean hasNext()
  {
    return this.currentIndex >= 0;
  }
  
  public final Object next()
    throws NoSuchElementException
  {
    return nextHeader();
  }
  
  public Header nextHeader()
    throws NoSuchElementException
  {
    int i = this.currentIndex;
    if (i < 0) {
      throw new NoSuchElementException("Iteration already finished.");
    }
    this.currentIndex = findNext(i);
    return this.allHeaders[i];
  }
  
  public void remove()
    throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException("Removing headers is not supported.");
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.message.BasicHeaderIterator
 * JD-Core Version:    0.7.0.1
 */