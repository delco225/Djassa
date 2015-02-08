package org.apache.http.impl.client;

import java.util.Collection;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.config.RequestConfig;

@Immutable
public class TargetAuthenticationStrategy
  extends AuthenticationStrategyImpl
{
  public static final TargetAuthenticationStrategy INSTANCE = new TargetAuthenticationStrategy();
  
  public TargetAuthenticationStrategy()
  {
    super(401, "WWW-Authenticate");
  }
  
  Collection<String> getPreferredAuthSchemes(RequestConfig paramRequestConfig)
  {
    return paramRequestConfig.getTargetPreferredAuthSchemes();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.TargetAuthenticationStrategy
 * JD-Core Version:    0.7.0.1
 */