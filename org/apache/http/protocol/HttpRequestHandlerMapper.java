package org.apache.http.protocol;

import org.apache.http.HttpRequest;

public abstract interface HttpRequestHandlerMapper
{
  public abstract HttpRequestHandler lookup(HttpRequest paramHttpRequest);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.HttpRequestHandlerMapper
 * JD-Core Version:    0.7.0.1
 */