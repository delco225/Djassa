package org.apache.http.client.methods;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.HeaderGroup;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

@NotThreadSafe
public class RequestBuilder
{
  private RequestConfig config;
  private HttpEntity entity;
  private HeaderGroup headergroup;
  private String method;
  private LinkedList<NameValuePair> parameters;
  private URI uri;
  private ProtocolVersion version;
  
  RequestBuilder()
  {
    this(null);
  }
  
  RequestBuilder(String paramString)
  {
    this.method = paramString;
  }
  
  public static RequestBuilder copy(HttpRequest paramHttpRequest)
  {
    Args.notNull(paramHttpRequest, "HTTP request");
    return new RequestBuilder().doCopy(paramHttpRequest);
  }
  
  public static RequestBuilder create(String paramString)
  {
    Args.notBlank(paramString, "HTTP method");
    return new RequestBuilder(paramString);
  }
  
  public static RequestBuilder delete()
  {
    return new RequestBuilder("DELETE");
  }
  
  private RequestBuilder doCopy(HttpRequest paramHttpRequest)
  {
    if (paramHttpRequest == null) {
      return this;
    }
    this.method = paramHttpRequest.getRequestLine().getMethod();
    this.version = paramHttpRequest.getRequestLine().getProtocolVersion();
    if ((paramHttpRequest instanceof HttpUriRequest))
    {
      this.uri = ((HttpUriRequest)paramHttpRequest).getURI();
      if (this.headergroup == null) {
        this.headergroup = new HeaderGroup();
      }
      this.headergroup.clear();
      this.headergroup.setHeaders(paramHttpRequest.getAllHeaders());
      if (!(paramHttpRequest instanceof HttpEntityEnclosingRequest)) {
        break label162;
      }
      this.entity = ((HttpEntityEnclosingRequest)paramHttpRequest).getEntity();
      label114:
      if (!(paramHttpRequest instanceof Configurable)) {
        break label170;
      }
    }
    label162:
    label170:
    for (this.config = ((Configurable)paramHttpRequest).getConfig();; this.config = null)
    {
      this.parameters = null;
      return this;
      this.uri = URI.create(paramHttpRequest.getRequestLine().getMethod());
      break;
      this.entity = null;
      break label114;
    }
  }
  
  public static RequestBuilder get()
  {
    return new RequestBuilder("GET");
  }
  
  public static RequestBuilder head()
  {
    return new RequestBuilder("HEAD");
  }
  
  public static RequestBuilder options()
  {
    return new RequestBuilder("OPTIONS");
  }
  
  public static RequestBuilder post()
  {
    return new RequestBuilder("POST");
  }
  
  public static RequestBuilder put()
  {
    return new RequestBuilder("PUT");
  }
  
  public static RequestBuilder trace()
  {
    return new RequestBuilder("TRACE");
  }
  
  public RequestBuilder addHeader(String paramString1, String paramString2)
  {
    if (this.headergroup == null) {
      this.headergroup = new HeaderGroup();
    }
    this.headergroup.addHeader(new BasicHeader(paramString1, paramString2));
    return this;
  }
  
  public RequestBuilder addHeader(Header paramHeader)
  {
    if (this.headergroup == null) {
      this.headergroup = new HeaderGroup();
    }
    this.headergroup.addHeader(paramHeader);
    return this;
  }
  
  public RequestBuilder addParameter(String paramString1, String paramString2)
  {
    return addParameter(new BasicNameValuePair(paramString1, paramString2));
  }
  
  public RequestBuilder addParameter(NameValuePair paramNameValuePair)
  {
    Args.notNull(paramNameValuePair, "Name value pair");
    if (this.parameters == null) {
      this.parameters = new LinkedList();
    }
    this.parameters.add(paramNameValuePair);
    return this;
  }
  
  public RequestBuilder addParameters(NameValuePair... paramVarArgs)
  {
    int i = paramVarArgs.length;
    for (int j = 0; j < i; j++) {
      addParameter(paramVarArgs[j]);
    }
    return this;
  }
  
