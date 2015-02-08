package org.apache.http.impl.client;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

@Deprecated
public class DecompressingHttpClient
  implements HttpClient
{
  private final HttpRequestInterceptor acceptEncodingInterceptor;
  private final HttpClient backend;
  private final HttpResponseInterceptor contentEncodingInterceptor;
  
  public DecompressingHttpClient()
  {
    this(new DefaultHttpClient());
  }
  
  public DecompressingHttpClient(HttpClient paramHttpClient)
  {
    this(paramHttpClient, new RequestAcceptEncoding(), new ResponseContentEncoding());
  }
  
  DecompressingHttpClient(HttpClient paramHttpClient, HttpRequestInterceptor paramHttpRequestInterceptor, HttpResponseInterceptor paramHttpResponseInterceptor)
  {
    this.backend = paramHttpClient;
    this.acceptEncodingInterceptor = paramHttpRequestInterceptor;
    this.contentEncodingInterceptor = paramHttpResponseInterceptor;
  }
  
  public <T> T execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, ResponseHandler<? extends T> paramResponseHandler)
    throws IOException, ClientProtocolException
  {
    return execute(paramHttpHost, paramHttpRequest, paramResponseHandler, null);
  }
  
  public <T> T execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, ResponseHandler<? extends T> paramResponseHandler, HttpContext paramHttpContext)
    throws IOException, ClientProtocolException
  {
    HttpResponse localHttpResponse = execute(paramHttpHost, paramHttpRequest, paramHttpContext);
    try
    {
      Object localObject2 = paramResponseHandler.handleResponse(localHttpResponse);
      HttpEntity localHttpEntity2;
      return localObject2;
    }
    finally
    {
      HttpEntity localHttpEntity1 = localHttpResponse.getEntity();
      if (localHttpEntity1 != null) {
        EntityUtils.consume(localHttpEntity1);
      }
    }
  }
  
  public <T> T execute(HttpUriRequest paramHttpUriRequest, ResponseHandler<? extends T> paramResponseHandler)
    throws IOException, ClientProtocolException
  {
    return execute(getHttpHost(paramHttpUriRequest), paramHttpUriRequest, paramResponseHandler);
  }
  
  public <T> T execute(HttpUriRequest paramHttpUriRequest, ResponseHandler<? extends T> paramResponseHandler, HttpContext paramHttpContext)
    throws IOException, ClientProtocolException
  {
    return execute(getHttpHost(paramHttpUriRequest), paramHttpUriRequest, paramResponseHandler, paramHttpContext);
  }
  
  public HttpResponse execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest)
    throws IOException, ClientProtocolException
  {
    return execute(paramHttpHost, paramHttpRequest, (HttpContext)null);
  }
  
  /* Error */
  public HttpResponse execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws IOException, ClientProtocolException
  {
    // Byte code:
    //   0: aload_3
    //   1: ifnull +116 -> 117
    //   4: aload_3
    //   5: astore 4
    //   7: aload_2
    //   8: instanceof 85
    //   11: ifeq +130 -> 141
    //   14: new 87	org/apache/http/impl/client/EntityEnclosingRequestWrapper
    //   17: dup
    //   18: aload_2
    //   19: checkcast 85	org/apache/http/HttpEntityEnclosingRequest
    //   22: invokespecial 90	org/apache/http/impl/client/EntityEnclosingRequestWrapper:<init>	(Lorg/apache/http/HttpEntityEnclosingRequest;)V
    //   25: astore 6
    //   27: aload_0
    //   28: getfield 36	org/apache/http/impl/client/DecompressingHttpClient:acceptEncodingInterceptor	Lorg/apache/http/HttpRequestInterceptor;
    //   31: aload 6
    //   33: aload 4
    //   35: invokeinterface 96 3 0
    //   40: aload_0
    //   41: getfield 34	org/apache/http/impl/client/DecompressingHttpClient:backend	Lorg/apache/http/client/HttpClient;
    //   44: aload_1
    //   45: aload 6
    //   47: aload 4
    //   49: invokeinterface 97 4 0
    //   54: astore 7
    //   56: aload_0
    //   57: getfield 38	org/apache/http/impl/client/DecompressingHttpClient:contentEncodingInterceptor	Lorg/apache/http/HttpResponseInterceptor;
    //   60: aload 7
    //   62: aload 4
    //   64: invokeinterface 102 3 0
    //   69: getstatic 108	java/lang/Boolean:TRUE	Ljava/lang/Boolean;
    //   72: aload 4
    //   74: ldc 110
    //   76: invokeinterface 114 2 0
    //   81: invokevirtual 118	java/lang/Boolean:equals	(Ljava/lang/Object;)Z
    //   84: ifeq +30 -> 114
    //   87: aload 7
    //   89: ldc 120
    //   91: invokeinterface 124 2 0
    //   96: aload 7
    //   98: ldc 126
    //   100: invokeinterface 124 2 0
    //   105: aload 7
    //   107: ldc 128
    //   109: invokeinterface 124 2 0
    //   114: aload 7
    //   116: areturn
    //   117: new 130	org/apache/http/protocol/BasicHttpContext
    //   120: dup
    //   121: invokespecial 131	org/apache/http/protocol/BasicHttpContext:<init>	()V
    //   124: astore 4
    //   126: goto -119 -> 7
    //   129: astore 5
    //   131: new 44	org/apache/http/client/ClientProtocolException
    //   134: dup
    //   135: aload 5
    //   137: invokespecial 134	org/apache/http/client/ClientProtocolException:<init>	(Ljava/lang/Throwable;)V
    //   140: athrow
    //   141: new 136	org/apache/http/impl/client/RequestWrapper
    //   144: dup
    //   145: aload_2
    //   146: invokespecial 139	org/apache/http/impl/client/RequestWrapper:<init>	(Lorg/apache/http/HttpRequest;)V
    //   149: astore 6
    //   151: goto -124 -> 27
    //   154: astore 10
    //   156: aload 7
    //   158: invokeinterface 62 1 0
    //   163: invokestatic 68	org/apache/http/util/EntityUtils:consume	(Lorg/apache/http/HttpEntity;)V
    //   166: aload 10
    //   168: athrow
    //   169: astore 9
    //   171: aload 7
    //   173: invokeinterface 62 1 0
    //   178: invokestatic 68	org/apache/http/util/EntityUtils:consume	(Lorg/apache/http/HttpEntity;)V
    //   181: aload 9
    //   183: athrow
    //   184: astore 8
    //   186: aload 7
    //   188: invokeinterface 62 1 0
    //   193: invokestatic 68	org/apache/http/util/EntityUtils:consume	(Lorg/apache/http/HttpEntity;)V
    //   196: aload 8
    //   198: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	199	0	this	DecompressingHttpClient
    //   0	199	1	paramHttpHost	HttpHost
    //   0	199	2	paramHttpRequest	HttpRequest
    //   0	199	3	paramHttpContext	HttpContext
    //   5	120	4	localObject1	Object
    //   129	7	5	localHttpException1	org.apache.http.HttpException
    //   25	125	6	localObject2	Object
    //   54	133	7	localHttpResponse	HttpResponse
    //   184	13	8	localRuntimeException	java.lang.RuntimeException
    //   169	13	9	localIOException	IOException
    //   154	13	10	localHttpException2	org.apache.http.HttpException
    // Exception table:
    //   from	to	target	type
    //   7	27	129	org/apache/http/HttpException
    //   27	56	129	org/apache/http/HttpException
    //   117	126	129	org/apache/http/HttpException
    //   141	151	129	org/apache/http/HttpException
    //   156	169	129	org/apache/http/HttpException
    //   171	184	129	org/apache/http/HttpException
    //   186	199	129	org/apache/http/HttpException
    //   56	114	154	org/apache/http/HttpException
    //   56	114	169	java/io/IOException
    //   56	114	184	java/lang/RuntimeException
  }
  
  public HttpResponse execute(HttpUriRequest paramHttpUriRequest)
    throws IOException, ClientProtocolException
  {
    return execute(getHttpHost(paramHttpUriRequest), paramHttpUriRequest, (HttpContext)null);
  }
  
  public HttpResponse execute(HttpUriRequest paramHttpUriRequest, HttpContext paramHttpContext)
    throws IOException, ClientProtocolException
  {
    return execute(getHttpHost(paramHttpUriRequest), paramHttpUriRequest, paramHttpContext);
  }
  
  public ClientConnectionManager getConnectionManager()
  {
    return this.backend.getConnectionManager();
  }
  
  public HttpClient getHttpClient()
  {
    return this.backend;
  }
  
  HttpHost getHttpHost(HttpUriRequest paramHttpUriRequest)
  {
    return URIUtils.extractHost(paramHttpUriRequest.getURI());
  }
  
  public HttpParams getParams()
  {
    return this.backend.getParams();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.DecompressingHttpClient
 * JD-Core Version:    0.7.0.1
 */