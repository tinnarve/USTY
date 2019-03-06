package com.ru.usty.elevator;

public class Elevator implements Runnable {

	int numberFloors, maxOccupants, currentFloor, currentOccupants;
	Operator operator;
	Boolean goUp;
	private boolean exit;

	Elevator(int numberFloors, int maxOccupants, Operator operator) {
		this.numberFloors = numberFloors;
		this.maxOccupants = maxOccupants;
		this.currentFloor = -1; //start in -1 so that it waits for people to get in for the first trip
		this.currentOccupants = 0;
		this.operator = operator;
		this.exit = false;
		if(currentFloor == numberFloors - 1) goUp = false;
		else goUp = true;
	}

	@Override
	public void run() {
		
		while (!exit) {
			if(goUp) moveUp();
			else moveDown();
			if(Thread.interrupted()) break;
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

	public void die()
	{
		System.out.println("Asked to stop. Stopping.");
		this.exit = true;
	}

	public void moveUp() {
		
		if(currentFloor + 1 < numberFloors)
		{
			currentFloor++;
			if(currentFloor == numberFloors-1) {
				goUp = false;
			}
			operator.openElevator(this); 
			// try {
			// 	Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME - 50);
			// } catch (InterruptedException e) {
			// 	die();
			// 	return;
			// }
			operator.closeElevator(this);
		}
	}

	public void moveDown() {
		
		if(currentFloor != 0)
		{
			currentFloor--;
			if(currentFloor == 0) {
				goUp = true;
			}
			operator.openElevator(this); 
			// try {
			// 	Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME - 50);
			// } catch (InterruptedException e) {
			// 	die();
			// 	return;
			// }
			operator.closeElevator(this);
			
		}
	}
	public Boolean getDirection() {
		return goUp;
	}
}
