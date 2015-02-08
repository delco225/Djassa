package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
class LoggingOutputStream
  extends OutputStream
{
  private final OutputStream out;
  private final Wire wire;
  
  public LoggingOutputStream(OutputStream paramOutputStream, Wire paramWire)
  {
    this.out = paramOutputStream;
    this.wire = paramWire;
  }
  
  public void close()
    throws IOException
  {
    try
    {
      this.out.close();
      return;
    }
    catch (IOException localIOException)
    {
      this.wire.output("[close] I/O error: " + localIOException.getMessage());
      throw localIOException;
    }
  }
  
  public void flush()
    throws IOException
  {
    try
    {
      this.out.flush();
      return;
    }
    catch (IOException localIOException)
    {
      this.wire.output("[flush] I/O error: " + localIOException.getMessage());
      throw localIOException;
    }
  }
  
  public void write(int paramInt)
    throws IOException
  {
    try
    {
      this.wire.output(paramInt);
      return;
    }
    catch (IOException localIOException)
    {
      this.wire.output("[write] I/O error: " + localIOException.getMessage());
      throw localIOException;
    }
  }
  
  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    try
    {
      this.wire.output(paramArrayOfByte);
      this.out.write(paramArrayOfByte);
      return;
    }
    catch (IOException localIOException)
    {
      this.wire.output("[write] I/O error: " + localIOException.getMessage());
      throw localIOException;
    }
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      this.wire.output(paramArrayOfByte, paramInt1, paramInt2);
      this.out.write(paramArrayOfByte, paramInt1, paramInt2);
      return;
    }
    catch (IOException localIOException)
    {
      this.wire.output("[write] I/O error: " + localIOException.getMessage());
      throw localIOException;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.LoggingOutputStream
 * JD-Core Version:    0.7.0.1
 */