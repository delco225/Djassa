package org.apache.http.impl.execchain;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import org.apache.http.HttpException;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

@Immutable
public class BackoffStrategyExec
  implements ClientExecChain
{
  private final BackoffManager backoffManager;
  private final ConnectionBackoffStrategy connectionBackoffStrategy;
  private final ClientExecChain requestExecutor;
  
  public BackoffStrategyExec(ClientExecChain paramClientExecChain, ConnectionBackoffStrategy paramConnectionBackoffStrategy, BackoffManager paramBackoffManager)
  {
    Args.notNull(paramClientExecChain, "HTTP client request executor");
    Args.notNull(paramConnectionBackoffStrategy, "Connection backoff strategy");
    Args.notNull(paramBackoffManager, "Backoff manager");
    this.requestExecutor = paramClientExecChain;
    this.connectionBackoffStrategy = paramConnectionBackoffStrategy;
    this.backoffManager = paramBackoffManager;
  }
  
  public CloseableHttpResponse execute(HttpRoute paramHttpRoute, HttpRequestWrapper paramHttpRequestWrapper, HttpClientContext paramHttpClientContext, HttpExecutionAware paramHttpExecutionAware)
    throws IOException, HttpException
  {
    Args.notNull(paramHttpRoute, "HTTP route");
    Args.notNull(paramHttpRequestWrapper, "HTTP request");
    Args.notNull(paramHttpClientContext, "HTTP context");
    CloseableHttpResponse localCloseableHttpResponse;
    try
    {
      localCloseableHttpResponse = this.requestExecutor.execute(paramHttpRoute, paramHttpRequestWrapper, paramHttpClientContext, paramHttpExecutionAware);
      if (this.connectionBackoffStrategy.shouldBackoff(localCloseableHttpResponse))
      {
        this.backoffManager.backOff(paramHttpRoute);
        return localCloseableHttpResponse;
      }
    }
    catch (Exception localException)
    {
      if (0 != 0) {
        null.close();
      }
      if (this.connectionBackoffStrategy.shouldBackoff(localException)) {
        this.backoffManager.backOff(paramHttpRoute);
      }
      if ((localException instanceof RuntimeException)) {
        throw ((RuntimeException)localException);
      }
      if ((localException instanceof HttpException)) {
        throw ((HttpException)localException);
      }
      if ((localException instanceof IOException)) {
        throw ((IOException)localException);
      }
      throw new UndeclaredThrowableException(localException);
    }
    this.backoffManager.probe(paramHttpRoute);
    return localCloseableHttpResponse;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.execchain.BackoffStrategyExec
 * JD-Core Version:    0.7.0.1
 */