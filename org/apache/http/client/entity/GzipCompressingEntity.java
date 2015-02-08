package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.Args;

public class GzipCompressingEntity
  extends HttpEntityWrapper
{
  private static final String GZIP_CODEC = "gzip";
  
  public GzipCompressingEntity(HttpEntity paramHttpEntity)
  {
    super(paramHttpEntity);
  }
  
  public InputStream getContent()
    throws IOException
  {
    throw new UnsupportedOperationException();
  }
  
  public Header getContentEncoding()
  {
    return new BasicHeader("Content-Encoding", "gzip");
  }
  
  public long getContentLength()
  {
    return -1L;
  }
  
  public boolean isChunked()
  {
    return true;
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    Args.notNull(paramOutputStream, "Output stream");
    GZIPOutputStream localGZIPOutputStream = new GZIPOutputStream(paramOutputStream);
    try
    {
      this.wrappedEntity.writeTo(localGZIPOutputStream);
      return;
    }
    finally
    {
      localGZIPOutputStream.close();
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.entity.GzipCompressingEntity
 * JD-Core Version:    0.7.0.1
 */