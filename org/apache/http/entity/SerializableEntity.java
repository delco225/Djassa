package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class SerializableEntity
  extends AbstractHttpEntity
{
  private Serializable objRef;
  private byte[] objSer;
  
  public SerializableEntity(Serializable paramSerializable)
  {
    Args.notNull(paramSerializable, "Source object");
    this.objRef = paramSerializable;
  }
  
  public SerializableEntity(Serializable paramSerializable, boolean paramBoolean)
    throws IOException
  {
    Args.notNull(paramSerializable, "Source object");
    if (paramBoolean)
    {
      createBytes(paramSerializable);
      return;
    }
    this.objRef = paramSerializable;
  }
  
  private void createBytes(Serializable paramSerializable)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localByteArrayOutputStream);
    localObjectOutputStream.writeObject(paramSerializable);
    localObjectOutputStream.flush();
    this.objSer = localByteArrayOutputStream.toByteArray();
  }
  
  public InputStream getContent()
    throws IOException, IllegalStateException
  {
    if (this.objSer == null) {
      createBytes(this.objRef);
    }
    return new ByteArrayInputStream(this.objSer);
  }
  
  public long getContentLength()
  {
    if (this.objSer == null) {
      return -1L;
    }
    return this.objSer.length;
  }
  
  public boolean isRepeatable()
  {
    return true;
  }
  
  public boolean isStreaming()
  {
    return this.objSer == null;
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    Args.notNull(paramOutputStream, "Output stream");
    if (this.objSer == null)
    {
      ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(paramOutputStream);
      localObjectOutputStream.writeObject(this.objRef);
      localObjectOutputStream.flush();
      return;
    }
    paramOutputStream.write(this.objSer);
    paramOutputStream.flush();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.entity.SerializableEntity
 * JD-Core Version:    0.7.0.1
 */