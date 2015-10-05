package pacman.behaviour.tree;


public enum Status {
	
	SUCCESS { public Status opposite() { return Status.FAILURE; }; },
	FAILURE { public Status opposite() { return Status.SUCCESS; }; },
	RUNNING { public Status opposite() { return Status.RUNNING; }; };
	
	public abstract Status opposite();
}
