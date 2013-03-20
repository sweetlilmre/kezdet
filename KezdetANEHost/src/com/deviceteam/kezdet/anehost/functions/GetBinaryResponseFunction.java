package com.deviceteam.kezdet.anehost.functions;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.deviceteam.kezdet.anehost.KezdetANEHost;
import com.deviceteam.kezdet.anehost.utils.HostResponseValues;

public class GetBinaryResponseFunction extends BaseFunction implements FREFunction
{
  public GetBinaryResponseFunction( KezdetANEHost host )
  {
    super( host, "KezdetANEHost-GetBinaryResponseFunction" );
  }

  @Override
  public FREObject call( FREContext arg0, FREObject[] arg1 )
  {
    HostResponseValues returnCode = HostResponseValues.UnknownError;
    byte[] returnValue = null;
    try
    {
      int pluginId = arg1[0].getAsInt();
      returnValue = _host.getBinaryResponse( pluginId ); 
      returnCode = HostResponseValues.OK;
    }
    catch( IndexOutOfBoundsException e )
    {
      returnCode = HostResponseValues.InvalidPluginId;
      Log.e(TAG, "GetBinaryResponseFunction invalid pluginId: " + e );
    }
    catch( Exception e )
    {
      Log.e(TAG, "GetBinaryResponseFunction failed: " + e );
    }
    return( GenerateReturnObject( returnCode, returnValue ) );
  }
}
