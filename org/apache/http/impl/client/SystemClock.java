package org.apache.http.impl.client;

class SystemClock
  implements Clock
{
  public long getCurrentTime()
  {
    return System.currentTimeMillis();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.SystemClock
 * JD-Core Version:    0.7.0.1
 */