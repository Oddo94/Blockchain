package blockchain;

import blockchain.model.Block;
import lombok.Getter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Getter
public class BlockChainManager {
    private int maxSize;
    private BlockChain blockChain;

    public BlockChainManager(int blockChainSize) {
        if(blockChainSize <= 0) {
            this.maxSize = 1;
        }

        this.maxSize = blockChainSize;
        this.blockChain = new BlockChain();
    }

    public void manageBlockChain(String requiredPrefixChar, int totalCharCount) {
//        int currentBlockChainSize = blockChain.getSize();
//        ExecutorService threadPool = Executors.newFixedThreadPool(5);
//        while(currentBlockChainSize < maxSize) {
//            int newBlockId = blockChain.getBlockIndex();
//            String previousBlockHash = blockChain.getPreviousBlockHash();
//
//            //Creates the runnable object which will try to generate a new block
//            BlockGenerator blockGenerator = new BlockGenerator(requiredPrefixChar, totalCharCount, newBlockId, previousBlockHash, this.blockChain);
//
//            //Executes the newly created runnable instance
//            threadPool.execute(blockGenerator);
//            //blockGenerator.run();
//
//            //Saves the new blockchain size(if the generation was successful the size will increase by one otherwise it will remain the same)
//            currentBlockChainSize = blockChain.getSize();
//            System.out.println("CURRENT BLOCKCHAIN SIZE:" + currentBlockChainSize);
//        }
//
//        blockChain.display();
//        threadPool.shutdown();

        int currentBlockChainSize = blockChain.getSize();
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for(int i = 0; i < maxSize; i++) {
            BlockGenerator blockGenerator = new BlockGenerator(requiredPrefixChar, totalCharCount, blockChain, maxSize);
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
