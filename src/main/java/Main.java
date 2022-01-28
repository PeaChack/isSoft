import model.Order;
import model.Product;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main {
	public static void main(String[] args) throws URISyntaxException, IOException {
		CsvHelper csvHelper = new CsvHelper();
		Map<LocalDate, Map<Product, Integer>> resultMap = new HashMap<>();

		Map<UUID, Order> orderMap = csvHelper.loadOrders();
		Map<UUID, Product> productMap = csvHelper.loadProducts();
		csvHelper.loadProductCount(orderMap, productMap);

		orderMap.forEach((orderId, order) -> {
			if (!resultMap.containsKey(order.getDate())) {
				resultMap.put(order.getDate(), new HashMap<>());
			}

			Map<Product, Integer> productIntegerMap = resultMap.get(order.getDate());

			order.getProductMap().forEach((product, count) ->
				productIntegerMap.put(product,
						!productIntegerMap.containsKey(product) ?
								count * product.getPrice() :
								productIntegerMap.get(product) + count * product.getPrice())
			);
		});

		for (Map.Entry<LocalDate, Map<Product, Integer>> entry : resultMap.entrySet()) {
			Product productKey = Collections.max(entry.getValue().entrySet(), Map.Entry.comparingByValue()).getKey();
			System.out.println(entry.getKey() + " : " + productKey.getName());
		}
	}
}
