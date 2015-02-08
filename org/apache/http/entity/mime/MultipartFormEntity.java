package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

class MultipartFormEntity
  implements HttpEntity
{
  private final long contentLength;
  private final Header contentType;
  private final AbstractMultipartForm multipart;
  
  MultipartFormEntity(AbstractMultipartForm paramAbstractMultipartForm, String paramString, long paramLong)
  {
    this.multipart = paramAbstractMultipartForm;
    this.contentType = new BasicHeader("Content-Type", paramString);
    this.contentLength = paramLong;
  }
  
  public void consumeContent()
    throws IOException, UnsupportedOperationException
  {
    if (isStreaming()) {
      throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
    }
  }
  
  public InputStream getContent()
    throws IOException
  {
    throw new UnsupportedOperationException("Multipart form entity does not implement #getContent()");
  }
  
  public Header getContentEncoding()
  {
    return null;
  }
  
  public long getContentLength()
  {
    return this.contentLength;
  }
  
  public Header getContentType()
  {
    return this.contentType;
  }
  
  AbstractMultipartForm getMultipart()
  {
    return this.multipart;
  }
  
  public boolean isChunked()
  {
    return !isRepeatable();
  }
  
  public boolean isRepeatable()
  {
    return this.contentLength != -1L;
  }
  
  public boolean isStreaming()
  {
    return !isRepeatable();
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    this.multipart.writeTo(paramOutputStream);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.mime.MultipartFormEntity
 * JD-Core Version:    0.7.0.1
 */