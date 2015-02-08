package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;

@Immutable
public class UnsupportedDigestAlgorithmException
  extends RuntimeException
{
  private static final long serialVersionUID = 319558534317118022L;
  
  public UnsupportedDigestAlgorithmException() {}
  
  public UnsupportedDigestAlgorithmException(String paramString)
  {
    super(paramString);
  }
  
  public UnsupportedDigestAlgorithmException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.UnsupportedDigestAlgorithmException
 * JD-Core Version:    0.7.0.1
 */