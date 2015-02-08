package org.apache.http.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;

public final class EntityUtils
{
  public static void consume(HttpEntity paramHttpEntity)
    throws IOException
  {
    if (paramHttpEntity == null) {}
    InputStream localInputStream;
    do
    {
      do
      {
        return;
      } while (!paramHttpEntity.isStreaming());
      localInputStream = paramHttpEntity.getContent();
    } while (localInputStream == null);
    localInputStream.close();
  }
  
  public static void consumeQuietly(HttpEntity paramHttpEntity)
  {
    try
    {
      consume(paramHttpEntity);
      return;
    }
    catch (IOException localIOException) {}
  }
  
  @Deprecated
  public static String getContentCharSet(HttpEntity paramHttpEntity)
    throws ParseException
  {
    Args.notNull(paramHttpEntity, "Entity");
    Header localHeader = paramHttpEntity.getContentType();
    String str = null;
    if (localHeader != null)
    {
      HeaderElement[] arrayOfHeaderElement = paramHttpEntity.getContentType().getElements();
      int i = arrayOfHeaderElement.length;
      str = null;
      if (i > 0)
      {
        NameValuePair localNameValuePair = arrayOfHeaderElement[0].getParameterByName("charset");
        str = null;
        if (localNameValuePair != null) {
          str = localNameValuePair.getValue();
        }
      }
    }
    return str;
  }
  
  @Deprecated
  public static String getContentMimeType(HttpEntity paramHttpEntity)
    throws ParseException
  {
    Args.notNull(paramHttpEntity, "Entity");
    Header localHeader = paramHttpEntity.getContentType();
    String str = null;
    if (localHeader != null)
    {
      HeaderElement[] arrayOfHeaderElement = paramHttpEntity.getContentType().getElements();
      int i = arrayOfHeaderElement.length;
      str = null;
      if (i > 0) {
        str = arrayOfHeaderElement[0].getName();
      }
    }
    return str;
  }
  
  public static byte[] toByteArray(HttpEntity paramHttpEntity)
    throws IOException
  {
    Args.notNull(paramHttpEntity, "Entity");
    InputStream localInputStream = paramHttpEntity.getContent();
    if (localInputStream == null) {
      return null;
    }
    try
    {
      boolean bool1 = paramHttpEntity.getContentLength() < 2147483647L;
      boolean bool2 = false;
      if (!bool1) {
        bool2 = true;
      }
      Args.check(bool2, "HTTP entity too large to be buffered in memory");
      int i = (int)paramHttpEntity.getContentLength();
      if (i < 0) {
        i = 4096;
      }
      ByteArrayBuffer localByteArrayBuffer = new ByteArrayBuffer(i);
      byte[] arrayOfByte1 = new byte[4096];
      for (;;)
      {
        int j = localInputStream.read(arrayOfByte1);
        if (j == -1) {
          break;
        }
        localByteArrayBuffer.append(arrayOfByte1, 0, j);
      }
      arrayOfByte2 = localByteArrayBuffer.toByteArray();
    }
    finally
    {
      localInputStream.close();
    }
    byte[] arrayOfByte2;
    localInputStream.close();
    return arrayOfByte2;
  }
  
  public static String toString(HttpEntity paramHttpEntity)
    throws IOException, ParseException
  {
    return toString(paramHttpEntity, (Charset)null);
  }
  
  public static String toString(HttpEntity paramHttpEntity, String paramString)
    throws IOException, ParseException
  {
    if (paramString != null) {}
    for (Charset localCharset = Charset.forName(paramString);; localCharset = null) {
      return toString(paramHttpEntity, localCharset);
    }
  }
  
