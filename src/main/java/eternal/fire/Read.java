package eternal.fire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Read {
    public static String readSrc() throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(Read.class.getResourceAsStream("/src.txt"), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            // End of File
            sb.append("!");
            return sb.toString().toUpperCase();
        }
    }
}
