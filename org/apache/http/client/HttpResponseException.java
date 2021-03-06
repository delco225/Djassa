package org.apache.http.client;

import org.apache.http.annotation.Immutable;

@Immutable
public class HttpResponseException
  extends ClientProtocolException
{
  private static final long serialVersionUID = -7186627969477257933L;
  private final int statusCode;
  
  public HttpResponseException(int paramInt, String paramString)
  {
    super(paramString);
    this.statusCode = paramInt;
  }
  
  public int getStatusCode()
  {
    return this.statusCode;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.HttpResponseException
 * JD-Core Version:    0.7.0.1
 */