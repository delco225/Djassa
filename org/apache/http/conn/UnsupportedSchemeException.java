package org.apache.http.conn;

import java.io.IOException;
import org.apache.http.annotation.Immutable;

@Immutable
public class UnsupportedSchemeException
  extends IOException
{
  private static final long serialVersionUID = 3597127619218687636L;
  
  public UnsupportedSchemeException(String paramString)
  {
    super(paramString);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.UnsupportedSchemeException
 * JD-Core Version:    0.7.0.1
 */