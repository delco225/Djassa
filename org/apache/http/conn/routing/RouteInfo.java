package org.apache.http.conn.routing;

import java.net.InetAddress;
import org.apache.http.HttpHost;

public abstract interface RouteInfo
{
  public abstract int getHopCount();
  
  public abstract HttpHost getHopTarget(int paramInt);
  
  public abstract LayerType getLayerType();
  
  public abstract InetAddress getLocalAddress();
  
  public abstract HttpHost getProxyHost();
  
  public abstract HttpHost getTargetHost();
  
  public abstract TunnelType getTunnelType();
  
  public abstract boolean isLayered();
  
  public abstract boolean isSecure();
  
  public abstract boolean isTunnelled();
  
  public static enum LayerType
  {
    static
    {
      LAYERED = new LayerType("LAYERED", 1);
      LayerType[] arrayOfLayerType = new LayerType[2];
      arrayOfLayerType[0] = PLAIN;
      arrayOfLayerType[1] = LAYERED;
      $VALUES = arrayOfLayerType;
    }
    
    private LayerType() {}
  }
  
  public static enum TunnelType
  {
    static
    {
      TunnelType[] arrayOfTunnelType = new TunnelType[2];
      arrayOfTunnelType[0] = PLAIN;
      arrayOfTunnelType[1] = TUNNELLED;
      $VALUES = arrayOfTunnelType;
    }
    
    private TunnelType() {}
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.routing.RouteInfo
 * JD-Core Version:    0.7.0.1
 */