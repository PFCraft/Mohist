package org.bukkit.plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;

public class EventExecutor1 implements EventExecutor {
    private Method method;
    private Class<? extends Event> eventClass;

    public EventExecutor1(Method method, Class<? extends Event> eventClass) {
        this.method = method;
        this.eventClass = eventClass;
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        try {
            if (!eventClass.isAssignableFrom(event.getClass())) {
                return;
            }
            // Spigot start
            method.invoke(listener, event);
            // Spigot end
        } catch (InvocationTargetException ex) {
            throw new EventException(ex.getCause());
        } catch (Throwable t) {
            throw new EventException(t);
        }
    }
}
