import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        try {
            System.out.println("=== Plugin Discovery System Test ===\n");
            
            // Load all TextTransformer plugins
            ArrayList<Object> plugins = PluginDiscovery.loadServices(TextTransformer.class);
            
            System.out.println("Found " + plugins.size() + " plugins:\n");
            
            String testText = "Hello World! This is a test.";
            System.out.println("Original text: " + testText + "\n");
            
            // Test each plugin
            for (Object plugin : plugins) {
                TextTransformer transformer = (TextTransformer) plugin;
                System.out.println("Plugin: " + transformer.getName());
                System.out.println("Result: " + transformer.transform(testText));
                System.out.println();
            }
            
        } catch (Exception e) {
            System.err.println("Error loading plugins: " + e.getMessage());
            e.printStackTrace();
        }
    }
}