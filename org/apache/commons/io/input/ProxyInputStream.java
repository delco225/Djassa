package org.apache.commons.io.input;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class ProxyInputStream
  extends FilterInputStream
{
  public ProxyInputStream(InputStream paramInputStream)
  {
    super(paramInputStream);
  }
  
  protected void afterRead(int paramInt)
    throws IOException
  {}
  
  public int available()
    throws IOException
  {
    try
    {
      int i = super.available();
      return i;
    }
    catch (IOException localIOException)
    {
      handleIOException(localIOException);
    }
    return 0;
  }
  
  protected void beforeRead(int paramInt)
    throws IOException
  {}
  
  public void close()
    throws IOException
  {
    try
    {
      this.in.close();
      return;
    }
    catch (IOException localIOException)
    {
      handleIOException(localIOException);
    }
  }
  
  protected void handleIOException(IOException paramIOException)
    throws IOException
  {
    throw paramIOException;
  }
  
  public void mark(int paramInt)
  {
    try
    {
      this.in.mark(paramInt);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean markSupported()
  {
    return this.in.markSupported();
  }
  
  public int read()
    throws IOException
  {
    int i = 1;
    try
    {
      beforeRead(1);
      int j = this.in.read();
      if (j != -1) {}
      for (;;)
      {
        afterRead(i);
        return j;
        i = -1;
      }
      return -1;
    }
    catch (IOException localIOException)
    {
      handleIOException(localIOException);
    }
  }
  
  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    if (paramArrayOfByte != null) {}
    for (;;)
    {
      try
      {
        i = paramArrayOfByte.length;
        beforeRead(i);
        int j = this.in.read(paramArrayOfByte);
        afterRead(j);
        return j;
      }
      catch (IOException localIOException)
      {
        int i;
        handleIOException(localIOException);
      }
      i = 0;
    }
    return -1;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      beforeRead(paramInt2);
      int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
      afterRead(i);
      return i;
    }
    catch (IOException localIOException)
    {
      handleIOException(localIOException);
    }
    return -1;
  }
  
  public void reset()
    throws IOException
  {
    try
    {
      this.in.reset();
      return;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        handleIOException(localIOException);
      }
    }
    finally {}
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    try
    {
      long l = this.in.skip(paramLong);
      return l;
    }
    catch (IOException localIOException)
    {
      handleIOException(localIOException);
    }
    return 0L;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.ProxyInputStream
 * JD-Core Version:    0.7.0.1
 */