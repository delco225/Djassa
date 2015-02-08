package org.apache.http.entity.mime;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.Args;

public class MultipartEntityBuilder
{
  private static final String DEFAULT_SUBTYPE = "form-data";
  private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  private List<FormBodyPart> bodyParts = null;
  private String boundary = null;
  private Charset charset = null;
  private HttpMultipartMode mode = HttpMultipartMode.STRICT;
  private String subType = "form-data";
  
  public static MultipartEntityBuilder create()
  {
    return new MultipartEntityBuilder();
  }
  
  private String generateBoundary()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Random localRandom = new Random();
    int i = 30 + localRandom.nextInt(11);
    for (int j = 0; j < i; j++) {
      localStringBuilder.append(MULTIPART_CHARS[localRandom.nextInt(MULTIPART_CHARS.length)]);
    }
    return localStringBuilder.toString();
  }
  
  private String generateContentType(String paramString, Charset paramCharset)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("multipart/form-data; boundary=");
    localStringBuilder.append(paramString);
    if (paramCharset != null)
    {
      localStringBuilder.append("; charset=");
      localStringBuilder.append(paramCharset.name());
    }
    return localStringBuilder.toString();
  }
  
  public MultipartEntityBuilder addBinaryBody(String paramString, File paramFile)
  {
    ContentType localContentType = ContentType.DEFAULT_BINARY;
    if (paramFile != null) {}
    for (String str = paramFile.getName();; str = null) {
      return addBinaryBody(paramString, paramFile, localContentType, str);
    }
  }
  
  public MultipartEntityBuilder addBinaryBody(String paramString1, File paramFile, ContentType paramContentType, String paramString2)
  {
    return addPart(paramString1, new FileBody(paramFile, paramContentType, paramString2));
  }
  
  public MultipartEntityBuilder addBinaryBody(String paramString, InputStream paramInputStream)
  {
    return addBinaryBody(paramString, paramInputStream, ContentType.DEFAULT_BINARY, null);
  }
  
  public MultipartEntityBuilder addBinaryBody(String paramString1, InputStream paramInputStream, ContentType paramContentType, String paramString2)
  {
    return addPart(paramString1, new InputStreamBody(paramInputStream, paramContentType, paramString2));
  }
  
  public MultipartEntityBuilder addBinaryBody(String paramString, byte[] paramArrayOfByte)
  {
    return addBinaryBody(paramString, paramArrayOfByte, ContentType.DEFAULT_BINARY, null);
  }
  
  public MultipartEntityBuilder addBinaryBody(String paramString1, byte[] paramArrayOfByte, ContentType paramContentType, String paramString2)
  {
    return addPart(paramString1, new ByteArrayBody(paramArrayOfByte, paramContentType, paramString2));
  }
  
  public MultipartEntityBuilder addPart(String paramString, ContentBody paramContentBody)
  {
    Args.notNull(paramString, "Name");
    Args.notNull(paramContentBody, "Content body");
    return addPart(new FormBodyPart(paramString, paramContentBody));
  }
  
  MultipartEntityBuilder addPart(FormBodyPart paramFormBodyPart)
  {
    if (paramFormBodyPart == null) {
      return this;
    }
    if (this.bodyParts == null) {
      this.bodyParts = new ArrayList();
    }
    this.bodyParts.add(paramFormBodyPart);
    return this;
  }
  
  public MultipartEntityBuilder addTextBody(String paramString1, String paramString2)
  {
    return addTextBody(paramString1, paramString2, ContentType.DEFAULT_TEXT);
  }
  
  public MultipartEntityBuilder addTextBody(String paramString1, String paramString2, ContentType paramContentType)
  {
    return addPart(paramString1, new StringBody(paramString2, paramContentType));
  }
  
  public HttpEntity build()
  {
    return buildEntity();
  }
  
  MultipartFormEntity buildEntity()
  {
    String str1;
    Charset localCharset;
    String str2;
    label29:
    Object localObject1;
    label49:
    HttpMultipartMode localHttpMultipartMode;
    label62:
    Object localObject2;
    if (this.subType != null)
    {
      str1 = this.subType;
      localCharset = this.charset;
      if (this.boundary == null) {
        break label133;
      }
      str2 = this.boundary;
      if (this.bodyParts == null) {
        break label141;
      }
      localObject1 = new ArrayList(this.bodyParts);
      if (this.mode == null) {
        break label149;
      }
      localHttpMultipartMode = this.mode;
      switch (1.$SwitchMap$org$apache$http$entity$mime$HttpMultipartMode[localHttpMultipartMode.ordinal()])
      {
      default: 
        localObject2 = new HttpStrictMultipart(str1, localCharset, str2, (List)localObject1);
      }
    }
    for (;;)
    {
      return new MultipartFormEntity((AbstractMultipartForm)localObject2, generateContentType(str2, localCharset), ((AbstractMultipartForm)localObject2).getTotalLength());
      str1 = "form-data";
      break;
      label133:
      str2 = generateBoundary();
      break label29;
      label141:
      localObject1 = Collections.emptyList();
      break label49;
      label149:
      localHttpMultipartMode = HttpMultipartMode.STRICT;
      break label62;
      localObject2 = new HttpBrowserCompatibleMultipart(str1, localCharset, str2, (List)localObject1);
      continue;
      localObject2 = new HttpRFC6532Multipart(str1, localCharset, str2, (List)localObject1);
    }
  }
  
  public MultipartEntityBuilder setBoundary(String paramString)
  {
    this.boundary = paramString;
    return this;
  }
  
  public MultipartEntityBuilder setCharset(Charset paramCharset)
  {
    this.charset = paramCharset;
    return this;
  }
  
  public MultipartEntityBuilder setLaxMode()
  {
    this.mode = HttpMultipartMode.BROWSER_COMPATIBLE;
    return this;
  }
  
  public MultipartEntityBuilder setMode(HttpMultipartMode paramHttpMultipartMode)
  {
    this.mode = paramHttpMultipartMode;
    return this;
  }
  
  public MultipartEntityBuilder setStrictMode()
  {
    this.mode = HttpMultipartMode.STRICT;
    return this;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.mime.MultipartEntityBuilder
 * JD-Core Version:    0.7.0.1
 */