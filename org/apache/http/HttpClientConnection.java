package org.apache.http;

import java.io.IOException;

public abstract interface HttpClientConnection
  extends HttpConnection
{
  public abstract void flush()
    throws IOException;
  
  public abstract boolean isResponseAvailable(int paramInt)
    throws IOException;
  
  public abstract void receiveResponseEntity(HttpResponse paramHttpResponse)
    throws HttpException, IOException;
  
  public abstract HttpResponse receiveResponseHeader()
    throws HttpException, IOException;
  
  public abstract void sendRequestEntity(HttpEntityEnclosingRequest paramHttpEntityEnclosingRequest)
    throws HttpException, IOException;
  
  public abstract void sendRequestHeader(HttpRequest paramHttpRequest)
    throws HttpException, IOException;
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.HttpClientConnection
 * JD-Core Version:    0.7.0.1
 */