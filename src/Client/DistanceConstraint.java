package Client;

public class DistanceConstraint implements Constraint {

	private SnakePart parent;
	private SnakePart self;

	public DistanceConstraint(SnakePart parent, SnakePart self) {
		this.parent = parent;
		this.self = self;
	}

	@Override
	public void satisfy() {
		//todo
	}
}