package com.deviceteam.kezdet.anehost;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.deviceteam.kezdet.anehost.functions.*;
import com.deviceteam.kezdet.Log;

import java.util.HashMap;
import java.util.Map;

public class KezdetANEHostContext extends FREContext
{
  public static final String TAG = "KezdetAirHost::KezdetANEHostContext";
  private KezdetANEHost _host;

  public KezdetANEHostContext(KezdetANEHost host)
  {
    super();
    _host = host;
  }

  @Override
  public void dispose()
  {
    _host.dispose();
    Log.verbose( TAG, "disposed." );
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
