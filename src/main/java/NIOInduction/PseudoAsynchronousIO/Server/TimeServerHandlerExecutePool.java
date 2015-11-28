package NIOInduction.PseudoAsynchronousIO.Server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA 14.
 * User: karl.zhao
 * Time: 2015/11/23 0023.
 */
public class TimeServerHandlerExecutePool {

    // Thread Pool
    private ExecutorService executor;

    public TimeServerHandlerExecutePool(int maxPoolSize,int queueSize){
        executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(), /* //获取当前系统的CPU 数目 */
                maxPoolSize,
                120L,             // 心跳时间频率
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void exceute(Runnable task){
        executor.execute(task);
    }
}
