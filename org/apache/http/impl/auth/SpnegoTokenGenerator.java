package org.apache.http.impl.auth;

import java.io.IOException;

@Deprecated
public abstract interface SpnegoTokenGenerator
{
  public abstract byte[] generateSpnegoDERObject(byte[] paramArrayOfByte)
    throws IOException;
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.SpnegoTokenGenerator
 * JD-Core Version:    0.7.0.1
 */