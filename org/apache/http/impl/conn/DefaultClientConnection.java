package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.impl.SocketHttpClientConnection;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
@NotThreadSafe
public class DefaultClientConnection
  extends SocketHttpClientConnection
  implements OperatedClientConnection, ManagedHttpClientConnection, HttpContext
{
  private final Map<String, Object> attributes = new HashMap();
  private boolean connSecure;
  private final Log headerLog = LogFactory.getLog("org.apache.http.headers");
  private final Log log = LogFactory.getLog(getClass());
  private volatile boolean shutdown;
  private volatile Socket socket;
  private HttpHost targetHost;
  private final Log wireLog = LogFactory.getLog("org.apache.http.wire");
  
  public void bind(Socket paramSocket)
    throws IOException
  {
    bind(paramSocket, new BasicHttpParams());
  }
  
  public void close()
    throws IOException
  {
    try
    {
      super.close();
      if (this.log.isDebugEnabled()) {
        this.log.debug("Connection " + this + " closed");
      }
      return;
    }
    catch (IOException localIOException)
    {
      this.log.debug("I/O error closing connection", localIOException);
    }
  }
  
  protected HttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer paramSessionInputBuffer, HttpResponseFactory paramHttpResponseFactory, HttpParams paramHttpParams)
  {
    return new DefaultHttpResponseParser(paramSessionInputBuffer, null, paramHttpResponseFactory, paramHttpParams);
  }
  
  protected SessionInputBuffer createSessionInputBuffer(Socket paramSocket, int paramInt, HttpParams paramHttpParams)
    throws IOException
  {
    if (paramInt > 0) {}
    for (;;)
    {
      Object localObject = super.createSessionInputBuffer(paramSocket, paramInt, paramHttpParams);
      if (this.wireLog.isDebugEnabled()) {
        localObject = new LoggingSessionInputBuffer((SessionInputBuffer)localObject, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(paramHttpParams));
      }
      return localObject;
      paramInt = 8192;
    }
  }
  
  protected SessionOutputBuffer createSessionOutputBuffer(Socket paramSocket, int paramInt, HttpParams paramHttpParams)
    throws IOException
  {
    if (paramInt > 0) {}
    for (;;)
    {
      Object localObject = super.createSessionOutputBuffer(paramSocket, paramInt, paramHttpParams);
      if (this.wireLog.isDebugEnabled()) {
        localObject = new LoggingSessionOutputBuffer((SessionOutputBuffer)localObject, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(paramHttpParams));
      }
      return localObject;
      paramInt = 8192;
    }
  }
  
  public Object getAttribute(String paramString)
  {
    return this.attributes.get(paramString);
  }
  
  public String getId()
  {
    return null;
  }
  
  public SSLSession getSSLSession()
  {
    if ((this.socket instanceof SSLSocket)) {
      return ((SSLSocket)this.socket).getSession();
    }
    return null;
  }
  
  public final Socket getSocket()
  {
    return this.socket;
  }
  
  public final HttpHost getTargetHost()
  {
    return this.targetHost;
  }
  
  public final boolean isSecure()
  {
    return this.connSecure;
  }
  
  public void openCompleted(boolean paramBoolean, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramHttpParams, "Parameters");
    assertNotOpen();
    this.connSecure = paramBoolean;
    bind(this.socket, paramHttpParams);
  }
  
  public void opening(Socket paramSocket, HttpHost paramHttpHost)
    throws IOException
  {
    assertNotOpen();
    this.socket = paramSocket;
    this.targetHost = paramHttpHost;
    if (this.shutdown)
    {
      paramSocket.close();
      throw new InterruptedIOException("Connection already shutdown");
    }
  }
  
  public HttpResponse receiveResponseHeader()
    throws HttpException, IOException
  {
    HttpResponse localHttpResponse = super.receiveResponseHeader();
    if (this.log.isDebugEnabled()) {
      this.log.debug("Receiving response: " + localHttpResponse.getStatusLine());
    }
    if (this.headerLog.isDebugEnabled())
    {
      this.headerLog.debug("<< " + localHttpResponse.getStatusLine().toString());
      for (Header localHeader : localHttpResponse.getAllHeaders()) {
        this.headerLog.debug("<< " + localHeader.toString());
      }
    }
    return localHttpResponse;
  }
  
  public Object removeAttribute(String paramString)
  {
    return this.attributes.remove(paramString);
  }
  
  public void sendRequestHeader(HttpRequest paramHttpRequest)
    throws HttpException, IOException
  {
    if (this.log.isDebugEnabled()) {
      this.log.debug("Sending request: " + paramHttpRequest.getRequestLine());
    }
    super.sendRequestHeader(paramHttpRequest);
    if (this.headerLog.isDebugEnabled())
    {
      this.headerLog.debug(">> " + paramHttpRequest.getRequestLine().toString());
      for (Header localHeader : paramHttpRequest.getAllHeaders()) {
        this.headerLog.debug(">> " + localHeader.toString());
      }
    }
  }
  
  public void setAttribute(String paramString, Object paramObject)
  {
    this.attributes.put(paramString, paramObject);
  }
  
  public void shutdown()
    throws IOException
  {
    this.shutdown = true;
    try
    {
      super.shutdown();
      if (this.log.isDebugEnabled()) {
        this.log.debug("Connection " + this + " shut down");
      }
      Socket localSocket = this.socket;
      if (localSocket != null) {
        localSocket.close();
      }
      return;
    }
    catch (IOException localIOException)
    {
      this.log.debug("I/O error shutting down connection", localIOException);
    }
  }
  
  public void update(Socket paramSocket, HttpHost paramHttpHost, boolean paramBoolean, HttpParams paramHttpParams)
    throws IOException
  {
    assertOpen();
    Args.notNull(paramHttpHost, "Target host");
    Args.notNull(paramHttpParams, "Parameters");
    if (paramSocket != null)
    {
      this.socket = paramSocket;
      bind(paramSocket, paramHttpParams);
    }
    this.targetHost = paramHttpHost;
    this.connSecure = paramBoolean;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.DefaultClientConnection
 * JD-Core Version:    0.7.0.1
 */