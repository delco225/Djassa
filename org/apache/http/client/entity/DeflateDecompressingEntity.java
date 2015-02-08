package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

public class DeflateDecompressingEntity
  extends DecompressingEntity
{
  public DeflateDecompressingEntity(HttpEntity paramHttpEntity)
  {
    super(paramHttpEntity);
  }
  
  InputStream decorate(InputStream paramInputStream)
    throws IOException
  {
    return new DeflateInputStream(paramInputStream);
  }
  
  public Header getContentEncoding()
  {
    return null;
  }
  
  public long getContentLength()
  {
    return -1L;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.entity.DeflateDecompressingEntity
 * JD-Core Version:    0.7.0.1
 */