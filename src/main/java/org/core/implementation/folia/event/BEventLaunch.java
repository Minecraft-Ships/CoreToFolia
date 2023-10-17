package org.core.implementation.folia.event;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.core.TranslateCore;
import org.core.event.Event;
import org.core.event.EventListener;
import org.core.platform.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BEventLaunch {

    protected final Plugin plugin;
    protected final EventListener listener;
    protected final Method method;
    protected long beforeTime;
    protected long afterTime;

    public BEventLaunch(Plugin plugin, EventListener listener, Method method) {
        this.plugin = plugin;
        this.listener = listener;
        this.method = method;
    }

    public long getBeforeTime() {
        return this.beforeTime;
    }

    public long getAfterTime() {
        return this.afterTime;
    }

    public long getTimeTaken() {
        return this.afterTime - this.beforeTime;
    }

    public void run(Event event) {
        this.beforeTime = System.currentTimeMillis();
        try {
            this.method.invoke(this.listener, event);
            this.afterTime = System.currentTimeMillis();
        } catch (IllegalAccessException e) {
            this.afterTime = System.currentTimeMillis();
            String message = "Failed to know what to do:" + "HEvent found on method, but method is not public at "
                    + this.listener.getClass().getName() + "." + this.method.getName() + "(" + Stream
                    .of(this.method.getParameters())
                    .map(p -> p.getType().getSimpleName() + " " + p.getName())
                    .collect(Collectors.joining(", ")) + ")";


            TranslateCore.getConsole().sendMessage(Component.text(message).color(TextColor.color(255, 0, 0)));
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            this.afterTime = System.currentTimeMillis();
            String message = "Failed to know what to do:" + "EventListener caused exception from " + this.listener
                    .getClass()
                    .getName() + "." + this.method.getName() + "(" + Stream
                    .of(this.method.getParameters())
                    .map(p -> p.getType().getSimpleName() + " " + p.getName())
                    .collect(Collectors.joining(", ")) + ")";


            TranslateCore.getConsole().sendMessage(Component.text(message).color(TextColor.color(255, 0, 0)));
            e.getTargetException().printStackTrace();
        } catch (Throwable e) {
            this.afterTime = System.currentTimeMillis();
            String message = "Failed to know what to do:" + "EventListener caused exception from " + this.listener
                    .getClass()
                    .getName() + "." + this.method.getName() + "(" + Stream
                    .of(this.method.getParameters())
                    .map(p -> p.getType().getSimpleName() + " " + p.getName())
                    .collect(Collectors.joining(", ")) + ")";


            TranslateCore.getConsole().sendMessage(Component.text(message).color(TextColor.color(255, 0, 0)));
            e.printStackTrace();
        }
    }
}
