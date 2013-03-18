package com.deviceteam.kezdet.host;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import android.app.Activity;
import android.content.Context;

import com.deviceteam.kezdet.exception.PluginCreateException;
import com.deviceteam.kezdet.exception.PluginLoadException;
import com.deviceteam.kezdet.exception.PluginVerifyException;
import com.deviceteam.kezdet.helpers.KezdetInterfaceMap;
import com.deviceteam.kezdet.interfaces.IPlugin;
import com.deviceteam.kezdet.interfaces.IPluginCallback;

/**
 * Manages plugins :)
 * @author sweetlilmre
 *
 */
public class PluginManager
{
  private PluginLoader _loader;
  private Context _context;
  private Activity _activity;
  
  /**
   * PluginManager constructor
   */
  public PluginManager()
  {
  }
  
  /**
   * Initialises the PluginLoader
   * @param context the Android application context 
   * @param activity the Android activity
   * @param parentClassloader the parent {@link java.lang.ClassLoader ClassLoader} context
   * @param certificateStream a stream representing an X506 public certificate that will be used to validate the signature of any loaded plugins
   * @throws PluginVerifyException on failure to load the certificate
   */
  public void init( Context context, Activity activity , ClassLoader parentClassloader, InputStream certificateStream ) throws PluginVerifyException
  {
    _context = context;
    _activity = activity;
    X509Certificate verificationCert = null;
    try
    {
      CertificateFactory cf = CertificateFactory.getInstance( "X.509" );
      verificationCert = (X509Certificate) cf.generateCertificate( certificateStream );      
    }
    catch( CertificateException e )
    {
      throw new PluginVerifyException( "Failure creating certificate from stream", e );
    }
    finally
    {
      if( certificateStream != null )
      {
        try
        {
          certificateStream.close();
        }
        catch( IOException e ) {}
      }
    }
    
    _loader = new PluginLoader( parentClassloader, verificationCert );
  }
  
  public void dispose()
  {
    
  }
  
  /**
   * Dynamically loads a plugin from a JAR file represented by the pluginStream parameter
   * @param pluginStream a stream representing a JAR file that contains the plugin
   * @param pluginClassName the name of the class in the JAR stream that implements IPlugin
   * @param callback an IPluginCallback interface used by the plugin to communicate asynchronous event information
   * @return an IPlugin interface
   * @throws PluginLoadException when the plugin fails to load (IOError)
   * @throws PluginVerifyException when the plugin cannot be successfully verified against the supplied public certificate 
   * @throws PluginCreateException when the plugin has been loaded and verified but cannot be instantiated
   */
  public IPlugin loadPlugin( InputStream pluginStream, String pluginClassName, IPluginCallback callback ) throws PluginLoadException, PluginVerifyException, PluginCreateException
  {
    IPlugin plugin = _loader.loadPlugin( pluginStream, pluginClassName );
    plugin.initialise( _context, _activity, callback );
    return( plugin );
  }
  
  /**
   * Invokes a method on a plugin method list
   * @param methods a Hashtable of method names mapped to {@link com.deviceteam.kezdet.interfaces#IInvokeMethod IInvokeMethod} references, see {@link #loadPlugin(InputStream, String, IPluginCallback) loadPlugin}
   * @param methodName the name of the method to invoke
   * @param jsonArgs a string in JSON object format passed to the method representing the method parameters
   * @return a string in JSON object format representing the return result of the method
   * @throws NoSuchMethodException
   */
  public String invoke( KezdetInterfaceMap methods, String methodName, String jsonArgs ) throws NoSuchMethodException
  {
    if( methods.containsKey( methodName ) )
    {
      return( methods.get( methodName ).invoke( jsonArgs ) );
    }
    throw( new NoSuchMethodException( "No such method: " + methodName ) );
  }
}
