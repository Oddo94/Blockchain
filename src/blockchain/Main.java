package blockchain;

import blockchain.utils.SecurityUtils;

public class Main {
    public static void main(String[] args) {
//        int blockChainSize = 5;
//        BlockChainManager blockChainManager = new BlockChainManager(blockChainSize);
//
//        blockChainManager.manageBlockChain();

        int prefixLength = 2;
        String prefixChar = "0";
        String input = null;
        String requiredPrefix  = SecurityUtils.generateRequiredPrefix(prefixChar, prefixLength);

        long startTime = System.currentTimeMillis();
        while(true) {
            int magicNumber = SecurityUtils.generateMagicNumber();

            input = String.format("11539827383396%d000000a3fe20573b5bb358d2291165e15662a5b057240e954c573fb1f2a6d0cb8", magicNumber);

            String generatedHashCode = SecurityUtils.applySha256(input);
            boolean isValidHashCode = SecurityUtils.isValidHashcode(requiredPrefix, generatedHashCode);

            System.out.println(String.format("Magic number: %d\nGenerated hashcode: %s\nIs valid hashcode: %s\n", magicNumber, generatedHashCode, isValidHashCode));

            if(isValidHashCode) {
                System.out.println("Found the correct hashcode! Exiting the loop...");
                break;
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = (endTime - startTime) / 1000;

        System.out.printf("The hashcode generation took: %ds", totalTime);

    }
}
