package org.apache.http.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public final class NetUtils
{
  public static void formatAddress(StringBuilder paramStringBuilder, SocketAddress paramSocketAddress)
  {
    Args.notNull(paramStringBuilder, "Buffer");
    Args.notNull(paramSocketAddress, "Socket address");
    if ((paramSocketAddress instanceof InetSocketAddress))
    {
      InetSocketAddress localInetSocketAddress = (InetSocketAddress)paramSocketAddress;
      Object localObject = localInetSocketAddress.getAddress();
      if (localObject != null) {
        localObject = ((InetAddress)localObject).getHostAddress();
      }
      paramStringBuilder.append(localObject).append(':').append(localInetSocketAddress.getPort());
      return;
    }
    paramStringBuilder.append(paramSocketAddress);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.util.NetUtils
 * JD-Core Version:    0.7.0.1
 */