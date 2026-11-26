package eu.jpereira.trainings.designpatterns.creational.builder.json;

import java.util.Iterator;
import java.util.List;

import eu.jpereira.trainings.designpatterns.creational.builder.ReportBodyBuilder;
import eu.jpereira.trainings.designpatterns.creational.builder.model.Customer;
import eu.jpereira.trainings.designpatterns.creational.builder.model.ReportBody;
import eu.jpereira.trainings.designpatterns.creational.builder.model.SoldItem;

public class JSONReportBuilder extends ReportBodyBuilder {

	private final JSONReportBody reportBody = new JSONReportBody();

	@Override
	public void addCustomer(Customer customer) {
		reportBody.addContent("sale:{customer:{");
		reportBody.addContent("name:\"");
		reportBody.addContent(customer.getName());
		reportBody.addContent("\",phone:\"");
		reportBody.addContent(customer.getPhone());
		reportBody.addContent("\"}");
	}

	@Override
	public void addItems(List<SoldItem> items) {
		reportBody.addContent(",items:[");
		Iterator<SoldItem> it = items.iterator();
		while (it.hasNext()) {
			SoldItem item = it.next();
			reportBody.addContent("{name:\"");
			reportBody.addContent(item.getName());
			reportBody.addContent("\",quantity:");
			reportBody.addContent(String.valueOf(item.getQuantity()));
			reportBody.addContent(",price:");
			reportBody.addContent(String.valueOf(item.getUnitPrice()));
			reportBody.addContent("}");
			if (it.hasNext()) {
				reportBody.addContent(",");
			}
		}
		reportBody.addContent("]}");
	}

	@Override
	public ReportBody getReportBody() {
		return reportBody;
	}
}
