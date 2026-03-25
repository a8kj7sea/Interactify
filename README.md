# Interactify <sup><sub>[![](https://jitpack.io/v/a8kj7sea/Interactify.svg)](https://jitpack.io/#a8kj7sea/Interactify)</sub></sup>

A robust, high-performance **asynchronous input middleware** for Minecraft environments. Interactify abstracts the complexities of platform-specific chat handling, providing a unified, fluent API for **Bukkit**, **Bungeecord**, and **Velocity**.

-----

## 🏗 System Architecture

Interactify operates on a **Provider-Based Architecture**, decoupling the core logic from the underlying server software. This ensures that your input flows remain consistent regardless of whether you are developing for a proxy or a game server.

### Core Components

  * **InputContext:** A stateful container holding player data, logic, and lifecycle settings.
  * **PlatformProvider:** An abstraction layer that handles message rendering and task scheduling.
  * **InputValidator:** A functional interface for real-time data integrity checks.

-----

## 🔌 Required Platform Plugins

To utilize the framework, you **must** install the corresponding platform-specific implementation on your server or proxy. These plugins act as the **Platform Provider**, bridging the API with the native server environment.

| Platform | Required Plugin | Purpose |
| :--- | :--- | :--- |
| **Bukkit / Spigot** | `Interactify-Bukkit.jar` | Handles events and task scheduling on game servers. |
| **Bungeecord** | `Interactify-Bungeecord.jar` | Manages chat input across the Bungeecord proxy. |
| **Velocity** | `Interactify-Velocity.jar` | Provides modern Adventure API support for Velocity proxies. |

> [\!IMPORTANT]
> **Design Philosophy: Centralized Implementation**
> The `Interactify-API` is a library and does not function standalone. Ensure the appropriate implementation jar is present in your `/plugins/` folder to enable the system.
>
> The core intent behind a centralized plugin is to prevent unnecessary memory overhead. Instead of every plugin shading and loading its own copy of the library, a single central plugin is responsible for handling all input sessions. This avoids duplicating classes in memory and creating multiple instances of the same system across different plugins-preventing redundant memory usage and potential conflicts.
>
> *Note: You are still free to use shading if you prefer. However, if you do, please review how listener registration and platform providers are handled to avoid operational issues.*

-----

## 🚀 Implementation Guide

### 1\. Velocity (High-Performance Proxy)

Leverage the native **Adventure API** for modern, component-based messaging.

```java
InputContext<Player> context = new InputContextBuilder<>(player, (p, input) -> {
    p.sendMessage(Component.text("Input Received: " + input, NamedTextColor.AQUA));
})
.name("velocity-auth-flow")
.prompt(Component.text("Please enter your verification code:", NamedTextColor.YELLOW))
.build();

InteractifyAPI.<Player>getInstance().registerContext(context);
```

### 2\. Bungeecord (Legacy Proxy)

Maintain compatibility with `TextComponent` while implementing strict validation logic.

```java
InputContext<ProxiedPlayer> context = new InputContextBuilder<>(player, (p, input) -> {
    ProxyServer.getInstance().broadcast(new TextComponent("§6[Broadcast] §f" + input));
})
.cancelKeyword("abort")
.validator((p, input) -> {
    if (input.length() < 10) {
        return InputValidationResult.fail(new StringRenderable("err", "§cMinimum 10 characters required."));
    }
    return InputValidationResult.ok();
})
.timeout(60, "§cSession expired due to inactivity.")
.build();

InteractifyAPI.<ProxiedPlayer>getInstance().registerContext(context);
```

### 3\. Bukkit / Spigot (Game Server)

Ideal for functional transactions, such as currency transfers or plot naming.

```java
InputContext<Player> context = new InputContextBuilder<>(sender, (p, input) -> {
    double amount = Double.parseDouble(input);
    Economy.deposit(receiver, amount);
    p.sendMessage("§aTransaction Successful.");
})
.validator((p, input) -> {
    try {
        double val = Double.parseDouble(input);
        return val > 0 ? InputValidationResult.ok() : InputValidationResult.fail(new StringRenderable("err", "Positive values only."));
    } catch (NumberFormatException e) {
        return InputValidationResult.fail(new StringRenderable("err", "Invalid numerical format."));
    }
})
.build();

InteractifyAPI.<Player>getInstance().registerContext(context);
```

-----

## 📦 Technical Specifications

  * **Requirements:** Java 21+ Runtime.
  * **Modularity:** Designed with **Clean Architecture** principles; optimized for minimal memory footprint and zero-allocation routing capabilities.
  * **Concurrency:** Thread-safe context registration and automated task cleanup.
