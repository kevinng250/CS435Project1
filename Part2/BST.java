import java.util.*;

class BST {
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
        public void setHeight(int val){
            this.height = val;
        }
    }
    Node root;
    BST(){
        root = null;
    }


    int height(Node node){
        if(node == null){
            return 0;
        }
        else{
            return node.height;
        }
        //retrieve height before rotation
    }
    int getHeight(Node node){
        if(node == null){
            return 0;
        }
        else {
            int lefty = getHeight(node.left);
            int righty = getHeight(node.right);
            return Math.max(lefty, righty) + 1;
        }
        //retrieve height after a rotation
    }
    Integer maxTraversed = 0;
    Node rightRotation(Node node){
        Node original = node;
        Node n = node.left;
        Node temp = node.left.right;
        original.left = temp;
        if(temp!= null) {
            temp.parent = original;
        }
        n.right = original;
        original.parent = n;
        n.height = Math.max(getHeight(n.left), getHeight(n.right)) + 1;
        original.height = Math.max(getHeight(original.left), getHeight(original.right))  + 1;
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
        original.parent = n;
        n.height = Math.max(getHeight(n.left), getHeight(n.right)) + 1;
        original.height = Math.max(getHeight(original.left), getHeight(original.right))+1;
        return n;
    }
    Node find(int val, Node root){
        if(root == null) {
            return null;
        }
        if(root.val == val){
            return root;
        }
        find(val, root.left);
        find(val, root.right);
        return root;
    }
    //QUESTION 1
    //RECURSIVELY
    void insertRec(int val){
        root = insertRecHelper(root, val);
    }
    Node balance(Node node, int balanceFactor){
        if(balanceFactor > 1){
//            System.out.println(root.val + " bf: " + balanceFactor);
//            System.out.println(height(root.left.left) - height(root.left.right));
            if((height(root.left.left) - height(root.left.right)) > 0){
                //Left Left Case
                return rightRotation(root);
            }
            else{
                //Left Right Case
                root.left = leftRotation(root.left);
                return rightRotation(root);
            }
        }
        else if(balanceFactor < -1){
//            System.out.println(root.val + " bf: " + balanceFactor);
            if((height(root.right.left) - height(root.right.right) < 0)){
                //Right Right Case
                return leftRotation(root);
            }
            else{
                //Right Left Case
                root.right = rightRotation(root.right);
                return leftRotation(root);
            }
        }
        return node;
    }
    Node insertRecHelper(Node root, int val){
        if(root == null){
            root = new Node(val);
//            System.out.println("root: " + root.val);
            root.height = 0;
        }
        else if(val < root.val) {
            Node lefty = insertRecHelper(root.left, val);
            root.left = lefty;
            lefty.parent = root;
        }
        else {
            Node righty = insertRecHelper(root.right, val);
            root.right = righty;
            righty.parent = root;
        }
//        root.height = Math.max(height(root.left), height(root.right)) + 1;
////        System.out.println(root.val + " " + root.height);
//        int balanceFactor = height(root.left) - height(root.right);
//        //System.out.println(root.val + " bf: " + balanceFactor);
//        if(balanceFactor > 1){
////            System.out.println(root.val + " bf: " + balanceFactor);
////            System.out.println(height(root.left.left) - height(root.left.right));
//            if((height(root.left.left) - height(root.left.right)) > 0){
//                //Left Left Case
//                return rightRotation(root);
//            }
//            else{
//                //Left Right Case
//                root.left = leftRotation(root.left);
//                return rightRotation(root);
//            }
//        }
//        else if(balanceFactor < -1){
////            System.out.println(root.val + " bf: " + balanceFactor);
//            if((height(root.right.left) - height(root.right.right) < 0)){
//                //Right Right Case
//                return leftRotation(root);
//            }
//            else{
//                //Right Left Case
//                root.right = rightRotation(root.right);
//                return leftRotation(root);
//            }
//        }
        return root;
//        return balance(root, balanceFactor);
    }
    public Node deleteRec(int val){
        this.root = deleteRecHelper(root, val);
        return this.root;
    }
    public Node deleteRecHelper(Node root, int val){
        if(root == null){
            return null;
        }
        if(root.val == val){
            //First Case
            if(root.left == null && root.right == null){
                root = null;
                System.out.println(val + " deleted");
            }
            //Second Case
            else if(root.left == null){
                root.right.parent = root.parent;
                return root.right;
            }
            else if(root.right == null) {
                root.left.parent = root.parent;
                return root.left;
            }
            //Third Case
            else
            {
                Node n = findNextRec(root);
                root.val = n.val;
                root.right = deleteRecHelper(root.right, n.val);
            }
        }
        else if(root.val > val){
//            System.out.println(root.left.val + " visited");
            root.left = deleteRecHelper(root.left, val);
        }
        else{
//            System.out.println(root.right.val + " visited");
            root.right = deleteRecHelper(root.right, val);

        }
        if(root != null) {
            root.height = Math.max(height(root.left), height(root.right)) + 1;
            int balanceFactor = height(root.left) - height(root.right);
            System.out.println(root.val + " bf: " + balanceFactor + "height: " + height(root));
            return balance(root, balanceFactor);
//            if(balanceFactor > 1){
////            System.out.println(root.val + " bf: " + balanceFactor);
////            System.out.println(height(root.left.left) - height(root.left.right));
//                if((height(root.left.left) - height(root.left.right)) > 0){
//                    //Left Left Case
//                    return rightRotation(root);
//                }
//                else{
//                    //Left Right Case
//                    root.left = leftRotation(root.left);
//                    return rightRotation(root);
//                }
//            }
//            else if(balanceFactor < -1){
////            System.out.println(root.val + " bf: " + balanceFactor);
//                if((height(root.right.left) - height(root.right.right) < 0)){
//                    //Right Right Case
//                    return leftRotation(root);
//                }
//                else{
//                    //Right Left Case
//                    root.right = rightRotation(root.right);
//                    return leftRotation(root);
//                }
//            }
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
        //Continue Traversing until one of the parents is greater than the val
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
            //Check Parents
            Node n =  findPrevRecHelper(node, node.val);
            if(n.val < node.val){
                return n;
            }
            else{
                return null;
            }
        }
        else{
            //Check left subtree
            return findMaxRec(node.left);
        }
    }
    public static Node findPrevRecHelper(Node node, int val){
        //Continue Traversing until finds first instance of parent < node
        if(node != null && node.val < val){
            return node;
        }
        else if(node.parent != null){
            return findPrevRecHelper(node.parent, val);
        }
        return node;
    }
    public static Node findMinRec(Node root){
        //Keep Traversing to the left most Node
        if(root.left != null){
            return findMinRec(root.left);
        }
        else{
            return root;
        }
    }
    public static Node findMaxRec(Node root){
        //Keep traversing to the right most Node
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
            maxTraversed = 1;
            return;
        }
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        int count = 1;
        while(queue.size() != 0){
            int n = queue.size();
            for(int i = 0; i < n; i++){
                Node curr = queue.remove();
                if(curr.left == null && val < curr.val){
                    curr.left = new Node(val);
                    curr.left.parent = curr;
                    count++;
                    break;
                }
                else if(curr.right == null && val > curr.val){
                    curr.right = new Node(val);
                    curr.right.parent = curr;
                    count++;
                    break;
                }
                if(val < curr.val){
                    queue.add(curr.left);
                    count++;
                }
                else{
                    queue.add(curr.right);
                    count++;
                }
            }

        }
        this.maxTraversed = Math.max(this.maxTraversed, count);
    }
    public Node deleteIter(int val){
        if(root == null){
            return null;
        }
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        Node prev = null;
        //prev Node used to keep track of the parent node
        while(queue.size() != 0){
            int n = queue.size();
            for(int i = 0; i < n; i++){
                Node curr = queue.remove();
                if(curr.val == val){
                    //First Case
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
                    //Second Case
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
                    //Third Case
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
        //Recursive Testing
        BST tree = new BST();
//        tree.insertRec(30);
//        tree.insertRec(5);
//        tree.insertRec(35);
//        tree.insertRec(32);
//        System.out.println(tree.root.val + " " + tree.height(tree.root));
//        System.out.println(tree.root.left.val + " " + tree.height(tree.root.left));
//        System.out.println(tree.root.right.val + " " + tree.height(tree.root.right));
//        System.out.println(tree.root.left.left.val + " " + tree.height(tree.root.left.left));
//        tree.insertRec(40);
//        tree.insertRec(45);
        tree.insertRec(13);
//        tree.insertRec(10);
        tree.insertRec(17);
        tree.insertRec(6);
        tree.insertRec(5);
        tree.insertRec(11);
//        tree.insertRec(16);
        tree.insertRec(15);
        tree.insertRec(4);
        System.out.println(tree.print());
        System.out.println("delete");
        tree.deleteRec(15);
//        System.out.println(tree.root.val);
        System.out.println(tree.print());



    }
}
