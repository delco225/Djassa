package org.apache.http.config;

import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import org.apache.http.Consts;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class ConnectionConfig
  implements Cloneable
{
  public static final ConnectionConfig DEFAULT = new Builder().build();
  private final int bufferSize;
  private final Charset charset;
  private final int fragmentSizeHint;
  private final CodingErrorAction malformedInputAction;
  private final MessageConstraints messageConstraints;
  private final CodingErrorAction unmappableInputAction;
  
  ConnectionConfig(int paramInt1, int paramInt2, Charset paramCharset, CodingErrorAction paramCodingErrorAction1, CodingErrorAction paramCodingErrorAction2, MessageConstraints paramMessageConstraints)
  {
    this.bufferSize = paramInt1;
    this.fragmentSizeHint = paramInt2;
    this.charset = paramCharset;
    this.malformedInputAction = paramCodingErrorAction1;
    this.unmappableInputAction = paramCodingErrorAction2;
    this.messageConstraints = paramMessageConstraints;
  }
  
  public static Builder copy(ConnectionConfig paramConnectionConfig)
  {
    Args.notNull(paramConnectionConfig, "Connection config");
    return new Builder().setCharset(paramConnectionConfig.getCharset()).setMalformedInputAction(paramConnectionConfig.getMalformedInputAction()).setUnmappableInputAction(paramConnectionConfig.getUnmappableInputAction()).setMessageConstraints(paramConnectionConfig.getMessageConstraints());
  }
  
  public static Builder custom()
  {
    return new Builder();
  }
  
  protected ConnectionConfig clone()
    throws CloneNotSupportedException
  {
    return (ConnectionConfig)super.clone();
  }
  
  public int getBufferSize()
  {
    return this.bufferSize;
  }
  
  public Charset getCharset()
  {
    return this.charset;
  }
  
  public int getFragmentSizeHint()
  {
    return this.fragmentSizeHint;
  }
  
  public CodingErrorAction getMalformedInputAction()
  {
    return this.malformedInputAction;
  }
  
  public MessageConstraints getMessageConstraints()
  {
    return this.messageConstraints;
  }
  
  public CodingErrorAction getUnmappableInputAction()
  {
    return this.unmappableInputAction;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[bufferSize=").append(this.bufferSize).append(", fragmentSizeHint=").append(this.fragmentSizeHint).append(", charset=").append(this.charset).append(", malformedInputAction=").append(this.malformedInputAction).append(", unmappableInputAction=").append(this.unmappableInputAction).append(", messageConstraints=").append(this.messageConstraints).append("]");
    return localStringBuilder.toString();
  }
  
  public static class Builder
  {
    private int bufferSize;
    private Charset charset;
    private int fragmentSizeHint = -1;
    private CodingErrorAction malformedInputAction;
    private MessageConstraints messageConstraints;
    private CodingErrorAction unmappableInputAction;
    
    public ConnectionConfig build()
    {
      Charset localCharset = this.charset;
      if ((localCharset == null) && ((this.malformedInputAction != null) || (this.unmappableInputAction != null))) {
        localCharset = Consts.ASCII;
      }
      int i;
      if (this.bufferSize > 0)
      {
        i = this.bufferSize;
        if (this.fragmentSizeHint < 0) {
          break label81;
        }
      }
      label81:
      for (int j = this.fragmentSizeHint;; j = i)
      {
        return new ConnectionConfig(i, j, localCharset, this.malformedInputAction, this.unmappableInputAction, this.messageConstraints);
        i = 8192;
        break;
      }
    }
    
    public Builder setBufferSize(int paramInt)
    {
      this.bufferSize = paramInt;
      return this;
    }
    
    public Builder setCharset(Charset paramCharset)
    {
      this.charset = paramCharset;
      return this;
    }
    
    public Builder setFragmentSizeHint(int paramInt)
    {
      this.fragmentSizeHint = paramInt;
      return this;
    }
    
    public Builder setMalformedInputAction(CodingErrorAction paramCodingErrorAction)
    {
      this.malformedInputAction = paramCodingErrorAction;
      if ((paramCodingErrorAction != null) && (this.charset == null)) {
        this.charset = Consts.ASCII;
      }
      return this;
    }
    
    public Builder setMessageConstraints(MessageConstraints paramMessageConstraints)
    {
      this.messageConstraints = paramMessageConstraints;
      return this;
    }
    
    public Builder setUnmappableInputAction(CodingErrorAction paramCodingErrorAction)
    {
      this.unmappableInputAction = paramCodingErrorAction;
      if ((paramCodingErrorAction != null) && (this.charset == null)) {
        this.charset = Consts.ASCII;
      }
      return this;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.config.ConnectionConfig
 * JD-Core Version:    0.7.0.1
 */