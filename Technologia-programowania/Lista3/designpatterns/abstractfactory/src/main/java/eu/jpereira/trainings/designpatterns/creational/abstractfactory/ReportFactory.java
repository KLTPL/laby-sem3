package eu.jpereira.trainings.designpatterns.creational.abstractfactory;

/**
 * Abstract Factory: creates a consistent family of report elements (one format).
 */
public interface ReportFactory {

	ReportHeader createHeader();

	ReportBody createBody();

	ReportFooter createFooter();
}
