package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;

@NotThreadSafe
public class IdentityInputStream
  extends InputStream
{
  private boolean closed = false;
  private final SessionInputBuffer in;
  
  public IdentityInputStream(SessionInputBuffer paramSessionInputBuffer)
  {
    this.in = ((SessionInputBuffer)Args.notNull(paramSessionInputBuffer, "Session input buffer"));
  }
  
  public int available()
    throws IOException
  {
    if ((this.in instanceof BufferInfo)) {
      return ((BufferInfo)this.in).length();
    }
    return 0;
  }
  
  public void close()
    throws IOException
  {
    this.closed = true;
  }
  
  public int read()
    throws IOException
  {
    if (this.closed) {
      return -1;
    }
    return this.in.read();
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (this.closed) {
      return -1;
    }
    return this.in.read(paramArrayOfByte, paramInt1, paramInt2);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.io.IdentityInputStream
 * JD-Core Version:    0.7.0.1
 */