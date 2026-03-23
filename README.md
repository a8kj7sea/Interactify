# Interactify <sup><sub> [![](https://jitpack.io/v/a8kj7sea/Interactify.svg)](https://jitpack.io/#a8kj7sea/Interactify)</sup></sub> 

A robust, high-performance **asynchronous input middleware** for Minecraft environments. Interactify abstracts the complexities of platform-specific chat handling, providing a unified, fluent API for **Bukkit**, **Bungeecord**, and **Velocity**.

---

## 🏗 System Architecture

Interactify operates on a **Provider-Based Architecture**, decoupling the core logic from the underlying server software. This ensures that your input flows remain consistent regardless of whether you are developing for a proxy or a game server.

### Core Components
* **InputContext:** A stateful container holding player data, logic, and lifecycle settings.
* **PlatformProvider:** An abstraction layer that handles message rendering and task scheduling.
* **InputValidator:** A functional interface for real-time data integrity checks.

---

## 🚀 Implementation Guide

### 1. Velocity (High-Performance Proxy)
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

### 2. Bungeecord (Legacy Proxy)
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

### 3. Bukkit/Spigot (Game Server)
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

---

## 📦 Technical Specifications

* **Requirements:** Java 21+ Runtime.
* **Modularity:** Designed with **Clean Architecture** principles; zero-allocation routing and minimal memory footprint.
* **Concurrency:** Thread-safe context registration and automated task cleanup.
