package blockchain;

import blockchain.model.Block;
import blockchain.utils.SecurityUtils;
import java.util.Date;
import java.util.LinkedList;

public class BlockChain {
    private LinkedList<Block> blockChain;

    public BlockChain() {
        this.blockChain = new LinkedList<>();
    }

    public void addBlock(Block block) {
        blockChain.add(block);
    }

    public Block generateBlock() {
        int id = -1;
        long timeStamp = new Date().getTime();
        String previousHash = null;
        String currentHash = null;
        String hashMethodInput = null;

        id = getBlockIndex(blockChain);
        hashMethodInput = String.format("%d%d%s%s", id, timeStamp, previousHash, currentHash);
        previousHash = getPreviousBlockHash(blockChain);
        currentHash = SecurityUtils.applySha256(hashMethodInput);

        Block block = new Block(id, timeStamp, previousHash, currentHash);

        return block;
    }

    public void display() {
        for (Block currentBlock : blockChain) {
            System.out.println(currentBlock.toString());
        }
    }

    public boolean isValid() {
        int blockChainSize = blockChain.size();

        if(blockChainSize == 0) {
            return false;
        }

        //If a single element is present no other checks are performed and the blockchain is considered valid
        if(blockChainSize == 1) {
            return true;
        }

        String previousBlockCurrentHash = blockChain.get(0).getCurrentHash();
        for(int i = 0; i < blockChain.size(); i++) {
            Block block = blockChain.get(i);

            String currentBlockPreviousHash = null;

            //For the first block there's no previous hash stored
            if(i == 0) {
                currentBlockPreviousHash = block.getCurrentHash();
            } else {
                currentBlockPreviousHash = block.getPreviousHash();
            }

             if(!currentBlockPreviousHash.equals(previousBlockCurrentHash)) {
                 return false;
             }

             //Sets the hash of the previous block for comparison with the next block from the list
            previousBlockCurrentHash = block.getCurrentHash();
        }

        return true;
    }

    private int getBlockIndex(LinkedList<Block> blockChain) {
        int currentListSize = blockChain.size();

        //Empty list scenario
        if(currentListSize == 0) {
            return 1;
        }

        Block lastBlock = blockChain.get(currentListSize - 1);

        if(lastBlock == null) {
            return 1;
        }

        //ID generation when the blockchain contains at least an element
        int previousBlockId = lastBlock.getId();
        int newBlockId = previousBlockId + 1;

        return newBlockId;
    }

    private String getPreviousBlockHash(LinkedList<Block> blockChain) {
        int currentListSize = blockChain.size();

        //Empty list scenario
        if(currentListSize == 0) {
            return "0";
        }

        Block lastBlock = blockChain.get(currentListSize - 1);

        if(lastBlock == null) {
            return "0";
        }

        //Returns the hash of the previous block
        return lastBlock.getCurrentHash();
    }
}
