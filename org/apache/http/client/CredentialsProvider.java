package org.apache.http.client;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;

public abstract interface CredentialsProvider
{
  public abstract void clear();
  
  public abstract Credentials getCredentials(AuthScope paramAuthScope);
  
  public abstract void setCredentials(AuthScope paramAuthScope, Credentials paramCredentials);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.CredentialsProvider
 * JD-Core Version:    0.7.0.1
 */