  public HttpUriRequest build()
  {
    Object localObject1;
    Object localObject2;
    if (this.uri != null)
    {
      localObject1 = this.uri;
      localObject2 = this.entity;
      if ((this.parameters != null) && (!this.parameters.isEmpty()))
      {
        if ((localObject2 != null) || ((!"POST".equalsIgnoreCase(this.method)) && (!"PUT".equalsIgnoreCase(this.method)))) {
          break label143;
        }
        localObject2 = new UrlEncodedFormEntity(this.parameters, HTTP.DEF_CONTENT_CHARSET);
      }
    }
    for (;;)
    {
      if (localObject2 == null) {}
      for (Object localObject3 = new InternalRequest(this.method);; localObject3 = localInternalEntityEclosingRequest)
      {
        for (;;)
        {
          ((HttpRequestBase)localObject3).setProtocolVersion(this.version);
          ((HttpRequestBase)localObject3).setURI((URI)localObject1);
          if (this.headergroup != null) {
            ((HttpRequestBase)localObject3).setHeaders(this.headergroup.getAllHeaders());
          }
          ((HttpRequestBase)localObject3).setConfig(this.config);
          return localObject3;
          localObject1 = URI.create("/");
          break;
          label143:
          InternalEntityEclosingRequest localInternalEntityEclosingRequest;
          try
          {
            URI localURI = new URIBuilder((URI)localObject1).addParameters(this.parameters).build();
            localObject1 = localURI;
          }
          catch (URISyntaxException localURISyntaxException) {}
        }
        localInternalEntityEclosingRequest = new InternalEntityEclosingRequest(this.method);
        localInternalEntityEclosingRequest.setEntity((HttpEntity)localObject2);
      }
    }
  }
  
  public RequestConfig getConfig()
  {
    return this.config;
  }
  
  public HttpEntity getEntity()
  {
    return this.entity;
  }
  
  public Header getFirstHeader(String paramString)
  {
    if (this.headergroup != null) {
      return this.headergroup.getFirstHeader(paramString);
    }
    return null;
  }
  
  public Header[] getHeaders(String paramString)
  {
    if (this.headergroup != null) {
      return this.headergroup.getHeaders(paramString);
    }
    return null;
  }
  
  public Header getLastHeader(String paramString)
  {
    if (this.headergroup != null) {
      return this.headergroup.getLastHeader(paramString);
    }
    return null;
  }
  
  public String getMethod()
  {
    return this.method;
  }
  
  public List<NameValuePair> getParameters()
  {
    if (this.parameters != null) {
      return new ArrayList(this.parameters);
    }
    return new ArrayList();
  }
  
  public URI getUri()
  {
    return this.uri;
  }
  
  public ProtocolVersion getVersion()
  {
    return this.version;
  }
  
  public RequestBuilder removeHeader(Header paramHeader)
  {
    if (this.headergroup == null) {
      this.headergroup = new HeaderGroup();
    }
    this.headergroup.removeHeader(paramHeader);
    return this;
  }
  
  public RequestBuilder removeHeaders(String paramString)
  {
    if ((paramString == null) || (this.headergroup == null)) {}
    for (;;)
    {
      return this;
      HeaderIterator localHeaderIterator = this.headergroup.iterator();
      while (localHeaderIterator.hasNext()) {
        if (paramString.equalsIgnoreCase(localHeaderIterator.nextHeader().getName())) {
          localHeaderIterator.remove();
        }
      }
    }
  }
  
  public RequestBuilder setConfig(RequestConfig paramRequestConfig)
  {
    this.config = paramRequestConfig;
    return this;
  }
  
  public RequestBuilder setEntity(HttpEntity paramHttpEntity)
  {
    this.entity = paramHttpEntity;
    return this;
  }
  
  public RequestBuilder setHeader(String paramString1, String paramString2)
  {
    if (this.headergroup == null) {
      this.headergroup = new HeaderGroup();
    }
    this.headergroup.updateHeader(new BasicHeader(paramString1, paramString2));
    return this;
  }
  
  public RequestBuilder setHeader(Header paramHeader)
  {
    if (this.headergroup == null) {
      this.headergroup = new HeaderGroup();
    }
    this.headergroup.updateHeader(paramHeader);
    return this;
  }
  
  public RequestBuilder setUri(String paramString)
  {
    if (paramString != null) {}
    for (URI localURI = URI.create(paramString);; localURI = null)
    {
      this.uri = localURI;
      return this;
    }
  }
  
  public RequestBuilder setUri(URI paramURI)
  {
    this.uri = paramURI;
    return this;
  }
  
  public RequestBuilder setVersion(ProtocolVersion paramProtocolVersion)
  {
    this.version = paramProtocolVersion;
    return this;
  }
  
  static class InternalEntityEclosingRequest
    extends HttpEntityEnclosingRequestBase
  {
    private final String method;
    
    InternalEntityEclosingRequest(String paramString)
    {
      this.method = paramString;
    }
    
    public String getMethod()
    {
      return this.method;
    }
  }
  
  static class InternalRequest
    extends HttpRequestBase
  {
    private final String method;
    
    InternalRequest(String paramString)
    {
      this.method = paramString;
    }
    
    public String getMethod()
    {
      return this.method;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.methods.RequestBuilder
 * JD-Core Version:    0.7.0.1
 */