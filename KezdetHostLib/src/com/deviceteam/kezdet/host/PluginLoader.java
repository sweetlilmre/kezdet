package com.deviceteam.kezdet.host;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.jar.JarFile;

import org.whipplugin.data.bundle.JarVerifier;

import android.content.Context;

import com.deviceteam.kezdet.exception.PluginCreateException;
import com.deviceteam.kezdet.exception.PluginLoadException;
import com.deviceteam.kezdet.exception.PluginVerifyException;
import com.deviceteam.kezdet.interfaces.IPlugin;
import com.google.dexmaker.AppDataDirGuesser;

public class PluginLoader
{
  private Context _context;
  private X509Certificate _cert;
  private ClassLoader _parentLoader;

  /**
   * PluginLoader constructor
   * @param context Android Context object
   * @param parentLoader Classloader object to use as a loader context
   */
  public PluginLoader( Context context, ClassLoader parentLoader )
  {
    _context = context;
    _parentLoader = parentLoader;
  }

  /**
   * Initialises the PluginLoader
   * @throws PluginVerifyException
   */
  public void init() throws PluginVerifyException
  {
    try
    {
      InputStream is = _context.getAssets().open( "kezdet-public.cer" );
      CertificateFactory cf = CertificateFactory.getInstance( "X.509" );
      _cert = (X509Certificate) cf.generateCertificate( is );
    }
    catch( IOException e )
    {
      throw new PluginVerifyException( "Unable to load verification certificate", e );
    }
    catch( CertificateException e )
    {
      throw new PluginVerifyException( "Unable to create verification certificate", e );
    }

  }

  private void CopyStream( InputStream is, OutputStream os )
  {
    byte[] b = new byte[1024];
    int noOfBytes = 0;

    try
    {
      while( ( noOfBytes = is.read( b ) ) != -1 )
      {
        os.write( b, 0, noOfBytes );
      }
      is.close();
      os.close();
    }
    catch( IOException e )
    {
    }
  }

  /**
   * Loads a plugin
   * @param jarName name of the JAR file that contains the pluign to load
   * @param className name of the class in the JAR file that implements IPlugin
   * @return an IPlugin instance if the plugin is sucessfully loaded
   * @throws PluginLoadException if the plugin cannot be loaded
   * @throws PluginVerifyException if the plugin verification does not pass (incorrectly signed or cryptographic failure)
   * @throws PluginCreateException if the plugin class could not be created (incorrect name or other error)
   */
  public IPlugin loadPlugin( String jarName, String className ) throws PluginLoadException, PluginVerifyException, PluginCreateException
  {
    try
    {
      File dexCache = new AppDataDirGuesser().guess();
      File result = File.createTempFile( "Generated", ".jar", dexCache );
      result.deleteOnExit();

      InputStream is = _context.getAssets().open( jarName );
      FileOutputStream fos = new FileOutputStream( result );

      CopyStream( is, fos );
      JarFile jf = new JarFile( result );
      JarVerifier.verify( jf, new X509Certificate[] { _cert } );

      ClassLoader cl = (ClassLoader) Class.forName( "dalvik.system.DexClassLoader" )
          .getConstructor( String.class, String.class, String.class, ClassLoader.class )
          .newInstance( result.getPath(), dexCache.getAbsolutePath(), null, _parentLoader );

      Class< ? > pluginClass = cl.loadClass( className );
      IPlugin plugin = (IPlugin) pluginClass.newInstance();
      return( plugin );

    }
    catch( IOException e )
    {
      throw new PluginLoadException( String.format( "Failed to load plugin: %s", jarName ), e );
    }
    catch( CertificateException e )
    {
      throw new PluginVerifyException( String.format( "Certificate issue while verifying plugin: %s", jarName ), e );
    }
    catch( SecurityException e )
    {
      throw new PluginVerifyException( String.format( "Invalid JAR while verifying plugin: %s", jarName ), e );
    }
    catch( ClassNotFoundException e )
    {
      throw new PluginCreateException( String.format( "Cannot find class %s in %s", className, jarName ), e );
    }
    catch( InstantiationException e )
    {
      throw new PluginCreateException( String.format( "Cannot instantiate class %s in %s", className, jarName ), e );
    }
    catch( IllegalArgumentException e )
    {
      throw new PluginCreateException( String.format( "Cannot instantiate class %s in %s", className, jarName ), e );
    }
    catch( IllegalAccessException e )
    {
      throw new PluginCreateException( String.format( "Cannot instantiate class %s in %s", className, jarName ), e );
    }
    catch( InvocationTargetException e )
    {
      throw new PluginCreateException( String.format( "Cannot instantiate class %s in %s", className, jarName ), e );
    }
    catch( NoSuchMethodException e )
    {
      throw new PluginCreateException( String.format( "Cannot find method in class %s in %s", className, jarName ), e );
    }
  }
}
