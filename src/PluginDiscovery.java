import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarFile;

public class PluginDiscovery
{
    private static final URLClassLoader theURLClassLoader;
    private static final File theMainFile;

    static {
    theMainFile = new File("G:/Projects/Plugin System with Service Loaders/services");
    
    // Scan ALL subdirectories for JAR files
    ArrayList<URL> urlList = new ArrayList<>();
    scanForJars(theMainFile, urlList);
    
    URL[] urls = urlList.toArray(new URL[0]);
    theURLClassLoader = new URLClassLoader(urls, App.class.getClassLoader());

    for(URL url : urls) {
    System.out.println("URLClassLoader has: " + url);
}
}

private static void scanForJars(File directory, ArrayList<URL> urlList) {
    File[] files = directory.listFiles();
    if (files != null) {
        for (File file : files) {
            if (file.isDirectory()) {
                scanForJars(file, urlList); // Recursive scan
            } else if (file.getName().endsWith(".jar")) {
                try {
                    urlList.add(file.toURI().toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

    public static ArrayList<Object> loadServices(Class<?> theInterface) throws InstantiationException, IllegalAccessException
    {
        ArrayList<Object> theServices = new ArrayList<Object>();
        if(!theInterface.isInterface())
            throw new IllegalArgumentException(theInterface.getName() + " is not an interface");

        File theFile = new File(theMainFile.getPath() + "/" + theInterface.getName()+"Services");

        String[] files = theFile.list((dir, name) -> name.endsWith(".jar"));
        if(files == null)
        {
            return theServices;
        }
        for(int count = 0; count < files.length; count++)
        {
            try(JarFile theJarFile = new JarFile(theFile.getPath() + "/" + files[count])) 
            {
                theJarFile.stream().filter((entry)->
                {
                    return entry.getName().endsWith(".class");
                })
                .forEach((entry)->
                {
                    String entryName = entry.getName();
                    entryName = entryName.substring(0, entryName.length() - 6);
                    entryName = entryName.replace('/', '.');
                    try 
                    {
                        Class<?> theClass = theURLClassLoader.loadClass(entryName);
                        for(Class<?> theEntryInterface : theClass.getInterfaces())
                        {
                            if(theEntryInterface.getName().equals(theInterface.getName()))
                            {
                                theServices.add(theClass.newInstance());
                            }
                        }
                    } 
                    catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) 
                    {
                        throw new RuntimeException(e);
                    }
                });
                
            } 
            catch (IOException e) 
            {
                throw new RuntimeException(e);
            }
        }
        return theServices;
    }
}