package org.apache.http.message;

import java.io.Serializable;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Immutable
public class BasicRequestLine
  implements RequestLine, Cloneable, Serializable
{
  private static final long serialVersionUID = 2810581718468737193L;
  private final String method;
  private final ProtocolVersion protoversion;
  private final String uri;
  
  public BasicRequestLine(String paramString1, String paramString2, ProtocolVersion paramProtocolVersion)
  {
    this.method = ((String)Args.notNull(paramString1, "Method"));
    this.uri = ((String)Args.notNull(paramString2, "URI"));
    this.protoversion = ((ProtocolVersion)Args.notNull(paramProtocolVersion, "Version"));
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
  
  public String getMethod()
  {
    return this.method;
  }
  
  public ProtocolVersion getProtocolVersion()
  {
    return this.protoversion;
  }
  
  public String getUri()
  {
    return this.uri;
  }
  
  public String toString()
  {
    return BasicLineFormatter.INSTANCE.formatRequestLine(null, this).toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.message.BasicRequestLine
 * JD-Core Version:    0.7.0.1
 */