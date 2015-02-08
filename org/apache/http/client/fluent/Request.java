package org.apache.http.client.fluent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;

public class Request
{
  public static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
  public static final Locale DATE_LOCALE = Locale.US;
  public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT");
  private final RequestConfig.Builder configBuilder;
  private SimpleDateFormat dateFormatter;
  private final InternalHttpRequest request;
  
  Request(String paramString, URI paramURI)
  {
    this.request = new InternalHttpRequest(paramString, paramURI);
    this.configBuilder = RequestConfig.custom();
  }
  
  public static Request Delete(String paramString)
  {
    return new Request("DELETE", URI.create(paramString));
  }
  
  public static Request Delete(URI paramURI)
  {
    return new Request("DELETE", paramURI);
  }
  
  public static Request Get(String paramString)
  {
    return new Request("GET", URI.create(paramString));
  }
  
  public static Request Get(URI paramURI)
  {
    return new Request("GET", paramURI);
  }
  
  public static Request Head(String paramString)
  {
    return new Request("HEAD", URI.create(paramString));
  }
  
  public static Request Head(URI paramURI)
  {
    return new Request("HEAD", paramURI);
  }
  
  public static Request Options(String paramString)
  {
    return new Request("OPTIONS", URI.create(paramString));
  }
  
  public static Request Options(URI paramURI)
  {
    return new Request("OPTIONS", paramURI);
  }
  
  public static Request Post(String paramString)
  {
    return new Request("POST", URI.create(paramString));
  }
  
  public static Request Post(URI paramURI)
  {
    return new Request("POST", paramURI);
  }
  
  public static Request Put(String paramString)
  {
    return new Request("PUT", URI.create(paramString));
  }
  
  public static Request Put(URI paramURI)
  {
    return new Request("PUT", paramURI);
  }
  
  public static Request Trace(String paramString)
  {
    return new Request("TRACE", URI.create(paramString));
  }
  
  public static Request Trace(URI paramURI)
  {
    return new Request("TRACE", paramURI);
  }
  
  private SimpleDateFormat getDateFormat()
  {
    if (this.dateFormatter == null)
    {
      this.dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", DATE_LOCALE);
      this.dateFormatter.setTimeZone(TIME_ZONE);
    }
    return this.dateFormatter;
  }
  
  public void abort()
    throws UnsupportedOperationException
  {
    this.request.abort();
  }
  
  public Request addHeader(String paramString1, String paramString2)
  {
    this.request.addHeader(paramString1, paramString2);
    return this;
  }
  
  public Request addHeader(Header paramHeader)
  {
    this.request.addHeader(paramHeader);
    return this;
  }
  
  public Request body(HttpEntity paramHttpEntity)
  {
    if ((this.request instanceof HttpEntityEnclosingRequest))
    {
      ((HttpEntityEnclosingRequest)this.request).setEntity(paramHttpEntity);
      return this;
    }
    throw new IllegalStateException(this.request.getMethod() + " request cannot enclose an entity");
  }
  
  public Request bodyByteArray(byte[] paramArrayOfByte)
  {
    return body(new InternalByteArrayEntity(paramArrayOfByte));
  }
  
