package com.deviceteam.kezdet.anehost.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.deviceteam.kezdet.anehost.KezdetANEHost;
import com.deviceteam.kezdet.anehost.utils.HostResponseValues;
import com.deviceteam.kezdet.interfaces.exception.BadPluginException;

public class GetJSONResponseFunction extends BaseFunction implements FREFunction
{
  public GetJSONResponseFunction(KezdetANEHost host)
  {
    super( host, "KezdetANEHost-GetJSONResponseFunction" );
  }

  @Override
  public FREObject call( FREContext arg0, FREObject[] arg1 )
  {
    HostResponseValues returnCode = HostResponseValues.UnknownError;
    String returnValue = null;
    try
    {
      int pluginId = arg1[0].getAsInt();
      returnValue = _host.get_pluginManager().getJSONResponse( pluginId ); 
      returnCode = HostResponseValues.OK;
    }
    catch( IndexOutOfBoundsException e )
    {
      returnCode = HostResponseValues.InvalidPluginId;
      LogE( "invalid pluginId: " + e );
    }
    catch( UnsupportedOperationException e )
    {
      LogE( "not supported: " + e );
      returnCode = HostResponseValues.UnsupportedOperation;
    }
    catch( BadPluginException e )
    {
      LogE( "plugin did not handle exception: " + e );
      returnCode = HostResponseValues.BadPlugin;
    }
    catch( Exception e )
    {
      LogE( "failed: " + e );
    }
    return( GenerateReturnObject( returnCode, returnValue ) );
    
  }
}
