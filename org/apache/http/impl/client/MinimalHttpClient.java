package org.apache.http.impl.client;

import java.util.concurrent.TimeUnit;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.execchain.MinimalClientExec;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.util.Args;

@ThreadSafe
class MinimalHttpClient
  extends CloseableHttpClient
{
  private final HttpClientConnectionManager connManager;
  private final HttpParams params;
  private final MinimalClientExec requestExecutor;
  
  public MinimalHttpClient(HttpClientConnectionManager paramHttpClientConnectionManager)
  {
    this.connManager = ((HttpClientConnectionManager)Args.notNull(paramHttpClientConnectionManager, "HTTP connection manager"));
    this.requestExecutor = new MinimalClientExec(new HttpRequestExecutor(), paramHttpClientConnectionManager, DefaultConnectionReuseStrategy.INSTANCE, DefaultConnectionKeepAliveStrategy.INSTANCE);
    this.params = new BasicHttpParams();
  }
  
  public void close()
  {
    this.connManager.shutdown();
  }
  
  /* Error */
  protected org.apache.http.client.methods.CloseableHttpResponse doExecute(org.apache.http.HttpHost paramHttpHost, org.apache.http.HttpRequest paramHttpRequest, org.apache.http.protocol.HttpContext paramHttpContext)
    throws java.io.IOException, org.apache.http.client.ClientProtocolException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 70
    //   3: invokestatic 24	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_2
    //   8: ldc 72
    //   10: invokestatic 24	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   13: pop
    //   14: aload_2
    //   15: instanceof 74
    //   18: istore 6
    //   20: aconst_null
    //   21: astore 7
    //   23: iload 6
    //   25: ifeq +9 -> 34
    //   28: aload_2
    //   29: checkcast 74	org/apache/http/client/methods/HttpExecutionAware
    //   32: astore 7
    //   34: aload_2
    //   35: invokestatic 80	org/apache/http/client/methods/HttpRequestWrapper:wrap	(Lorg/apache/http/HttpRequest;)Lorg/apache/http/client/methods/HttpRequestWrapper;
    //   38: astore 9
    //   40: aload_3
    //   41: ifnull +72 -> 113
    //   44: aload_3
    //   45: invokestatic 86	org/apache/http/client/protocol/HttpClientContext:adapt	(Lorg/apache/http/protocol/HttpContext;)Lorg/apache/http/client/protocol/HttpClientContext;
    //   48: astore 10
    //   50: new 88	org/apache/http/conn/routing/HttpRoute
    //   53: dup
    //   54: aload_1
    //   55: invokespecial 91	org/apache/http/conn/routing/HttpRoute:<init>	(Lorg/apache/http/HttpHost;)V
    //   58: astore 11
    //   60: aload_2
    //   61: instanceof 93
    //   64: istore 12
    //   66: aconst_null
    //   67: astore 13
    //   69: iload 12
    //   71: ifeq +14 -> 85
    //   74: aload_2
    //   75: checkcast 93	org/apache/http/client/methods/Configurable
    //   78: invokeinterface 97 1 0
    //   83: astore 13
    //   85: aload 13
    //   87: ifnull +10 -> 97
    //   90: aload 10
    //   92: aload 13
    //   94: invokevirtual 101	org/apache/http/client/protocol/HttpClientContext:setRequestConfig	(Lorg/apache/http/client/config/RequestConfig;)V
    //   97: aload_0
    //   98: getfield 49	org/apache/http/impl/client/MinimalHttpClient:requestExecutor	Lorg/apache/http/impl/execchain/MinimalClientExec;
    //   101: aload 11
    //   103: aload 9
    //   105: aload 10
    //   107: aload 7
    //   109: invokevirtual 105	org/apache/http/impl/execchain/MinimalClientExec:execute	(Lorg/apache/http/conn/routing/HttpRoute;Lorg/apache/http/client/methods/HttpRequestWrapper;Lorg/apache/http/client/protocol/HttpClientContext;Lorg/apache/http/client/methods/HttpExecutionAware;)Lorg/apache/http/client/methods/CloseableHttpResponse;
    //   112: areturn
    //   113: new 107	org/apache/http/protocol/BasicHttpContext
    //   116: dup
    //   117: invokespecial 108	org/apache/http/protocol/BasicHttpContext:<init>	()V
    //   120: astore_3
    //   121: goto -77 -> 44
    //   124: astore 8
    //   126: new 66	org/apache/http/client/ClientProtocolException
    //   129: dup
    //   130: aload 8
    //   132: invokespecial 111	org/apache/http/client/ClientProtocolException:<init>	(Ljava/lang/Throwable;)V
    //   135: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	136	0	this	MinimalHttpClient
    //   0	136	1	paramHttpHost	org.apache.http.HttpHost
    //   0	136	2	paramHttpRequest	org.apache.http.HttpRequest
    //   0	136	3	paramHttpContext	org.apache.http.protocol.HttpContext
    //   18	6	6	bool1	boolean
    //   21	87	7	localHttpExecutionAware	org.apache.http.client.methods.HttpExecutionAware
    //   124	7	8	localHttpException	org.apache.http.HttpException
    //   38	66	9	localHttpRequestWrapper	org.apache.http.client.methods.HttpRequestWrapper
    //   48	58	10	localHttpClientContext	org.apache.http.client.protocol.HttpClientContext
    //   58	44	11	localHttpRoute	HttpRoute
    //   64	6	12	bool2	boolean
    //   67	26	13	localRequestConfig	org.apache.http.client.config.RequestConfig
    // Exception table:
    //   from	to	target	type
    //   34	40	124	org/apache/http/HttpException
    //   44	66	124	org/apache/http/HttpException
    //   74	85	124	org/apache/http/HttpException
    //   90	97	124	org/apache/http/HttpException
    //   97	113	124	org/apache/http/HttpException
    //   113	121	124	org/apache/http/HttpException
  }
  
  public ClientConnectionManager getConnectionManager()
  {
    new ClientConnectionManager()
    {
      public void closeExpiredConnections()
      {
        MinimalHttpClient.this.connManager.closeExpiredConnections();
      }
      
      public void closeIdleConnections(long paramAnonymousLong, TimeUnit paramAnonymousTimeUnit)
      {
        MinimalHttpClient.this.connManager.closeIdleConnections(paramAnonymousLong, paramAnonymousTimeUnit);
      }
      
      public SchemeRegistry getSchemeRegistry()
      {
        throw new UnsupportedOperationException();
      }
      
      public void releaseConnection(ManagedClientConnection paramAnonymousManagedClientConnection, long paramAnonymousLong, TimeUnit paramAnonymousTimeUnit)
      {
        throw new UnsupportedOperationException();
      }
      
      public ClientConnectionRequest requestConnection(HttpRoute paramAnonymousHttpRoute, Object paramAnonymousObject)
      {
        throw new UnsupportedOperationException();
      }
      
      public void shutdown()
      {
        MinimalHttpClient.this.connManager.shutdown();
      }
    };
  }
  
  public HttpParams getParams()
  {
    return this.params;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.MinimalHttpClient
 * JD-Core Version:    0.7.0.1
 */