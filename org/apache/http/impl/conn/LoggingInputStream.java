package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
class LoggingInputStream
  extends InputStream
{
  private final InputStream in;
  private final Wire wire;
  
  public LoggingInputStream(InputStream paramInputStream, Wire paramWire)
  {
    this.in = paramInputStream;
    this.wire = paramWire;
  }
  
  public int available()
    throws IOException
  {
    try
    {
      int i = this.in.available();
      return i;
    }
    catch (IOException localIOException)
    {
      this.wire.input("[available] I/O error : " + localIOException.getMessage());
      throw localIOException;
    }
  }
  
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
      this.wire.input("[close] I/O error: " + localIOException.getMessage());
      throw localIOException;
    }
  }
  
  public void mark(int paramInt)
  {
    super.mark(paramInt);
  }
  
  public boolean markSupported()
  {
    return false;
  }
  
  public int read()
    throws IOException
  {
    try
    {
      int i = this.in.read();
      if (i == -1)
      {
        this.wire.input("end of stream");
        return i;
      }
      this.wire.input(i);
      return i;
    }
    catch (IOException localIOException)
    {
      this.wire.input("[read] I/O error: " + localIOException.getMessage());
      throw localIOException;
    }
  }
  
  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    int i;
    try
    {
      i = this.in.read(paramArrayOfByte);
      if (i == -1)
      {
        this.wire.input("end of stream");
        return i;
      }
      if (i > 0)
      {
        this.wire.input(paramArrayOfByte, 0, i);
        return i;
      }
    }
    catch (IOException localIOException)
    {
      this.wire.input("[read] I/O error: " + localIOException.getMessage());
      throw localIOException;
    }
    return i;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i;
    try
    {
      i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
      if (i == -1)
      {
        this.wire.input("end of stream");
        return i;
      }
      if (i > 0)
      {
        this.wire.input(paramArrayOfByte, paramInt1, i);
        return i;
      }
    }
    catch (IOException localIOException)
    {
      this.wire.input("[read] I/O error: " + localIOException.getMessage());
      throw localIOException;
    }
    return i;
  }
  
  public void reset()
    throws IOException
  {
    super.reset();
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    try
    {
      long l = super.skip(paramLong);
      return l;
    }
    catch (IOException localIOException)
    {
      this.wire.input("[skip] I/O error: " + localIOException.getMessage());
      throw localIOException;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.LoggingInputStream
 * JD-Core Version:    0.7.0.1
 */