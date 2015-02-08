package org.apache.http.entity.mime;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

class HttpRFC6532Multipart
  extends AbstractMultipartForm
{
  private final List<FormBodyPart> parts;
  
  public HttpRFC6532Multipart(String paramString1, Charset paramCharset, String paramString2, List<FormBodyPart> paramList)
  {
    super(paramString1, paramCharset, paramString2);
    this.parts = paramList;
  }
  
  protected void formatMultipartHeader(FormBodyPart paramFormBodyPart, OutputStream paramOutputStream)
    throws IOException
  {
    Iterator localIterator = paramFormBodyPart.getHeader().iterator();
    while (localIterator.hasNext()) {
      writeField((MinimalField)localIterator.next(), MIME.UTF8_CHARSET, paramOutputStream);
    }
  }
  
  public List<FormBodyPart> getBodyParts()
  {
    return this.parts;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.mime.HttpRFC6532Multipart
 * JD-Core Version:    0.7.0.1
 */