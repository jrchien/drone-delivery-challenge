package challenge;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import challenge.exporter.DeliveryExporter;
import challenge.importer.OrderImporter;
import challenge.model.Delivery;
import challenge.model.Order;
import challenge.scheduler.OrderSchedulers;

/**
 * The main application.
 * 
 * @author jeffrey
 */
public class Application {

  private static final Logger LOG = LoggerFactory.getLogger(Application.class);

  /**
   * The main method.
   * 
   * @param args The program arguments.
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      LOG.error("Expects file path.");
      System.exit(1);
    }

    List<Order> orders = OrderImporter.parseFile(args[0]);
    List<Delivery> deliveries = OrderSchedulers.bestFit().schedule(orders);
    System.out.println(DeliveryExporter.exportToFile(deliveries));
    System.exit(1);
  }

}
