package org.apache.http.config;

import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class SocketConfig
  implements Cloneable
{
  public static final SocketConfig DEFAULT = new Builder().build();
  private final boolean soKeepAlive;
  private final int soLinger;
  private final boolean soReuseAddress;
  private final int soTimeout;
  private final boolean tcpNoDelay;
  
  SocketConfig(int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, boolean paramBoolean3)
  {
    this.soTimeout = paramInt1;
    this.soReuseAddress = paramBoolean1;
    this.soLinger = paramInt2;
    this.soKeepAlive = paramBoolean2;
    this.tcpNoDelay = paramBoolean3;
  }
  
  public static Builder copy(SocketConfig paramSocketConfig)
  {
    Args.notNull(paramSocketConfig, "Socket config");
    return new Builder().setSoTimeout(paramSocketConfig.getSoTimeout()).setSoReuseAddress(paramSocketConfig.isSoReuseAddress()).setSoLinger(paramSocketConfig.getSoLinger()).setSoKeepAlive(paramSocketConfig.isSoKeepAlive()).setTcpNoDelay(paramSocketConfig.isTcpNoDelay());
  }
  
  public static Builder custom()
  {
    return new Builder();
  }
  
  protected SocketConfig clone()
    throws CloneNotSupportedException
  {
    return (SocketConfig)super.clone();
  }
  
  public int getSoLinger()
  {
    return this.soLinger;
  }
  
  public int getSoTimeout()
  {
    return this.soTimeout;
  }
  
  public boolean isSoKeepAlive()
  {
    return this.soKeepAlive;
  }
  
  public boolean isSoReuseAddress()
  {
    return this.soReuseAddress;
  }
  
  public boolean isTcpNoDelay()
  {
    return this.tcpNoDelay;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[soTimeout=").append(this.soTimeout).append(", soReuseAddress=").append(this.soReuseAddress).append(", soLinger=").append(this.soLinger).append(", soKeepAlive=").append(this.soKeepAlive).append(", tcpNoDelay=").append(this.tcpNoDelay).append("]");
    return localStringBuilder.toString();
  }
  
  public static class Builder
  {
    private boolean soKeepAlive;
    private int soLinger = -1;
    private boolean soReuseAddress;
    private int soTimeout;
    private boolean tcpNoDelay = true;
    
    public SocketConfig build()
    {
      return new SocketConfig(this.soTimeout, this.soReuseAddress, this.soLinger, this.soKeepAlive, this.tcpNoDelay);
    }
    
    public Builder setSoKeepAlive(boolean paramBoolean)
    {
      this.soKeepAlive = paramBoolean;
      return this;
    }
    
    public Builder setSoLinger(int paramInt)
    {
      this.soLinger = paramInt;
      return this;
    }
    
    public Builder setSoReuseAddress(boolean paramBoolean)
    {
      this.soReuseAddress = paramBoolean;
      return this;
    }
    
    public Builder setSoTimeout(int paramInt)
    {
      this.soTimeout = paramInt;
      return this;
    }
    
    public Builder setTcpNoDelay(boolean paramBoolean)
    {
      this.tcpNoDelay = paramBoolean;
      return this;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.config.SocketConfig
 * JD-Core Version:    0.7.0.1
 */