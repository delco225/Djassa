package org.apache.http.client;

import java.io.IOException;
import org.apache.http.HttpResponse;

public abstract interface ResponseHandler<T>
{
  public abstract T handleResponse(HttpResponse paramHttpResponse)
    throws ClientProtocolException, IOException;
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.ResponseHandler
 * JD-Core Version:    0.7.0.1
 */