package org.apache.http.impl.conn;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpInetSocketAddress;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeLayeredSocketFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
@ThreadSafe
public class DefaultClientConnectionOperator
  implements ClientConnectionOperator
{
  protected final DnsResolver dnsResolver;
  private final Log log = LogFactory.getLog(getClass());
  protected final SchemeRegistry schemeRegistry;
  
  public DefaultClientConnectionOperator(SchemeRegistry paramSchemeRegistry)
  {
    Args.notNull(paramSchemeRegistry, "Scheme registry");
    this.schemeRegistry = paramSchemeRegistry;
    this.dnsResolver = new SystemDefaultDnsResolver();
  }
  
  public DefaultClientConnectionOperator(SchemeRegistry paramSchemeRegistry, DnsResolver paramDnsResolver)
  {
    Args.notNull(paramSchemeRegistry, "Scheme registry");
    Args.notNull(paramDnsResolver, "DNS resolver");
    this.schemeRegistry = paramSchemeRegistry;
    this.dnsResolver = paramDnsResolver;
  }
  
  private SchemeRegistry getSchemeRegistry(HttpContext paramHttpContext)
  {
    SchemeRegistry localSchemeRegistry = (SchemeRegistry)paramHttpContext.getAttribute("http.scheme-registry");
    if (localSchemeRegistry == null) {
      localSchemeRegistry = this.schemeRegistry;
    }
    return localSchemeRegistry;
  }
  
  public OperatedClientConnection createConnection()
  {
    return new DefaultClientConnection();
  }
  
  public void openConnection(OperatedClientConnection paramOperatedClientConnection, HttpHost paramHttpHost, InetAddress paramInetAddress, HttpContext paramHttpContext, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramOperatedClientConnection, "Connection");
    Args.notNull(paramHttpHost, "Target host");
    Args.notNull(paramHttpParams, "HTTP parameters");
    boolean bool;
    SchemeSocketFactory localSchemeSocketFactory;
    InetAddress[] arrayOfInetAddress;
    int i;
    int j;
    if (!paramOperatedClientConnection.isOpen())
    {
      bool = true;
      Asserts.check(bool, "Connection must not be open");
      Scheme localScheme = getSchemeRegistry(paramHttpContext).getScheme(paramHttpHost.getSchemeName());
      localSchemeSocketFactory = localScheme.getSchemeSocketFactory();
      arrayOfInetAddress = resolveHostname(paramHttpHost.getHostName());
      i = localScheme.resolvePort(paramHttpHost.getPort());
      j = 0;
    }
    int k;
    HttpInetSocketAddress localHttpInetSocketAddress;
    label279:
    label305:
    label356:
    for (;;)
    {
      InetAddress localInetAddress;
      if (j < arrayOfInetAddress.length)
      {
        localInetAddress = arrayOfInetAddress[j];
        if (j != -1 + arrayOfInetAddress.length) {
          break label279;
        }
      }
      for (k = 1;; k = 0)
      {
        Object localObject = localSchemeSocketFactory.createSocket(paramHttpParams);
        paramOperatedClientConnection.opening((Socket)localObject, paramHttpHost);
        localHttpInetSocketAddress = new HttpInetSocketAddress(paramHttpHost, localInetAddress, i);
        InetSocketAddress localInetSocketAddress = null;
        if (paramInetAddress != null) {
          localInetSocketAddress = new InetSocketAddress(paramInetAddress, 0);
        }
        if (this.log.isDebugEnabled()) {
          this.log.debug("Connecting to " + localHttpInetSocketAddress);
        }
        try
        {
          Socket localSocket = localSchemeSocketFactory.connectSocket((Socket)localObject, localHttpInetSocketAddress, localInetSocketAddress, paramHttpParams);
          if (localObject != localSocket)
          {
            localObject = localSocket;
            paramOperatedClientConnection.opening((Socket)localObject, paramHttpHost);
          }
          prepareSocket((Socket)localObject, paramHttpContext, paramHttpParams);
          paramOperatedClientConnection.openCompleted(localSchemeSocketFactory.isSecure((Socket)localObject), paramHttpParams);
          return;
        }
        catch (ConnectException localConnectException)
        {
          if (k == 0) {
            break label305;
          }
          throw localConnectException;
        }
        catch (ConnectTimeoutException localConnectTimeoutException)
        {
          if (k == 0) {
            break label305;
          }
          throw localConnectTimeoutException;
          if (!this.log.isDebugEnabled()) {
            break label356;
          }
          this.log.debug("Connect to " + localHttpInetSocketAddress + " timed out. " + "Connection will be retried using another IP address");
          j++;
        }
        bool = false;
        break;
      }
    }
  }
  
  protected void prepareSocket(Socket paramSocket, HttpContext paramHttpContext, HttpParams paramHttpParams)
    throws IOException
  {
    paramSocket.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(paramHttpParams));
    paramSocket.setSoTimeout(HttpConnectionParams.getSoTimeout(paramHttpParams));
    int i = HttpConnectionParams.getLinger(paramHttpParams);
    if (i >= 0) {
      if (i <= 0) {
        break label44;
      }
    }
    label44:
    for (boolean bool = true;; bool = false)
    {
      paramSocket.setSoLinger(bool, i);
      return;
    }
  }
  
  protected InetAddress[] resolveHostname(String paramString)
    throws UnknownHostException
  {
    return this.dnsResolver.resolve(paramString);
  }
  
  public void updateSecureConnection(OperatedClientConnection paramOperatedClientConnection, HttpHost paramHttpHost, HttpContext paramHttpContext, HttpParams paramHttpParams)
    throws IOException
  {
    Args.notNull(paramOperatedClientConnection, "Connection");
    Args.notNull(paramHttpHost, "Target host");
    Args.notNull(paramHttpParams, "Parameters");
    Asserts.check(paramOperatedClientConnection.isOpen(), "Connection must be open");
    Scheme localScheme = getSchemeRegistry(paramHttpContext).getScheme(paramHttpHost.getSchemeName());
    Asserts.check(localScheme.getSchemeSocketFactory() instanceof SchemeLayeredSocketFactory, "Socket factory must implement SchemeLayeredSocketFactory");
    SchemeLayeredSocketFactory localSchemeLayeredSocketFactory = (SchemeLayeredSocketFactory)localScheme.getSchemeSocketFactory();
    Socket localSocket = localSchemeLayeredSocketFactory.createLayeredSocket(paramOperatedClientConnection.getSocket(), paramHttpHost.getHostName(), localScheme.resolvePort(paramHttpHost.getPort()), paramHttpParams);
    prepareSocket(localSocket, paramHttpContext, paramHttpParams);
    paramOperatedClientConnection.update(localSocket, paramHttpHost, localSchemeLayeredSocketFactory.isSecure(localSocket), paramHttpParams);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.DefaultClientConnectionOperator
 * JD-Core Version:    0.7.0.1
 */