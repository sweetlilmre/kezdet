del /q *.ane
unzip.exe -o ..\bin\KezdetAIRHost.swc
del /q catalog.xml
rmdir /S /Q final
mkdir final\android
mkdir final\default
copy extension.xml final
copy ..\bin\KezdetAIRHost.swc final
copy library.swf final\android
copy library.swf final\default
copy ..\..\KezdetANEHost\bin\KezdetANEHost.jar final\android\KezdetANEHost.jar
call "C:\FlexSDK\air3.4\bin\adt" -package -storetype pkcs12 -keystore signer.p12 -storepass test -target ane KezdetANEHost.ane final\extension.xml -swc final\KezdetAIRHost.swc -platform Android-ARM -C final\android . -platform default -C final\default .
