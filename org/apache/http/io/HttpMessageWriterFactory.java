package org.apache.http.io;

import org.apache.http.HttpMessage;

public abstract interface HttpMessageWriterFactory<T extends HttpMessage>
{
  public abstract HttpMessageWriter<T> create(SessionOutputBuffer paramSessionOutputBuffer);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.io.HttpMessageWriterFactory
 * JD-Core Version:    0.7.0.1
 */