package net.moubiecat.chatcontrol;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class InjectRegistration {
    public static Injector INJECTOR = null;

    private final Map<Class, Object> instance = new HashMap<>();
    private final Map<Class, Map<Named, Object>> namedInstance = new HashMap<>();
    private final Map<Class, Class> implementation = new HashMap<>();

    /**
     * 註冊實例注入器
     *
     * @param clazz    類別
     * @param instance 實例
     * @param <T>      類別
     */
    public <T> void register(@NotNull Class<T> clazz, @NotNull T instance) {
        this.instance.put(clazz, instance);
    }

    /**
     * 註冊實例注入器
     *
     * @param clazz    類別
     * @param named    名稱
     * @param instance 實例
     * @param <T>      類別
     */
    public <T> void register(@NotNull Class<T> clazz, @NotNull Named named, @NotNull T instance) {
        this.namedInstance.computeIfAbsent(clazz, k -> new HashMap<>()).put(named, instance);
    }

    /**
     * 註冊實例注入器
     *
     * @param clazz          類別
     * @param implementation 實現類別
     * @param <T>            類別
     */
    public <T> void register(@NotNull Class<T> clazz, @NotNull Class<? extends T> implementation) {
        this.implementation.put(clazz, implementation);
    }

    /**
     * 綁定注入器
     */
    public void bindInjector() {
        final Injector instanceInjector = Guice.createInjector(
                binder -> this.instance.forEach((clazz, plugin) -> binder.bind(clazz).toInstance(plugin)));
        final Injector namedInstanceInjector = instanceInjector.createChildInjector(
                binder -> this.namedInstance.forEach((clazz, map) -> map.forEach((named, plugin) -> binder.bind(clazz).annotatedWith(named).toInstance(plugin))));
        INJECTOR = namedInstanceInjector.createChildInjector(
                binder -> this.implementation.forEach((clazz, implementation) -> binder.bind(clazz).to(implementation)));
    }
}
