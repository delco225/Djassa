package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineFormatter;

@NotThreadSafe
public class DefaultHttpResponseWriter
  extends AbstractMessageWriter<HttpResponse>
{
  public DefaultHttpResponseWriter(SessionOutputBuffer paramSessionOutputBuffer)
  {
    super(paramSessionOutputBuffer, null);
  }
  
  public DefaultHttpResponseWriter(SessionOutputBuffer paramSessionOutputBuffer, LineFormatter paramLineFormatter)
  {
    super(paramSessionOutputBuffer, paramLineFormatter);
  }
  
  protected void writeHeadLine(HttpResponse paramHttpResponse)
    throws IOException
  {
    this.lineFormatter.formatStatusLine(this.lineBuf, paramHttpResponse.getStatusLine());
    this.sessionBuffer.writeLine(this.lineBuf);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.io.DefaultHttpResponseWriter
 * JD-Core Version:    0.7.0.1
 */