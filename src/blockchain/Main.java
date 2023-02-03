package blockchain;

public class Main {
    public static void main(String[] args) {
        int blockChainSize = 5;
        BlockChainManager blockChainManager = new BlockChainManager(blockChainSize);

        blockChainManager.manageBlockChain();
    }
}
