package com.deviceteam.kezdet.airhost
{
  import flash.events.StatusEvent;
  import flash.external.ExtensionContext;
  import flash.utils.ByteArray;
  import flash.utils.Dictionary;
  
  public class KezdetAIRHost
  {
    private var _extContext : ExtensionContext;
    private var _plugins : Dictionary = new Dictionary();


    /**
     * Cleans up the instance of the native extension.
     */	
    public function dispose() : void
    {
      _extContext.dispose();
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
         var callback : IPluginCallback = _plugins[ pluginId ] as IPluginCallback;
         callback.handleEvent( event.code, event.level );
      }
    }
    
    public function KezdetAIRHost( certPath : String, jarPath : String )
    {
      super();
      _extContext = ExtensionContext.createExtensionContext( "com.deviceteam.kezdet.airhost", "");
      
      if ( !_extContext )
      {
        throw new Error( "KezdetANEHost native extension is not supported on this platform." );
      }
      
      _extContext.addEventListener( StatusEvent.STATUS, onStatus );
      var ret : Object = _extContext.call( "initManager", certPath, jarPath );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "KezdetANEHost::initManager failed", ret == null ? 0 : ret["code"] );
      }
    }
    
    public function load( jarName : String, pluginClassName : String, callback : IPluginCallback ) : int
    {
      var ret : Object = _extContext.call( "load", jarName, pluginClassName );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "Plugin load() failed", ret == null ? 0 : ret["code"] );
      }
      var pluginId : int = ret["value"] as int;
      _plugins[ pluginId ] = callback;
      
      return( pluginId ); 
    }
    
    public function invoke( pluginId : int, methodName : String, args : String ) : String
    {
      var ret : Object = _extContext.call(  "invoke", pluginId, methodName, args );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "Plugin invoke() failed", ret == null ? 0 : ret["code"] );
      }

      return( ret["value"] as String );
    }
    
    public function clearData( pluginId : int ) : void
    {
      var ret : Object = _extContext.call(  "clearData", pluginId );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "Plugin clearData() failed", ret == null ? 0 : ret["code"] );
      }      
    }
    
    public function getResponseType( pluginId : int ) : String
    {
      var ret : Object = _extContext.call(  "getResponseType", pluginId );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "Plugin invoke() failed with code", ret == null ? 0 : ret["code"] );
      }
      
      return( ret["value"] as String );      
    }

    public function getJSONResponse( pluginId : int ) : String
    {
      var ret : Object = _extContext.call(  "getJSONResponse", pluginId );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "Plugin invoke() failed with code", ret == null ? 0 : ret["code"] );
      }
      
      return( ret["value"] as String );       
    }

    public function getBinaryResponse( pluginId : int ) : ByteArray
    {
      var ret : Object = _extContext.call(  "getBinaryResponse", pluginId );
      if( ret == null || ret["code"] != 0 )
      {
        throw new Error( "Plugin invoke() failed with code", ret == null ? 0 : ret["code"] );
      }
      
      return( ret["value"] as ByteArray );        
    }
  }
}
