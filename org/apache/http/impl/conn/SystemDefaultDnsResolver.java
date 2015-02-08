package org.apache.http.impl.conn;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.http.conn.DnsResolver;

public class SystemDefaultDnsResolver
  implements DnsResolver
{
  public static final SystemDefaultDnsResolver INSTANCE = new SystemDefaultDnsResolver();
  
  public InetAddress[] resolve(String paramString)
    throws UnknownHostException
  {
    return InetAddress.getAllByName(paramString);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.SystemDefaultDnsResolver
 * JD-Core Version:    0.7.0.1
 */