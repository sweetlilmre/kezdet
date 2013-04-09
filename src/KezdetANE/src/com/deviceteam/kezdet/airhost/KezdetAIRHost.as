package com.deviceteam.kezdet.airhost
{
  import flash.events.EventDispatcher;
  import flash.events.StatusEvent;
  import flash.external.ExtensionContext;
  import flash.utils.ByteArray;
  
  public class KezdetAIRHost extends EventDispatcher
  {
    private var extContext:ExtensionContext;

    /**
     * Cleans up the instance of the native extension.
     */	
    public function dispose() : void
    {
      extContext.dispose();
    }
    
    //----------------------------------------
    //
    // Handlers
    //
    //----------------------------------------
    
    private function onStatus( event:StatusEvent ):void
    {
      var delim : int = event.code.indexOf( ":" );
      var pluginId : int = -1;
      if( delim != -1 )
      {
         pluginId = parseInt( event.code.substr( 0, delim ) );
         event.code = event.code.substring( delim + 1 );
      }
      dispatchEvent( new KezdetEvent( KezdetEvent.UPDATE, pluginId, event.code, event.level, false, false ) );
    }
    
    public function KezdetAIRHost( jarPath : String )
    {
      super();
      extContext = ExtensionContext.createExtensionContext( "com.deviceteam.kezdet.airhost", "");
      
      if ( !extContext )
      {
        throw new Error( "KezdetANEHost native extension is not supported on this platform." );
      }
      
      extContext.addEventListener( StatusEvent.STATUS, onStatus );
      var ret : Object = extContext.call( "initManager", jarPath );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "KezdetANEHost::initManager failed", ret == null ? 0 : ret["code"] );
      }
    }
    
    public function load( jarName : String, pluginClassName : String ) : int
    {
      var ret : Object = extContext.call( "load", jarName, pluginClassName );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "Plugin load() failed", ret == null ? 0 : ret["code"] );
      }
      
      return( ret["value"] as int ); 
    }
    
    public function invoke( pluginId : int, methodName : String, args : String ) : String
    {
      var ret : Object = extContext.call(  "invoke", pluginId, methodName, args );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "Plugin invoke() failed", ret == null ? 0 : ret["code"] );
      }

      return( ret["value"] as String );
    }
    
    public function clearData( pluginId : int ) : void
    {
      var ret : Object = extContext.call(  "clearData", pluginId );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "Plugin clearData() failed", ret == null ? 0 : ret["code"] );
      }      
    }
    
    public function getResponseType( pluginId : int ) : String
    {
      var ret : Object = extContext.call(  "getResponseType", pluginId );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "Plugin invoke() failed with code", ret == null ? 0 : ret["code"] );
      }
      
      return( ret["value"] as String );      
    }

    public function getJSONResponse( pluginId : int ) : String
    {
      var ret : Object = extContext.call(  "getJSONResponse", pluginId );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "Plugin invoke() failed with code", ret == null ? 0 : ret["code"] );
      }
      
      return( ret["value"] as String );       
    }

    public function getBinaryResponse( pluginId : int ) : ByteArray
    {
      var ret : Object = extContext.call(  "getBinaryResponse", pluginId );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "Plugin invoke() failed with code", ret == null ? 0 : ret["code"] );
      }
      
      return( ret["value"] as ByteArray );        
    }
  }
}
