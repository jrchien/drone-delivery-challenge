package challenge;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.function.Consumer;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests the main application.
 * 
 * @author jeffrey
 */
public class ApplicationTest {

  private static final Logger LOG = LoggerFactory.getLogger(ApplicationTest.class);

  /**
   * Tests the main method and checks the exported output.
   */
  @Test
  public void testMainMethod() {
    redirectSystemOutStream((outputStream) -> {
      Application.main(new String[] {"src/test/resources/test-input-3.txt"});

      String filePath = outputStream.toString().trim();
      try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
        Assert.assertEquals("Should match expected size.", 23, bufferedReader.lines().count());
      } catch (IOException exception) {
        LOG.error("Unable to read file at path.", exception);
        Assert.fail(String.format("Unable to read file at path: %s.", filePath));
      }
    });
  }

  private void redirectSystemOutStream(Consumer<ByteArrayOutputStream> systemOutConsumer) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
    PrintStream originalStream = System.out;
    System.setOut(printStream);
    systemOutConsumer.accept(outputStream);
    System.out.flush();
    System.setOut(originalStream);
  }

}
