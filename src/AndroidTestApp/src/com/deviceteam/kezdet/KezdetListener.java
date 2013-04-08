package com.deviceteam.kezdet;

import com.deviceteam.kezdet.interfaces.IPluginCallback;

public class KezdetListener implements IPluginCallback
{
  private KezdetHostActivity _activity;

  public KezdetListener( KezdetHostActivity activity )
  {
    _activity = activity;
  }

  @Override
  public void onPluginCallback( String code, String level )
  {
    _activity.DisplayResult( code, level );
  }

}
