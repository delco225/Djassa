package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@NotThreadSafe
public class BufferedHttpEntity
  extends HttpEntityWrapper
{
  private final byte[] buffer;
  
  public BufferedHttpEntity(HttpEntity paramHttpEntity)
    throws IOException
  {
    super(paramHttpEntity);
    if ((!paramHttpEntity.isRepeatable()) || (paramHttpEntity.getContentLength() < 0L))
    {
      this.buffer = EntityUtils.toByteArray(paramHttpEntity);
      return;
    }
    this.buffer = null;
  }
  
  public InputStream getContent()
    throws IOException
  {
    if (this.buffer != null) {
      return new ByteArrayInputStream(this.buffer);
    }
    return super.getContent();
  }
  
  public long getContentLength()
  {
    if (this.buffer != null) {
      return this.buffer.length;
    }
    return super.getContentLength();
  }
  
  public boolean isChunked()
  {
    return (this.buffer == null) && (super.isChunked());
  }
  
  public boolean isRepeatable()
  {
    return true;
  }
  
  public boolean isStreaming()
  {
    return (this.buffer == null) && (super.isStreaming());
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    Args.notNull(paramOutputStream, "Output stream");
    if (this.buffer != null)
    {
      paramOutputStream.write(this.buffer);
      return;
    }
    super.writeTo(paramOutputStream);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.BufferedHttpEntity
 * JD-Core Version:    0.7.0.1
 */