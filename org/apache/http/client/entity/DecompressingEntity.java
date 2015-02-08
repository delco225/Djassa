package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.util.Args;

abstract class DecompressingEntity
  extends HttpEntityWrapper
{
  private static final int BUFFER_SIZE = 2048;
  private InputStream content;
  
  public DecompressingEntity(HttpEntity paramHttpEntity)
  {
    super(paramHttpEntity);
  }
  
  private InputStream getDecompressingStream()
    throws IOException
  {
    return new LazyDecompressingInputStream(this.wrappedEntity.getContent(), this);
  }
  
  abstract InputStream decorate(InputStream paramInputStream)
    throws IOException;
  
  public InputStream getContent()
    throws IOException
  {
    if (this.wrappedEntity.isStreaming())
    {
      if (this.content == null) {
        this.content = getDecompressingStream();
      }
      return this.content;
    }
    return getDecompressingStream();
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    Args.notNull(paramOutputStream, "Output stream");
    InputStream localInputStream = getContent();
    try
    {
      byte[] arrayOfByte = new byte[2048];
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
 * Qualified Name:     org.apache.http.client.entity.DecompressingEntity
 * JD-Core Version:    0.7.0.1
 */