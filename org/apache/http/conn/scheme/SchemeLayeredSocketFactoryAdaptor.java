package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.http.params.HttpParams;

@Deprecated
class SchemeLayeredSocketFactoryAdaptor
  extends SchemeSocketFactoryAdaptor
  implements SchemeLayeredSocketFactory
{
  private final LayeredSocketFactory factory;
  
  SchemeLayeredSocketFactoryAdaptor(LayeredSocketFactory paramLayeredSocketFactory)
  {
    super(paramLayeredSocketFactory);
    this.factory = paramLayeredSocketFactory;
  }
  
  public Socket createLayeredSocket(Socket paramSocket, String paramString, int paramInt, HttpParams paramHttpParams)
    throws IOException, UnknownHostException
  {
    return this.factory.createSocket(paramSocket, paramString, paramInt, true);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.scheme.SchemeLayeredSocketFactoryAdaptor
 * JD-Core Version:    0.7.0.1
 */