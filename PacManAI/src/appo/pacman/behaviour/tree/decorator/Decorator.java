package appo.pacman.behaviour.tree.decorator;

import appo.pacman.behaviour.tree.INode;

public abstract class Decorator implements INode {
	
	protected final INode child;
	
	protected Decorator(INode child) {
		this.child = child;
	}
}