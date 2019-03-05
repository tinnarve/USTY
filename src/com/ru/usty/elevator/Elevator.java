package com.ru.usty.elevator;

public class Elevator implements Runnable {

	int numberFloors, maxOccupants, currentFloor, currentOccupants;
	Operator operator;

	Elevator(int numberFloors, int maxOccupants, Operator operator) {
		this.numberFloors = numberFloors;
		this.maxOccupants = maxOccupants;
		this.currentFloor = 0;
		this.currentOccupants = 0;
		this.operator = operator;
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
			operator.openElevator(this); 
			try {
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			operator.closeElevator(this);
		}
	}

	public void moveDown() {
		
		if(currentFloor != 0)
		{
			currentFloor--;
			operator.openElevator(this); 
			try {
				Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			operator.closeElevator(this);
		}
	}
}
