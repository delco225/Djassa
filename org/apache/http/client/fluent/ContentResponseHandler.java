package org.apache.http.client.fluent;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

class ContentResponseHandler
  implements ResponseHandler<Content>
{
  public Content handleResponse(HttpResponse paramHttpResponse)
    throws ClientProtocolException, IOException
  {
    StatusLine localStatusLine = paramHttpResponse.getStatusLine();
    HttpEntity localHttpEntity = paramHttpResponse.getEntity();
    if (localStatusLine.getStatusCode() >= 300) {
      throw new HttpResponseException(localStatusLine.getStatusCode(), localStatusLine.getReasonPhrase());
    }
    if (localHttpEntity != null) {
      return new Content(EntityUtils.toByteArray(localHttpEntity), ContentType.getOrDefault(localHttpEntity));
    }
    return Content.NO_CONTENT;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.fluent.ContentResponseHandler
 * JD-Core Version:    0.7.0.1
 */