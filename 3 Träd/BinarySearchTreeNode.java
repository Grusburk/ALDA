// Mattin Lotfi malo5163
package alda.tree;

/**
 * Denna klass representerar noderna i ett binärt sökträd utan dubletter.
 * 
 * Detta är den enda av de tre klasserna ni ska göra några ändringar i. (Om ni
 * inte vill lägga till fler testfall.) De ändringar som är tillåtna är dock
 * begränsade av följande regler:
 * <ul>
 * <li>Ni får INTE lägga till några fler instansvariabler.
 * <li>Ni får INTE lägga till några statiska variabler.
 * <li>Ni får INTE använda några loopar någonstans.
 * <li>Ni FÅR lägga till fler metoder, dessa ska då vara privata.
 * </ul>
 * 
 * @author henrikbe
 * 
 * @param <T>
 */
public class BinarySearchTreeNode<T extends Comparable<T>> {

	private T data;
	private BinarySearchTreeNode<T> left;
	private BinarySearchTreeNode<T> right;

	public BinarySearchTreeNode(T data) {
		this.data = data;
	}

	/**
	 * Lägger till en nod i det binära sökträdet. Om noden redan existerar så
	 * lämnas trädet oförändrat.
	 * 
	 * @param data
	 *            datat för noden som ska läggas till.
	 * @return true om en ny nod lades till trädet.
	 */
	public boolean add(T data) {
		if (data.compareTo(this.data) < 0) {
			if (left == null) {
				left = new BinarySearchTreeNode<>(data);
				return true;
			} else{
				return left.add(data);
			}
		}
		if (data.compareTo(this.data) > 0) {
			if (right == null) {
				right = new BinarySearchTreeNode<>(data);
				return true;
			} else{
				return right.add(data);
			}
		}
		return false;
	}

	/**
	 * Privat hjälpmetod som är till nytta vid borttag. Ni behöver inte
	 * skriva/utnyttja denna metod om ni inte vill.
	 * 
	 * @return det minsta elementet i det (sub)träd som noden utgör root i.
	 */
	private T findMin() {
		return null;
	}
	private T findMax() {
		if (this.right != null) {
			return this.right.findMax();
		} else {
			return this.data;
		}
	}


	/**
	 * Tar bort ett element ur tr det. Om elementet inte existerar s l mnas
	 * tr det of r ndrat.
	 *
	 * @param data
	 *            elementet som ska tas bort ur tr det.
	 * @return en referens till nodens subtr d efter borttaget.
	 */
	public BinarySearchTreeNode<T> remove(T data) {
		return remove(data, this, null);
	}

	private boolean hasRightNode() {
		return right != null;
	}

	private boolean hasLeftNode() {
		return left != null;
	}

	private boolean isNodeALeaf() {
		return !hasLeftNode() && !hasRightNode();
	}

	/* Denna var riktigt svår, fick göra om från början antalet gånger. Som källa för att förstå visuellt hur trädet fungera
	* Använde jag mig utav https://www.cs.usfca.edu/~galles/visualization/BST.html
	* Denna sidan hjälpte mig mycket med att förstå hur noderna skulle gå och flytta sig.
	* Slutade upp med att när testaddRandomRemove hade failat så la jag in alla noder på hemsidan för att se varför min kod skrev ut fel och felsökte genom att
	* visuellt se hur det skall gå till och vad som gick fel när jag skrivit ut detta.
	 */
	private BinarySearchTreeNode<T> remove(T data,  BinarySearchTreeNode<T> parent, BinarySearchTreeNode<T> currentParent) {
		if (data.compareTo(this.data) == 0) {
			if (isNodeALeaf()) {
				if (data.compareTo(currentParent.data) < 0) {
					currentParent.left = null;
				} else {
					currentParent.right = null;
				}
			} else {
				if (hasLeftNode() && hasRightNode()) {
					T temp = left.findMax();
					remove(left.findMax(), parent, this);
					this.data = temp;
					return parent;
				} else if(hasLeftNode() && currentParent == null) {
					parent = this.left;
					return parent;
				} else if (hasRightNode() && currentParent == null) {
					parent = this.right;
					return parent;
				}
				if (data.compareTo(currentParent.data) < 0) {
					if (this.left == null) {
						currentParent.left = right;
					} else {
						currentParent.left = left;
					}
					return parent;
				} else {
					if (this.right == null) {
						currentParent.right = left;
					} else {
						currentParent.right = right;
					}
					return parent;
				}
			}
		}
		if (this.data!=null && data.compareTo(this.data) < 0){
			if (left != null) {
				return left.remove(data, parent, this);
			}
		}
		if (this.data != null && data.compareTo(this.data) > 0) {
			if (right != null) {
				return right.remove(data, parent, this);
			}
		}
		return parent;
	}

	/**
	 * Kontrollerar om ett givet element finns i det (sub)träd som noden utgör
	 * root i.
	 *
	 * @param data
	 *            det sökta elementet.
	 * @return true om det sökta elementet finns i det (sub)träd som noden utgör
	 *         root i.
	 */
	public boolean contains(T data) {
		if (this.data.compareTo(data) == 0) {
			return true;
		}

		if (left != null &&data.compareTo(this.data) < 0 && left.contains(data)) {
			return true;
		}

		if (right != null && data.compareTo(this.data) > 0 && right.contains(data)){
			return true;
		}
			return false;
	}

	/**
	 * Storleken på det (sub)träd som noden utgör root i.
	 * 
	 * @return det totala antalet noder i det (sub)träd som noden utgör root i.
	 */
	public int size() {
		int calculated = 1;
		if (this.left != null){
			calculated += this.left.size();
		}
		if (this.right != null){
			calculated += this.right.size();
		}
		return (calculated);
	}


	/**
	 * Det högsta djupet i det (sub)träd som noden utgör root i.
	 * 
	 * @return djupet.
	 */
	public int depth() {
		int leftDepthCalculation = 0;
		int rightDepthCalculation = 0;
		if (this.left != null){
			leftDepthCalculation++;
			leftDepthCalculation += this.left.depth();
		}
		if (this.right != null){
			rightDepthCalculation++;
			rightDepthCalculation += this.right.depth();
		}
		if (rightDepthCalculation > leftDepthCalculation){
			return rightDepthCalculation;
		} else {
			return leftDepthCalculation;
		}
	}

	/**
	 * Returnerar en strängrepresentation för det (sub)träd som noden utgör root
	 * i. Denna representation består av elementens dataobjekt i sorterad
	 * ordning med ", " mellan elementen.
	 * 
	 * @return strängrepresentationen för det (sub)träd som noden utgör root i.
	 */
	public String toString() {
		String output = "";

		if (left != null) {
			output += left + ", ";
		}
		output += data;
		if (right != null) {
			output += ", " + right;
		}
		return output;
	}
}
