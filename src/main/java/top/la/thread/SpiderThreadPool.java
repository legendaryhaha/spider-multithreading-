package top.la.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.la.service.IRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/***
 *
 * @Author:fsn
 * @Date: 2020/4/11 16:01
 * @Description
 */

public class SpiderThreadPool {
    private Logger log = LoggerFactory.getLogger(SpiderThreadPool.class);

    private final int dataLength = 1000;
    private final int queueNumber = 10;
    private final int threadNumber = 10;


    private Executor executor = Executors.newFixedThreadPool(threadNumber);
    private List<ArrayBlockingQueue<IRequest>> queue = new ArrayList<>();

    private SpiderThreadPool() {
        init();
    }

    private void init() {
        for (int index = 0; index < queueNumber; index++) {
            ArrayBlockingQueue<IRequest> blockingQueue = new ArrayBlockingQueue<>(dataLength);
            this.queue.add(blockingQueue);
            this.executor.execute(new SpiderThread(index, blockingQueue));
        }
    }

    /**
     * 这里可以自行扩展, 根据需要, 可以设置每个队列存放不同的任务,
     * 在添加的时候指定对应的{key}队列进行存放, 默认随机
     * @param key 队列的下标
     * @param request
     */
    public void add(String key, IRequest request) {
        int index;
        if (key != null && " ".equals(key)) {
            index = Integer.valueOf(key);
        } else {
            int hashcode;
            int hash = (key == null) ? 0 : (hashcode = key.hashCode()) ^ (hashcode >>> 16);
            // 对hashcode取摸
            index = (this.getSize() - 1) & hash;
        }
        ArrayBlockingQueue<IRequest> tmpQueue = this.getQueue(index);
        try {
            if (tmpQueue.size() < dataLength) {
                tmpQueue.offer(request);
            } else {
                for (ArrayBlockingQueue<IRequest> item : this.queue) {
                    if (item.size() < dataLength) {
                        tmpQueue.offer(request);
                        return;
                    }
                }
                log.error("队列已满太满，无法装入更多请求");
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    private ArrayBlockingQueue<IRequest> getQueue(int index) {
        return this.queue.get(index);
    }

    private int getSize() {
        return this.queue.size();
    }

    public static SpiderThreadPool getSingle() {
        return Single.spiderThreadPool;
    }

    static class Single {
        private static SpiderThreadPool spiderThreadPool;

        static {
            spiderThreadPool = new SpiderThreadPool();
        }
    }

    private class SpiderThread implements Runnable {
        private String name;
        private ArrayBlockingQueue<IRequest> queue;
        private long time = 200;

        public SpiderThread(int index, ArrayBlockingQueue<IRequest> blockingQueue) {
            this.name = String.valueOf(index);
            this.queue = blockingQueue;
        }

        @Override
        public void run() {
           while (true) {
               try {
                   if (queue == null || queue.isEmpty()) {
                       return;
                   }
                   IRequest request = this.queue.take();
                   request.sync();
                   log.info("线程SpiderThread${} 当前队列中有 {} 请求", name, queue.size());
                   Thread.sleep(time);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        }
    }

}
