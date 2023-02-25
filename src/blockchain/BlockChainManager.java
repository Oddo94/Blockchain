package blockchain;
import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Getter
public class BlockChainManager {
    private int maxSize;
    private BlockChain blockChain;
    private int numberOfThreads;

    public BlockChainManager(int blockChainSize, int numberOfThreads) {
        if(blockChainSize <= 0) {
            this.maxSize = 1;
        }

        this.maxSize = blockChainSize;
        this.blockChain = new BlockChain();
        this.numberOfThreads = numberOfThreads;
    }

    public void manageBlockChain() {
        int currentBlockChainSize = blockChain.getSize();
        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfThreads);
        for(int i = 0; i < numberOfThreads; i++) {
            BlockGenerator blockGenerator = new BlockGenerator(blockChain, maxSize);
            threadPool.execute(blockGenerator);
        }
        blockChain.display();
        threadPool.shutdown();

        try {
            if(!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
               threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }


    }


}
