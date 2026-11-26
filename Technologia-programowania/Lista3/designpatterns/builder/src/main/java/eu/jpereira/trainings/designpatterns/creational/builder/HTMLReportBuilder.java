package eu.jpereira.trainings.designpatterns.creational.builder;

import java.util.List;

import eu.jpereira.trainings.designpatterns.creational.builder.model.Customer;
import eu.jpereira.trainings.designpatterns.creational.builder.model.ReportBody;
import eu.jpereira.trainings.designpatterns.creational.builder.model.SoldItem;

public class HTMLReportBuilder extends ReportBodyBuilder {

	private final HTMLReportBody reportBody = new HTMLReportBody();

	@Override
	public void addCustomer(Customer customer) {
		reportBody.putContent("<span class=\"customerName\">");
		reportBody.putContent(customer.getName());
		reportBody.putContent("</span><span class=\"customerPhone\">");
		reportBody.putContent(customer.getPhone());
		reportBody.putContent("</span>");
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
		reportBody.putContent("</items>");
	}

	@Override
	public ReportBody getReportBody() {
		return reportBody;
	}
}
