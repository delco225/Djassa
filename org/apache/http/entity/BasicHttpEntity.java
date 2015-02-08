package org.apache.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@NotThreadSafe
public class BasicHttpEntity
  extends AbstractHttpEntity
{
  private InputStream content;
  private long length = -1L;
  
  public InputStream getContent()
    throws IllegalStateException
  {
    if (this.content != null) {}
    for (boolean bool = true;; bool = false)
    {
      Asserts.check(bool, "Content has not been provided");
      return this.content;
    }
  }
  
  public long getContentLength()
  {
    return this.length;
  }
  
  public boolean isRepeatable()
  {
    return false;
  }
  
  public boolean isStreaming()
  {
    return this.content != null;
  }
  
  public void setContent(InputStream paramInputStream)
  {
    this.content = paramInputStream;
  }
  
  public void setContentLength(long paramLong)
  {
    this.length = paramLong;
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    Args.notNull(paramOutputStream, "Output stream");
    InputStream localInputStream = getContent();
    try
    {
      byte[] arrayOfByte = new byte[4096];
      for (;;)
      {
        int i = localInputStream.read(arrayOfByte);
        if (i == -1) {
          break;
        }
        paramOutputStream.write(arrayOfByte, 0, i);
      }
    }
    finally
    {
      localInputStream.close();
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.BasicHttpEntity
 * JD-Core Version:    0.7.0.1
 */