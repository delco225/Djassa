package org.apache.http.entity.mime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;

abstract class AbstractMultipartForm
{
  private static final ByteArrayBuffer CR_LF = encode(MIME.DEFAULT_CHARSET, "\r\n");
  private static final ByteArrayBuffer FIELD_SEP = encode(MIME.DEFAULT_CHARSET, ": ");
  private static final ByteArrayBuffer TWO_DASHES = encode(MIME.DEFAULT_CHARSET, "--");
  private final String boundary;
  protected final Charset charset;
  private final String subType;
  
  public AbstractMultipartForm(String paramString1, String paramString2)
  {
    this(paramString1, null, paramString2);
  }
  
  public AbstractMultipartForm(String paramString1, Charset paramCharset, String paramString2)
  {
    Args.notNull(paramString1, "Multipart subtype");
    Args.notNull(paramString2, "Multipart boundary");
    this.subType = paramString1;
    if (paramCharset != null) {}
    for (;;)
    {
      this.charset = paramCharset;
      this.boundary = paramString2;
      return;
      paramCharset = MIME.DEFAULT_CHARSET;
    }
  }
  
  private static ByteArrayBuffer encode(Charset paramCharset, String paramString)
  {
    ByteBuffer localByteBuffer = paramCharset.encode(CharBuffer.wrap(paramString));
    ByteArrayBuffer localByteArrayBuffer = new ByteArrayBuffer(localByteBuffer.remaining());
    localByteArrayBuffer.append(localByteBuffer.array(), localByteBuffer.position(), localByteBuffer.remaining());
    return localByteArrayBuffer;
  }
  
  private static void writeBytes(String paramString, OutputStream paramOutputStream)
    throws IOException
  {
    writeBytes(encode(MIME.DEFAULT_CHARSET, paramString), paramOutputStream);
  }
  
  private static void writeBytes(String paramString, Charset paramCharset, OutputStream paramOutputStream)
    throws IOException
  {
    writeBytes(encode(paramCharset, paramString), paramOutputStream);
  }
  
  private static void writeBytes(ByteArrayBuffer paramByteArrayBuffer, OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write(paramByteArrayBuffer.buffer(), 0, paramByteArrayBuffer.length());
  }
  
  protected static void writeField(MinimalField paramMinimalField, OutputStream paramOutputStream)
    throws IOException
  {
    writeBytes(paramMinimalField.getName(), paramOutputStream);
    writeBytes(FIELD_SEP, paramOutputStream);
    writeBytes(paramMinimalField.getBody(), paramOutputStream);
    writeBytes(CR_LF, paramOutputStream);
  }
  
  protected static void writeField(MinimalField paramMinimalField, Charset paramCharset, OutputStream paramOutputStream)
    throws IOException
  {
    writeBytes(paramMinimalField.getName(), paramCharset, paramOutputStream);
    writeBytes(FIELD_SEP, paramOutputStream);
    writeBytes(paramMinimalField.getBody(), paramCharset, paramOutputStream);
    writeBytes(CR_LF, paramOutputStream);
  }
  
  void doWriteTo(OutputStream paramOutputStream, boolean paramBoolean)
    throws IOException
  {
    ByteArrayBuffer localByteArrayBuffer = encode(this.charset, getBoundary());
    Iterator localIterator = getBodyParts().iterator();
    while (localIterator.hasNext())
    {
      FormBodyPart localFormBodyPart = (FormBodyPart)localIterator.next();
      writeBytes(TWO_DASHES, paramOutputStream);
      writeBytes(localByteArrayBuffer, paramOutputStream);
      writeBytes(CR_LF, paramOutputStream);
      formatMultipartHeader(localFormBodyPart, paramOutputStream);
      writeBytes(CR_LF, paramOutputStream);
      if (paramBoolean) {
        localFormBodyPart.getBody().writeTo(paramOutputStream);
      }
      writeBytes(CR_LF, paramOutputStream);
    }
    writeBytes(TWO_DASHES, paramOutputStream);
    writeBytes(localByteArrayBuffer, paramOutputStream);
    writeBytes(TWO_DASHES, paramOutputStream);
    writeBytes(CR_LF, paramOutputStream);
  }
  
  protected abstract void formatMultipartHeader(FormBodyPart paramFormBodyPart, OutputStream paramOutputStream)
    throws IOException;
  
  public abstract List<FormBodyPart> getBodyParts();
  
  public String getBoundary()
  {
    return this.boundary;
  }
  
  public Charset getCharset()
  {
    return this.charset;
  }
  
  public String getSubType()
  {
    return this.subType;
  }
  
  public long getTotalLength()
  {
    long l1 = -1L;
    long l2 = 0L;
    Iterator localIterator = getBodyParts().iterator();
    while (localIterator.hasNext())
    {
      long l3 = ((FormBodyPart)localIterator.next()).getBody().getContentLength();
      if (l3 < 0L) {
        break label92;
      }
      l2 += l3;
    }
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      doWriteTo(localByteArrayOutputStream, false);
      int i = localByteArrayOutputStream.toByteArray().length;
      l1 = l2 + i;
      label92:
      return l1;
    }
    catch (IOException localIOException) {}
    return l1;
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    doWriteTo(paramOutputStream, true);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.mime.AbstractMultipartForm
 * JD-Core Version:    0.7.0.1
 */