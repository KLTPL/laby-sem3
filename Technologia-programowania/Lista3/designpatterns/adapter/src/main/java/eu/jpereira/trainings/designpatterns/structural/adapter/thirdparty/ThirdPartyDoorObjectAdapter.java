package eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty;

import eu.jpereira.trainings.designpatterns.structural.adapter.exceptions.CodeMismatchException;
import eu.jpereira.trainings.designpatterns.structural.adapter.exceptions.IncorrectDoorCodeException;
import eu.jpereira.trainings.designpatterns.structural.adapter.model.Door;
import eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.exceptions.CannotChangeCodeForUnlockedDoor;
import eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.exceptions.CannotChangeStateOfLockedDoor;
import eu.jpereira.trainings.designpatterns.structural.adapter.thirdparty.exceptions.CannotUnlockDoorException;

/**
 * Object Adapter: wraps a ThirdPartyDoor and delegates to it through the Door interface.
 * The wrapped door is private, so callers cannot bypass the adapter's logic.
 * Invariant kept by all methods: LOCKED implies CLOSED.
 */
public class ThirdPartyDoorObjectAdapter implements Door {

	private final ThirdPartyDoor door;

	public ThirdPartyDoorObjectAdapter() {
		this.door = new ThirdPartyDoor();
	}

	@Override
	public void open(String code) throws IncorrectDoorCodeException {
		try {
			door.unlock(code);
		} catch (CannotUnlockDoorException e) {
			throw new IncorrectDoorCodeException();
		}
		try {
			door.setState(ThirdPartyDoor.DoorState.OPEN);
		} catch (CannotChangeStateOfLockedDoor e) {
			throw new IllegalStateException("Door locked right after successful unlock", e);
		}
	}

	@Override
	public void close() {
		if (door.getLockStatus() == ThirdPartyDoor.LockStatus.LOCKED) {
			return; // locked door is already closed
		}
		try {
			door.setState(ThirdPartyDoor.DoorState.CLOSED);
		} catch (CannotChangeStateOfLockedDoor e) {
			throw new IllegalStateException("Lock status checked as UNLOCKED", e);
		}
	}

	@Override
	public boolean isOpen() {
		return door.getState() == ThirdPartyDoor.DoorState.OPEN;
	}

	@Override
	public void changeCode(String oldCode, String newCode, String newCodeConfirmation)
			throws IncorrectDoorCodeException, CodeMismatchException {
		if (!newCode.equals(newCodeConfirmation)) {
			throw new CodeMismatchException();
		}
		ThirdPartyDoor.LockStatus initialLockStatus = door.getLockStatus();
		try {
			door.unlock(oldCode);
		} catch (CannotUnlockDoorException e) {
			throw new IncorrectDoorCodeException();
		}
		try {
			door.setNewLockCode(newCode);
		} catch (CannotChangeCodeForUnlockedDoor e) {
			throw new IllegalStateException("Door locked right after successful unlock", e);
		}
		if (initialLockStatus == ThirdPartyDoor.LockStatus.LOCKED) {
			door.lock();
		}
	}

	@Override
	public boolean testCode(String code) {
		ThirdPartyDoor.LockStatus initialLockStatus = door.getLockStatus();
		try {
			door.unlock(code);
		} catch (CannotUnlockDoorException e) {
			return false;
		}
		if (initialLockStatus == ThirdPartyDoor.LockStatus.LOCKED) {
			door.lock();
		}
		return true;
	}
}
