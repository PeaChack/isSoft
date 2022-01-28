import au.com.bytecode.opencsv.CSVReader;
import model.Order;
import model.Product;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CsvHelper {
	public Map<UUID, Order> loadOrders() throws IOException, URISyntaxException {
		Map<UUID, Order> orderMap = new HashMap<>();
		URI ordersUri = Main.class.getClassLoader().getResource("orders.csv").toURI();
		File orderFile = new File(ordersUri);
		CSVReader csvReader = new CSVReader(new FileReader(orderFile), ',', '"', 1);
		String[] nextLine;
		while ((nextLine = csvReader.readNext()) != null) {
			orderMap.put(UUID.fromString(nextLine[0]), new Order(UUID.fromString(nextLine[0]), LocalDateTime.parse(nextLine[1]).toLocalDate()));
		}
		csvReader.close();

		return orderMap;
	}

	public Map<UUID, Product> loadProducts() throws IOException, URISyntaxException {
		Map<UUID, Product> productMap = new HashMap<>();
		URI productsUri = Main.class.getClassLoader().getResource("products.csv").toURI();
		File productsFile = new File(productsUri);
		CSVReader csvReader = new CSVReader(new FileReader(productsFile), ',', '"', 1);
		String[] nextLine;
		while ((nextLine = csvReader.readNext()) != null) {
			productMap.put(UUID.fromString(nextLine[0]), new Product(UUID.fromString(nextLine[0]), nextLine[1], Integer.parseInt(nextLine[2])));
		}
		csvReader.close();

		return productMap;
	}

	public void loadProductCount(Map<UUID, Order> orderMap, Map<UUID, Product> productMap) throws IOException, URISyntaxException {
		URI totalOrderUri = Main.class.getClassLoader().getResource("order_items.csv").toURI();
		File totalOrderFile = new File(totalOrderUri);
		CSVReader csvReader = new CSVReader(new FileReader(totalOrderFile), ',', '"', 1);
		String[] nextLine;
		while ((nextLine = csvReader.readNext()) != null) {
			Order order = orderMap.get(UUID.fromString(nextLine[0]));
			Product product = productMap.get(UUID.fromString(nextLine[1]));
			int count = Integer.parseInt(nextLine[2]);
			order.putProduct(product, count);
		}
		csvReader.close();
	}
}
