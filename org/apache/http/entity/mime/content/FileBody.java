package org.apache.http.entity.mime.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public class FileBody
  extends AbstractContentBody
{
  private final File file;
  private final String filename;
  
  public FileBody(File paramFile) {}
  
  @Deprecated
  public FileBody(File paramFile, String paramString)
  {
    this(paramFile, ContentType.create(paramString), null);
  }
  
  @Deprecated
  public FileBody(File paramFile, String paramString1, String paramString2)
  {
    this(paramFile, null, paramString1, paramString2);
  }
  
  @Deprecated
  public FileBody(File paramFile, String paramString1, String paramString2, String paramString3)
  {
    this(paramFile, ContentType.create(paramString2, paramString3), paramString1);
  }
  
  public FileBody(File paramFile, ContentType paramContentType)
  {
    this(paramFile, paramContentType, null);
  }
  
  public FileBody(File paramFile, ContentType paramContentType, String paramString)
  {
    super(paramContentType);
    Args.notNull(paramFile, "File");
    this.file = paramFile;
    this.filename = paramString;
  }
  
  public long getContentLength()
  {
    return this.file.length();
  }
  
  public File getFile()
  {
    return this.file;
  }
  
  public String getFilename()
  {
    return this.filename;
  }
  
  public InputStream getInputStream()
    throws IOException
  {
    return new FileInputStream(this.file);
  }
  
  public String getTransferEncoding()
  {
    return "binary";
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
 * Qualified Name:     org.apache.http.entity.mime.content.FileBody
 * JD-Core Version:    0.7.0.1
 */