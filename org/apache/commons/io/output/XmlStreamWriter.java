package org.apache.commons.io.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.input.XmlStreamReader;

public class XmlStreamWriter
  extends Writer
{
  private static final int BUFFER_SIZE = 4096;
  static final Pattern ENCODING_PATTERN = XmlStreamReader.ENCODING_PATTERN;
  private final String defaultEncoding;
  private String encoding;
  private final OutputStream out;
  private Writer writer;
  private StringWriter xmlPrologWriter = new StringWriter(4096);
  
  public XmlStreamWriter(File paramFile)
    throws FileNotFoundException
  {
    this(paramFile, null);
  }
  
  public XmlStreamWriter(File paramFile, String paramString)
    throws FileNotFoundException
  {
    this(new FileOutputStream(paramFile), paramString);
  }
  
  public XmlStreamWriter(OutputStream paramOutputStream)
  {
    this(paramOutputStream, null);
  }
  
  public XmlStreamWriter(OutputStream paramOutputStream, String paramString)
  {
    this.out = paramOutputStream;
    if (paramString != null) {}
    for (;;)
    {
      this.defaultEncoding = paramString;
      return;
      paramString = "UTF-8";
    }
  }
  
  private void detectEncoding(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = paramInt2;
    StringBuffer localStringBuffer = this.xmlPrologWriter.getBuffer();
    if (paramInt2 + localStringBuffer.length() > 4096) {
      i = 4096 - localStringBuffer.length();
    }
    this.xmlPrologWriter.write(paramArrayOfChar, paramInt1, i);
    if (localStringBuffer.length() >= 5)
    {
      if (!localStringBuffer.substring(0, 5).equals("<?xml")) {
        break label242;
      }
      int j = localStringBuffer.indexOf("?>");
      if (j <= 0) {
        break label220;
      }
      Matcher localMatcher = ENCODING_PATTERN.matcher(localStringBuffer.substring(0, j));
      if (!localMatcher.find()) {
        break label209;
      }
      this.encoding = localMatcher.group(1).toUpperCase();
      this.encoding = this.encoding.substring(1, -1 + this.encoding.length());
    }
    for (;;)
    {
      if (this.encoding != null)
      {
        this.xmlPrologWriter = null;
        this.writer = new OutputStreamWriter(this.out, this.encoding);
        this.writer.write(localStringBuffer.toString());
        if (paramInt2 > i) {
          this.writer.write(paramArrayOfChar, paramInt1 + i, paramInt2 - i);
        }
      }
      return;
      label209:
      this.encoding = this.defaultEncoding;
      continue;
      label220:
      if (localStringBuffer.length() >= 4096)
      {
        this.encoding = this.defaultEncoding;
        continue;
        label242:
        this.encoding = this.defaultEncoding;
      }
    }
  }
  
  public void close()
    throws IOException
  {
    if (this.writer == null)
    {
      this.encoding = this.defaultEncoding;
      this.writer = new OutputStreamWriter(this.out, this.encoding);
      this.writer.write(this.xmlPrologWriter.toString());
    }
    this.writer.close();
  }
  
  public void flush()
    throws IOException
  {
    if (this.writer != null) {
      this.writer.flush();
    }
  }
  
  public String getDefaultEncoding()
  {
    return this.defaultEncoding;
  }
  
  public String getEncoding()
  {
    return this.encoding;
  }
  
  public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    if (this.xmlPrologWriter != null)
    {
      detectEncoding(paramArrayOfChar, paramInt1, paramInt2);
      return;
    }
    this.writer.write(paramArrayOfChar, paramInt1, paramInt2);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.output.XmlStreamWriter
 * JD-Core Version:    0.7.0.1
 */