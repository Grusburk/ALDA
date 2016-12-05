/*
 * Henrik Thulin heth7132
 * Mattin Lotfi malo5163
 */

package HuffClass;

public class HuffTreeNode {

	private boolean solo = false;

	private Object left = null;
	private Object right = null;

	private int lfreq = 0;
	private int rfreq = 0;

	public HuffTreeNode(Object o1, int o1f, Object o2, int o2f) {

		if (o1f < o2f) {
			this.left = o1;
			this.lfreq = o1f;
			this.right = o2;
			this.rfreq = o2f;
		} else {
			this.left = o2;
			this.lfreq = o2f;
			this.right = o1;
			this.rfreq = o1f;
		}
	}

	public int GetFrequency() {
		return this.lfreq + this.rfreq;
	}

	public String toString() {

		return "[" + this.lfreq + ":" + this.left + "&" + this.rfreq + ":" + this.right + "]";
	}

	/*public String GetBinaryString(Object o) {

		int b = GetBinary(o);		
		
		return String.format("%"+GetDepth(o)+"s", Integer.toBinaryString(b)).replace(' ', '0');
	 
	}*/

	public int GetBinary(Object o) {
		
		return GetBinary(o, GetDepth(o) - 1);
	}
	
	private int GetBinary(Object o, int depth) {
		
		if (this.left != null && this.left.equals(o))
			return 0;

		if (this.right != null && this.right.equals(o))
			return 1 << depth;

		if (this.left instanceof HuffTreeNode)
			if (((HuffTreeNode) this.left).GetDepth(o) > -1)
				return 0 | ((HuffTreeNode) this.left).GetBinary(o, depth - 1);

		if (this.right instanceof HuffTreeNode)
			if (((HuffTreeNode) this.right).GetDepth(o) > -1)
				return (1 << depth) | ((HuffTreeNode) this.right).GetBinary(o, depth - 1);

		return 0;
	}

	public int GetDepth(Object o) {

		return GetDepth(o, 0);
	}	
	
	private int GetDepth(Object o, int current) {

		if (this.left != null && this.left.equals(o))
			return current + 1;

		if (this.right != null && this.right.equals(o))
			return current + 1;

		if (this.left instanceof HuffTreeNode) {
			
			int depth = ((HuffTreeNode) this.left).GetDepth(o, current + 1);
			
			if (depth > -1)
				return depth;			
		}

		if (this.right instanceof HuffTreeNode) {
			
			int depth = ((HuffTreeNode) this.right).GetDepth(o, current + 1);
			
			if (depth > -1)
				return depth;			
		}

		return -1;
	}	

}
