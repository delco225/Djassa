package org.apache.http.client;

import java.io.IOException;
import org.apache.http.annotation.Immutable;

@Immutable
public class ClientProtocolException
  extends IOException
{
  private static final long serialVersionUID = -5596590843227115865L;
  
  public ClientProtocolException() {}
  
  public ClientProtocolException(String paramString)
  {
    super(paramString);
  }
  
  public ClientProtocolException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    initCause(paramThrowable);
  }
  
  public ClientProtocolException(Throwable paramThrowable)
  {
    initCause(paramThrowable);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.ClientProtocolException
 * JD-Core Version:    0.7.0.1
 */