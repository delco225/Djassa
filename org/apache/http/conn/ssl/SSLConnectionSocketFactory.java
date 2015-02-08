package org.apache.http.conn.ssl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

@ThreadSafe
public class SSLConnectionSocketFactory
  implements LayeredConnectionSocketFactory
{
  public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
  public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new BrowserCompatHostnameVerifier();
  public static final String SSL = "SSL";
  public static final String SSLV2 = "SSLv2";
  public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = new StrictHostnameVerifier();
  public static final String TLS = "TLS";
  private final X509HostnameVerifier hostnameVerifier;
  private final SSLSocketFactory socketfactory;
  private final String[] supportedCipherSuites;
  private final String[] supportedProtocols;
  
  public SSLConnectionSocketFactory(SSLContext paramSSLContext)
  {
    this(paramSSLContext, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
  }
  
  public SSLConnectionSocketFactory(SSLContext paramSSLContext, X509HostnameVerifier paramX509HostnameVerifier)
  {
    this(((SSLContext)Args.notNull(paramSSLContext, "SSL context")).getSocketFactory(), null, null, paramX509HostnameVerifier);
  }
  
  public SSLConnectionSocketFactory(SSLContext paramSSLContext, String[] paramArrayOfString1, String[] paramArrayOfString2, X509HostnameVerifier paramX509HostnameVerifier)
  {
    this(((SSLContext)Args.notNull(paramSSLContext, "SSL context")).getSocketFactory(), paramArrayOfString1, paramArrayOfString2, paramX509HostnameVerifier);
  }
  
  public SSLConnectionSocketFactory(SSLSocketFactory paramSSLSocketFactory, X509HostnameVerifier paramX509HostnameVerifier)
  {
    this(paramSSLSocketFactory, null, null, paramX509HostnameVerifier);
  }
  
  public SSLConnectionSocketFactory(SSLSocketFactory paramSSLSocketFactory, String[] paramArrayOfString1, String[] paramArrayOfString2, X509HostnameVerifier paramX509HostnameVerifier)
  {
    this.socketfactory = ((SSLSocketFactory)Args.notNull(paramSSLSocketFactory, "SSL socket factory"));
    this.supportedProtocols = paramArrayOfString1;
    this.supportedCipherSuites = paramArrayOfString2;
    if (paramX509HostnameVerifier != null) {}
    for (;;)
    {
      this.hostnameVerifier = paramX509HostnameVerifier;
      return;
      paramX509HostnameVerifier = BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
    }
  }
  
  public static SSLConnectionSocketFactory getSocketFactory()
    throws SSLInitializationException
  {
    return new SSLConnectionSocketFactory(SSLContexts.createDefault(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
  }
  
  public static SSLConnectionSocketFactory getSystemSocketFactory()
    throws SSLInitializationException
  {
    return new SSLConnectionSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault(), split(System.getProperty("https.protocols")), split(System.getProperty("https.cipherSuites")), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
  }
  
  private static String[] split(String paramString)
  {
    if (TextUtils.isBlank(paramString)) {
      return null;
    }
    return paramString.split(" *, *");
  }
  
  private void verifyHostname(SSLSocket paramSSLSocket, String paramString)
    throws IOException
  {
    try
    {
      this.hostnameVerifier.verify(paramString, paramSSLSocket);
      return;
    }
    catch (IOException localIOException) {}
    try
    {
      paramSSLSocket.close();
      label17:
      throw localIOException;
    }
    catch (Exception localException)
    {
      break label17;
    }
  }
  
  public Socket connectSocket(int paramInt, Socket paramSocket, HttpHost paramHttpHost, InetSocketAddress paramInetSocketAddress1, InetSocketAddress paramInetSocketAddress2, HttpContext paramHttpContext)
    throws IOException
  {
    Args.notNull(paramHttpHost, "HTTP host");
    Args.notNull(paramInetSocketAddress1, "Remote address");
    if (paramSocket != null) {}
    for (Socket localSocket = paramSocket;; localSocket = createSocket(paramHttpContext))
    {
      if (paramInetSocketAddress2 != null) {
        localSocket.bind(paramInetSocketAddress2);
      }
      try
      {
        localSocket.connect(paramInetSocketAddress1, paramInt);
        if (!(localSocket instanceof SSLSocket)) {
          break;
        }
        SSLSocket localSSLSocket = (SSLSocket)localSocket;
        localSSLSocket.startHandshake();
        verifyHostname(localSSLSocket, paramHttpHost.getHostName());
        return localSocket;
      }
      catch (IOException localIOException1) {}
    }
    try
    {
      localSocket.close();
      label93:
      throw localIOException1;
      return createLayeredSocket(localSocket, paramHttpHost.getHostName(), paramInetSocketAddress1.getPort(), paramHttpContext);
    }
    catch (IOException localIOException2)
    {
      break label93;
    }
  }
  
  public Socket createLayeredSocket(Socket paramSocket, String paramString, int paramInt, HttpContext paramHttpContext)
    throws IOException
  {
    SSLSocket localSSLSocket = (SSLSocket)this.socketfactory.createSocket(paramSocket, paramString, paramInt, true);
    if (this.supportedProtocols != null) {
      localSSLSocket.setEnabledProtocols(this.supportedProtocols);
    }
    if (this.supportedCipherSuites != null) {
      localSSLSocket.setEnabledCipherSuites(this.supportedCipherSuites);
    }
    prepareSocket(localSSLSocket);
    localSSLSocket.startHandshake();
    verifyHostname(localSSLSocket, paramString);
    return localSSLSocket;
  }
  
  public Socket createSocket(HttpContext paramHttpContext)
    throws IOException
  {
    return SocketFactory.getDefault().createSocket();
  }
  
  X509HostnameVerifier getHostnameVerifier()
  {
    return this.hostnameVerifier;
  }
  
  protected void prepareSocket(SSLSocket paramSSLSocket)
    throws IOException
  {}
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.ssl.SSLConnectionSocketFactory
 * JD-Core Version:    0.7.0.1
 */