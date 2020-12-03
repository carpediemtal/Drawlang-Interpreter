package eternal.fire.token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Reader {
    private String fileName;

    public Reader(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 从指定文件中将文本读取为字符串
     *
     * @return 从指定文件中读取的字符串
     * @throws IOException 文件不存在
     */
    public String readSrc() throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(Reader.class.getResourceAsStream(String.format("/%s", fileName)), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
