package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.entity.mime.content.ContentBody;

@Deprecated
public class HttpMultipart
  extends AbstractMultipartForm
{
  private final HttpMultipartMode mode;
  private final List<FormBodyPart> parts;
  
  public HttpMultipart(String paramString1, String paramString2)
  {
    this(paramString1, null, paramString2);
  }
  
  public HttpMultipart(String paramString1, Charset paramCharset, String paramString2)
  {
    this(paramString1, paramCharset, paramString2, HttpMultipartMode.STRICT);
  }
  
  public HttpMultipart(String paramString1, Charset paramCharset, String paramString2, HttpMultipartMode paramHttpMultipartMode)
  {
    super(paramString1, paramCharset, paramString2);
    this.mode = paramHttpMultipartMode;
    this.parts = new ArrayList();
  }
  
  public void addBodyPart(FormBodyPart paramFormBodyPart)
  {
    if (paramFormBodyPart == null) {
      return;
    }
    this.parts.add(paramFormBodyPart);
  }
  
  protected void formatMultipartHeader(FormBodyPart paramFormBodyPart, OutputStream paramOutputStream)
    throws IOException
  {
    Header localHeader = paramFormBodyPart.getHeader();
    Iterator localIterator;
    switch (1.$SwitchMap$org$apache$http$entity$mime$HttpMultipartMode[this.mode.ordinal()])
    {
    default: 
      localIterator = localHeader.iterator();
    }
    while (localIterator.hasNext())
    {
      writeField((MinimalField)localIterator.next(), paramOutputStream);
      continue;
      writeField(localHeader.getField("Content-Disposition"), this.charset, paramOutputStream);
      if (paramFormBodyPart.getBody().getFilename() != null) {
        writeField(localHeader.getField("Content-Type"), this.charset, paramOutputStream);
      }
    }
  }
  
  public List<FormBodyPart> getBodyParts()
  {
    return this.parts;
  }
  
  public HttpMultipartMode getMode()
  {
    return this.mode;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.mime.HttpMultipart
 * JD-Core Version:    0.7.0.1
 */