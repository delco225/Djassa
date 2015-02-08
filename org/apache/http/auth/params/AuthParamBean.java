package org.apache.http.auth.params;

import org.apache.http.params.HttpAbstractParamBean;
import org.apache.http.params.HttpParams;

@Deprecated
public class AuthParamBean
  extends HttpAbstractParamBean
{
  public AuthParamBean(HttpParams paramHttpParams)
  {
    super(paramHttpParams);
  }
  
  public void setCredentialCharset(String paramString)
  {
    AuthParams.setCredentialCharset(this.params, paramString);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.auth.params.AuthParamBean
 * JD-Core Version:    0.7.0.1
 */