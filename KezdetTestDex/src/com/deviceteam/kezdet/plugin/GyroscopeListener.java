package com.deviceteam.kezdet.plugin;


import com.deviceteam.kezdet.interfaces.IPluginCallback;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class GyroscopeListener implements SensorEventListener
{
	private IPluginCallback _callback;

	public GyroscopeListener(IPluginCallback callback)
	{
			this._callback = callback;
	}

  public void onAccuracyChanged( Sensor sensor, int accuracy )
  {
    // TODO Auto-generated method stub
  }

  public void onSensorChanged( SensorEvent event )
  {
    if(_callback != null) {

      StringBuilder s = new StringBuilder(Float.toString(event.values[0]));

      s.append("&").append(Float.toString(event.values[1])).append("&").append(Float.toString(event.values[2]));

      Log.i("GyroscopeListener", s.toString());

      _callback.onPluginCallback( "CHANGE", s.toString());
    }
  }
}
