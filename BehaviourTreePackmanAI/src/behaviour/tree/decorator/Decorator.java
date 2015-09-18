package behaviour.tree.decorator;

import behaviour.tree.INode;

public abstract class Decorator implements INode {
	
	protected final INode child;
	
	protected Decorator(INode child) {
		this.child = child;
	}
}