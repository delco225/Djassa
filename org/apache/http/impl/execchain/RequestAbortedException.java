package org.apache.http.impl.execchain;

import java.io.InterruptedIOException;
import org.apache.http.annotation.Immutable;

@Immutable
public class RequestAbortedException
  extends InterruptedIOException
{
  private static final long serialVersionUID = 4973849966012490112L;
  
  public RequestAbortedException(String paramString)
  {
    super(paramString);
  }
  
  public RequestAbortedException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    if (paramThrowable != null) {
      initCause(paramThrowable);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.execchain.RequestAbortedException
 * JD-Core Version:    0.7.0.1
 */