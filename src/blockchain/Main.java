package blockchain;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter how many zeros the hash must start with:");
//        int totalCharCount = scanner.nextInt();

        int totalCharCount = 1;
        String requiredPrefixChar = "0";
        int blockChainSize = 5;

        BlockChainManager blockChainManager = new BlockChainManager(blockChainSize);
        blockChainManager.manageBlockChain();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        blockChainManager.getBlockChain().display();

    }
}
