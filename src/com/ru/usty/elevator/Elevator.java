package com.ru.usty.elevator;

public class Elevator implements Runnable {

	int numberFloors, maxOccupants, currentFloor, currentOccupants;

	Elevator(int numberFloors, int maxOccupants) {
		this.numberFloors = numberFloors;
		this.maxOccupants = maxOccupants;
		this.currentFloor = 0;
		this.currentOccupants = 0;
	}

	@Override
	public void run() {
		while (true) {
			// TODO stuff
			if(currentFloor == 0) moveUp();
			else moveDown();
			Thread.yield();
		}
	}

	public void addPerson(Person p)
	{
		p.getOnElevator(this);
		currentOccupants++;
	}

	public void removePerson() {
		currentOccupants--;
	}

	public void moveUp() {
		if(currentFloor + 1 < numberFloors)
		{
			currentFloor++;
			try {
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void moveDown() {
		
		if(currentFloor != 0)
		{
			currentFloor--;
			try {
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
