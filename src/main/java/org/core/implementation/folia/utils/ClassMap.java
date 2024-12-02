package org.core.implementation.folia.utils;

import org.core.utils.lamda.bi.ThrowableBiFunction;
import org.core.utils.lamda.single.ThrowableSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ClassMap<O, M, T extends Throwable> {

    private final Map<Class<? extends O>, Class<? extends M>> mainMapping = new ConcurrentHashMap<>();
    private final Map<String, Class<? extends M>> cachedMapping = new ConcurrentHashMap<>();
    private final @NotNull ThrowableBiFunction<O, Class<? extends M>, M, T> toInstance;

    public ClassMap(@NotNull ThrowableBiFunction<O, Class<? extends M>, M, T> function) {
        Objects.requireNonNull(function);
        this.toInstance = function;
    }

    public synchronized void register(@NotNull Class<? extends O> originalClass, @NotNull Class<? extends M> toClass) {
        this.mainMapping.put(originalClass, toClass);
        this.cachedMapping.put(originalClass.getName(), toClass);
    }

    public void register(@NotNull Iterable<Map.Entry<Class<? extends O>, Class<? extends M>>> map) {
        for (Map.Entry<Class<? extends O>, Class<? extends M>> entry : map) {
            this.register(entry.getKey(), entry.getValue());
        }
    }

    public void register(@NotNull Map<Class<? extends O>, Class<? extends M>> map) {
        this.register(map.entrySet());
    }

    public M map(@NotNull O instance) throws T, NoSuchElementException {
        return this.map(instance, () -> {
            throw new NoSuchElementException("Could not find a mapping for " + instance.getClass().getName());
        });
    }

    public <SupplierThrow extends Throwable> M map(@NotNull O instance,
                                                   @NotNull ThrowableSupplier<? extends M, SupplierThrow> orElse)
            throws T, SupplierThrow {
        Class<? extends M> mappedClass = this.cachedMapping.get(instance.getClass().getName());
        if (mappedClass != null) {
            return this.toInstance.map(instance, mappedClass);
        }
        Optional<Class<? extends M>> opMappedClass = this.mainMapping
                .entrySet()
                .parallelStream()
                .filter(clazz -> clazz.getKey().isInstance(instance))
                .findAny()
                .map(Map.Entry::getValue);
        if (opMappedClass.isEmpty()) {
            return orElse.run();
        }
        mappedClass = opMappedClass.get();
        this.cachedMapping.put(instance.getClass().getName(), mappedClass);
        return this.toInstance.map(instance, mappedClass);
    }

}
