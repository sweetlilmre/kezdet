package com.deviceteam.kezdet.anehost;

import java.util.HashMap;
import java.util.Map;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.deviceteam.kezdet.anehost.functions.ClearDataFunction;
import com.deviceteam.kezdet.anehost.functions.GetBinaryResponseFunction;
import com.deviceteam.kezdet.anehost.functions.GetJSONResponseFunction;
import com.deviceteam.kezdet.anehost.functions.GetResponseTypeFunction;
import com.deviceteam.kezdet.anehost.functions.InitManagerFunction;
import com.deviceteam.kezdet.anehost.functions.InvokeFunction;
import com.deviceteam.kezdet.anehost.functions.LoadFunction;

public class KezdetANEHostContext extends FREContext
{
  private KezdetANEHost _host;

  public KezdetANEHostContext(KezdetANEHost loader)
  {
    super();
    _host = loader;
  }
  
  @Override
  public void dispose()
  {
    _host.dispose();
  }

  @Override
  public Map<String, FREFunction> getFunctions()
  {
    Map<String, FREFunction> functions = new HashMap<String, FREFunction>();
    
    functions.put( "initManager", new InitManagerFunction( _host ) );
    functions.put( "load", new LoadFunction( _host ) );
    functions.put( "invoke", new InvokeFunction( _host ) );
    functions.put( "clearData", new ClearDataFunction( _host ) );
    functions.put( "getResponseType", new GetResponseTypeFunction( _host ) );
    functions.put( "getJSONResponse", new GetJSONResponseFunction( _host ) );
    functions.put( "getBinaryResponse", new GetBinaryResponseFunction( _host ) );
    
    return functions;  
  }

}
