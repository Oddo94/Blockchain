package blockchain;

import blockchain.model.Block;

public class BlockChainManager {
    private int size;
    private BlockChain blockChain;

    public BlockChainManager(int blockChainSize) {
        if(blockChainSize <= 0) {
            this.size = 1;
        }

        this.size = blockChainSize;
        this.blockChain = new BlockChain();
    }

    public void manageBlockChain(String requiredPrefixChar, int totalCharCount) {
        System.out.println("Inside the manage blockchain method");
        for (int i = 0; i < size; i++) {
            Block block = blockChain.generateBlock(requiredPrefixChar, totalCharCount);
            blockChain.addBlock(block);
        }

        blockChain.display();
    }


}
