package org.apache.http.impl.client;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;

@Deprecated
@Immutable
public class TunnelRefusedException
  extends HttpException
{
  private static final long serialVersionUID = -8646722842745617323L;
  private final HttpResponse response;
  
  public TunnelRefusedException(String paramString, HttpResponse paramHttpResponse)
  {
    super(paramString);
    this.response = paramHttpResponse;
  }
  
  public HttpResponse getResponse()
  {
    return this.response;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.TunnelRefusedException
 * JD-Core Version:    0.7.0.1
 */