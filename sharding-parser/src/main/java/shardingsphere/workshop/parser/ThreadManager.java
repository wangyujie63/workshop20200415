package shardingsphere.workshop.parser;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author wangyujie63
 * 创建基于内核数的线程池
 * 用于异步处理任务
 */
@Slf4j
public class ThreadManager {

    private static volatile ThreadPoolExecutor mThreadPoolExecutor;
    private static ThreadPoolExecutor initExecutor() {
        if(mThreadPoolExecutor == null) {
            synchronized(ThreadManager.class) {
                if(mThreadPoolExecutor == null) {
                    log.info("create thread ,the ThreadName is 【"+ Thread.currentThread().getName()+"】");
                    TimeUnit unit =  TimeUnit.MILLISECONDS;
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
                    LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
                    int corePoolSize = Runtime.getRuntime().availableProcessors()*2;
                    log.info("核心线程数量corePoolSize="+corePoolSize);
                    int maximumPoolSize = corePoolSize + 1;
                    long keepAliveTime = Integer.MAX_VALUE;
                    mThreadPoolExecutor = new ThreadPoolExecutor(
                            corePoolSize,
                            maximumPoolSize,
                            keepAliveTime,
                            unit,
                            workQueue,
                            threadFactory,
                            handler);
                }
            }
        }
        return mThreadPoolExecutor;
    }

    /**执行任务*/
    public static void executeTask(Runnable r) {
        initExecutor();
        mThreadPoolExecutor.execute(r);
    }

    /**执行有返回值任务*/
    public static void executeFutureTask(FutureTask r) {
        initExecutor();
        mThreadPoolExecutor.execute(r);
    }

}

