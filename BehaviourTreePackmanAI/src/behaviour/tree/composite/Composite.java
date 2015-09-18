package behaviour.tree.composite;

import java.util.List;

import behaviour.tree.INode;

public abstract class Composite implements INode {
	
	protected final List<INode> children;
	
	protected Composite(List<INode> children) {
		this.children = children;
	}
}