  public Request bodyByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return body(new InternalByteArrayEntity(paramArrayOfByte, paramInt1, paramInt2));
  }
  
  public Request bodyFile(File paramFile, ContentType paramContentType)
  {
    return body(new InternalFileEntity(paramFile, paramContentType));
  }
  
  public Request bodyForm(Iterable<? extends NameValuePair> paramIterable)
  {
    return bodyForm(paramIterable, Consts.ISO_8859_1);
  }
  
  public Request bodyForm(Iterable<? extends NameValuePair> paramIterable, Charset paramCharset)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext()) {
      localArrayList.add((NameValuePair)localIterator.next());
    }
    ContentType localContentType = ContentType.create("application/x-www-form-urlencoded", paramCharset);
    if (paramCharset != null) {}
    for (String str = paramCharset.name();; str = null) {
      return bodyString(URLEncodedUtils.format(localArrayList, str), localContentType);
    }
  }
  
  public Request bodyForm(NameValuePair... paramVarArgs)
  {
    return bodyForm(Arrays.asList(paramVarArgs), Consts.ISO_8859_1);
  }
  
  public Request bodyStream(InputStream paramInputStream)
  {
    return body(new InternalInputStreamEntity(paramInputStream, -1L, null));
  }
  
  public Request bodyStream(InputStream paramInputStream, ContentType paramContentType)
  {
    return body(new InternalInputStreamEntity(paramInputStream, -1L, paramContentType));
  }
  
  public Request bodyString(String paramString, ContentType paramContentType)
  {
    Charset localCharset;
    if (paramContentType != null)
    {
      localCharset = paramContentType.getCharset();
      if (localCharset == null) {
        break label47;
      }
    }
    for (;;)
    {
      try
      {
        byte[] arrayOfByte2 = paramString.getBytes(localCharset.name());
        localObject = arrayOfByte2;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        label47:
        byte[] arrayOfByte1;
        Object localObject = paramString.getBytes();
        continue;
      }
      return body(new InternalByteArrayEntity((byte[])localObject, paramContentType));
      localCharset = null;
      break;
      arrayOfByte1 = paramString.getBytes();
      localObject = arrayOfByte1;
    }
  }
  
  @Deprecated
  public Request config(String paramString, Object paramObject)
  {
    return this;
  }
  
  public Request connectTimeout(int paramInt)
  {
    this.configBuilder.setConnectTimeout(paramInt);
    return this;
  }
  
  @Deprecated
  public Request elementCharset(String paramString)
  {
    return this;
  }
  
  public Response execute()
    throws ClientProtocolException, IOException
  {
    this.request.setConfig(this.configBuilder.build());
    return new Response(Executor.CLIENT.execute(this.request));
  }
  
  InternalHttpRequest prepareRequest()
  {
    this.request.setConfig(this.configBuilder.build());
    return this.request;
  }
  
  @Deprecated
  public Request removeConfig(String paramString)
  {
    return this;
  }
  
  public Request removeHeader(Header paramHeader)
  {
    this.request.removeHeader(paramHeader);
    return this;
  }
  
  public Request removeHeaders(String paramString)
  {
    this.request.removeHeaders(paramString);
    return this;
  }
  
  public Request setCacheControl(String paramString)
  {
    this.request.setHeader("Cache-Control", paramString);
    return this;
  }
  
  public Request setDate(Date paramDate)
  {
    this.request.setHeader("Date", getDateFormat().format(paramDate));
    return this;
  }
  
  public Request setHeader(String paramString1, String paramString2)
  {
    this.request.setHeader(paramString1, paramString2);
    return this;
  }
  
  public Request setHeader(Header paramHeader)
  {
    this.request.setHeader(paramHeader);
    return this;
  }
  
  public Request setHeaders(Header... paramVarArgs)
  {
    this.request.setHeaders(paramVarArgs);
    return this;
  }
  
  public Request setIfModifiedSince(Date paramDate)
  {
    this.request.setHeader("If-Modified-Since", getDateFormat().format(paramDate));
    return this;
  }
  
  public Request setIfUnmodifiedSince(Date paramDate)
  {
    this.request.setHeader("If-Unmodified-Since", getDateFormat().format(paramDate));
    return this;
  }
  
  public Request socketTimeout(int paramInt)
  {
    this.configBuilder.setSocketTimeout(paramInt);
    return this;
  }
  
  public Request staleConnectionCheck(boolean paramBoolean)
  {
    this.configBuilder.setStaleConnectionCheckEnabled(paramBoolean);
    return this;
  }
  
  public String toString()
  {
    return this.request.getRequestLine().toString();
  }
  
  public Request useExpectContinue()
  {
    this.configBuilder.setExpectContinueEnabled(true);
    return this;
  }
  
  public Request userAgent(String paramString)
  {
    this.request.setHeader("User-Agent", paramString);
    return this;
  }
  
  public Request version(HttpVersion paramHttpVersion)
  {
    this.request.setProtocolVersion(paramHttpVersion);
    return this;
  }
  
  public Request viaProxy(HttpHost paramHttpHost)
  {
    this.configBuilder.setProxy(paramHttpHost);
    return this;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.fluent.Request
 * JD-Core Version:    0.7.0.1
 */