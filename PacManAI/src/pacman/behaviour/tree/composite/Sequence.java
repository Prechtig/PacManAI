package pacman.behaviour.tree.composite;

import pacman.behaviour.tree.INode;
import pacman.behaviour.tree.Status;
import pacman.game.Game;

public class Sequence extends Composite {
	
	public Sequence(INode... children) {
		super(children);
	}

	@Override
	public Status run(Game game) {
		Status status = null;
		for(INode child : children) {
			status = child.run(game);
			if(status != Status.SUCCESS) {
				return status;
			}
		}
		if(status == null)
			throw new IllegalStateException("Composite node without children");
		return status;
	}
}