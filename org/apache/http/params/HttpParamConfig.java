package org.apache.http.params;

import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.ConnectionConfig.Builder;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.MessageConstraints.Builder;
import org.apache.http.config.SocketConfig;
import org.apache.http.config.SocketConfig.Builder;

@Deprecated
public final class HttpParamConfig
{
  public static ConnectionConfig getConnectionConfig(HttpParams paramHttpParams)
  {
    MessageConstraints localMessageConstraints = getMessageConstraints(paramHttpParams);
    String str = (String)paramHttpParams.getParameter("http.protocol.element-charset");
    ConnectionConfig.Builder localBuilder = ConnectionConfig.custom();
    if (str != null) {}
    for (Charset localCharset = Charset.forName(str);; localCharset = null) {
      return localBuilder.setCharset(localCharset).setMalformedInputAction((CodingErrorAction)paramHttpParams.getParameter("http.malformed.input.action")).setMalformedInputAction((CodingErrorAction)paramHttpParams.getParameter("http.unmappable.input.action")).setMessageConstraints(localMessageConstraints).build();
    }
  }
  
  public static MessageConstraints getMessageConstraints(HttpParams paramHttpParams)
  {
    return MessageConstraints.custom().setMaxHeaderCount(paramHttpParams.getIntParameter("http.connection.max-header-count", -1)).setMaxLineLength(paramHttpParams.getIntParameter("http.connection.max-line-length", -1)).build();
  }
  
  public static SocketConfig getSocketConfig(HttpParams paramHttpParams)
  {
    return SocketConfig.custom().setSoTimeout(paramHttpParams.getIntParameter("http.socket.timeout", 0)).setSoReuseAddress(paramHttpParams.getBooleanParameter("http.socket.reuseaddr", false)).setSoKeepAlive(paramHttpParams.getBooleanParameter("http.socket.keepalive", false)).setSoLinger(paramHttpParams.getIntParameter("http.socket.linger", -1)).setTcpNoDelay(paramHttpParams.getBooleanParameter("http.tcp.nodelay", true)).build();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.params.HttpParamConfig
 * JD-Core Version:    0.7.0.1
 */