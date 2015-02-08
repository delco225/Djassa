package org.apache.http.config;

import org.apache.http.util.Args;

public class MessageConstraints
  implements Cloneable
{
  public static final MessageConstraints DEFAULT = new Builder().build();
  private final int maxHeaderCount;
  private final int maxLineLength;
  
  MessageConstraints(int paramInt1, int paramInt2)
  {
    this.maxLineLength = paramInt1;
    this.maxHeaderCount = paramInt2;
  }
  
  public static Builder copy(MessageConstraints paramMessageConstraints)
  {
    Args.notNull(paramMessageConstraints, "Message constraints");
    return new Builder().setMaxHeaderCount(paramMessageConstraints.getMaxHeaderCount()).setMaxLineLength(paramMessageConstraints.getMaxLineLength());
  }
  
  public static Builder custom()
  {
    return new Builder();
  }
  
  public static MessageConstraints lineLen(int paramInt)
  {
    return new MessageConstraints(Args.notNegative(paramInt, "Max line length"), -1);
  }
  
  protected MessageConstraints clone()
    throws CloneNotSupportedException
  {
    return (MessageConstraints)super.clone();
  }
  
  public int getMaxHeaderCount()
  {
    return this.maxHeaderCount;
  }
  
  public int getMaxLineLength()
  {
    return this.maxLineLength;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[maxLineLength=").append(this.maxLineLength).append(", maxHeaderCount=").append(this.maxHeaderCount).append("]");
    return localStringBuilder.toString();
  }
  
  public static class Builder
  {
    private int maxHeaderCount = -1;
    private int maxLineLength = -1;
    
    public MessageConstraints build()
    {
      return new MessageConstraints(this.maxLineLength, this.maxHeaderCount);
    }
    
    public Builder setMaxHeaderCount(int paramInt)
    {
      this.maxHeaderCount = paramInt;
      return this;
    }
    
    public Builder setMaxLineLength(int paramInt)
    {
      this.maxLineLength = paramInt;
      return this;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.config.MessageConstraints
 * JD-Core Version:    0.7.0.1
 */