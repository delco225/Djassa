package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineFormatter;

@NotThreadSafe
public class DefaultHttpRequestWriter
  extends AbstractMessageWriter<HttpRequest>
{
  public DefaultHttpRequestWriter(SessionOutputBuffer paramSessionOutputBuffer)
  {
    this(paramSessionOutputBuffer, null);
  }
  
  public DefaultHttpRequestWriter(SessionOutputBuffer paramSessionOutputBuffer, LineFormatter paramLineFormatter)
  {
    super(paramSessionOutputBuffer, paramLineFormatter);
  }
  
  protected void writeHeadLine(HttpRequest paramHttpRequest)
    throws IOException
  {
    this.lineFormatter.formatRequestLine(this.lineBuf, paramHttpRequest.getRequestLine());
    this.sessionBuffer.writeLine(this.lineBuf);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.io.DefaultHttpRequestWriter
 * JD-Core Version:    0.7.0.1
 */