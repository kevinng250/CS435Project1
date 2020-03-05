import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

class AVL {
    class Node{
        int val;
        int height;
        Node left, right, parent;
        public Node(int val) {
            this.val = val;
            left = null;
            right = null;
            parent = null;
            height = 0;
        }
    }
    Node root;
    AVL(){
        root = null;
    }
    int getHeight(Node node){
        if(node == null){
            return 0;
        }
        else{
            return node.height;
        }
        //retrieve height before rotation
    }
    int updateHeight(Node node){
        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }
    Node rightRotation(Node node){
        Node original = node;
        Node n = node.left;
        Node temp = node.left.right;
        original.left = temp;
        if(temp!= null) {
            temp.parent = original;
        }
        n.right = original;
        if(node.parent != null){
            n.parent = node.parent;
        }
        original.parent = n;
        original.height = updateHeight(original);
        n.height = updateHeight(n);
        return n;
    }
    Node leftRotation(Node node){
        Node original = node;
        Node n = node.right;
        Node temp = node.right.left;
        original.right = temp;
        if(temp != null){
            temp.parent = original;
        }
        n.left = original;
        if(node.parent != null){
            n.parent = node.parent;
        }
        original.parent = n;
        original.height = updateHeight(original);
        n.height = updateHeight(n);
        return n;
    }
    Integer maxTraversed = 0;
    //ITERATIVELY
    public void insertIter(int val){
        if(root == null){
            root = new Node(val);
            root.height = 1;
            return;
        }
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        int count = 1;
        Node save = root; //Will eventually save the inserted node
        while(queue.size() != 0) {
            int n = queue.size();
            for (int i = 0; i < n; i++) {
                Node curr = queue.remove();
                if (curr.left == null && val < curr.val) {
                    curr.left = new Node(val);
                    curr.left.parent = curr;
                    save = curr.left;
                    break;
                } else if (curr.right == null && val > curr.val) {
                    curr.right = new Node(val);
                    curr.right.parent = curr;
                    save = curr.right;
                    break;
                }
                if (val < curr.val) {
                    queue.add(curr.left);

                } else {
                    queue.add(curr.right);
                }
            }
            count++;
        }
        this.maxTraversed = Math.max(this.maxTraversed, count);
        //BALANCE
        //starts from inserted node and goes up the parents
        Node curr = save;
        while(curr != null){
            curr.height = updateHeight(curr);
            int bf = getHeight(curr.left) - getHeight(curr.right);
            if(bf > 1 || bf < -1){
                //curr is the root
                if(curr.parent == null){
                    Node n = balance(curr, bf);
                    n.parent = null;
                    this.root = n;
                }
                //curr is the left child of parent
                else if(curr.parent.val > curr.val){
                    curr = curr.parent;
                    Node n = balance(curr.left, bf);
                    curr.left = n;
                }
                //curr is the right child of parent
                else{
                    curr = curr.parent;
                    Node n = balance(curr.right, bf);
                    curr.right = n;
                }
            }
            curr = curr.parent;
        }
    }
    public Node deleteIter(int val){
        if(root == null){
            maxTraversed = 1;
            return null;
        }
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        Node prev = null;
        //prev Node used to keep track of the parent node
        Stack<Node> stack = new Stack<Node>();
        Node save = root;
        int count = 1;
        while(queue.size() != 0){
            System.out.println(count);
            int n = queue.size();
            for(int i = 0; i < n; i++){
                Node curr = queue.remove();
                if(curr.val == val){
                    //First Case - No children
                    if(curr.left == null && curr.right == null){
                        save = prev;
                        if(prev != null){
                            if(prev.val < curr.val){
                                prev.right = null;
                            }
                            else{
                                prev.left = null;
                            }
                        }
                    }
                    //Second Case
                    //No left child
                    else if(curr.left == null){
                        save = prev;
                        if(prev.val < curr.val) {
                            prev.right = curr.right;
                        }
                        else{
                            prev.left = curr.right;
                        }
                        curr.right.parent = prev;
                    }
                    //No right child
                    else if(curr.right == null){
                        save = prev;
                        if(prev.val<curr.val){
                            prev.right = curr.left;
                        }
                        else{
                            prev.left = curr.left;
                        }
                        curr.left.parent = prev;
                    }
                    //Third Case - both children exist
                    else{
                        Node temp = findNextIter(curr); //Finds next greatest node
                        curr.val = temp.val;
                        val = curr.val;                 //Reset the the val to the val being deleted
                        prev = curr;
                        queue.add(curr.right);          //Continue traversal
                    }
                }
                //Add children of current
                //Set previous equal to current
                else if(val < curr.val){
                    queue.add(curr.left);
                    prev = curr;
                }
                else{
                    queue.add(curr.right);
                    prev = curr;
                }
            }
            //Keep track of traversal count;
            count++;
        }
        System.out.println(prev.val);
        Node curr = prev;
        //Starts from previous of deleted node and goes up the parents
        while(curr!= null){
            curr.height = updateHeight(curr);//Update Height
            int bf = getHeight(curr.left) - getHeight(curr.right); //Gets the balance factor
            if(bf > 1 || bf < -1){
                //curr is at the root
                if(curr.parent == null){
                    Node n = balance(curr, bf);
                    n.parent = null;
                    this.root = n;  //Since curr is root, the return balanced node becomes the root
                }
                //curr is the left child of parent
                else if(curr.parent.val > curr.val){
                    curr = curr.parent;
                    Node n = balance(curr.left, bf);
                    curr.left = n;
                }
                //curr is the right child of parent
                else{
                    curr = curr.parent;
                    Node n = balance(curr.right, bf);
                    curr.right = n;
                }
            }
            //updates curr to parent
            curr = curr.parent;
        }
        return root;
    }
    Node balance(Node node, int balanceFactor){
        if(balanceFactor > 1){
            if((getHeight(node.left.left) - getHeight(node.left.right)) > 0){
                //Left Left Case
                Node n = rightRotation(node);

                return n;
            }
            else{
                //Left Right Case - Right heavy
                node.left = leftRotation(node.left);
                return rightRotation(node);
            }
        }
        else if(balanceFactor < -1){
            if((getHeight(node.right.left) - getHeight(node.right.right) < 0)){
                //Right Right Case
                return leftRotation(node);
            }
            else{
                //Right Left Case - Left heavy
                node.right = rightRotation(node.right);
                return leftRotation(node);
            }
        }
        //return a new node containing the rearranged nodes
        return node;
    }
    public static Node findNextIter(Node node){
        if(node.right == null){
            Node curr = node;
            //Traverse the parents
            while(curr.val <= node.val && curr.parent != null){
                curr = curr.parent;
            }
            if(curr.parent == null && curr.val < node.val){
                return null;
            }
            return curr;
        }
        else{
            //Find smallest in subtree
            return findMinIter(node.right);
        }
    }
    public static Node findPrevIter(Node node){
        if(node.left == null){
            Node curr = node;
            //Traverse the parents
            while(curr.val >= node.val && curr.parent != null){
                curr = curr.parent;
            }
            if(curr.parent == null){
                return null;
            }
            return curr;
        }
        else{
            //Finds the Max of the left subtree
            return findMaxIter(node.left);
        }
    }
    public static Node findMinIter(Node node){
        //Keep traversing until left most node is found
        while(node.left != null){
            node = node.left;
        }
        return node;
    }
    public static Node findMaxIter(Node node){
        //keep traversing until right most node is found
        while(node.right != null){
            node = node.right;
        }
        return node;
    }{}

