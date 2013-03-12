package com.deviceteam.kezdet.plugin;

import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import com.deviceteam.kezdet.interfaces.IInvokeMethod;
import com.deviceteam.kezdet.interfaces.IPlugin;
import com.deviceteam.kezdet.interfaces.IPluginCallback;
import com.google.gson.Gson;

public class KezdetPlugin implements IPlugin
{
  private IPluginCallback _callback;
  private SensorManager _sensorManager = null;
  private Sensor _gyroscope = null;
  private GyroscopeListener _listener = null;
  private Gson _gson = new Gson();

  @Override
  public void initialise( Context context, Activity activity, IPluginCallback callback )
  {
    this._callback = callback;

    _sensorManager = (SensorManager) activity.getSystemService( Activity.SENSOR_SERVICE );
    _gyroscope = _sensorManager.getDefaultSensor( Sensor.TYPE_GYROSCOPE );
    _listener = new GyroscopeListener( _callback );
  }

  @Override
  public void dispose()
  {
    stop();
  }

  @Override
  public byte[] getBinaryData() throws UnsupportedOperationException
  {
    throw( new UnsupportedOperationException() );
  }

  @Override
  public String getJSONData() throws UnsupportedOperationException
  {
    return null;
  }

  @Override
  public EventDataResponseType getReponseType()
  {
    return( EventDataResponseType.JSON );
  }

  public Sensor getGyroscope()
  {
    return _gyroscope;
  }

  public boolean start( String jsonArgs )
  {
    if( _gyroscope != null )
    {
      Integer rate = _gson.fromJson( jsonArgs, Integer.class );
      _sensorManager.registerListener( _listener, _gyroscope, rate );
      return( true );
    }
    else
    {
      return( false );
    }
  }

  public boolean stop()
  {
    Log.i( "GyroscopeStopFunction", "call" );
    if( _gyroscope != null )
    {
      _sensorManager.unregisterListener( _listener );
      return( true );
    }
    else
    {
      return( false );
    }
  }

  @Override
  public void registerMethods( Hashtable< String, IInvokeMethod > methods )
  {
    methods.put( "start", new IInvokeMethod()
    {
      @Override
      public String invoke( String jsonArgs )
      {
        return( _gson.toJson( start( jsonArgs ), Boolean.class ) );
      }
    } );

    methods.put( "stop", new IInvokeMethod()
    {
      @Override
      public String invoke( String jsonArgs )
      {
        return( _gson.toJson( stop(), Boolean.class ) );
      }
    } );

  }

}
