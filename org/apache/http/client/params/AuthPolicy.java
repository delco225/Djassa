package org.apache.http.client.params;

import org.apache.http.annotation.Immutable;

@Deprecated
@Immutable
public final class AuthPolicy
{
  public static final String BASIC = "Basic";
  public static final String DIGEST = "Digest";
  public static final String KERBEROS = "Kerberos";
  public static final String NTLM = "NTLM";
  public static final String SPNEGO = "negotiate";
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.params.AuthPolicy
 * JD-Core Version:    0.7.0.1
 */