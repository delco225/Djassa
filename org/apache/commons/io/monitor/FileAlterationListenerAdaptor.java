package org.apache.commons.io.monitor;

import java.io.File;

public class FileAlterationListenerAdaptor
  implements FileAlterationListener
{
  public void onDirectoryChange(File paramFile) {}
  
  public void onDirectoryCreate(File paramFile) {}
  
  public void onDirectoryDelete(File paramFile) {}
  
  public void onFileChange(File paramFile) {}
  
  public void onFileCreate(File paramFile) {}
  
  public void onFileDelete(File paramFile) {}
  
  public void onStart(FileAlterationObserver paramFileAlterationObserver) {}
  
  public void onStop(FileAlterationObserver paramFileAlterationObserver) {}
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.monitor.FileAlterationListenerAdaptor
 * JD-Core Version:    0.7.0.1
 */