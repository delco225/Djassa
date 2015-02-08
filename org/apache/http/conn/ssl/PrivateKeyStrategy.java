package org.apache.http.conn.ssl;

import java.net.Socket;
import java.util.Map;

public abstract interface PrivateKeyStrategy
{
  public abstract String chooseAlias(Map<String, PrivateKeyDetails> paramMap, Socket paramSocket);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.ssl.PrivateKeyStrategy
 * JD-Core Version:    0.7.0.1
 */