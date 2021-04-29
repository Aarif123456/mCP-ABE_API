package it.unisa.dia.gas.plaf.jpbc.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ExecutorServiceUtils {

    private static final ExecutorService fixedThreadPool;
    private static final ExecutorService cachedThreadPool;

    static {
        fixedThreadPool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 4
        );
        cachedThreadPool = Executors.newCachedThreadPool();
    }


    private ExecutorServiceUtils() {
    }


    public static ExecutorService getFixedThreadPool() {
        return fixedThreadPool;
    }

    public static ExecutorService getCachedThreadPool() {
        return cachedThreadPool;
    }

    public static void shutdown() {
        fixedThreadPool.shutdown();
        cachedThreadPool.shutdown();
    }


    /**
     * @author Angelo De Caro (jpbclib@gmail.com)
     * @since 2.0.0
     */
    public abstract static class IndexCallable<T> implements Callable<T> {
        protected final int i;
        protected int j;

        public IndexCallable(int i) {
            this.i = i;
        }

        public IndexCallable(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    /**
     * @author Angelo De Caro (jpbclib@gmail.com)
     * @since 1.2.2
     */
    public abstract static class IndexRunnable implements Runnable {
        protected final int i;
        protected int j;

        public IndexRunnable(int i) {
            this.i = i;
        }

        public IndexRunnable(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    /**
     * @author Angelo De Caro (jpbclib@gmail.com)
     * @since 2.0.0
     */
    public abstract static class IntervalCallable<T> implements Callable<T> {
        protected final int from;
        protected final int to;

        protected IntervalCallable(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

}
