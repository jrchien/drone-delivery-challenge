package challenge;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import challenge.exporter.DeliveryExporter;
import challenge.importer.OrderImporter;
import challenge.model.Delivery;
import challenge.scheduler.OrderScheduler;

public class Application {

  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    if (args.length != 1) {
      LOG.error("Expects file path.");
      System.exit(1);
    }

    List<Delivery> delivered = OrderScheduler.bestFitSchedule(OrderImporter.parseFile(args[0]));
    System.out.println(DeliveryExporter.exportToFile(delivered));
    System.exit(1);
  }

}
