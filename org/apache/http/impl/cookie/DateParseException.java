package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;

@Immutable
public class DateParseException
  extends Exception
{
  private static final long serialVersionUID = 4417696455000643370L;
  
  public DateParseException() {}
  
  public DateParseException(String paramString)
  {
    super(paramString);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.DateParseException
 * JD-Core Version:    0.7.0.1
 */