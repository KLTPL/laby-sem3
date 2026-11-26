package eu.jpereira.trainings.designpatterns.structural.facade;

import eu.jpereira.trainings.designpatterns.structural.facade.model.Book;
import eu.jpereira.trainings.designpatterns.structural.facade.model.Customer;
import eu.jpereira.trainings.designpatterns.structural.facade.model.DispatchReceipt;
import eu.jpereira.trainings.designpatterns.structural.facade.model.Order;
import eu.jpereira.trainings.designpatterns.structural.facade.service.BookDBService;
import eu.jpereira.trainings.designpatterns.structural.facade.service.CustomerDBService;
import eu.jpereira.trainings.designpatterns.structural.facade.service.CustomerNotificationService;
import eu.jpereira.trainings.designpatterns.structural.facade.service.OrderingService;
import eu.jpereira.trainings.designpatterns.structural.facade.service.WharehouseService;

/**
 * Facade: one simple call (placeOrder) hides the coordination of five subsystem services.
 */
public class DefaultBookstoreFacade implements BookstoreFacade {

	private CustomerDBService customerService;
	private BookDBService bookService;
	private OrderingService orderingService;
	private CustomerNotificationService customerNotificationService;
	private WharehouseService warehouseService;

	@Override
	public void placeOrder(String customerId, String isbn) {
		Customer customer = customerService.findCustomerById(customerId);
		Book book = bookService.findBookByISBN(isbn);

		Order order = orderingService.createOrder(customer, book);
		customerNotificationService.notifyClient(order);

		DispatchReceipt dispatchReceipt = warehouseService.dispatch(order);
		customerNotificationService.notifyClient(dispatchReceipt);
	}

	public void setCustomerService(CustomerDBService customerService) {
		this.customerService = customerService;
	}

	public void setBookService(BookDBService bookService) {
		this.bookService = bookService;
	}

	public void setOrderingService(OrderingService orderingService) {
		this.orderingService = orderingService;
	}

	public void setCustomerNotificationService(CustomerNotificationService customerNotificationService) {
		this.customerNotificationService = customerNotificationService;
	}

	public void setWarehouseService(WharehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}
}
