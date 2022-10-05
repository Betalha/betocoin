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
        while (sc.hasNextLine()) {
            String data = sc.nextLine();
            if (data == "") break;
            beto.addBlock(data);
            System.out.println(beto.getlast().toString());
        }
        System.out.println("Betocoin:");
        System.out.println("blockchain valid: " + beto.validate());
        System.out.println(beto.toString());
    }
}
class BlockChain {
    private LinkedList<Block> chain = new LinkedList<>();
    private int index = 0;
    private String prevH;
    public BlockChain(){
        Timestamp instant = Timestamp.from(Instant.now());
        Block b0 = new Block(index, "genesis block",instant ,"0");
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

    public boolean validate(){
        for (int i = 0;i<chain.size()-1;i++){
            if (chain.get(i).Hash != chain.get(i+1).prevHash) return false;
        }
        return true;
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
    String data, prevHash, Hash, H;
    Timestamp timestamp;
    public Block(int index,String data, Timestamp timestamp, String prevHash) {
        this.index = index;
        this.data = data;
        this.timestamp = timestamp;
        this.prevHash = prevHash;
        CalculateHash();
    }
    private void CalculateHash() {
        try
        {
            Hash = toHexString(getSHA(this.index + this.data + this.timestamp + this.prevHash));
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
        }
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
                ", previousHash='" + prevHash + '\'' +
                ", Hash='" + Hash + '\'' +
                '}';
    }
}
//toHexString(getSHA(---))