package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.ConnectionClosedException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;

@NotThreadSafe
public class ContentLengthInputStream
  extends InputStream
{
  private static final int BUFFER_SIZE = 2048;
  private boolean closed = false;
  private final long contentLength;
  private SessionInputBuffer in = null;
  private long pos = 0L;
  
  public ContentLengthInputStream(SessionInputBuffer paramSessionInputBuffer, long paramLong)
  {
    this.in = ((SessionInputBuffer)Args.notNull(paramSessionInputBuffer, "Session input buffer"));
    this.contentLength = Args.notNegative(paramLong, "Content length");
  }
  
  public int available()
    throws IOException
  {
    if ((this.in instanceof BufferInfo)) {
      return Math.min(((BufferInfo)this.in).length(), (int)(this.contentLength - this.pos));
    }
    return 0;
  }
  
  public void close()
    throws IOException
  {
    if (!this.closed) {}
    try
    {
      if (this.pos < this.contentLength)
      {
        byte[] arrayOfByte = new byte[2048];
        int i;
        do
        {
          i = read(arrayOfByte);
        } while (i >= 0);
      }
      return;
    }
    finally
    {
      this.closed = true;
    }
  }
  
  public int read()
    throws IOException
  {
    if (this.closed) {
      throw new IOException("Attempted read from closed stream.");
    }
    int i;
    if (this.pos >= this.contentLength) {
      i = -1;
    }
    do
    {
      return i;
      i = this.in.read();
      if (i != -1) {
        break;
      }
    } while (this.pos >= this.contentLength);
    throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + this.contentLength + "; received: " + this.pos);
    this.pos = (1L + this.pos);
    return i;
  }
  
  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (this.closed) {
      throw new IOException("Attempted read from closed stream.");
    }
    int j;
    if (this.pos >= this.contentLength) {
      j = -1;
    }
    do
    {
      return j;
      int i = paramInt2;
      if (this.pos + paramInt2 > this.contentLength) {
        i = (int)(this.contentLength - this.pos);
      }
      j = this.in.read(paramArrayOfByte, paramInt1, i);
      if ((j == -1) && (this.pos < this.contentLength)) {
        throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + this.contentLength + "; received: " + this.pos);
      }
    } while (j <= 0);
    this.pos += j;
    return j;
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    long l2;
    if (paramLong <= 0L) {
      l2 = 0L;
    }
    label83:
    for (;;)
    {
      return l2;
      byte[] arrayOfByte = new byte[2048];
      long l1 = Math.min(paramLong, this.contentLength - this.pos);
      l2 = 0L;
      for (;;)
      {
        if (l1 <= 0L) {
          break label83;
        }
        int i = read(arrayOfByte, 0, (int)Math.min(2048L, l1));
        if (i == -1) {
          break;
        }
        l2 += i;
        l1 -= i;
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.io.ContentLengthInputStream
 * JD-Core Version:    0.7.0.1
 */