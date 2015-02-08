package org.apache.http.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class FileEntity
  extends AbstractHttpEntity
  implements Cloneable
{
  protected final File file;
  
  public FileEntity(File paramFile)
  {
    this.file = ((File)Args.notNull(paramFile, "File"));
  }
  
  @Deprecated
  public FileEntity(File paramFile, String paramString)
  {
    this.file = ((File)Args.notNull(paramFile, "File"));
    setContentType(paramString);
  }
  
  public FileEntity(File paramFile, ContentType paramContentType)
  {
    this.file = ((File)Args.notNull(paramFile, "File"));
    if (paramContentType != null) {
      setContentType(paramContentType.toString());
    }
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
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
 * Qualified Name:     org.apache.http.entity.FileEntity
 * JD-Core Version:    0.7.0.1
 */