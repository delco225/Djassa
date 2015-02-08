package org.apache.http.impl.client;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpRequest;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Immutable;

@Immutable
public class StandardHttpRequestRetryHandler
  extends DefaultHttpRequestRetryHandler
{
  private final Map<String, Boolean> idempotentMethods = new ConcurrentHashMap();
  
  public StandardHttpRequestRetryHandler()
  {
    this(3, false);
  }
  
  public StandardHttpRequestRetryHandler(int paramInt, boolean paramBoolean)
  {
    super(paramInt, paramBoolean);
    this.idempotentMethods.put("GET", Boolean.TRUE);
    this.idempotentMethods.put("HEAD", Boolean.TRUE);
    this.idempotentMethods.put("PUT", Boolean.TRUE);
    this.idempotentMethods.put("DELETE", Boolean.TRUE);
    this.idempotentMethods.put("OPTIONS", Boolean.TRUE);
    this.idempotentMethods.put("TRACE", Boolean.TRUE);
  }
  
  protected boolean handleAsIdempotent(HttpRequest paramHttpRequest)
  {
    String str = paramHttpRequest.getRequestLine().getMethod().toUpperCase(Locale.US);
    Boolean localBoolean = (Boolean)this.idempotentMethods.get(str);
    return (localBoolean != null) && (localBoolean.booleanValue());
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.StandardHttpRequestRetryHandler
 * JD-Core Version:    0.7.0.1
 */