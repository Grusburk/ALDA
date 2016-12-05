/*
 * Henrik Thulin heth7132
 * Mattin Lotfi malo5163
 */

package HuffClass;

public class Tuple<X, Y> {
	
	private X t1;
	private Y t2;
	
	public Tuple(X t1, Y t2) {
		
		this.t1 = t1;
		this.t2 = t2;			
	}
	
	public X getT1() {
		return this.t1;
	}
	
	public Y getT2() {
		return this.t2;
	}
	
	public String toString() {
		return "["+this.t1+":"+this.t2+"]";
	}
}
