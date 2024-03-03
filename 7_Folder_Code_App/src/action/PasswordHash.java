package action;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {
    public String passwordHash(String password) throws NoSuchAlgorithmException{

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(password.getBytes());
        BigInteger bigInt =new BigInteger(1,messageDigest);

        return bigInt.toString(16);
    }

}
