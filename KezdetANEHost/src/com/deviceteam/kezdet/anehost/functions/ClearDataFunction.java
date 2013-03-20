package com.deviceteam.kezdet.anehost.functions;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.deviceteam.kezdet.anehost.KezdetANEHost;
import com.deviceteam.kezdet.anehost.utils.HostResponseValues;

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
      _host.clearPluginData( pluginId );
      returnCode = HostResponseValues.OK;
    }
    catch( Exception e )
    {
      Log.e(TAG, "ClearDataFunction failed: " + e );
    }
    return GenerateReturnObject( returnCode, 0 );
  }
}
