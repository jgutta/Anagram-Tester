import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.PrintWriter;
//To run dict1, give the filename as a commandline argument. The program will also default to dict1 if nofile is provided.
//to run dict2, use the flag -Xmx2048m, and give dict2 as an argument.

public class Anagrams{
	static int ArrSize = 500000; // The size of the hashtable
	static int Anagram = 0;// Number of anagram classes
	static String file = ""; // filename. called as static so can be used to name output. Expecting the format dict#, output will be anagram#
	public static anaNode[] bigger(anaNode[] table){ // In case the hashtable gets too full, this function remapps the old hashtable to a larger one.
		int temp = ArrSize; // old array size
		ArrSize *= 1.3; //new array size
		anaNode[] bigger = new anaNode[ArrSize];
		for(int i = 0; i <temp; i++)
			bigger = insert(table[i], bigger, (int)(table[i].getKey() % ArrSize)); // for every entry in the old array, rehash to the new array and return that.
		return bigger;

	}

	public static anaNode[] insert(anaNode a, anaNode[] table, int index){// Used to insert nodes to the table, chain for anagrams and linearly probe otherwise
						if(table[index] == null){ // if the space is free, insert the word there and add a new anagram class to the count.
							table[index] = a;
							Anagram++;
						}
						else if(table[index] != null && table[index].getKey() == a.getKey()){ // if there is a collision of two words with the same key, they are the same word.
							if(table[index].hasNext()){
								table[index].getTail().setNext(a); // add to the end of the list using a tail pointer if the length of the linked list >= 2
								table[index].setTail(a);
							}
							else{
								table[index].setNext(a); // add the second entry to the list
								table[index].setTail(a);
							}
							table[index].delKey(); //because nothing added here is the head of a list, there is no reason to maintain the key. it is deleted to conserve memory.

						}
						else{//if there is a collision  and the words are not the same:
							while(table[index] != null && table[index].getKey() != a.getKey())// walk through the table until we find an open space or an equivalent key
									index = (index + 1) % ArrSize;// there is a risk of an infinte loop here, but that is prevented by a check in the main that makes sure the table
																	//is never completely full.

							table = insert(a, table, index);// insert at the new index.

						}
					return table;
	}

	public static void dump(anaNode[] hashT){ //dumps contents of the hash table to text file. This prints all anagram classes on the same line.
		String name = "anagram" + file.charAt(file.length() - 1) + ".txt";
		try{	PrintWriter out = new PrintWriter(name, "UTF-8");
			out.println("Number of Anagram Classes/ Lines: " + Anagram);
			for(int i = 0; i < ArrSize; i++)
				if(hashT[i] != null){
					out.println(hashT[i].toString() + "\n");


			}
			out.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public static void main(String []args){
		if(args.length > 0)
		file = args[0];// takes file from args
		else{
		System.out.println("NO FILE GIVEN, DEFAULT TO DICT1");
		file = "dict1";
		}

		anaNode[] hashT = new anaNode[ArrSize]; //main hash table array
		int id = 0; //holder for the key % array size

		try{ BufferedReader diction = new BufferedReader(new FileReader(file));
			while(diction.ready()){ //while there is a line to be read:
				anaNode nud = new anaNode(diction.readLine());// read the line and store it in a node. this also generates the key.
				id = (int)(nud.getKey() % ArrSize);// get the index where the word maps in the hash table
				hashT = insert(nud, hashT, id);// insert at that index

				if(Anagram / ArrSize > .9)// if the table is more than 90% full, move entries to a bigger table. The threshold is high because lookup time is unimportant,
					hashT = bigger(hashT);//  but after a certain point insert will take a very long time. This also avoids an infinite loop in the case where there are no indicies left in
									// the array to occupy.
		}
		diction.close();// close reader to free resources
		dump(hashT); // print to file.


		}
		catch (Exception e) { System.out.println(e);
		}


	}
}