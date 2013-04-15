package com.deviceteam.kezdet.airhost
{
  import flash.events.Event;
  
  public class KezdetEvent extends Event
  {
    public static const UPDATE:String = "UPDATE";
    
    public var pluginId : int;
    public var message : String;
    public var param : String;
    
    public function KezdetEvent(type:String, pluginId:int, message:String, param:String, bubbles:Boolean=false, cancelable:Boolean=false)
    {
      super(type, bubbles, cancelable);
      this.pluginId = pluginId;
      this.message = message;
      this.param = param;
    }
  }
}