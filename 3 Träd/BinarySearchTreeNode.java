// Mattin Lotfi malo5163
package alda.tree;

/**
 * Denna klass representerar noderna i ett bin�rt s�ktr�d utan dubletter.
 * 
 * Detta �r den enda av de tre klasserna ni ska g�ra n�gra �ndringar i. (Om ni
 * inte vill l�gga till fler testfall.) De �ndringar som �r till�tna �r dock
 * begr�nsade av f�ljande regler:
 * <ul>
 * <li>Ni f�r INTE l�gga till n�gra fler instansvariabler.
 * <li>Ni f�r INTE l�gga till n�gra statiska variabler.
 * <li>Ni f�r INTE anv�nda n�gra loopar n�gonstans.
 * <li>Ni F�R l�gga till fler metoder, dessa ska d� vara privata.
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
	 * L�gger till en nod i det bin�ra s�ktr�det. Om noden redan existerar s�
	 * l�mnas tr�det of�r�ndrat.
	 * 
	 * @param data
	 *            datat f�r noden som ska l�ggas till.
	 * @return true om en ny nod lades till tr�det.
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
	 * Privat hj�lpmetod som �r till nytta vid borttag. Ni beh�ver inte
	 * skriva/utnyttja denna metod om ni inte vill.
	 * 
	 * @return det minsta elementet i det (sub)tr�d som noden utg�r root i.
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

	/* Denna var riktigt sv�r, fick g�ra om fr�n b�rjan antalet g�nger. Som k�lla f�r att f�rst� visuellt hur tr�det fungera
	* Anv�nde jag mig utav https://www.cs.usfca.edu/~galles/visualization/BST.html
	* Denna sidan hj�lpte mig mycket med att f�rst� hur noderna skulle g� och flytta sig.
	* Slutade upp med att n�r testaddRandomRemove hade failat s� la jag in alla noder p� hemsidan f�r att se varf�r min kod skrev ut fel och fels�kte genom att
	* visuellt se hur det skall g� till och vad som gick fel n�r jag skrivit ut detta.
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
	 * Kontrollerar om ett givet element finns i det (sub)tr�d som noden utg�r
	 * root i.
	 *
	 * @param data
	 *            det s�kta elementet.
	 * @return true om det s�kta elementet finns i det (sub)tr�d som noden utg�r
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
	 * Storleken p� det (sub)tr�d som noden utg�r root i.
	 * 
	 * @return det totala antalet noder i det (sub)tr�d som noden utg�r root i.
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
	 * Det h�gsta djupet i det (sub)tr�d som noden utg�r root i.
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
	 * Returnerar en str�ngrepresentation f�r det (sub)tr�d som noden utg�r root
	 * i. Denna representation best�r av elementens dataobjekt i sorterad
	 * ordning med ", " mellan elementen.
	 * 
	 * @return str�ngrepresentationen f�r det (sub)tr�d som noden utg�r root i.
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
