import javax.swing.*;
import java.util.*;

class BST {
    class Node{
        int val;
        Node left, right, parent;
        public Node(int val) {
            this.val = val;
            left = null;
            right = null;
            parent = null;
        }
    }
    Node root;
    BST(){
        root = null;
    }
    //QUESTION 1
    //RECURSIVELY
    void insertRec(int val){
        root = insertRecHelper(root, val);
    }
    Node insertRecHelper(Node root, int val){
        if(root == null){
            root = new Node(val);
            return root;
        }
        if(val < root.val) {
            Node lefty = insertRecHelper(root.left, val);
            root.left = lefty;
            lefty.parent = root;
        }
        else {
            Node righty = insertRecHelper(root.right, val);
            root.right = righty;
            righty.parent = root;
        }
        return root;
    }
    public Node deleteRec(int val){
        return deleteRecHelper(root, val);
    }
    public Node deleteRecHelper(Node root, int val){
        if(root == null){
            return null;
        }
        if(root.val == val){
            if(root.left == null && root.right == null){
                root = null;
            }
            else if(root.left == null){
                root.right.parent = root.parent;
                return root.right;
            }
            else if(root.right == null) {
                root.left.parent = root.parent;
                return root.left;
            }
            else
            {
                Node n = findNextRec(root);
                root.val = n.val;
                root.right = deleteRecHelper(root.right, n.val);
            }
        }
        else if(root.val > val){
            root.left = deleteRecHelper(root.left, val);
        }
        else{
            root.right = deleteRecHelper(root.right, val);
        }
        return root;
    }
    public static Node findNextRec(Node node){
        if(node.right == null){
            //check the parents
            Node n =  findNextRecHelper(node, node.val);
            if(n.val > node.val){
                return n;
            }
            else{
                return null;
            }
        }
        else{            //check minimum of the subtree
            return findMinRec(node.right);
        }
    }
    public static Node findNextRecHelper(Node node, int val){
        if(node != null && node.val > val){
            return node;
        }
        else if(node.parent != null){
            return findNextRecHelper(node.parent, val);
        }
        return node;
    }
    public static Node findPrevRec(Node node){
        if(node.left == null){
            Node n =  findPrevRecHelper(node, node.val);
            if(n.val < node.val){
                return n;
            }
            else{
                return null;
            }
        }
        else{
            return findMaxRec(node.left);
        }
    }
    public static Node findPrevRecHelper(Node node, int val){
        if(node != null && node.val < val){
            return node;
        }
        else if(node.parent != null){
            return findPrevRecHelper(node.parent, val);
        }
        return node;
    }
    public static Node findMinRec(Node root){
//            while(root.left != null){
//                root = root.left;
//            }
//            return root;
        if(root.left != null){
            return findMinRec(root.left);
        }
        else{
            return root;
        }
    }
    public static Node findMaxRec(Node root){
        if(root.right != null){
            return findMaxRec(root.right);
        }
        else{
            return root;
        }
    }
    //ITERATIVELY
    public void insertIter(int val){
        if(root == null){
            root = new Node(val);
            return;
        }
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        while(queue.size() != 0){
            int n = queue.size();
            for(int i = 0; i < n; i++){
                Node curr = queue.remove();
                if(curr.left == null && val < curr.val){
                    curr.left = new Node(val);
                    curr.left.parent = curr;
                    break;
                }
                else if(curr.right == null && val > curr.val){
                    curr.right = new Node(val);
                    curr.right.parent = curr;
                    break;
                }
                if(val < curr.val){
                    queue.add(curr.left);
                }
                else{
                    queue.add(curr.right);
                }
            }
        }
    }
    public Node deleteIter(int val){
        if(root == null){
            return null;
        }
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        Node prev = null;
        while(queue.size() != 0){
            int n = queue.size();
            for(int i = 0; i < n; i++){
                Node curr = queue.remove();
                if(curr.val == val){
                    if(curr.left == null && curr.right == null){
                        if(prev != null){
                            if(prev.val < curr.val){
                                prev.right = null;
                            }
                            else{
                                prev.left = null;
                            }
                        }
                    }
                    else if(curr.left == null){
                        if(prev.val < curr.val) {
                            prev.right = curr.right;
                        }
                        else{
                            prev.left = curr.right;
                        }
                        curr.right.parent = prev;
                    }
                    else if(curr.right == null){
                        if(prev.val<curr.val){
                            prev.right = curr.left;
                        }
                        else{
                            prev.left = curr.left;
                        }
                        curr.left.parent = prev;
                    }
                    else{
                        Node temp = findNextIter(curr);
                        curr.val = temp.val;
                        val = curr.val;
                        prev = curr;
                        queue.add(curr.right);
                    }
                }
                else if(val < curr.val){
                    queue.add(curr.left);
                    prev = curr;
                }
                else{
                    queue.add(curr.right);
                    prev = curr;
                }
            }
        }
        return root;
    }
    public static Node findNextIter(Node node){
        if(node.right == null){
            Node curr = node;
            while(curr.val <= node.val && curr.parent != null){
                curr = curr.parent;
            }
            if(curr.parent == null && curr.val < node.val){
                return null;
            }
            return curr;
        }
        else{
            return findMinIter(node.right);
        }
    }
    public static Node findPrevIter(Node node){
        if(node.left == null){
            Node curr = node;
            while(curr.val >= node.val && curr.parent != null){
                curr = curr.parent;
            }
            if(curr.parent == null){
                return null;
            }
            return curr;
        }
        else{
            return findMaxIter(node.left);
        }
    }
    public static Node findMinIter(Node node){
        while(node.left != null){
            node = node.left;
        }
        return node;
    }
    public static Node findMaxIter(Node node){
        while(node.right != null){
            node = node.right;
        }
        return node;
    }{}

