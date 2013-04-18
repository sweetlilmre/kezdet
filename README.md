Kezdet
======

Kezdet is a plug-in system for dynamic code loading on Android.
Plug-ins are built as signed jar files that implement a specific interface and are dynamically loaded into the application.
This allows a developer to add additional functionality to an Android application post deployment / submission to the Play Store.

Originally Kezdet was developed to extend the External Interface system in Adobe AIR.
This system whilst allowing access to native functionality was limited in that any additions would require a new APK build and re-submission to the Play Store.
As AIR itself is able to dynamically load content this resulted in an asymmetric situation and so Kezdet was born to address this.

Kezdet now features a nifty ANT based build system as developed by the inimitable BigShow (thanks Scotty!).
For a new clone, calling "built.bat" or "ant" should do it.

Building Kezdet:

Please ensure that you have ANT version 1.8.4+ and the latest Android SDK installed (21.1 for tools and 16.0.2  for platform tools as of this).
Check local.properties.dist for an example of how to set up your local.properties file.
The minimum information required is the path to the AIR SDK via the FLEX_HOME property and the path to Android SDK via ANDROID_HOME property or environment variable.

About:

Kezdet is the Hungarian word for inception (and in honour of TamasS): we have to go deeper :)
