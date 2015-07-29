package at.yawk.fiction.impl;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.SneakyThrows;

/**
 * @author yawkat
 */
public class PageParserProvider {
    private final Map<Class<?>, PageParser<?>> parsers = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <P extends PageParser<?>> P getParser(Class<P> type) {
        P parser;
        lock.readLock().lock();
        try {
            parser = (P) parsers.get(type);
        } finally {
            lock.readLock().unlock();
        }
        if (parser == null) {
            Constructor<P> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            parser = constructor.newInstance();
            parser.provider = this;
            lock.writeLock().lock();
            try {
                PageParser<?> oldParser = parsers.get(type);
                if (oldParser != null) {
                    parser = (P) oldParser;
                } else {
                    parsers.put(type, parser);
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        return parser;
    }
}
