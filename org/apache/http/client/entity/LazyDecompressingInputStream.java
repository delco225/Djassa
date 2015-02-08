package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
class LazyDecompressingInputStream
  extends InputStream
{
  private final DecompressingEntity decompressingEntity;
  private final InputStream wrappedStream;
  private InputStream wrapperStream;
  
  public LazyDecompressingInputStream(InputStream paramInputStream, DecompressingEntity paramDecompressingEntity)
  {
    this.wrappedStream = paramInputStream;
    this.decompressingEntity = paramDecompressingEntity;
  }
  
  private void initWrapper()
    throws IOException
  {
    if (this.wrapperStream == null) {
      this.wrapperStream = this.decompressingEntity.decorate(this.wrappedStream);
    }
  }
  
  public int available()
    throws IOException
  {
    initWrapper();
    return this.wrapperStream.available();
  }
  
  public void close()
    throws IOException
  {
    try
    {
      if (this.wrapperStream != null) {
        this.wrapperStream.close();
      }
      return;
    }
    finally
    {
      this.wrappedStream.close();
    }
  }
  
  public boolean markSupported()
  {
    return false;
  }
  
  public int read()
    throws IOException
  {
    initWrapper();
    return this.wrapperStream.read();
  }
  
  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    initWrapper();
    return this.wrapperStream.read(paramArrayOfByte);
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    initWrapper();
    return this.wrapperStream.read(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    initWrapper();
    return this.wrapperStream.skip(paramLong);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.entity.LazyDecompressingInputStream
 * JD-Core Version:    0.7.0.1
 */