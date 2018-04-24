# Warning
You may notice that when you launch the application, the following warning information will prompt in the console:

```Mar 04, 2018 9:37:13 AM java.util.prefs.WindowsPreferences <init>
WARNING: Could not open/create prefs root node Software\JavaSoft\Prefs at root 0x80000002. Windows RegCreateKeyEx(...) returned error code 5.```

I digged up and found that this is a JDK bug. According to the resources, this bug may disappear if you have a higher version of JDK. However, it doesn't affect
the deployment of our simulator.
To see more information about this issue, go [here](https://stackoverflow.com/questions/5354838/java-java-util-preferences-failing).


# Toolkit Legal Note
toolkit used
- JavaFX & JavaSceneBuilder 2.0
[Site](http://www.oracle.com/technetwork/java/javase/downloads/javafxscenebuilder-info-2157684.html)

- XML