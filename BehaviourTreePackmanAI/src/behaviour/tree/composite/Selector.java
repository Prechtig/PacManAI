package behaviour.tree.composite;

import pacman.game.Game;
import behaviour.tree.INode;
import behaviour.tree.Status;

public class Selector extends Composite {
	
	public Selector(INode... children) {
		super(children);
	}

	@Override
	public Status run(Game game) {
		Status status = null;
		for(INode child : children) {
			status = child.run(game);
			if(status != Status.FAILURE) {
				return status;
			}
		}
		if(status == null)
			throw new IllegalStateException("Composite node without children");
		return status;
	}

}
