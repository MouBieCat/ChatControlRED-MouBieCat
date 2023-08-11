package net.moubiecat.chatcontrol;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class InjectRegistration {
    public static Injector INJECTOR = null;

    private final Map<Class, Object> instances = new ConcurrentHashMap<>();
    private final Map<Class, Class<? extends Provider>> providers = new ConcurrentHashMap<>();

    /**
     * 註冊插件
     *
     * @param cls      類別
     * @param instance 實例
     * @param <T>      類別
     */
    public <T extends Plugin> void bindPluginInstance(@NotNull Class<? extends Plugin> cls, @NotNull T instance) {
        this.instances.putIfAbsent(cls, instance);
    }

    /**
     * 註冊實例
     *
     * @param cls      類別
     * @param instance 實例
     * @param <T>      類別
     */
    public <T> void bindInstance(@NotNull Class<T> cls, @NotNull T instance) {
        this.instances.putIfAbsent(cls, instance);
    }

    /**
     * 綁定提供器
     *
     * @param service  服務
     * @param provider 提供器
     * @param <T>      類別
     * @param <P>      提供器
     */
    public <T, P extends Provider<T>> void addProvider(@NotNull Class<T> service, @NotNull Class<P> provider) {
        this.providers.put(service, provider);
    }

    /**
     * 綁定注入器
     */
    public void bindInjector() {
        INJECTOR = Guice.createInjector(binder -> {
            // 綁定實例
            instances.forEach((cls, ins) -> {
                if (ins instanceof Plugin plugin)
                    binder.bind(Plugin.class).annotatedWith(Names.named(plugin.getName())).toInstance(plugin);

                binder.bind(cls).toInstance(ins);
            });
            // 綁定提供器
            this.providers.forEach((service, provider) -> binder.bind(service).toProvider(provider));
        });
    }
}
