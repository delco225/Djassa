package org.apache.http.client.fluent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

class InternalFileEntity
  extends AbstractHttpEntity
  implements Cloneable
{
  private final File file;
  
  public InternalFileEntity(File paramFile, ContentType paramContentType)
  {
    this.file = ((File)Args.notNull(paramFile, "File"));
    if (paramContentType != null) {
      setContentType(paramContentType.toString());
    }
  }
  
  public InputStream getContent()
    throws IOException
  {
    return new FileInputStream(this.file);
  }
  
  public long getContentLength()
  {
    return this.file.length();
  }
  
  public boolean isRepeatable()
  {
    return true;
  }
  
  public boolean isStreaming()
  {
    return false;
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    Args.notNull(paramOutputStream, "Output stream");
    FileInputStream localFileInputStream = new FileInputStream(this.file);
    try
    {
      byte[] arrayOfByte = new byte[4096];
      for (;;)
      {
        int i = localFileInputStream.read(arrayOfByte);
        if (i == -1) {
          break;
        }
        paramOutputStream.write(arrayOfByte, 0, i);
      }
    }
    finally
    {
      localFileInputStream.close();
    }
    localFileInputStream.close();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.fluent.InternalFileEntity
 * JD-Core Version:    0.7.0.1
 */