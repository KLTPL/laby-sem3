package eu.jpereira.trainings.designpatterns.creational.builder.xml;

import java.util.List;

import eu.jpereira.trainings.designpatterns.creational.builder.ReportBodyBuilder;
import eu.jpereira.trainings.designpatterns.creational.builder.model.Customer;
import eu.jpereira.trainings.designpatterns.creational.builder.model.ReportBody;
import eu.jpereira.trainings.designpatterns.creational.builder.model.SoldItem;

public class XMLReportBuilder extends ReportBodyBuilder {

	private final XMLReportBody reportBody = new XMLReportBody();

	@Override
	public void addCustomer(Customer customer) {
		reportBody.putContent("<sale><customer><name>");
		reportBody.putContent(customer.getName());
		reportBody.putContent("</name><phone>");
		reportBody.putContent(customer.getPhone());
		reportBody.putContent("</phone></customer>");
	}

	@Override
	public void addItems(List<SoldItem> items) {
		reportBody.putContent("<items>");
		for (SoldItem item : items) {
			reportBody.putContent("<item><name>");
			reportBody.putContent(item.getName());
			reportBody.putContent("</name><quantity>");
			reportBody.putContent(item.getQuantity());
			reportBody.putContent("</quantity><price>");
			reportBody.putContent(item.getUnitPrice());
			reportBody.putContent("</price></item>");
		}
		reportBody.putContent("</items></sale>");
	}

	@Override
	public ReportBody getReportBody() {
		return reportBody;
	}
}