    //QUESTION 2: SORT
    int[] sort(Node root){
        List<Integer> list = new ArrayList<>();
        //does an inOrder traversal
        inOrder(root, list);
        int[] a = new int[list.size()];
        for(int i = 0; i < list.size(); i++){
            a[i] = list.get(i);
        }
        return a;
    }
    public static void inOrder(Node root, List<Integer> list){
        //traverses inOrder and adds to list
        if(root != null) {
            inOrder(root.left, list);
            list.add(root.val);
            inOrder(root.right, list);
        }
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

    //Extra
    BST populateBST(int[] nums){
        BST tree = new BST();
        for(int i = 0; i < nums.length; i++){
            tree.insertRec(nums[i]);
        }
        return tree;
    }

    StringBuilder print(){
        StringBuilder s = new StringBuilder();
        inOrderPrint(this.root, s);
        return s;
    }
    public static void inOrderPrint(Node root, StringBuilder s){
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
//        AVL avl = new AVL();
//        avl.insertIter(7);
//        avl.insertIter(5);
//        avl.insertIter(2);
//        avl.insertIter(15);
//        avl.insertIter(6);
//        avl.insertIter(4);
//        avl.insertIter(1);
//        avl.insertIter(16);
//        avl.insertIter(9);
//        avl.insertIter(3);
//        avl.delete(9);
//        avl.delete(15);
//        System.out.println(avl.print());
//        System.out.println(avl.findNextIter(avl.root.right.right.left).val);
        //5A


        int[] largeRandomArray = getRandomArray(1000000);
        int a = largeRandomArray.length;
        AVL avlIter = new AVL();
        BST bstRec = new BST();

        long bavl1 = System.nanoTime();
        for(int i = 0; i < a; i++){
            avlIter.insertIter(largeRandomArray[i]);
        }
        long aavl1 = System.nanoTime();
        long diff1 = (aavl1 - bavl1);
        System.out.println("Iterative 100000 AVL Random Max Traversal Time: " +  diff1 + " nanoseconds" );

        long bbstRec1 = System.nanoTime();
        for(int i = 0; i < a; i++){
            bstRec.insertRec(largeRandomArray[i]);
        }
        long abstRec1 = System.nanoTime();
        long diff2 = (abstRec1 - bbstRec1);
        System.out.println("Recusive 100000 BST Random Max Traversal Time: " +  diff2 + " nanoseconds" );

//        //5B
        int[] smallRandomArray = getRandomArray(10);
        int b = smallRandomArray.length;
        AVL avlSmall = new AVL();
        BST bstSmall = new BST();
        long bavlSmall = System.nanoTime();
        for(int i = 0; i < b; i++){
            avlSmall.insertIter(smallRandomArray[i]);
        }
        long aavlSmall = System.nanoTime();
        long nanoseconds = (aavlSmall - bavlSmall);
        System.out.println("Iterative Small AVL Random Max Traversal Time: " +  nanoseconds + " nanoseconds" );
        long bbstSmall = System.nanoTime();

        for(int i = 0; i < b; i++){
            bstSmall.insertRec(smallRandomArray[i]);
        }
        long abstSmall = System.nanoTime();
        long nanoseconds1 = (abstSmall - bbstSmall);
        System.out.println("Recursive Small BST Random Max Traversal Time: " +  nanoseconds1 + " nanoseconds" );
//
//        //5c
        BST bstIter = new BST();
        AVL avlIter2 = new AVL();
        long beforeIter2 = System.nanoTime();
        for(int i = 0; i < a; i++){
            avlIter2.insertIter(largeRandomArray[i]);
        }
        long afterIter2 = System.nanoTime();
        long duration = (afterIter2 - beforeIter2);
        long time_sec = TimeUnit.NANOSECONDS.toSeconds(duration);
        System.out.println("Iterative AVL Random Max Traversal: " + avlIter2.maxTraversed + "   Time: " + time_sec + " seconds" );

        long beforeBSTIter = System.nanoTime();
        for(int i = 0; i < a ; i++){
            bstIter.insertIter(largeRandomArray[i]);
        }
        long afterBSTIter = System.nanoTime();
        long duration2 = (afterBSTIter - beforeBSTIter);
        long time_sec2 = TimeUnit.NANOSECONDS.toSeconds(duration2);
//        //6a
        System.out.println("Iterative BST Random Max Traversal: " + bstIter.maxTraversed + "    Time: " + time_sec2 + " seconds");


        int[] sortedArray = getSortedArray(100000);
        int c = sortedArray.length;
        AVL avlSorted = new AVL();
        BST bstSorted = new BST();
        long beforeAVLSorted = System.nanoTime();
        for(int i = 0; i < c; i++){
            avlSorted.insertIter(sortedArray[i]);

        }
        long afterAVLSorted = System.nanoTime();
        long time_sec3 = TimeUnit.NANOSECONDS.toSeconds(afterAVLSorted - beforeAVLSorted);
        System.out.println("Iterative AVL Sorted Max Traversal: " + avlSorted.maxTraversed + "  Time: " + time_sec3 + " seconds");

        long beforeBSTSorted = System.nanoTime();
        for(int i = 0; i < c; i++){
            bstSorted.insertIter(sortedArray[i]);
        }
        long afterBSTSorted = System.nanoTime();
        long time_sec4 = TimeUnit.NANOSECONDS.toSeconds(afterBSTSorted - beforeBSTSorted);
        System.out.println("Iterative BST Sorted Max Traversal: " + bstSorted.maxTraversed + "  Time: " + time_sec4 + " seconds");
        ////9999 levels are traversed because nodes are become a linked list when inserted into bst
    }
}
