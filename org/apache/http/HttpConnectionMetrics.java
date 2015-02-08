package org.apache.http;

public abstract interface HttpConnectionMetrics
{
  public abstract Object getMetric(String paramString);
  
  public abstract long getReceivedBytesCount();
  
  public abstract long getRequestCount();
  
  public abstract long getResponseCount();
  
  public abstract long getSentBytesCount();
  
  public abstract void reset();
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.HttpConnectionMetrics
 * JD-Core Version:    0.7.0.1
 */