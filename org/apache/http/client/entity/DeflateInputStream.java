package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class DeflateInputStream
  extends InputStream
{
  private InputStream sourceStream;
  
  public DeflateInputStream(InputStream paramInputStream)
    throws IOException
  {
    byte[] arrayOfByte1 = new byte[6];
    PushbackInputStream localPushbackInputStream = new PushbackInputStream(paramInputStream, arrayOfByte1.length);
    int i = localPushbackInputStream.read(arrayOfByte1);
    if (i == -1) {
      throw new IOException("Unable to read the response");
    }
    byte[] arrayOfByte2 = new byte[1];
    Inflater localInflater = new Inflater();
    for (;;)
    {
      try
      {
        j = localInflater.inflate(arrayOfByte2);
        if (j == 0) {
          if (localInflater.finished()) {
            throw new IOException("Unable to read the response");
          }
        }
      }
      catch (DataFormatException localDataFormatException)
      {
        int j;
        localPushbackInputStream.unread(arrayOfByte1, 0, i);
        this.sourceStream = new DeflateStream(localPushbackInputStream, new Inflater(true));
        return;
        if (localInflater.needsDictionary())
        {
          if (j != -1) {
            break;
          }
          throw new IOException("Unable to read the response");
        }
      }
      finally
      {
        localInflater.end();
      }
      if (localInflater.needsInput()) {
        localInflater.setInput(arrayOfByte1);
      }
    }
    localPushbackInputStream.unread(arrayOfByte1, 0, i);
    this.sourceStream = new DeflateStream(localPushbackInputStream, new Inflater());
    localInflater.end();
  }
  
  public int available()
    throws IOException
  {
    return this.sourceStream.available();
  }
  
  public void close()
    throws IOException
  {
    this.sourceStream.close();
  }
  
  public void mark(int paramInt)
  {
    this.sourceStream.mark(paramInt);
  }
  
  public boolean markSupported()
  {
    return this.sourceStream.markSupported();
  }
  
  public int read()
    throws IOException
  {
    return this.sourceStream.read();
  }
  
  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return this.sourceStream.read(paramArrayOfByte);
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    return this.sourceStream.read(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public void reset()
    throws IOException
  {
    this.sourceStream.reset();
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    return this.sourceStream.skip(paramLong);
  }
  
  static class DeflateStream
    extends InflaterInputStream
  {
    private boolean closed = false;
    
    public DeflateStream(InputStream paramInputStream, Inflater paramInflater)
    {
      super(paramInflater);
    }
    
    public void close()
      throws IOException
    {
      if (this.closed) {
        return;
      }
      this.closed = true;
      this.inf.end();
      super.close();
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.entity.DeflateInputStream
 * JD-Core Version:    0.7.0.1
 */