  /* Error */
  public static String toString(HttpEntity paramHttpEntity, Charset paramCharset)
    throws IOException, ParseException
  {
    // Byte code:
    //   0: aload_0
    //   1: ldc 37
    //   3: invokestatic 43	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: invokeinterface 22 1 0
    //   13: astore_3
    //   14: aload_3
    //   15: ifnonnull +5 -> 20
    //   18: aconst_null
    //   19: areturn
    //   20: aload_0
    //   21: invokeinterface 77 1 0
    //   26: ldc2_w 78
    //   29: lcmp
    //   30: istore 5
    //   32: iconst_0
    //   33: istore 6
    //   35: iload 5
    //   37: ifgt +6 -> 43
    //   40: iconst_1
    //   41: istore 6
    //   43: iload 6
    //   45: ldc 81
    //   47: invokestatic 85	org/apache/http/util/Args:check	(ZLjava/lang/String;)V
    //   50: aload_0
    //   51: invokeinterface 77 1 0
    //   56: lstore 7
    //   58: lload 7
    //   60: l2i
    //   61: istore 9
    //   63: iload 9
    //   65: ifge +8 -> 73
    //   68: sipush 4096
    //   71: istore 9
    //   73: aload_0
    //   74: invokestatic 120	org/apache/http/entity/ContentType:get	(Lorg/apache/http/HttpEntity;)Lorg/apache/http/entity/ContentType;
    //   77: astore 11
    //   79: aconst_null
    //   80: astore 12
    //   82: aload 11
    //   84: ifnull +14 -> 98
    //   87: aload 11
    //   89: invokevirtual 124	org/apache/http/entity/ContentType:getCharset	()Ljava/nio/charset/Charset;
    //   92: astore 13
    //   94: aload 13
    //   96: astore 12
    //   98: aload 12
    //   100: ifnonnull +6 -> 106
    //   103: aload_1
    //   104: astore 12
    //   106: aload 12
    //   108: ifnonnull +8 -> 116
    //   111: getstatic 130	org/apache/http/protocol/HTTP:DEF_CONTENT_CHARSET	Ljava/nio/charset/Charset;
    //   114: astore 12
    //   116: new 132	java/io/InputStreamReader
    //   119: dup
    //   120: aload_3
    //   121: aload 12
    //   123: invokespecial 135	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/nio/charset/Charset;)V
    //   126: astore 14
    //   128: new 137	org/apache/http/util/CharArrayBuffer
    //   131: dup
    //   132: iload 9
    //   134: invokespecial 138	org/apache/http/util/CharArrayBuffer:<init>	(I)V
    //   137: astore 15
    //   139: sipush 1024
    //   142: newarray char
    //   144: astore 16
    //   146: aload 14
    //   148: aload 16
    //   150: invokevirtual 143	java/io/Reader:read	([C)I
    //   153: istore 17
    //   155: iload 17
    //   157: iconst_m1
    //   158: if_icmpeq +40 -> 198
    //   161: aload 15
    //   163: aload 16
    //   165: iconst_0
    //   166: iload 17
    //   168: invokevirtual 146	org/apache/http/util/CharArrayBuffer:append	([CII)V
    //   171: goto -25 -> 146
    //   174: astore 4
    //   176: aload_3
    //   177: invokevirtual 27	java/io/InputStream:close	()V
    //   180: aload 4
    //   182: athrow
    //   183: astore 10
    //   185: new 148	java/io/UnsupportedEncodingException
    //   188: dup
    //   189: aload 10
    //   191: invokevirtual 151	java/nio/charset/UnsupportedCharsetException:getMessage	()Ljava/lang/String;
    //   194: invokespecial 154	java/io/UnsupportedEncodingException:<init>	(Ljava/lang/String;)V
    //   197: athrow
    //   198: aload 15
    //   200: invokevirtual 156	org/apache/http/util/CharArrayBuffer:toString	()Ljava/lang/String;
    //   203: astore 18
    //   205: aload_3
    //   206: invokevirtual 27	java/io/InputStream:close	()V
    //   209: aload 18
    //   211: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	212	0	paramHttpEntity	HttpEntity
    //   0	212	1	paramCharset	Charset
    //   13	193	3	localInputStream	InputStream
    //   174	7	4	localObject1	Object
    //   30	6	5	bool1	boolean
    //   33	11	6	bool2	boolean
    //   56	3	7	l	long
    //   61	72	9	i	int
    //   183	7	10	localUnsupportedCharsetException	java.nio.charset.UnsupportedCharsetException
    //   77	11	11	localContentType	org.apache.http.entity.ContentType
    //   80	42	12	localObject2	Object
    //   92	3	13	localCharset	Charset
    //   126	21	14	localInputStreamReader	java.io.InputStreamReader
    //   137	62	15	localCharArrayBuffer	CharArrayBuffer
    //   144	20	16	arrayOfChar	char[]
    //   153	14	17	j	int
    //   203	7	18	str	String
    // Exception table:
    //   from	to	target	type
    //   20	32	174	finally
    //   43	58	174	finally
    //   73	79	174	finally
    //   87	94	174	finally
    //   111	116	174	finally
    //   116	146	174	finally
    //   146	155	174	finally
    //   161	171	174	finally
    //   185	198	174	finally
    //   198	205	174	finally
    //   73	79	183	java/nio/charset/UnsupportedCharsetException
    //   87	94	183	java/nio/charset/UnsupportedCharsetException
  }
  
  public static void updateEntity(HttpResponse paramHttpResponse, HttpEntity paramHttpEntity)
    throws IOException
  {
    Args.notNull(paramHttpResponse, "Response");
    consume(paramHttpResponse.getEntity());
    paramHttpResponse.setEntity(paramHttpEntity);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.util.EntityUtils
 * JD-Core Version:    0.7.0.1
 */