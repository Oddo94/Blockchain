package blockchain;

import blockchain.model.Block;
import blockchain.utils.SecurityUtils;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AllArgsConstructor
public class BlockGenerator implements Runnable {
    private String requiredPrefixChar;
    private int totalCharCount;
//    private int blockId;
//    private String previousHash;
    private BlockChain blockChain;
    private int maxSize;

    @Override
    public void run() {
        // System.out.printf("GENERATING BLOCK USING THREAD %s", Thread.currentThread().getName());
        int currentBlockChainSize = blockChain.getSize();
        while(currentBlockChainSize < maxSize) {
            int newBlockId = blockChain.getBlockIndex();
            String previousBlockHash = blockChain.getPreviousBlockHash();

            //Creates the runnable object which will try to generate a new block
            //BlockGenerator blockGenerator = new BlockGenerator(this.requiredPrefixChar, totalCharCount, newBlockId, previousBlockHash, this.blockChain);

//            Block block = generateBlock(this.requiredPrefixChar, this.totalCharCount, this.blockId, this.previousHash);
            Block block = generateBlock(this.requiredPrefixChar, this.totalCharCount);

            blockChain.addBlock(block);

            //Saves the new blockchain size(if the generation was successful the size will increase by one otherwise it will remain the same)
            currentBlockChainSize = blockChain.getSize();
            System.out.println("CURRENT BLOCKCHAIN SIZE:" + currentBlockChainSize);
        }
    }
    private synchronized Block generateBlock(String requiredPrefixChar, int totalCharCount) {
        //Retrieves the elements from which the hashcode of the block will be generated
        int id = blockChain.getBlockIndex();
        long timeStamp = new Date().getTime();
        int magicNumber = SecurityUtils.generateMagicNumber();
        String previousHash = blockChain.getPreviousBlockHash();

        //This variable will hold the hashcode value once this complies to all the requirements
        String currentHash = null;

        //Creates the input which will be used to generate the hashcode
        String hashMethodInput = String.format("%d%d%%ds%s", id, timeStamp, magicNumber, previousHash, currentHash);

        //Retrieves the required prefix of the hashcode(containing the specified character and having the specified length)
        String hashCodePrefix = SecurityUtils.generateRequiredPrefix(requiredPrefixChar, totalCharCount);

        long startTime = System.currentTimeMillis();
        while(true) {
            //           System.out.println("Trying to find a valid hashcode....");
            String generatedHashCode = SecurityUtils.applySha256(hashMethodInput);
            boolean isValidHashCode = SecurityUtils.isValidHashcode(hashCodePrefix, generatedHashCode);

//            System.out.println(String.format("Magic number: %d\nGenerated hashcode: %s\nIs valid hashcode: %s\n", magicNumber, generatedHashCode, isValidHashCode));

            if(isValidHashCode) {
                //               System.out.println("Found the correct hashcode! Exiting the loop...");
                currentHash = generatedHashCode;
                break;
            }

            //If the generated hashcode does not start with the required prefix a new magic number is generated and the whole process repeats
            magicNumber = SecurityUtils.generateMagicNumber();
            hashMethodInput = String.format("%d%d%%ds%s", id, timeStamp, magicNumber, previousHash, currentHash);
        }

        long endTime = System.currentTimeMillis();
        int generationTime =(int) (endTime - startTime) / 1000;

        //Checks again just in case another thread has generated a valid block in the meantime
        if(blockChain.getSize() > 0) {
            id = blockChain.getBlockIndex();
            previousHash = blockChain.getPreviousBlockHash();
        }


        Block block = new Block(id, timeStamp, magicNumber, previousHash, currentHash, generationTime);

        return block;
    }

//    private synchronized Block generateBlock(String requiredPrefixChar, int totalCharCount, int blockId, String previousHash) {
// //       System.out.println("Inside the generate block method");
//        //Retrieves the elements from which the hashcode of the block will be generated
//        int id = blockId;
//        long timeStamp = new Date().getTime();
//        int magicNumber = SecurityUtils.generateMagicNumber();
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
// //           System.out.println("Trying to find a valid hashcode....");
//            String generatedHashCode = SecurityUtils.applySha256(hashMethodInput);
//            boolean isValidHashCode = SecurityUtils.isValidHashcode(hashCodePrefix, generatedHashCode);
//
////            System.out.println(String.format("Magic number: %d\nGenerated hashcode: %s\nIs valid hashcode: %s\n", magicNumber, generatedHashCode, isValidHashCode));
//
//            if(isValidHashCode) {
// //               System.out.println("Found the correct hashcode! Exiting the loop...");
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
}
