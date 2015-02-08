package org.apache.http.client.fluent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

public class Response
{
  private boolean consumed;
  private final HttpResponse response;
  
  Response(HttpResponse paramHttpResponse)
  {
    this.response = paramHttpResponse;
  }
  
  private void assertNotConsumed()
  {
    if (this.consumed) {
      throw new IllegalStateException("Response content has been already consumed");
    }
  }
  
  private void dispose()
  {
    if (this.consumed) {
      return;
    }
    try
    {
      InputStream localInputStream = this.response.getEntity().getContent();
      if (localInputStream != null) {
        localInputStream.close();
      }
      return;
    }
    catch (Exception localException) {}finally
    {
      this.consumed = true;
    }
  }
  
  public void discardContent()
  {
    dispose();
  }
  
  public <T> T handleResponse(ResponseHandler<T> paramResponseHandler)
    throws ClientProtocolException, IOException
  {
    assertNotConsumed();
    try
    {
      Object localObject2 = paramResponseHandler.handleResponse(this.response);
      return localObject2;
    }
    finally
    {
      dispose();
    }
  }
  
  public Content returnContent()
    throws ClientProtocolException, IOException
  {
    return (Content)handleResponse(new ContentResponseHandler());
  }
  
  public HttpResponse returnResponse()
    throws IOException
  {
    assertNotConsumed();
    try
    {
      HttpEntity localHttpEntity = this.response.getEntity();
      if (localHttpEntity != null)
      {
        ByteArrayEntity localByteArrayEntity = new ByteArrayEntity(EntityUtils.toByteArray(localHttpEntity));
        localByteArrayEntity.setContentType(ContentType.getOrDefault(localHttpEntity).toString());
        this.response.setEntity(localByteArrayEntity);
      }
      HttpResponse localHttpResponse = this.response;
      return localHttpResponse;
    }
    finally
    {
      this.consumed = true;
    }
  }
  
  public void saveContent(File paramFile)
    throws IOException
  {
    assertNotConsumed();
    StatusLine localStatusLine = this.response.getStatusLine();
    if (localStatusLine.getStatusCode() >= 300) {
      throw new HttpResponseException(localStatusLine.getStatusCode(), localStatusLine.getReasonPhrase());
    }
    FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
    try
    {
      HttpEntity localHttpEntity = this.response.getEntity();
      if (localHttpEntity != null) {
        localHttpEntity.writeTo(localFileOutputStream);
      }
      return;
    }
    finally
    {
      this.consumed = true;
      localFileOutputStream.close();
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.fluent.Response
 * JD-Core Version:    0.7.0.1
 */