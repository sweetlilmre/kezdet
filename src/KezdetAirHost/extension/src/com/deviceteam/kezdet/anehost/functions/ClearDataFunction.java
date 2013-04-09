package com.deviceteam.kezdet.anehost.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.deviceteam.kezdet.anehost.KezdetANEHost;
import com.deviceteam.kezdet.anehost.utils.HostResponseValues;
import com.deviceteam.kezdet.interfaces.exception.BadPluginException;

public class ClearDataFunction extends BaseFunction implements FREFunction
{
  public ClearDataFunction( KezdetANEHost host )
  {
    super( host, "KezdetANEHost-ClearDataFunction" );
    _host = host;
  }

  @Override
  public FREObject call( FREContext arg0, FREObject[] arg1 )
  {
    HostResponseValues returnCode = HostResponseValues.UnknownError;
    try
    {
      int pluginId = arg1[0].getAsInt();
      _host.get_pluginManager().clearPluginData( pluginId );
      returnCode = HostResponseValues.OK;
    }
    catch( BadPluginException e )
    {
      LogE("plugin did not handle exception: " + e );
      returnCode = HostResponseValues.BadPlugin;
    }
    catch( Exception e )
    {
      LogE("failed: " + e );
    }
    return GenerateReturnObject( returnCode, 0 );
  }
}
