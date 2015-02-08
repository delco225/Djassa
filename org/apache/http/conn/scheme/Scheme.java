package org.apache.http.conn.scheme;

import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Deprecated
@Immutable
public final class Scheme
{
  private final int defaultPort;
  private final boolean layered;
  private final String name;
  private final SchemeSocketFactory socketFactory;
  private String stringRep;
  
  public Scheme(String paramString, int paramInt, SchemeSocketFactory paramSchemeSocketFactory)
  {
    Args.notNull(paramString, "Scheme name");
    if ((paramInt > 0) && (paramInt <= 65535)) {}
    for (boolean bool = true;; bool = false)
    {
      Args.check(bool, "Port is invalid");
      Args.notNull(paramSchemeSocketFactory, "Socket factory");
      this.name = paramString.toLowerCase(Locale.ENGLISH);
      this.defaultPort = paramInt;
      if (!(paramSchemeSocketFactory instanceof SchemeLayeredSocketFactory)) {
        break;
      }
      this.layered = true;
      this.socketFactory = paramSchemeSocketFactory;
      return;
    }
    if ((paramSchemeSocketFactory instanceof LayeredSchemeSocketFactory))
    {
      this.layered = true;
      this.socketFactory = new SchemeLayeredSocketFactoryAdaptor2((LayeredSchemeSocketFactory)paramSchemeSocketFactory);
      return;
    }
    this.layered = false;
    this.socketFactory = paramSchemeSocketFactory;
  }
  
  @Deprecated
  public Scheme(String paramString, SocketFactory paramSocketFactory, int paramInt)
  {
    Args.notNull(paramString, "Scheme name");
    Args.notNull(paramSocketFactory, "Socket factory");
    boolean bool;
    if ((paramInt > 0) && (paramInt <= 65535))
    {
      bool = true;
      Args.check(bool, "Port is invalid");
      this.name = paramString.toLowerCase(Locale.ENGLISH);
      if (!(paramSocketFactory instanceof LayeredSocketFactory)) {
        break label88;
      }
      this.socketFactory = new SchemeLayeredSocketFactoryAdaptor((LayeredSocketFactory)paramSocketFactory);
    }
    for (this.layered = true;; this.layered = false)
    {
      this.defaultPort = paramInt;
      return;
      bool = false;
      break;
      label88:
      this.socketFactory = new SchemeSocketFactoryAdaptor(paramSocketFactory);
    }
  }
  
  public final boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    Scheme localScheme;
    do
    {
      return true;
      if (!(paramObject instanceof Scheme)) {
        break;
      }
      localScheme = (Scheme)paramObject;
    } while ((this.name.equals(localScheme.name)) && (this.defaultPort == localScheme.defaultPort) && (this.layered == localScheme.layered));
    return false;
    return false;
  }
  
  public final int getDefaultPort()
  {
    return this.defaultPort;
  }
  
  public final String getName()
  {
    return this.name;
  }
  
  public final SchemeSocketFactory getSchemeSocketFactory()
  {
    return this.socketFactory;
  }
  
  @Deprecated
  public final SocketFactory getSocketFactory()
  {
    if ((this.socketFactory instanceof SchemeSocketFactoryAdaptor)) {
      return ((SchemeSocketFactoryAdaptor)this.socketFactory).getFactory();
    }
    if (this.layered) {
      return new LayeredSocketFactoryAdaptor((LayeredSchemeSocketFactory)this.socketFactory);
    }
    return new SocketFactoryAdaptor(this.socketFactory);
  }
  
  public int hashCode()
  {
    return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(17, this.defaultPort), this.name), this.layered);
  }
  
  public final boolean isLayered()
  {
    return this.layered;
  }
  
  public final int resolvePort(int paramInt)
  {
    if (paramInt <= 0) {
      paramInt = this.defaultPort;
    }
    return paramInt;
  }
  
  public final String toString()
  {
    if (this.stringRep == null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(this.name);
      localStringBuilder.append(':');
      localStringBuilder.append(Integer.toString(this.defaultPort));
      this.stringRep = localStringBuilder.toString();
    }
    return this.stringRep;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.scheme.Scheme
 * JD-Core Version:    0.7.0.1
 */