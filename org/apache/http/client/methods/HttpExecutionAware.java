package org.apache.http.client.methods;

import org.apache.http.concurrent.Cancellable;

public abstract interface HttpExecutionAware
{
  public abstract boolean isAborted();
  
  public abstract void setCancellable(Cancellable paramCancellable);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.methods.HttpExecutionAware
 * JD-Core Version:    0.7.0.1
 */