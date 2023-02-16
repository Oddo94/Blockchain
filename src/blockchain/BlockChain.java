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

    public synchronized void addBlock(Block block) {
        if(blockChain.size() == 5) {
            System.out.println("MAXIMUM BLOCKCHAIN SIZE REACHED!");
            return;
        }

        blockChain.add(block);
    }

//    public Block generateBlock(String requiredPrefixChar, int totalCharCount) {
//        System.out.println("Inside the generate block method");
//        //Retrieves the elements from which the hashcode of the block will be generated
//        int id = getBlockIndex(blockChain);
//        long timeStamp = new Date().getTime();
//        int magicNumber = SecurityUtils.generateMagicNumber();
//        String previousHash = getPreviousBlockHash(blockChain);
//
//        //This variable will hold the hashcode value once this complies to all the requirements
//        String currentHash = null;
//
//        //Creates the input which will be used to generate the hashcode
//        String hashMethodInput = String.format("%d%d%%ds%s", id, timeStamp, magicNumber, previousHash, currentHash);
//
//        //Retrieves the required prefix of the hashcode(containing the specified character and having the specified length)
//        String hashCodePrefix = SecurityUtils.generateRequiredPrefix(requiredPrefixChar, totalCharCount);
//
//        long startTime = System.currentTimeMillis();
//        while(true) {
//            System.out.println("Trying to find a valid hashcode....");
//            String generatedHashCode = SecurityUtils.applySha256(hashMethodInput);
//            boolean isValidHashCode = SecurityUtils.isValidHashcode(hashCodePrefix, generatedHashCode);
//
//            System.out.println(String.format("Magic number: %d\nGenerated hashcode: %s\nIs valid hashcode: %s\n", magicNumber, generatedHashCode, isValidHashCode));
//
//            if(isValidHashCode) {
//                System.out.println("Found the correct hashcode! Exiting the loop...");
//                currentHash = generatedHashCode;
//                break;
//            }
//
//            //If the generated hashcode does not start with the required prefix a new magic number is generated and the whole process repeats
//            magicNumber = SecurityUtils.generateMagicNumber();
//            hashMethodInput = String.format("%d%d%%ds%s", id, timeStamp, magicNumber, previousHash, currentHash);
//        }
//
//        long endTime = System.currentTimeMillis();
//        int generationTime =(int) (endTime - startTime) / 1000;
//
//        Block block = new Block(id, timeStamp, magicNumber, previousHash, currentHash, generationTime);
//
//        return block;
//    }

    public void display() {
        for (Block currentBlock : blockChain) {
            System.out.println(currentBlock.toString());
        }
    }

    public boolean isValid() {
        int blockChainSize = blockChain.size();

//        if(blockChainSize == 0) {
//            return false;
//        }

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

    public synchronized int getBlockIndex() {
        int currentListSize = this.blockChain.size();

        //Empty list scenario
        if(currentListSize == 0) {
            return 1;
        }

        Block lastBlock = this.blockChain.get(currentListSize - 1);

        if(lastBlock == null) {
            return 1;
        }

        //ID generation when the blockchain contains at least an element
        int previousBlockId = lastBlock.getId();
        int newBlockId = previousBlockId + 1;

        return newBlockId;
    }

    public synchronized String getPreviousBlockHash() {
        int currentListSize = this.blockChain.size();

        //Empty list scenario
        if(currentListSize == 0) {
            return "0";
        }

        Block lastBlock = this.blockChain.get(currentListSize - 1);

        if(lastBlock == null) {
            return "0";
        }

        //Returns the hash of the previous block
        return lastBlock.getCurrentHash();
    }

    public synchronized int getSize() {
        return this.blockChain.size();
    }
}
