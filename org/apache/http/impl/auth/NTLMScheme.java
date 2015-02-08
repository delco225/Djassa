package org.apache.http.impl.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.InvalidCredentialsException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.NTCredentials;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class NTLMScheme
  extends AuthSchemeBase
{
  private String challenge;
  private final NTLMEngine engine;
  private State state;
  
  public NTLMScheme()
  {
    this(new NTLMEngineImpl());
  }
  
  public NTLMScheme(NTLMEngine paramNTLMEngine)
  {
    Args.notNull(paramNTLMEngine, "NTLM engine");
    this.engine = paramNTLMEngine;
    this.state = State.UNINITIATED;
    this.challenge = null;
  }
  
  public Header authenticate(Credentials paramCredentials, HttpRequest paramHttpRequest)
    throws AuthenticationException
  {
    NTCredentials localNTCredentials;
    try
    {
      localNTCredentials = (NTCredentials)paramCredentials;
      if (this.state == State.FAILED) {
        throw new AuthenticationException("NTLM authentication failed");
      }
    }
    catch (ClassCastException localClassCastException)
    {
      throw new InvalidCredentialsException("Credentials cannot be used for NTLM authentication: " + paramCredentials.getClass().getName());
    }
    String str;
    CharArrayBuffer localCharArrayBuffer;
    if (this.state == State.CHALLENGE_RECEIVED)
    {
      str = this.engine.generateType1Msg(localNTCredentials.getDomain(), localNTCredentials.getWorkstation());
      this.state = State.MSG_TYPE1_GENERATED;
      localCharArrayBuffer = new CharArrayBuffer(32);
      if (!isProxy()) {
        break label232;
      }
      localCharArrayBuffer.append("Proxy-Authorization");
    }
    for (;;)
    {
      localCharArrayBuffer.append(": NTLM ");
      localCharArrayBuffer.append(str);
      return new BufferedHeader(localCharArrayBuffer);
      if (this.state == State.MSG_TYPE2_RECEVIED)
      {
        str = this.engine.generateType3Msg(localNTCredentials.getUserName(), localNTCredentials.getPassword(), localNTCredentials.getDomain(), localNTCredentials.getWorkstation(), this.challenge);
        this.state = State.MSG_TYPE3_GENERATED;
        break;
      }
      throw new AuthenticationException("Unexpected state: " + this.state);
      label232:
      localCharArrayBuffer.append("Authorization");
    }
  }
  
  public String getParameter(String paramString)
  {
    return null;
  }
  
  public String getRealm()
  {
    return null;
  }
  
  public String getSchemeName()
  {
    return "ntlm";
  }
  
  public boolean isComplete()
  {
    return (this.state == State.MSG_TYPE3_GENERATED) || (this.state == State.FAILED);
  }
  
  public boolean isConnectionBased()
  {
    return true;
  }
  
  protected void parseChallenge(CharArrayBuffer paramCharArrayBuffer, int paramInt1, int paramInt2)
    throws MalformedChallengeException
  {
    this.challenge = paramCharArrayBuffer.substringTrimmed(paramInt1, paramInt2);
    if (this.challenge.length() == 0) {
      if (this.state == State.UNINITIATED) {
        this.state = State.CHALLENGE_RECEIVED;
      }
    }
    do
    {
      return;
      this.state = State.FAILED;
      return;
      if (this.state.compareTo(State.MSG_TYPE1_GENERATED) < 0)
      {
        this.state = State.FAILED;
        throw new MalformedChallengeException("Out of sequence NTLM response message");
      }
    } while (this.state != State.MSG_TYPE1_GENERATED);
    this.state = State.MSG_TYPE2_RECEVIED;
  }
  
  static enum State
  {
    static
    {
      CHALLENGE_RECEIVED = new State("CHALLENGE_RECEIVED", 1);
      MSG_TYPE1_GENERATED = new State("MSG_TYPE1_GENERATED", 2);
      MSG_TYPE2_RECEVIED = new State("MSG_TYPE2_RECEVIED", 3);
      MSG_TYPE3_GENERATED = new State("MSG_TYPE3_GENERATED", 4);
      FAILED = new State("FAILED", 5);
      State[] arrayOfState = new State[6];
      arrayOfState[0] = UNINITIATED;
      arrayOfState[1] = CHALLENGE_RECEIVED;
      arrayOfState[2] = MSG_TYPE1_GENERATED;
      arrayOfState[3] = MSG_TYPE2_RECEVIED;
      arrayOfState[4] = MSG_TYPE3_GENERATED;
      arrayOfState[5] = FAILED;
      $VALUES = arrayOfState;
    }
    
    private State() {}
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.NTLMScheme
 * JD-Core Version:    0.7.0.1
 */