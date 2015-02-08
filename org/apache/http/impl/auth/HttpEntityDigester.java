package org.apache.http.impl.auth;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;

class HttpEntityDigester
  extends OutputStream
{
  private boolean closed;
  private byte[] digest;
  private final MessageDigest digester;
  
  HttpEntityDigester(MessageDigest paramMessageDigest)
  {
    this.digester = paramMessageDigest;
    this.digester.reset();
  }
  
  public void close()
    throws IOException
  {
    if (this.closed) {
      return;
    }
    this.closed = true;
    this.digest = this.digester.digest();
    super.close();
  }
  
  public byte[] getDigest()
  {
    return this.digest;
  }
  
  public void write(int paramInt)
    throws IOException
  {
    if (this.closed) {
      throw new IOException("Stream has been already closed");
    }
    this.digester.update((byte)paramInt);
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (this.closed) {
      throw new IOException("Stream has been already closed");
    }
    this.digester.update(paramArrayOfByte, paramInt1, paramInt2);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.HttpEntityDigester
 * JD-Core Version:    0.7.0.1
 */