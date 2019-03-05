package com.ru.usty.elevator;

public class Person implements Runnable {

	int source, dest;
	Elevator myElevator;

	Person(int source, int dest) {
		this.source = source;
		this.dest = dest;
	}

	@Override
	public void run() {
		try {
			ElevatorScene.inSem[source].acquire();
			ElevatorScene.addPersonToAvailableElevatorAtFloor(this, source);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(true)
		{
			if(myElevator != null && myElevator.currentFloor == dest)
			{
				ElevatorScene.personExitsAtFloor(dest);
				myElevator.removePerson();
				return;
			}	
			Thread.yield();
		}
	}
	
	public void getOnElevator(Elevator e)
	{
		myElevator = e;
	}
}
