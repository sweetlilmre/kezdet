package com.deviceteam.kezdet.anehost.utils;

import com.deviceteam.kezdet.helpers.KezdetInterfaceMap;
import com.deviceteam.kezdet.interfaces.IPlugin;

public class PluginInfo
{
  private final KezdetInterfaceMap _methods;
  private final IPlugin _plugin;
  
  public IPlugin get_plugin()
  {
    return( _plugin );
  }
  
  public KezdetInterfaceMap get_methods()
  {
    return( _methods );
  }
  
  public PluginInfo( IPlugin plugin, KezdetInterfaceMap methods )
  {
    _methods = methods;
    _plugin = plugin;
  }
}
