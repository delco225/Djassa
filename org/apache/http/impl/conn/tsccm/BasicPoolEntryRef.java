package org.apache.http.impl.conn.tsccm;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

@Deprecated
public class BasicPoolEntryRef
  extends WeakReference<BasicPoolEntry>
{
  private final HttpRoute route;
  
  public BasicPoolEntryRef(BasicPoolEntry paramBasicPoolEntry, ReferenceQueue<Object> paramReferenceQueue)
  {
    super(paramBasicPoolEntry, paramReferenceQueue);
    Args.notNull(paramBasicPoolEntry, "Pool entry");
    this.route = paramBasicPoolEntry.getPlannedRoute();
  }
  
  public final HttpRoute getRoute()
  {
    return this.route;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.tsccm.BasicPoolEntryRef
 * JD-Core Version:    0.7.0.1
 */