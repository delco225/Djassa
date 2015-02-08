package org.apache.http.impl.auth;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

@Deprecated
public class NegotiateScheme
  extends GGSSchemeBase
{
  private static final String KERBEROS_OID = "1.2.840.113554.1.2.2";
  private static final String SPNEGO_OID = "1.3.6.1.5.5.2";
  private final Log log = LogFactory.getLog(getClass());
  private final SpnegoTokenGenerator spengoGenerator;
  
  public NegotiateScheme()
  {
    this(null, false);
  }
  
  public NegotiateScheme(SpnegoTokenGenerator paramSpnegoTokenGenerator)
  {
    this(paramSpnegoTokenGenerator, false);
  }
  
  public NegotiateScheme(SpnegoTokenGenerator paramSpnegoTokenGenerator, boolean paramBoolean)
  {
    super(paramBoolean);
    this.spengoGenerator = paramSpnegoTokenGenerator;
  }
  
  public Header authenticate(Credentials paramCredentials, HttpRequest paramHttpRequest)
    throws AuthenticationException
  {
    return authenticate(paramCredentials, paramHttpRequest, null);
  }
  
  public Header authenticate(Credentials paramCredentials, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws AuthenticationException
  {
    return super.authenticate(paramCredentials, paramHttpRequest, paramHttpContext);
  }
  
  protected byte[] generateToken(byte[] paramArrayOfByte, String paramString)
    throws GSSException
  {
    Oid localOid = new Oid("1.3.6.1.5.5.2");
    Object localObject = paramArrayOfByte;
    int i = 0;
    try
    {
      byte[] arrayOfByte2 = generateGSSToken((byte[])localObject, localOid, paramString);
      localObject = arrayOfByte2;
    }
    catch (GSSException localGSSException)
    {
      for (;;)
      {
        try
        {
          byte[] arrayOfByte1 = this.spengoGenerator.generateSpnegoDERObject((byte[])localObject);
          localObject = arrayOfByte1;
          return localObject;
        }
        catch (IOException localIOException)
        {
          this.log.error(localIOException.getMessage(), localIOException);
        }
        localGSSException = localGSSException;
        if (localGSSException.getMajor() != 2) {
          continue;
        }
        this.log.debug("GSSException BAD_MECH, retry with Kerberos MECH");
        i = 1;
      }
      throw localGSSException;
    }
    if (i != 0)
    {
      this.log.debug("Using Kerberos MECH 1.2.840.113554.1.2.2");
      localObject = generateGSSToken((byte[])localObject, new Oid("1.2.840.113554.1.2.2"), paramString);
      if ((localObject == null) || (this.spengoGenerator == null)) {}
    }
    return localObject;
  }
  
  public String getParameter(String paramString)
  {
    Args.notNull(paramString, "Parameter name");
    return null;
  }
  
  public String getRealm()
  {
    return null;
  }
  
  public String getSchemeName()
  {
    return "Negotiate";
  }
  
  public boolean isConnectionBased()
  {
    return true;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.NegotiateScheme
 * JD-Core Version:    0.7.0.1
 */