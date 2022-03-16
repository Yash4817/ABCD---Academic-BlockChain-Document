import Includes.*;
import java.util.*;

public class MerkleTree{

	// Check the TreeNode.java file for more details
	public TreeNode rootnode;
	public int numstudents;

	public String Build(List<Pair<String,Integer>> documents){
		 ArrayList<TreeNode> children = new ArrayList<>();
                for(int i=0;i<documents.size();i++){
                        TreeNode child = new TreeNode();
                        child.left =null;
                        child.right = null;
                        child.val = documents.get(i).get_first()+"_"+documents.get(i).get_second();
			child.maxleafval = documents.get(i).get_second();
			child.numberLeaves=1;
			child.isLeaf = true;
                        children.add(child);
                        }
                numstudents = children.size();
                CRF obj = new CRF(64);
                while(children.size() != 1){
                         ArrayList<TreeNode> parents = new ArrayList<>();

                        for(int i=0;i<children.size();i+=2 ){
                                TreeNode father = new TreeNode();
                                father.left = children.get(i);
                                children.get(i).parent = father;
                                father.right = children.get(i+1);
                                children.get(i+1).parent = father;
                                father.val = obj.Fn(children.get(i).val + "#" + children.get(i+1).val);
				father.maxleafval = Math.max(father.left.maxleafval,father.right.maxleafval);
				father.numberLeaves = father.left.numberLeaves + father.right.numberLeaves;
				father.isLeaf = false;
                                parents.add(father);
                        }
                        children = parents;
                }
                rootnode = children.get(0);
		//System.out.println("rootnode.val ="+rootnode.val);
                return rootnode.val;
	}

	/*
		Pair is a generic data structure defined in the Pair.java file
		It has two attributes - First and Second, that can be set either manually or
		using the constructor

		Edit: The constructor is added
	*/


	public String UpdateDocument(int student_id, int newScore){
		int t = (int)(Math.log(numstudents)/Math.log(2));
                String newval = "";
	       	if(numstudents==1){
                        TreeNode curr = this.rootnode;
		
			for(int i=0;i<curr.val.length();i++){
				if(curr.val.charAt(i) == '_'){
					break;
				}	
				newval += curr.val.charAt(i);
			}
			curr.val = newval + "_" + newScore;
			System.out.println(curr.val);
                        curr.maxleafval = newScore;
                        return curr.val;
                }
                int temp = student_id;
                ArrayList<Integer> path = new ArrayList<>();
                path.add(temp);
                for(int i=0;i<t-1;i++){
                        if(temp % 2 ==0){
                                temp = temp/2;
                                path.add(temp);
                        }
                        else{
                                temp = (temp-1)/2;
                                path.add(temp);
                        }
                }
                TreeNode curr = this.rootnode;
                int j = path.size();
                for(int i=0;i<j;i++){
                        if(path.get(j-i-1)%2==0){
                                curr = curr.left;
                        }else{
                                curr = curr.right;
                        }

                }
                
		for(int i=0;i<curr.val.length();i++){
                                if(curr.val.charAt(i) == '_'){
                                        break;
                                }
                                newval = newval + curr.val.charAt(i);
                       
		 }
                        curr.val = newval + "_" + newScore;
                        curr.maxleafval = newScore;
			
                CRF obj = new CRF(64);
                for(int i=0;i<j;i++){
                        curr = curr.parent;
                        curr.val = obj.Fn(curr.left.val + "#" + curr.right.val);
			curr.maxleafval = Math.max(curr.left.maxleafval,curr.right.maxleafval);
                }
                return curr.val;
	}
	public List<Pair<String,String>> QueryDocument(int doc_idx){
		int t = (int)(Math.log(numstudents)/Math.log(2));
		int temp = doc_idx;
		ArrayList<Integer> path = new ArrayList<>();
		path.add(temp);
		for(int i=0;i<t-1;i++){
			if(temp % 2 ==0){
				temp = temp/2;
				path.add(temp);
			}
			else{
				temp = (temp-1)/2;
				path.add(temp);
			}
		}
		int j = path.size();
		ArrayList<Pair<String,String>> p = new ArrayList<>();
		TreeNode curr = this.rootnode;
				for(int i=0;i<j;i++){
			if(path.get(j-i-1)%2==0){
                                curr = curr.left;
                        }else{
                                curr = curr.right;
                        }

		}
		for(int i=0;i<j;i++){
			curr = curr.parent;
			p.add(new Pair<String,String>(curr.left.val,curr.right.val)); 
		}
		p.add(new Pair<String,String>(curr.val,null));
		return p;
      	}
}
