package blockchain;

public class Main {
    public static void main(String[] args) {
        int blockChainSize = 5;
        int numberOfThreads = 100;

        BlockChainManager blockChainManager = new BlockChainManager(blockChainSize, numberOfThreads);
        blockChainManager.manageBlockChain();

         try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println();
        blockChainManager.getBlockChain().display();

    }
}
