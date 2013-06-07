package com.deviceteam.kezdet.anehost.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.deviceteam.kezdet.anehost.KezdetANEHost;
import com.deviceteam.kezdet.anehost.utils.HostResponseValues;
import com.deviceteam.kezdet.interfaces.exception.BadPluginException;

public class GetResponseTypeFunction extends BaseFunction implements FREFunction
{
  public GetResponseTypeFunction( KezdetANEHost host )
  {
    super( host, "KezdetAirHost::GetResponseTypeFunction" );
  };


  @Override
  public FREObject call( FREContext arg0, FREObject[] arg1 )
  {
    HostResponseValues returnCode = HostResponseValues.UnknownError;
    String returnValue = null;
    try
    {
      int pluginId = arg1[0].getAsInt();
      returnValue = _host.getPluginManager().getResponseType( pluginId );
      returnCode = HostResponseValues.OK;
    }
    catch( IndexOutOfBoundsException e )
    {
      returnCode = HostResponseValues.InvalidPluginId;
      LogE( "invalid pluginId: ", e );
    }
    catch( BadPluginException e )
    {
      returnCode = HostResponseValues.BadPlugin;
      LogE( " plugin behaved badly ", e );
    }
    catch( Exception e )
    {
      LogE( "failed: ", e );
    }
    return( GenerateReturnObject( returnCode, returnValue ) );
  }
}