    //QUESTION 2: SORT
    int[] sort(Node root){
        List<Integer> list = new ArrayList<>();
        inOrder(root, list);
        int[] a = new int[list.size()];
        for(int i = 0; i < list.size(); i++){
            a[i] = list.get(i);
        }
        return a;
    }

    //QUESTION 3A
    public static int[] getRandomArray(int n){
        int[] a = new int[n];
        List<Integer> list = new ArrayList<Integer>();
        HashSet<Integer> h = new HashSet<Integer>();
        while(list.size() != n){
            int sum = (int)(Math.random() * n);
            if(!h.contains(sum)) {
                list.add(sum);
                h.add(sum);
            }
            else{
                continue;
            }
        }
        for(int i = 0; i < n; i++){
            a[i] = list.get(i);
        }
        return a;
    }
    //QUESTION 3B
    public static int[] getSortedArray(int n){
        int[] a = new int[n];
        int temp = n;
        for(int i = 0; i < n; i++){
            a[i] = temp--;
        }
        return a;
    }

    BST populateBST(int[] nums){
        BST tree = new BST();
        for(int i = 0; i < nums.length; i++){
            tree.insertRec(nums[i]);
        }
        return tree;
    }

    public static void inOrder(Node root, List<Integer> list){
        if(root != null) {
            inOrder(root.left, list);
            list.add(root.val);
            inOrder(root.right, list);
        }
    }
    StringBuilder print(){
        StringBuilder s = new StringBuilder();
        inOrderPrint(this.root, s);
        return s;
    }
    public static void inOrderPrint(Node root, StringBuilder s){
//        if(root != null){
//
//            s.append(root.val + " ");
//            inOrderPrint(root.left, s);
//
//            inOrderPrint(root.right, s);
//
//        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while(queue.size() != 0){
            int n = queue.size();
            for(int i = 0; i < n; i++){
                Node node = queue.remove();
                s.append(node.val + " ");
                if(node.left != null ){
                    queue.add(node.left);
                }
                if(node.right != null ){
                    queue.add(node.right);
                }
            }
        }
    }

    public static void main(String[] args){
        BST tree = new BST();
        tree.insertRec(13);
        tree.insertRec(6);
        tree.insertRec(18);
        tree.insertRec(4);
        tree.insertRec(8);
        tree.insertRec(20);
        tree.insertRec(15);
        tree.insertRec(7);
        tree.insertRec(11);
        tree.insertRec(9);
        tree.insertRec(12);
        tree.insertRec(10);
        tree.insertRec(19);
        System.out.println(tree.print());
        tree.deleteRec(6);
        System.out.println(tree.print());
        System.out.println(tree.root.left.right.right.right.val);
        System.out.println("Prev: " + tree.findPrevRec(tree.root.left.right.right.right).val);
        System.out.println("Next: " + tree.findNextRec(tree.root.left.right.right.right).val);
        System.out.println(tree.root.right.right.left.val);
        System.out.println("Prev: " + tree.findPrevRec(tree.root.right.right.left).val);
        System.out.println("Next: " + tree.findNextRec(tree.root.right.right.left).val);
        BST tree2 = new BST();
        tree2.insertIter(13);
        tree2.insertIter(6);
        tree2.insertIter(18);
        tree2.insertIter(4);
        tree2.insertIter(8);
        tree2.insertIter(20);
        tree2.insertIter(15);
        tree2.insertIter(7);
        tree2.insertIter(11);
        tree2.insertIter(9);
        tree2.insertIter(12);
        tree2.insertIter(10);
        tree2.insertIter(19);
        System.out.println(tree2.print());
        tree2.deleteIter(6);
        System.out.println(tree2.print());
        System.out.println(tree.root.left.right.right.right.val);
        System.out.println("Prev: " + tree2.findPrevIter(tree.root.left.right.right.right).val);
        System.out.println("Next: " + tree2.findNextIter(tree.root.left.right.right.right).val);
        System.out.println(tree2.root.right.right.left.val);
        System.out.println("Prev: " + tree2.findPrevIter(tree2.root.right.right.left).val);
        System.out.println("Next: " + tree2.findNextIter(tree2.root.right.right.left).val);

    }
}
