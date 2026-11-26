package eu.jpereira.trainings.designpatterns.creational.builder;

import java.util.List;

import eu.jpereira.trainings.designpatterns.creational.builder.model.Customer;
import eu.jpereira.trainings.designpatterns.creational.builder.model.ReportBody;
import eu.jpereira.trainings.designpatterns.creational.builder.model.SoldItem;

/**
 * Builder: steps to build a ReportBody. Each report format provides its own subclass.
 */
public abstract class ReportBodyBuilder {

	public abstract void addCustomer(Customer customer);

	public abstract void addItems(List<SoldItem> items);

	public abstract ReportBody getReportBody();
}
