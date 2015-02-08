package org.apache.http.client.entity;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;

@NotThreadSafe
public class EntityBuilder
{
  private byte[] binary;
  private boolean chunked;
  private String contentEncoding;
  private ContentType contentType;
  private File file;
  private boolean gzipCompress;
  private List<NameValuePair> parameters;
  private Serializable serializable;
  private InputStream stream;
  private String text;
  
  private void clearContent()
  {
    this.text = null;
    this.binary = null;
    this.stream = null;
    this.parameters = null;
    this.serializable = null;
    this.file = null;
  }
  
  public static EntityBuilder create()
  {
    return new EntityBuilder();
  }
  
  private ContentType getContentOrDefault(ContentType paramContentType)
  {
    if (this.contentType != null) {
      paramContentType = this.contentType;
    }
    return paramContentType;
  }
  
  public HttpEntity build()
  {
    Object localObject;
    if (this.text != null) {
      localObject = new StringEntity(this.text, getContentOrDefault(ContentType.DEFAULT_TEXT));
    }
    for (;;)
    {
      if ((((AbstractHttpEntity)localObject).getContentType() != null) && (this.contentType != null)) {
        ((AbstractHttpEntity)localObject).setContentType(this.contentType.toString());
      }
      ((AbstractHttpEntity)localObject).setContentEncoding(this.contentEncoding);
      ((AbstractHttpEntity)localObject).setChunked(this.chunked);
      if (this.gzipCompress) {
        localObject = new GzipCompressingEntity((HttpEntity)localObject);
      }
      return localObject;
      if (this.binary != null)
      {
        localObject = new ByteArrayEntity(this.binary, getContentOrDefault(ContentType.DEFAULT_BINARY));
      }
      else if (this.stream != null)
      {
        localObject = new InputStreamEntity(this.stream, 1L, getContentOrDefault(ContentType.DEFAULT_BINARY));
      }
      else
      {
        if (this.parameters != null)
        {
          List localList = this.parameters;
          if (this.contentType != null) {}
          for (Charset localCharset = this.contentType.getCharset();; localCharset = null)
          {
            localObject = new UrlEncodedFormEntity(localList, localCharset);
            break;
          }
        }
        if (this.serializable != null)
        {
          localObject = new SerializableEntity(this.serializable);
          ((AbstractHttpEntity)localObject).setContentType(ContentType.DEFAULT_BINARY.toString());
        }
        else if (this.file != null)
        {
          localObject = new FileEntity(this.file, getContentOrDefault(ContentType.DEFAULT_BINARY));
        }
        else
        {
          localObject = new BasicHttpEntity();
        }
      }
    }
  }
  
  public EntityBuilder chunked()
  {
    this.chunked = true;
    return this;
  }
  
  public byte[] getBinary()
  {
    return this.binary;
  }
  
  public String getContentEncoding()
  {
    return this.contentEncoding;
  }
  
  public ContentType getContentType()
  {
    return this.contentType;
  }
  
  public File getFile()
  {
    return this.file;
  }
  
  public List<NameValuePair> getParameters()
  {
    return this.parameters;
  }
  
  public Serializable getSerializable()
  {
    return this.serializable;
  }
  
  public InputStream getStream()
  {
    return this.stream;
  }
  
  public String getText()
  {
    return this.text;
  }
  
  public EntityBuilder gzipCompress()
  {
    this.gzipCompress = true;
    return this;
  }
  
  public boolean isChunked()
  {
    return this.chunked;
  }
  
  public boolean isGzipCompress()
  {
    return this.gzipCompress;
  }
  
  public EntityBuilder setBinary(byte[] paramArrayOfByte)
  {
    clearContent();
    this.binary = paramArrayOfByte;
    return this;
  }
  
  public EntityBuilder setContentEncoding(String paramString)
  {
    this.contentEncoding = paramString;
    return this;
  }
  
  public EntityBuilder setContentType(ContentType paramContentType)
  {
    this.contentType = paramContentType;
    return this;
  }
  
  public EntityBuilder setFile(File paramFile)
  {
    clearContent();
    this.file = paramFile;
    return this;
  }
  
  public EntityBuilder setParameters(List<NameValuePair> paramList)
  {
    clearContent();
    this.parameters = paramList;
    return this;
  }
  
  public EntityBuilder setParameters(NameValuePair... paramVarArgs)
  {
    return setParameters(Arrays.asList(paramVarArgs));
  }
  
  public EntityBuilder setSerializable(Serializable paramSerializable)
  {
    clearContent();
    this.serializable = paramSerializable;
    return this;
  }
  
  public EntityBuilder setStream(InputStream paramInputStream)
  {
    clearContent();
    this.stream = paramInputStream;
    return this;
  }
  
  public EntityBuilder setText(String paramString)
  {
    clearContent();
    this.text = paramString;
    return this;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.entity.EntityBuilder
 * JD-Core Version:    0.7.0.1
 */