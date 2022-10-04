package blockchain;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Scanner;

class Betocoin {
    public static void main(String args[])
    {
        Scanner sc = new Scanner(System.in);
        BlockChain beto = new BlockChain();
        beto.createGenesis();
        while (sc.hasNextLine()) {
            String data = sc.nextLine();
            if (data == "") break;
            beto.addBlock(data);
            System.out.println(beto.getlast().toString());
        }
        System.out.println("Betocoin:");
        System.out.println(beto.toString());
    }
}
class BlockChain {
    private LinkedList<Block> chain = new LinkedList<>();
    private int index = 0;
    private String prevH;
    public void createGenesis(){
        Timestamp instant = Timestamp.from(Instant.now());
        Block b0 = new Block(index, "geneis",instant ,"0");
        chain.add(b0);
        prevH = b0.Hash;
        index++;
    }

    public void addBlock(String data){
        Timestamp instant = Timestamp.from(Instant.now());
        Block B = new Block(index, data, instant, prevH);
        chain.add(B);
        prevH = B.Hash;
        index++;
    }

    public Block getlast(){
        return chain.getLast();
    }

    @Override
    public String toString() {
        return "this Chain{ "
                + chain +
                '}';
    }
}

class Block {
    int index;
    String data, PHash, Hash, H;
    Timestamp timestamp;
    public Block(int x,String y, Timestamp w, String z) {
        this.index = x;
        this.data = y;
        this.timestamp = w;
        this.PHash = z;
        this.Hash = CalculateHash();
    }
    private String CalculateHash() {
        try
        {
            H = toHexString(getSHA(this.index + this.data + this.timestamp + this.PHash));
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
        }
        return H;
    }
    private static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    private static String toHexString(byte[] hash)
    {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    @Override
    public String toString() {
        return "Block{" +
                "index=" + index +
                ", data='" + data + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", PHash='" + PHash + '\'' +
                ", Hash='" + Hash + '\'' +
                '}';
    }
}
//toHexString(getSHA(---))