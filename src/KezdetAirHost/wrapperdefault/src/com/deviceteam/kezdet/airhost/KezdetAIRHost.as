package com.deviceteam.kezdet.airhost
{
  import flash.events.StatusEvent;
  import flash.utils.ByteArray;
  
  public class KezdetAIRHost
  {
    /**
     * Cleans up the instance of the native extension.
     */	
    public function dispose() : void
    {
    }
    
    //----------------------------------------
    //
    // Handlers
    //
    //----------------------------------------
    
    public function KezdetAIRHost( certName : String, jarPath : String )
    {
      super();
    }
    
    public function load( jarName : String, pluginClassName : String, callback : IPluginCallback ) : int
    {
      return( -1 ); 
    }
    
    public function invoke( pluginId : int, methodName : String, args : String ) : String
    {
      return( "" );
    }
    
    public function clearData( pluginId : int ) : void
    {
    }
    
    public function getResponseType( pluginId : int ) : String
    {
      return( "" );      
    }

    public function getJSONResponse( pluginId : int ) : String
    {
      return( "" );       
    }

    public function getBinaryResponse( pluginId : int ) : ByteArray
    {
      return( null );        
    }
  }
}
