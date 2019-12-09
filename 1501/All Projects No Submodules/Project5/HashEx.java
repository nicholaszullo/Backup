import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.security.MessageDigest;

public class HashEx {
	public static LargeInteger hash(String file) {

		// lazily catch all exceptions...
		try {
			// read in the file to hash
			Path path = Paths.get(file);
			byte[] data = Files.readAllBytes(path);

			// create class instance to create SHA-256 hash
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// process the file
			md.update(data);
			// generate a hash of the file
			byte[] digest = md.digest();

			return new LargeInteger(digest);
		} catch(Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
}

