import Includes.*;
import java.util.*;

public class BlockChain{
	// PLEASE USE YOUR ENTRY NUMBER AS THE START STRING
	public static final String start_string = "LabModule5";
	public Block firstblock;
	public Block lastblock;

	/*
		Note that the Exceptions have already been defined for you in the includes file,
		you just have to raise them accordingly

	*/

	public String InsertBlock(List<Pair<String,Integer>> Documents, int inputyear){
		Block obj = new Block();
		CRF crf = new CRF(64);
		obj.year = inputyear;
		String rootval;
		MerkleTree studlist = new MerkleTree();
		rootval = studlist.Build(Documents);
		//System.out.println(studlist.Build(Documents));
		obj.mtree = studlist;
		obj.value = rootval + "_" + obj.mtree.rootnode.maxleafval;
		if(firstblock == null && lastblock == null){
			obj.previous = null;
			obj.next = null;
			firstblock = obj;
			lastblock = obj;
			obj.dgst = crf.Fn(this.start_string + "#" + obj.value);
			 
		}
		else{
			obj.previous = lastblock;
			lastblock.next = obj;
			obj.next = null;
			lastblock = obj;
			obj.dgst = crf.Fn(obj.previous.dgst + "#" + obj.value);
		}
	//System.out.println(obj.value + " " + obj.year + " " + obj.dgst);
		
		return obj.dgst;
	}

	public Pair<List<Pair<String,String>>, List<Pair<String,String>>> ProofofScore(int student_id, int year){
		Block temp = new Block();
		temp = firstblock;
		while(temp.year != year){
			temp = temp.next;
		}	
		List<Pair<String,String>> p1 =  temp.mtree.QueryDocument(student_id);
		ArrayList<Pair<String,String>> p2 = new ArrayList<>(); 
		while(temp!=null){
			p2.add(new Pair<String,String>(temp.value,temp.dgst));
			temp = temp.next;
		}
		Pair<List<Pair<String,String>>, List<Pair<String,String>>> p = new Pair<>();
		p.First = p1;
		p.Second = p2;
		return p;
	}
}
