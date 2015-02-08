package org.apache.http.impl.execchain;

import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.ProtocolException;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.Args;
import org.apache.http.util.VersionInfo;

@Immutable
public class MinimalClientExec
  implements ClientExecChain
{
  private final HttpClientConnectionManager connManager;
  private final HttpProcessor httpProcessor;
  private final ConnectionKeepAliveStrategy keepAliveStrategy;
  private final Log log = LogFactory.getLog(getClass());
  private final HttpRequestExecutor requestExecutor;
  private final ConnectionReuseStrategy reuseStrategy;
  
  public MinimalClientExec(HttpRequestExecutor paramHttpRequestExecutor, HttpClientConnectionManager paramHttpClientConnectionManager, ConnectionReuseStrategy paramConnectionReuseStrategy, ConnectionKeepAliveStrategy paramConnectionKeepAliveStrategy)
  {
    Args.notNull(paramHttpRequestExecutor, "HTTP request executor");
    Args.notNull(paramHttpClientConnectionManager, "Client connection manager");
    Args.notNull(paramConnectionReuseStrategy, "Connection reuse strategy");
    Args.notNull(paramConnectionKeepAliveStrategy, "Connection keep alive strategy");
    HttpRequestInterceptor[] arrayOfHttpRequestInterceptor = new HttpRequestInterceptor[4];
    arrayOfHttpRequestInterceptor[0] = new RequestContent();
    arrayOfHttpRequestInterceptor[1] = new RequestTargetHost();
    arrayOfHttpRequestInterceptor[2] = new RequestClientConnControl();
    arrayOfHttpRequestInterceptor[3] = new RequestUserAgent(VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", getClass()));
    this.httpProcessor = new ImmutableHttpProcessor(arrayOfHttpRequestInterceptor);
    this.requestExecutor = paramHttpRequestExecutor;
    this.connManager = paramHttpClientConnectionManager;
    this.reuseStrategy = paramConnectionReuseStrategy;
    this.keepAliveStrategy = paramConnectionKeepAliveStrategy;
  }
  
  static void rewriteRequestURI(HttpRequestWrapper paramHttpRequestWrapper, HttpRoute paramHttpRoute)
    throws ProtocolException
  {
    try
    {
      URI localURI1 = paramHttpRequestWrapper.getURI();
      if (localURI1 != null)
      {
        if (localURI1.isAbsolute()) {}
        URI localURI2;
        for (Object localObject = URIUtils.rewriteURI(localURI1, null, true);; localObject = localURI2)
        {
          paramHttpRequestWrapper.setURI((URI)localObject);
          return;
          localURI2 = URIUtils.rewriteURI(localURI1);
        }
      }
      return;
    }
    catch (URISyntaxException localURISyntaxException)
    {
      throw new ProtocolException("Invalid URI: " + paramHttpRequestWrapper.getRequestLine().getUri(), localURISyntaxException);
    }
  }
  
  /* Error */
  public org.apache.http.client.methods.CloseableHttpResponse execute(HttpRoute paramHttpRoute, HttpRequestWrapper paramHttpRequestWrapper, org.apache.http.client.protocol.HttpClientContext paramHttpClientContext, org.apache.http.client.methods.HttpExecutionAware paramHttpExecutionAware)
    throws java.io.IOException, org.apache.http.HttpException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 163
    //   3: invokestatic 44	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_2
    //   8: ldc 165
    //   10: invokestatic 44	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   13: pop
    //   14: aload_3
    //   15: ldc 167
    //   17: invokestatic 44	org/apache/http/util/Args:notNull	(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;
    //   20: pop
    //   21: aload_2
    //   22: aload_1
    //   23: invokestatic 169	org/apache/http/impl/execchain/MinimalClientExec:rewriteRequestURI	(Lorg/apache/http/client/methods/HttpRequestWrapper;Lorg/apache/http/conn/routing/HttpRoute;)V
    //   26: aload_0
    //   27: getfield 87	org/apache/http/impl/execchain/MinimalClientExec:connManager	Lorg/apache/http/conn/HttpClientConnectionManager;
    //   30: aload_1
    //   31: aconst_null
    //   32: invokeinterface 175 3 0
    //   37: astore 8
    //   39: aload 4
    //   41: ifnull +40 -> 81
    //   44: aload 4
    //   46: invokeinterface 180 1 0
    //   51: ifeq +21 -> 72
    //   54: aload 8
    //   56: invokeinterface 185 1 0
    //   61: pop
    //   62: new 187	org/apache/http/impl/execchain/RequestAbortedException
    //   65: dup
    //   66: ldc 189
    //   68: invokespecial 190	org/apache/http/impl/execchain/RequestAbortedException:<init>	(Ljava/lang/String;)V
    //   71: athrow
    //   72: aload 4
    //   74: aload 8
    //   76: invokeinterface 194 2 0
    //   81: aload_3
    //   82: invokevirtual 200	org/apache/http/client/protocol/HttpClientContext:getRequestConfig	()Lorg/apache/http/client/config/RequestConfig;
    //   85: astore 9
    //   87: aload 9
    //   89: invokevirtual 206	org/apache/http/client/config/RequestConfig:getConnectionRequestTimeout	()I
    //   92: istore 15
    //   94: iload 15
    //   96: ifle +99 -> 195
    //   99: iload 15
    //   101: i2l
    //   102: lstore 16
    //   104: getstatic 212	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
    //   107: astore 18
    //   109: aload 8
    //   111: lload 16
    //   113: aload 18
    //   115: invokeinterface 216 4 0
    //   120: astore 19
    //   122: new 218	org/apache/http/impl/execchain/ConnectionHolder
    //   125: dup
    //   126: aload_0
    //   127: getfield 36	org/apache/http/impl/execchain/MinimalClientExec:log	Lorg/apache/commons/logging/Log;
    //   130: aload_0
    //   131: getfield 87	org/apache/http/impl/execchain/MinimalClientExec:connManager	Lorg/apache/http/conn/HttpClientConnectionManager;
    //   134: aload 19
    //   136: invokespecial 221	org/apache/http/impl/execchain/ConnectionHolder:<init>	(Lorg/apache/commons/logging/Log;Lorg/apache/http/conn/HttpClientConnectionManager;Lorg/apache/http/HttpClientConnection;)V
    //   139: astore 20
    //   141: aload 4
    //   143: ifnull +125 -> 268
    //   146: aload 4
    //   148: invokeinterface 180 1 0
    //   153: ifeq +106 -> 259
    //   156: aload 20
    //   158: invokevirtual 224	org/apache/http/impl/execchain/ConnectionHolder:close	()V
    //   161: new 187	org/apache/http/impl/execchain/RequestAbortedException
    //   164: dup
    //   165: ldc 189
    //   167: invokespecial 190	org/apache/http/impl/execchain/RequestAbortedException:<init>	(Ljava/lang/String;)V
    //   170: athrow
    //   171: astore 35
    //   173: new 226	java/io/InterruptedIOException
    //   176: dup
    //   177: ldc 228
    //   179: invokespecial 229	java/io/InterruptedIOException:<init>	(Ljava/lang/String;)V
    //   182: astore 36
    //   184: aload 36
    //   186: aload 35
    //   188: invokevirtual 233	java/io/InterruptedIOException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   191: pop
    //   192: aload 36
    //   194: athrow
    //   195: lconst_0
    //   196: lstore 16
    //   198: goto -94 -> 104
    //   201: astore 13
    //   203: invokestatic 239	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   206: invokevirtual 242	java/lang/Thread:interrupt	()V
    //   209: new 187	org/apache/http/impl/execchain/RequestAbortedException
    //   212: dup
    //   213: ldc 189
    //   215: aload 13
    //   217: invokespecial 243	org/apache/http/impl/execchain/RequestAbortedException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   220: astore 14
    //   222: aload 14
    //   224: athrow
    //   225: astore 10
    //   227: aload 10
    //   229: invokevirtual 247	java/util/concurrent/ExecutionException:getCause	()Ljava/lang/Throwable;
    //   232: astore 11
    //   234: aload 11
    //   236: ifnonnull +7 -> 243
    //   239: aload 10
    //   241: astore 11
    //   243: new 187	org/apache/http/impl/execchain/RequestAbortedException
    //   246: dup
    //   247: ldc 249
    //   249: aload 11
    //   251: invokespecial 243	org/apache/http/impl/execchain/RequestAbortedException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   254: astore 12
    //   256: aload 12
    //   258: athrow
    //   259: aload 4
    //   261: aload 20
    //   263: invokeinterface 194 2 0
    //   268: aload 19
    //   270: invokeinterface 254 1 0
    //   275: ifne +47 -> 322
    //   278: aload 9
    //   280: invokevirtual 257	org/apache/http/client/config/RequestConfig:getConnectTimeout	()I
    //   283: istore 39
    //   285: aload_0
    //   286: getfield 87	org/apache/http/impl/execchain/MinimalClientExec:connManager	Lorg/apache/http/conn/HttpClientConnectionManager;
    //   289: astore 40
    //   291: iload 39
    //   293: ifle +341 -> 634
    //   296: aload 40
    //   298: aload 19
    //   300: aload_1
    //   301: iload 39
    //   303: aload_3
    //   304: invokeinterface 261 5 0
    //   309: aload_0
    //   310: getfield 87	org/apache/http/impl/execchain/MinimalClientExec:connManager	Lorg/apache/http/conn/HttpClientConnectionManager;
    //   313: aload 19
    //   315: aload_1
    //   316: aload_3
    //   317: invokeinterface 265 4 0
    //   322: aload 9
    //   324: invokevirtual 268	org/apache/http/client/config/RequestConfig:getSocketTimeout	()I
    //   327: istore 24
    //   329: iload 24
    //   331: iflt +12 -> 343
    //   334: aload 19
    //   336: iload 24
    //   338: invokeinterface 272 2 0
    //   343: aload_2
    //   344: invokevirtual 276	org/apache/http/client/methods/HttpRequestWrapper:getOriginal	()Lorg/apache/http/HttpRequest;
    //   347: astore 25
    //   349: aload 25
    //   351: instanceof 278
    //   354: istore 26
    //   356: aconst_null
    //   357: astore 27
    //   359: iload 26
    //   361: ifeq +66 -> 427
    //   364: aload 25
    //   366: checkcast 278	org/apache/http/client/methods/HttpUriRequest
    //   369: invokeinterface 279 1 0
    //   374: astore 28
    //   376: aload 28
    //   378: invokevirtual 109	java/net/URI:isAbsolute	()Z
    //   381: istore 29
    //   383: aconst_null
    //   384: astore 27
    //   386: iload 29
    //   388: ifeq +39 -> 427
    //   391: aload 28
    //   393: invokevirtual 282	java/net/URI:getHost	()Ljava/lang/String;
    //   396: astore 30
    //   398: aload 28
    //   400: invokevirtual 285	java/net/URI:getPort	()I
    //   403: istore 31
    //   405: aload 28
    //   407: invokevirtual 288	java/net/URI:getScheme	()Ljava/lang/String;
    //   410: astore 32
    //   412: new 290	org/apache/http/HttpHost
    //   415: dup
    //   416: aload 30
    //   418: iload 31
    //   420: aload 32
    //   422: invokespecial 293	org/apache/http/HttpHost:<init>	(Ljava/lang/String;ILjava/lang/String;)V
    //   425: astore 27
    //   427: aload 27
    //   429: ifnonnull +9 -> 438
    //   432: aload_1
    //   433: invokevirtual 299	org/apache/http/conn/routing/HttpRoute:getTargetHost	()Lorg/apache/http/HttpHost;
    //   436: astore 27
    //   438: aload_3
    //   439: ldc_w 301
    //   442: aload 27
    //   444: invokevirtual 305	org/apache/http/client/protocol/HttpClientContext:setAttribute	(Ljava/lang/String;Ljava/lang/Object;)V
    //   447: aload_3
    //   448: ldc_w 307
    //   451: aload_2
    //   452: invokevirtual 305	org/apache/http/client/protocol/HttpClientContext:setAttribute	(Ljava/lang/String;Ljava/lang/Object;)V
    //   455: aload_3
    //   456: ldc_w 309
    //   459: aload 19
    //   461: invokevirtual 305	org/apache/http/client/protocol/HttpClientContext:setAttribute	(Ljava/lang/String;Ljava/lang/Object;)V
    //   464: aload_3
    //   465: ldc_w 311
    //   468: aload_1
    //   469: invokevirtual 305	org/apache/http/client/protocol/HttpClientContext:setAttribute	(Ljava/lang/String;Ljava/lang/Object;)V
    //   472: aload_0
    //   473: getfield 83	org/apache/http/impl/execchain/MinimalClientExec:httpProcessor	Lorg/apache/http/protocol/HttpProcessor;
    //   476: aload_2
    //   477: aload_3
    //   478: invokeinterface 317 3 0
    //   483: aload_0
    //   484: getfield 85	org/apache/http/impl/execchain/MinimalClientExec:requestExecutor	Lorg/apache/http/protocol/HttpRequestExecutor;
    //   487: aload_2
    //   488: aload 19
    //   490: aload_3
    //   491: invokevirtual 322	org/apache/http/protocol/HttpRequestExecutor:execute	(Lorg/apache/http/HttpRequest;Lorg/apache/http/HttpClientConnection;Lorg/apache/http/protocol/HttpContext;)Lorg/apache/http/HttpResponse;
    //   494: astore 33
    //   496: aload_0
    //   497: getfield 83	org/apache/http/impl/execchain/MinimalClientExec:httpProcessor	Lorg/apache/http/protocol/HttpProcessor;
    //   500: aload 33
    //   502: aload_3
    //   503: invokeinterface 325 3 0
    //   508: aload_0
    //   509: getfield 89	org/apache/http/impl/execchain/MinimalClientExec:reuseStrategy	Lorg/apache/http/ConnectionReuseStrategy;
    //   512: aload 33
    //   514: aload_3
    //   515: invokeinterface 331 3 0
    //   520: ifeq +64 -> 584
    //   523: aload 20
    //   525: aload_0
    //   526: getfield 91	org/apache/http/impl/execchain/MinimalClientExec:keepAliveStrategy	Lorg/apache/http/conn/ConnectionKeepAliveStrategy;
    //   529: aload 33
    //   531: aload_3
    //   532: invokeinterface 337 3 0
    //   537: getstatic 212	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
    //   540: invokevirtual 341	org/apache/http/impl/execchain/ConnectionHolder:setValidFor	(JLjava/util/concurrent/TimeUnit;)V
    //   543: aload 20
    //   545: invokevirtual 344	org/apache/http/impl/execchain/ConnectionHolder:markReusable	()V
    //   548: aload 33
    //   550: invokeinterface 350 1 0
    //   555: astore 34
    //   557: aload 34
    //   559: ifnull +13 -> 572
    //   562: aload 34
    //   564: invokeinterface 355 1 0
    //   569: ifne +33 -> 602
    //   572: aload 20
    //   574: invokevirtual 358	org/apache/http/impl/execchain/ConnectionHolder:releaseConnection	()V
    //   577: aload 33
    //   579: aconst_null
    //   580: invokestatic 364	org/apache/http/impl/execchain/Proxies:enhanceResponse	(Lorg/apache/http/HttpResponse;Lorg/apache/http/impl/execchain/ConnectionHolder;)Lorg/apache/http/client/methods/CloseableHttpResponse;
    //   583: areturn
    //   584: aload 20
    //   586: invokevirtual 367	org/apache/http/impl/execchain/ConnectionHolder:markNonReusable	()V
    //   589: goto -41 -> 548
    //   592: astore 23
    //   594: aload 20
    //   596: invokevirtual 370	org/apache/http/impl/execchain/ConnectionHolder:abortConnection	()V
    //   599: aload 23
    //   601: athrow
    //   602: aload 33
    //   604: aload 20
    //   606: invokestatic 364	org/apache/http/impl/execchain/Proxies:enhanceResponse	(Lorg/apache/http/HttpResponse;Lorg/apache/http/impl/execchain/ConnectionHolder;)Lorg/apache/http/client/methods/CloseableHttpResponse;
    //   609: astore 38
    //   611: aload 38
    //   613: areturn
    //   614: astore 22
    //   616: aload 20
    //   618: invokevirtual 370	org/apache/http/impl/execchain/ConnectionHolder:abortConnection	()V
    //   621: aload 22
    //   623: athrow
    //   624: astore 21
    //   626: aload 20
    //   628: invokevirtual 370	org/apache/http/impl/execchain/ConnectionHolder:abortConnection	()V
    //   631: aload 21
    //   633: athrow
    //   634: iconst_0
    //   635: istore 39
    //   637: goto -341 -> 296
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	640	0	this	MinimalClientExec
    //   0	640	1	paramHttpRoute	HttpRoute
    //   0	640	2	paramHttpRequestWrapper	HttpRequestWrapper
    //   0	640	3	paramHttpClientContext	org.apache.http.client.protocol.HttpClientContext
    //   0	640	4	paramHttpExecutionAware	org.apache.http.client.methods.HttpExecutionAware
    //   37	73	8	localConnectionRequest	org.apache.http.conn.ConnectionRequest
    //   85	238	9	localRequestConfig	org.apache.http.client.config.RequestConfig
    //   225	15	10	localExecutionException	java.util.concurrent.ExecutionException
    //   232	18	11	localObject	Object
    //   254	3	12	localRequestAbortedException1	RequestAbortedException
    //   201	15	13	localInterruptedException	java.lang.InterruptedException
    //   220	3	14	localRequestAbortedException2	RequestAbortedException
    //   92	8	15	i	int
    //   102	95	16	l	long
    //   107	7	18	localTimeUnit	java.util.concurrent.TimeUnit
    //   120	369	19	localHttpClientConnection	org.apache.http.HttpClientConnection
    //   139	488	20	localConnectionHolder	ConnectionHolder
    //   624	8	21	localRuntimeException	java.lang.RuntimeException
    //   614	8	22	localIOException	java.io.IOException
    //   592	8	23	localHttpException	org.apache.http.HttpException
    //   327	10	24	j	int
    //   347	18	25	localHttpRequest	org.apache.http.HttpRequest
    //   354	6	26	bool1	boolean
    //   357	86	27	localHttpHost	org.apache.http.HttpHost
    //   374	32	28	localURI	URI
    //   381	6	29	bool2	boolean
    //   396	21	30	str1	java.lang.String
    //   403	16	31	k	int
    //   410	11	32	str2	java.lang.String
    //   494	109	33	localHttpResponse	org.apache.http.HttpResponse
    //   555	8	34	localHttpEntity	org.apache.http.HttpEntity
    //   171	16	35	localConnectionShutdownException	org.apache.http.impl.conn.ConnectionShutdownException
    //   182	11	36	localInterruptedIOException	java.io.InterruptedIOException
    //   609	3	38	localCloseableHttpResponse	org.apache.http.client.methods.CloseableHttpResponse
    //   283	353	39	m	int
    //   289	8	40	localHttpClientConnectionManager	HttpClientConnectionManager
    // Exception table:
    //   from	to	target	type
    //   146	171	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   259	268	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   268	291	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   296	322	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   322	329	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   334	343	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   343	356	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   364	383	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   391	427	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   432	438	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   438	548	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   548	557	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   562	572	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   572	584	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   584	589	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   602	611	171	org/apache/http/impl/conn/ConnectionShutdownException
    //   87	94	201	java/lang/InterruptedException
    //   104	122	201	java/lang/InterruptedException
    //   87	94	225	java/util/concurrent/ExecutionException
    //   104	122	225	java/util/concurrent/ExecutionException
    //   146	171	592	org/apache/http/HttpException
    //   259	268	592	org/apache/http/HttpException
    //   268	291	592	org/apache/http/HttpException
    //   296	322	592	org/apache/http/HttpException
    //   322	329	592	org/apache/http/HttpException
    //   334	343	592	org/apache/http/HttpException
    //   343	356	592	org/apache/http/HttpException
    //   364	383	592	org/apache/http/HttpException
    //   391	427	592	org/apache/http/HttpException
    //   432	438	592	org/apache/http/HttpException
    //   438	548	592	org/apache/http/HttpException
    //   548	557	592	org/apache/http/HttpException
    //   562	572	592	org/apache/http/HttpException
    //   572	584	592	org/apache/http/HttpException
    //   584	589	592	org/apache/http/HttpException
    //   602	611	592	org/apache/http/HttpException
    //   146	171	614	java/io/IOException
    //   259	268	614	java/io/IOException
    //   268	291	614	java/io/IOException
    //   296	322	614	java/io/IOException
    //   322	329	614	java/io/IOException
    //   334	343	614	java/io/IOException
    //   343	356	614	java/io/IOException
    //   364	383	614	java/io/IOException
    //   391	427	614	java/io/IOException
    //   432	438	614	java/io/IOException
    //   438	548	614	java/io/IOException
    //   548	557	614	java/io/IOException
    //   562	572	614	java/io/IOException
    //   572	584	614	java/io/IOException
    //   584	589	614	java/io/IOException
    //   602	611	614	java/io/IOException
    //   146	171	624	java/lang/RuntimeException
    //   259	268	624	java/lang/RuntimeException
    //   268	291	624	java/lang/RuntimeException
    //   296	322	624	java/lang/RuntimeException
    //   322	329	624	java/lang/RuntimeException
    //   334	343	624	java/lang/RuntimeException
    //   343	356	624	java/lang/RuntimeException
    //   364	383	624	java/lang/RuntimeException
    //   391	427	624	java/lang/RuntimeException
    //   432	438	624	java/lang/RuntimeException
    //   438	548	624	java/lang/RuntimeException
    //   548	557	624	java/lang/RuntimeException
    //   562	572	624	java/lang/RuntimeException
    //   572	584	624	java/lang/RuntimeException
    //   584	589	624	java/lang/RuntimeException
    //   602	611	624	java/lang/RuntimeException
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.execchain.MinimalClientExec
 * JD-Core Version:    0.7.0.1
 */