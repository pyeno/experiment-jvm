package io.github.jojoti.utildaemonthreads;

import java.util.concurrent.CountDownLatch;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public abstract class DaemonThreads {

    private final CountDownLatch latch;
    private final Errors errors;
    private final int size;

    private DaemonThreads(int size, String daemonName, Errors errors) {
        this.size = size;
        this.latch = new CountDownLatch(size);
        this.errors = errors;
        // 添加守护线程
        Thread awaitThread = new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                errors.onError("CountDown await", e);
            }
        });
        awaitThread.setName(daemonName);
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    public static DaemonThreads newDaemonThreads(int size, String daemonName, Errors errors) {
        return new DaemonThreads(size, daemonName, errors) {
        };
    }

    public DaemonThreads startThreads(String eventName, Handler runnable) throws Exception {
        try {
            runnable.handle();
        } catch (Exception e) {
            // 启动 任务失败 则关闭所有的 latch 关闭所有 countDown
            for (long i = 0; i < this.latch.getCount(); i++) {
                try {
                    this.latch.countDown();
                } catch (Exception ex) {
                    this.errors.onError(eventName, ex);
                }
            }
            throw e;
        }
        return this;
    }

    public DaemonThreads startThreads(Handler runnable) throws Exception {
        return startThreads("On stop all", runnable);
    }

    public DaemonThreads downThreads(String eventName, Handler handler) {
        try {
            handler.handle();
        } catch (Exception e) {
            this.errors.onError(eventName, e);
        } finally {
            try {
                this.latch.countDown();
            } catch (Exception e) {
                this.errors.onError(eventName, e);
            }
        }
        return this;
    }

    public DaemonThreads downThreads(Handler handler) {
        return downThreads("Down", handler);
    }

    public boolean isRunning() {
        return this.latch.getCount() > 0;
    }

    public boolean isHealth() {
        return this.latch.getCount() == size;
    }

}
