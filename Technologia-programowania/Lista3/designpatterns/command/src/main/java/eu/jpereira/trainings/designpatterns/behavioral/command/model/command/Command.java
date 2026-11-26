package eu.jpereira.trainings.designpatterns.behavioral.command.model.command;

import eu.jpereira.trainings.designpatterns.behavioral.command.model.command.results.DBServerInstanceResult;
import eu.jpereira.trainings.designpatterns.behavioral.command.model.exceptions.CouldNotConnectException;

/**
 * Command: encapsulates a request to a receiver as an object,
 * so it can be scheduled, queued or grouped (see CommandJob).
 */
public interface Command {

	void execute() throws CouldNotConnectException;

	@SuppressWarnings("rawtypes")
	DBServerInstanceResult getResult();
}
