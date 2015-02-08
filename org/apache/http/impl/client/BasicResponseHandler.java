package org.apache.http.impl.client;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

@Immutable
public class BasicResponseHandler
  implements ResponseHandler<String>
{
  public String handleResponse(HttpResponse paramHttpResponse)
    throws HttpResponseException, IOException
  {
    StatusLine localStatusLine = paramHttpResponse.getStatusLine();
    HttpEntity localHttpEntity = paramHttpResponse.getEntity();
    if (localStatusLine.getStatusCode() >= 300)
    {
      EntityUtils.consume(localHttpEntity);
      throw new HttpResponseException(localStatusLine.getStatusCode(), localStatusLine.getReasonPhrase());
    }
    if (localHttpEntity == null) {
      return null;
    }
    return EntityUtils.toString(localHttpEntity);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.BasicResponseHandler
 * JD-Core Version:    0.7.0.1
 */