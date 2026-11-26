package eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty;

import eu.jpereira.trainings.designpatterns.structural.adapter.exceptions.CodeMismatchException;
import eu.jpereira.trainings.designpatterns.structural.adapter.exceptions.IncorrectDoorCodeException;
import eu.jpereira.trainings.designpatterns.structural.adapter.model.Door;
import eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.exceptions.CannotChangeCodeForUnlockedDoor;
import eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.exceptions.CannotChangeStateOfLockedDoor;
import eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.exceptions.CannotUnlockDoorException;

/**
 * Class Adapter: inherits ThirdPartyDoor and exposes it through the Door interface.
 * Invariant kept by all methods: LOCKED implies CLOSED.
 */
public class ThirdPartyDoorAdaper extends ThirdPartyDoor implements Door {

	@Override
	public void open(String code) throws IncorrectDoorCodeException {
		try {
			unlock(code);
		} catch (CannotUnlockDoorException e) {
			throw new IncorrectDoorCodeException();
		}
		try {
			setState(DoorState.OPEN);
		} catch (CannotChangeStateOfLockedDoor e) {
			throw new IllegalStateException("Door locked right after successful unlock", e);
		}
	}

	@Override
	public void close() {
		if (getLockStatus() == LockStatus.LOCKED) {
			return; // locked door is already closed
		}
		try {
			setState(DoorState.CLOSED);
		} catch (CannotChangeStateOfLockedDoor e) {
			throw new IllegalStateException("Lock status checked as UNLOCKED", e);
		}
	}

	@Override
	public boolean isOpen() {
		return getState() == DoorState.OPEN;
	}

	@Override
	public void changeCode(String oldCode, String newCode, String newCodeConfirmation)
			throws IncorrectDoorCodeException, CodeMismatchException {
		if (!newCode.equals(newCodeConfirmation)) {
			throw new CodeMismatchException();
		}
		LockStatus initialLockStatus = getLockStatus();
		try {
			unlock(oldCode);
		} catch (CannotUnlockDoorException e) {
			throw new IncorrectDoorCodeException();
		}
		try {
			setNewLockCode(newCode);
		} catch (CannotChangeCodeForUnlockedDoor e) {
			throw new IllegalStateException("Door locked right after successful unlock", e);
		}
		if (initialLockStatus == LockStatus.LOCKED) {
			lock();
		}
	}

	@Override
	public boolean testCode(String code) {
		LockStatus initialLockStatus = getLockStatus();
		try {
			unlock(code);
		} catch (CannotUnlockDoorException e) {
			return false;
		}
		if (initialLockStatus == LockStatus.LOCKED) {
			lock();
		}
		return true;
	}
}
