package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthenticationException;

@Immutable
public class NTLMEngineException
  extends AuthenticationException
{
  private static final long serialVersionUID = 6027981323731768824L;
  
  public NTLMEngineException() {}
  
  public NTLMEngineException(String paramString)
  {
    super(paramString);
  }
  
  public NTLMEngineException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.NTLMEngineException
 * JD-Core Version:    0.7.0.1
 */