
public class anaNode{
	private String orig = "";
	private anaNode next = null, tail = null;
	private long key = 0;
	private int[] primes = {101, 97, 89, 83, 79, 73, 71, 67, 61, 59, 53, 47, 43, 41, 37, 31, 29, 23, 19, 17, 13, 11, 7, 5, 3, 2}; //List of prime numbers used for keygen/primeify
	public anaNode(String o){ //constructer. generates the key on each creation.
		orig = o;
		next = null;
		key = keyGen(o);
	}
	public long getKey(){ //returns the key. not all nodes store a key, so this regenerates it if necessary.
		if(key == 0)
			return keyGen(orig);
		else
			return key;

	}
	public void saveKey(){ // saves a key to one that was deleted. never used in the program as it stands, but it was at one point.
	key = keyGen(orig);
	}
	public void delKey(){ // removes key
		key = 0;
	}
	public void setTail(anaNode n){ // sets a pointer to the tail of the list. this is only used on the nodes that become heads of lists, which are those that are inserted first.
		tail = n; // this speeds up insertion by preventing the need to walk through the list.
	}
	public anaNode getTail(){// same as above, only returns tail instead of sets.
		return tail;
	}
	public String getOrig(){ // get word
		return orig;
	}
	public boolean hasNext(){ //used for toString method to go down chains of anagrams
		return next != null;
	}
	public anaNode getNext(){ // same as above.
		return next;
	}
	public void setNext(anaNode n){ //sets next node in linked list. each linked list is comprised of anagrams
		next = n;
	}

	public String toString(){// goes through each list and returns the line of anagrams.
		String str = "";
		if(hasNext())
			str = str +" "+ orig + " " + next.toString();
		else
			str = str + " " + orig;
		return str;
	}

	private long keyGen(String in){// unique identifier generation.
		long gKey = 1;
		for(int i = 0; i < in.length(); i++){// for each letter of the word:
			gKey *= primeify(in.charAt(i)); // get the prime specified for each letter, and multiply that by the key.

			if(gKey > Math.pow(2, 63))// if it is getting close to the end of the size of a long:
			gKey = (gKey % (long)Math.pow(2, 63)); //Make it smaller
			if(gKey < 1)//if negative:
			gKey *= -1;// flip sign


			}

		return gKey;// final key
	}

	private int primeify(char in){ //sub out letter for an arbitrarily chosen prime
		int temp = (int)in; // get ascii
		if(temp > 96 && temp < 123)//for lowercase
			return primes[temp - 97];// get prime
		else if(temp > 64 && temp < 91)//for uppercase
			return primes[temp - 65];// get prime
		else
			return 1; // anything else. should never be triggered.
	}


}