package blockchain;

public class Main {
    public static void main(String[] args) {
        int numberOfThreads = 10;
        int blockChainSize = 5;

        BlockChainManager blockChainManager = new BlockChainManager(blockChainSize, numberOfThreads);
        blockChainManager.manageBlockChain();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        blockChainManager.getBlockChain().display();

    }
}
