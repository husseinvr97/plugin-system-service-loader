# Plugin Discovery System

A lightweight, custom-built plugin discovery and loading framework for Java applications. This system enables dynamic plugin architecture through runtime class loading and reflection, allowing applications to be extended without modifying core code.

## 🎯 Overview

This project implements a Service Provider Interface (SPI)-like mechanism from scratch, demonstrating deep understanding of Java's ClassLoader architecture, reflection API, and plugin-based design patterns.

## ✨ Features

- **Dynamic Plugin Discovery**: Automatically discovers and loads plugins from JAR files at runtime
- **Interface-Based Architecture**: Plugins implement a common interface for type-safe operations
- **Flexible Plugin Organization**: Supports service-specific directory structure
- **Zero Configuration**: No XML or annotation processing required
- **Custom ClassLoader Implementation**: Handles multiple JAR files with proper class isolation
- **Reflection-Based Validation**: Automatically verifies plugin compatibility

## 🏗️ Architecture

### Core Components

1. **PluginDiscovery**: Main class responsible for:
   - Scanning plugin directories for JAR files
   - Loading classes using custom URLClassLoader
   - Validating interface implementations
   - Instantiating and registering plugins

2. **Service Interface**: Defines the contract plugins must implement
   - Example: `TextTransformer` interface with `transform()` and `getName()` methods

3. **Plugin Implementations**: Concrete classes packaged as separate JARs
   - Each plugin is independently deployable
   - No compile-time dependencies between plugins

### Directory Structure

```
project-root/
├── src/
│   ├── PluginDiscovery.java
│   ├── TextTransformer.java (interface)
│   └── App.java
└── services/
    └── TextTransformerServices/
        ├── UppercaseTransformer.jar
        ├── ReverseTransformer.jar
        ├── EncryptTransformer.jar
        └── CompressTransformer.jar
```

### Convention

For an interface named `X`, plugins should be placed in:
```
services/XServices/*.jar
```

## 🚀 Quick Start

### 1. Define Your Service Interface

```java
public interface TextTransformer {
    String transform(String input);
    String getName();
}
```

### 2. Create Plugin Implementations

```java
public class UppercaseTransformer implements TextTransformer {
    @Override
    public String transform(String input) {
        return input.toUpperCase();
    }
    
    @Override
    public String getName() {
        return "Uppercase Transformer";
    }
}
```

### 3. Package as JAR

```bash
javac -cp . UppercaseTransformer.java
jar cf UppercaseTransformer.jar UppercaseTransformer.class
```

### 4. Place in Services Directory

```
services/TextTransformerServices/UppercaseTransformer.jar
```

### 5. Load and Use Plugins

```java
ArrayList<Object> plugins = PluginDiscovery.loadServices(TextTransformer.class);

for (Object plugin : plugins) {
    TextTransformer transformer = (TextTransformer) plugin;
    System.out.println(transformer.getName());
    System.out.println(transformer.transform("Hello World"));
}
```

## 📦 Example Plugins Included

- **UppercaseTransformer**: Converts text to uppercase
- **ReverseTransformer**: Reverses text character order
- **EncryptTransformer**: Simple Caesar cipher encryption
- **CompressTransformer**: Removes duplicate characters and extra spaces

## 🔧 How It Works

### Plugin Discovery Process

1. **JAR Scanning**: Recursively scans the services directory for `.jar` files
2. **ClassLoader Setup**: Creates URLClassLoader with all discovered JARs
3. **Class Extraction**: Reads each JAR to enumerate `.class` files
4. **Path Conversion**: Converts file paths to fully qualified class names
5. **Class Loading**: Dynamically loads each class using the URLClassLoader
6. **Interface Validation**: Uses reflection to verify interface implementation
7. **Instantiation**: Creates instances of valid plugin classes
8. **Registration**: Returns collection of loaded plugins

### Key Technical Decisions

- **Custom URLClassLoader**: Ensures plugins can reference the service interface
- **Parent-First Delegation**: Interface loaded by parent classloader prevents ClassCastException
- **Recursive JAR Discovery**: Supports nested directory structures
- **Stream-Based Processing**: Efficient JAR entry iteration
- **Reflection API**: Runtime type checking without compile-time dependencies

## 🔮 Future Enhancements

Potential improvements to consider:

- [ ] Plugin metadata and versioning support
- [ ] Dependency resolution between plugins
- [ ] Hot-reload capabilities
- [ ] Plugin lifecycle management (init/start/stop/destroy)
- [ ] Configuration file support for plugins
- [ ] Plugin security and sandboxing
- [ ] Performance metrics and monitoring
- [ ] CLI for plugin management
- [ ] Generic interface support (not just specific interfaces)
- [ ] Plugin repository/marketplace concept

**Note**: This is a custom implementation built for learning purposes. For production systems, consider using Java's built-in `ServiceLoader` API or established frameworks like OSGi or Java Platform Module System (JPMS).