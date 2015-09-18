package behaviour.tree.composite;

import java.util.List;

import pacman.game.Game;
import behaviour.tree.INode;
import behaviour.tree.Status;

public class Sequence extends Composite {
	
	public Sequence(List<INode> children) {
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