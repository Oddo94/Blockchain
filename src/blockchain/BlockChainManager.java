package blockchain;
import lombok.Getter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Getter
public class BlockChainManager {
    private int maxSize;
    private BlockChain blockChain;
    private int numberOfThreads;

    public BlockChainManager(int blockChainSize, int numberOfThreads) {
        if(blockChainSize <= 0) {
            this.maxSize = 1;
        } else {
            this.maxSize = blockChainSize;
        }

        int availableThreads = Runtime.getRuntime().availableProcessors();

        this.blockChain = new BlockChain();
        this.numberOfThreads = numberOfThreads > availableThreads ? availableThreads : numberOfThreads;
    }

    public void manageBlockChain() {
        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfThreads);
        IntStream.range(1, numberOfThreads + 1)
                .mapToObj(i -> new BlockGenerator(blockChain, maxSize))
                .forEach(threadPool::submit);

        threadPool.shutdown();

        try {
            threadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch(InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}
