// Tree node
class Node {
	// First four attributes correspond to the actual tree(OrgHierarchy tree).
	public int Id;
	public int Level;
	public linkedList EmployeesList = new linkedList();
	public Node Boss;

	// Below four attributes correspond to the AVL Tree created to store ids of employees.
	public int Height;
	public Node LeftChild;
	public Node RightChild;
	public Node Parent;

	// Below two attributes correspond to the Linked List created to store employees list working under some boss.
	public Node LinkedNext;
	public Node LinkedPrev;

	public int getId()   								{return Id;}
	public void setId(int x)						{this.Id = x;}
	public int getLevel()  								{return Level;}
	public void setLevel(int y)						{this.Level = y;}
	public linkedList getEmployeesList() 				{return EmployeesList;}
	public void setEmployeesList(linkedList newl)	{this.EmployeesList = newl;}
	public Node getBoss() 								{return Boss;}
	public void setBoss(Node b)						{this.Boss = b;}
	
	public int getHeight()					{return Height;}
	public void setHeight(int h)		{this.Height = h;}
	public Node getLeftChild()				{return LeftChild;}
	public void setLeftChild(Node lc)	{this.LeftChild = lc;}
	public Node getRightChild()				{return RightChild;}
	public void setRightChild(Node rc)	{this.RightChild = rc;}
	public Node getParent()					{return Parent;}
	public void setParent(Node pr)		{this.Parent = pr;}

	public Node getLinkedNext()				{return LinkedNext;}
	public void setLinkedNext(Node n)	{this.LinkedNext = n;}
	public Node getLinkedPrev()				{return LinkedPrev;}
	public void setLinkedPrev(Node p) 	{this.LinkedPrev = p;}

	public Node(int a){											// To create head and trailer nodes for linked list implementation
		this.EmployeesList = null;
	}
	public Node(){
		this.LeftChild = null;this.RightChild = null;this.Parent = null;this.LinkedNext = null;this.LinkedPrev = null;this.Height = -1;this.Boss = null;
	}
}
class NoImbalanceException extends Exception{  				   // Thrown when there is no height imbalance in case of insertion and deletion in AVL Tree implementation.
    NoImbalanceException(String s){  
        super(s);  
    }  
}

class queue{
	Object[] A = new Object[2];
	int front = 0;
	int rear = 0;
	int N = 2;
	public int size(){
		N = A.length;
		return (N + rear - front) % N;
	}
	
	public boolean isEmpty(){
		if (front == rear){
			return true;
		}else{
			return false;
		}
	}
	public void enqueue(Object obj){
		N = A.length;
		if (size() == N-1){						// array is full
			Object[] B = new Object[2*N];		//This deals with the expansion of array(when enqueuing the new object makes the size of queue equal to the size of array) 
			for (int i = 0; i < N-1; i++){		//by creating the new array of double the size of initial one , copying all the elements into the new one and
				B[i] = A[(front+i)%N];			//then pointing the new array by A only and then adjust the pointer to the front and rear element of the queue.
			}
			A = B;
			front = 0;
			rear = N-1;
			A[rear] = obj;
			rear = (rear+1)% (2*N);
		}else{
			A[rear] = obj;
			rear = (rear+1)% N;
		}
	}
	public Object dequeue (){
		N = A.length;
		if (isEmpty()){
			return null;
		}else{
			Object ans = A[front];
			front = (front+1)%N;
			return ans;
		}
	}
	public Object front(){
		if (isEmpty()){
			return null;
		}else{
			return A[front];
		}
	}
}

class linkedList{
	Node head;
	Node tail;
	public Node gethead(){return head;}
	public Node gettail(){return tail;}

	public void Initialize(){				// It creates the empty doubly linked list by assigning pointers to the attributes of head and tail nodes.
		head = new Node(1);
		tail = new Node(1);
		head.setLinkedNext(tail);
		head.setLinkedPrev(null);
		tail.setLinkedNext(null);
		tail.setLinkedPrev(head);
	}
	public void addNode(Node n){
		tail.getLinkedPrev().setLinkedNext(n);
		n.setLinkedPrev(tail.getLinkedPrev());
		n.setLinkedNext(tail);
		tail.setLinkedPrev(n);
	}
	public void delNode(Node n){
		n.getLinkedPrev().setLinkedNext(n.getLinkedNext());
		n.getLinkedNext().setLinkedPrev(n.getLinkedPrev());
		n.setLinkedPrev(null);
		n.setLinkedNext(null);
	}
	public void transferBossIds(Node manageboss){					// Changes the pointer to the boss nodes of employees
		Node counter;											    // in case of firing an employee who manages other employees under it.
		counter = this.head.getLinkedNext();						// It takes O(k) time where k = no of employees of the fired employee.
		while(counter.getLinkedNext() != null){
			counter.setBoss(manageboss);
			counter = counter.getLinkedNext();
		}
	}
	public void transferEmployee(linkedList newBoss){				// Adds the employees of fired employee to the employees list of new boss. 
		head.getLinkedNext().setLinkedPrev(newBoss.gettail().getLinkedPrev());
		newBoss.gettail().getLinkedPrev().setLinkedNext(head.getLinkedNext());
		tail.getLinkedPrev().setLinkedNext(newBoss.gettail());
		newBoss.gettail().setLinkedPrev(tail.getLinkedPrev());
	}
}

// AVL tree is formed to store ids of all employees in the organisation 
// in sorted order for efiicient searching of a particular node.
class AVLTree{									
	Node root = null;
	public Node getroot(){
		return root;
	}
	public void setroot(Node new_root){
		this.root = new_root;
	}
	public int abs(int a){
		if (a >= 0){
			return a;
		}else{
			return (-1)*a;
		}
	}
	public int max(int a ,int b){
		if (a >= b){
			return a;
		}else{
			return b;
		}
	}
	public int Height(Node n){			// Returns the height of the node : rerurns -1 if node is null.
		if (n != null){
			return n.getHeight();
		}else{
			return -1;
		}
	}
	public void Parent(Node par, Node child){  		// Sets the node child as parent to the node par; do nothing in case child node is null.
		if (child == null){
			return;
		}else{
			child.setParent(par);
		}
	}

	// Searches the node given the id in the AVL Tree in O(log n)[O(h)] time and
	// returns the node if present or returns the parent node to which the new node with given id can be inserted.
	public Node SearchNode(int id){				
		Node pointer = root;
		Node counter = root;
		while(root != null){
			if (id == pointer.getId()){
				return pointer;
			}else if (id < pointer.getId()){
				pointer = pointer.getLeftChild();
				if (pointer == null){
					return counter;
				}else{
					counter = counter.getLeftChild();
				}
			}else {
				pointer = pointer.getRightChild();
				if (pointer == null){
					return counter;
				}else{
					counter = counter.getRightChild();
				}
			}
		}
		return null;
	}
	
// Starting from the given node it finds the first unbalanced node tracing the path of that given node.
	public Node FirstUnbalNode(Node st) throws NoImbalanceException{
		Node pointer = st;
		int hL;
		int hR;
		Node pointer_L;
		Node pointer_R;
		while(pointer != null){
			pointer_L = pointer.getLeftChild();
			pointer_R = pointer.getRightChild();
			hL = Height(pointer_L);
			hR = Height(pointer_R);
			int new_h = max(hL, hR)+1;
			if (abs(hL - hR) <= 1){
				if (new_h != pointer.getHeight()){
					pointer.setHeight(new_h);
					pointer = pointer.getParent();
				}else{
					throw new NoImbalanceException("No imbalance occur.");
				}
			}else{
				pointer.setHeight(new_h);
				return pointer;
			}
		}
		throw new NoImbalanceException("No imbalance occur.");
	}

	// Given a node z, it balances the height of the AVL tree for that node; however ancestors of the node z may be unbalanced still.
	public Node BalanceNode(Node z){
		Node z_Par = z.getParent();
		Node[] A = new Node[7];
		int hist = 0;
		Node z_L = z.getLeftChild();
		Node z_R = z.getRightChild();
		Node y = new Node();
		if (Height(z_L) > Height(z_R)){
			y = z_L;
			A[5] = z;
			A[6] = z_R;
		}else{
			y = z_R;
			A[0] = z_L;
			A[1] = z;
			hist = 2;
		}
		Node y_L = y.getLeftChild();
		Node y_R = y.getRightChild();
		if (Height(y_L) > Height(y_R)){
			Node x =y_L;
			A[hist+4] = y_R;
			A[hist+3] = y;
			A[hist+2] = x.getRightChild();
			A[hist+1] = x;
			A[hist+0] = x.getLeftChild();
		}else if (Height(y_L) <Height(y_R)){
			Node x = y_R;
			A[hist+4] = x.getRightChild();
			A[hist+3] = x;
			A[hist+2] = x.getLeftChild();
			A[hist+1] = y;
			A[hist+0] = y_L;
		}else{
			if (y == z_L){
				Node x = y_L;
				A[hist+4] = y_R;
				A[hist+3] = y;
				A[hist+2] = x.getRightChild();
				A[hist+1] = x;
				A[hist+0] = x.getLeftChild();
			}else{
				Node x = y_R;
				A[hist+4] = x.getRightChild();
				A[hist+3] = x;
				A[hist+2] = x.getLeftChild();
				A[hist+1] = y;
				A[hist+0] = y_L;
			}
		}
		A[1].setLeftChild(A[0]);
		A[1].setRightChild(A[2]);

		A[5].setLeftChild(A[4]);
		A[5].setRightChild(A[6]);

		A[3].setLeftChild(A[1]);
		A[3].setRightChild(A[5]);


		A[1].setHeight( max ( Height(A[0]), Height(A[2]) ) +1 );
		A[5].setHeight( max ( Height(A[4]), Height(A[6]) ) +1 );
		A[3].setHeight( max ( Height(A[1]), Height(A[5]) ) +1 );

		Parent(A[1],A[0]);
		Parent(A[1],A[2]);
		Parent(A[5],A[4]);
		Parent(A[5],A[6]);
		Parent(A[3],A[1]);
		Parent(A[3],A[5]);

		A[3].setParent(z_Par);
		if (z_Par!= null){
			if (z == z_Par.getLeftChild()){
				z_Par.setLeftChild(A[3]);
			}else{
				z_Par.setRightChild(A[3]);
			}
		}else{
			setroot(A[3]);
		}
		return z_Par;
	}

	// Inserts the node in the AVL Tree maintaining the height balance of it.
	public void InsertNode(Node employee) throws IllegalIDException{
		Node place = SearchNode(employee.getId());
		if (place == null || employee.getId() == place.getId()){
			throw new IllegalIDException("Either Organisation is empty! or This id already exists in the organisation!");
		}else if (employee.getId() < place.getId()){
			place.setLeftChild(employee);
			employee.setParent(place);
		}else{
			place.setRightChild(employee);
			employee.setParent(place);
		}
		Node im = new Node();
		try {
			im = FirstUnbalNode(place);
		} catch (NoImbalanceException ex) {
			return;
		}
		BalanceNode(im);
	}

	// Deletes the given node from the AVL Tree maintaining the height balance of it.
	public void DeleteNode(Node employee){
		Node par_employee = employee.getParent();
		Node employee_L = employee.getLeftChild();
		Node employee_R = employee.getRightChild();
		if (employee_L == null){
			if (par_employee == null) {
				Parent(null,employee_R);
				setroot(employee_R);
			}else if (par_employee.getLeftChild() == employee){
				Parent(par_employee,employee_R);
				par_employee.setLeftChild(employee_R);
			}else{
				Parent(par_employee,employee_R);
				par_employee.setRightChild(employee_R);
			}
		}else if (employee_R == null){
			if (par_employee == null){
				Parent(null,employee_L);
				setroot(employee_L);
			}else if (par_employee.getLeftChild() == employee){
				Parent(par_employee,employee_L);
				par_employee.setLeftChild(employee_L);
			}else{
				Parent(par_employee,employee_L);
				par_employee.setRightChild(employee_L);
			}
		}else{
			Node pointer = employee_R;
			Node counter = pointer;
			while (Height(pointer)>= 0 ){
				pointer = pointer.getLeftChild();
				if (pointer == null){
					pointer = counter;
					break;
				}else{
					counter = pointer;
				}		
			}
			Node newemployee = pointer;
			
			if (newemployee == employee_R){						// If the node to be replaced in the place of the node to be deleted is the right child of the latter.
				newemployee.setParent(par_employee);
				if (par_employee == null){
					setroot(newemployee);
				}else if (par_employee.getLeftChild() == employee){
					par_employee.setLeftChild(newemployee);
				}else{
					par_employee.setRightChild(newemployee);//
				}
				newemployee.setLeftChild(employee_L);
				employee_L.setParent(newemployee);
				par_employee = newemployee;
			}else{
				Node par_newemployee = newemployee.getParent();
				Node newemployee_R = newemployee.getRightChild();
				newemployee.setLeftChild(employee_L);
				newemployee.setRightChild(employee_R);//
				newemployee.setParent(par_employee);
				newemployee.setHeight(employee.getHeight());
				employee_L.setParent(newemployee);
				employee_R.setParent(newemployee);//
				if (par_employee == null){
					setroot(newemployee);
				}else if (par_employee.getLeftChild() == employee){
					par_employee.setLeftChild(newemployee);
				}else{
					par_employee.setRightChild(newemployee);//
				}
				
				if (par_newemployee.getLeftChild() == newemployee){
					Parent(par_newemployee,newemployee_R);
					par_newemployee.setLeftChild(newemployee_R);
				}else{
					Parent(par_newemployee,newemployee_R);
					par_newemployee.setRightChild(newemployee_R);
				}
				par_employee = par_newemployee;
				}
		}
		Node unbal = new Node();
		while(true){							// Recursibely balances the unbalanced node.
			try {
				unbal = FirstUnbalNode(par_employee);
			} catch (NoImbalanceException ex) {
				break;
			}
			par_employee = BalanceNode(unbal);
		}	
	}
}


public class OrgHierarchy implements OrgHierarchyInterface{
	
	// Node root
	Node owner = new Node();  // Creation of the root node, the owner of the organisation.

	int size = 0;
	public Node getOwner(){return owner;}
	AVLTree OrganisationTree = new AVLTree();

	public boolean isEmpty(){
		if (size == 0){
			return true;
		}else{
			return false;
		}
	} 

	public int size(){
		return size;
	}

	public int level(int id) throws IllegalIDException{
		Node req = OrganisationTree.SearchNode(id);
		if (req == null || req.getId() != id){
			throw new IllegalIDException("Either Organisation is empty! or The employee with this id does not exist!");
		}else{
			return req.getLevel();
		}
	} 

	public void hireOwner(int id) throws NotEmptyException{
			if (isEmpty()){
				owner.setId(id);
				owner.setLevel(1);
				owner.getEmployeesList().Initialize();
				OrganisationTree.setroot(owner);
				owner.setHeight(0);
				owner.setBoss(null);
				size++;
			}else{
				throw new NotEmptyException("Owner already present!");
			}
	}

	public void hireEmployee(int id, int bossid) throws IllegalIDException{
		Node employee = new Node();						// Creation of the new node and setting parameters to it.
		employee.setId(id);
		employee.setHeight(0);
		employee.getEmployeesList().Initialize();
		try {
			OrganisationTree.InsertNode(employee);		// Insertion of this newly created node in the AVL Tree.
		} catch (IllegalIDException ex) {
			throw new IllegalIDException("Either Organisation is empty! or The employee with this id already exists in the organisation!");
		}
		Node boss = OrganisationTree.SearchNode(bossid); // Add tbe entry of new employee in the employees list of the boss and setting remaining parameters of the employee. 
		if (boss != null && boss.getId() == bossid){
			boss.getEmployeesList().addNode(employee);
			employee.setLevel(boss.getLevel() + 1);
			employee.setBoss(boss);
			size++;
		}else{
			throw new IllegalIDException("The employee with boss id does not exist in the organisation!");
		}
	} 

	public void fireEmployee(int id) throws IllegalIDException{
		Node employee = OrganisationTree.SearchNode(id);
		if (employee == null || id != employee.getId()){
			throw new IllegalIDException("Either Organisation is empty! or The employee with this id does not exist!");
		}
		Node boss = employee.getBoss();     	// Deletion of employee from the employees list of the boss.
		if (boss != null){
			boss.getEmployeesList().delNode(employee);
		}else{
			throw new IllegalIDException("Owner can't be fired!!");
		}
		OrganisationTree.DeleteNode(employee);	// Deletion of employee from the AVL Tree.
		size--;
	}
	public void fireEmployee(int id, int manageid) throws IllegalIDException{
		Node employee = OrganisationTree.SearchNode(id);
		if (employee == null || id != employee.getId()){
			throw new IllegalIDException("Either Organisation is empty! or The employee with this id does not exist!");
		}
		Node boss = employee.getBoss();			// Deletion of employee from the employees list of the boss.
		if (boss != null ){
			boss.getEmployeesList().delNode(employee);
		}else{
			throw new IllegalIDException("Owner can't be fired!!");
		}
		Node managingBoss = OrganisationTree.SearchNode(manageid);	// Transferring the employees of former employee to the new managing boss.
		if (managingBoss == null || manageid != managingBoss.getId()){
			throw new IllegalIDException("The boss with this manage id does not exist!");
		}
		if (managingBoss.getLevel() != employee.getLevel()){
			throw new IllegalIDException("Employee with manage id is not at same level with the employee with id as id!!");
		}
		employee.getEmployeesList().transferBossIds(managingBoss);
		employee.getEmployeesList().transferEmployee(managingBoss.getEmployeesList());

		OrganisationTree.DeleteNode(employee);		// Deletion of employee from the AVL Tree.
		size--;
	} 

	public int boss(int id) throws IllegalIDException{
		if (isEmpty()){
			throw new IllegalIDException("Organisation is empty!");
		}
		if (owner.getId() == id){
			return -1;
		}
		Node employee = OrganisationTree.SearchNode(id);
		if (id != employee.getId()){
			throw new IllegalIDException("The employee with this id does not exist!");
		}
		return employee.getBoss().getId();
	}

	public int lowestCommonBoss(int id1, int id2) throws IllegalIDException{
		Node employee1 = OrganisationTree.SearchNode(id1);
		Node employee2 = OrganisationTree.SearchNode(id2);
		if (employee1 == null || employee2 == null || employee1.getId() != id1 || employee2.getId() != id2){
			throw new IllegalIDException("Either Organisation is empty! or Atleast one of the employee with given ids do not exist!");
		}
		Node boss1 = employee1.getBoss();
		Node boss2 = employee2.getBoss();
		if (boss1 == null || boss2 == null){
			return -1;
		}
		while (boss1 != boss2){				 
			int a1 = boss1.getLevel();
			int a2 = boss2.getLevel();
			if (a1 < a2){
				boss2 = boss2.getBoss();
			}else if (a1 > a2){
				boss1 = boss1.getBoss();
			}else{
				boss2 = boss2.getBoss();
				boss1 = boss1.getBoss();
			}
		}
		return boss1.getId();
		}

	public String toString(int id) throws IllegalIDException{
		MyStack A = new MyStack();					// Two stacks are created to sort the employees' id's which are at the same level.
		MyStack B = new MyStack();
		String s = "";
		Node start = OrganisationTree.SearchNode(id);
		if (start == null || id != start.getId()){
			throw new IllegalIDException("Either Organisation is empty! or The employee with this id does not exist!");
		}

		queue que = new queue();					// Queue is created to ensure that that no node of depth k is visited before visiting the node of depth k-1.
		que.enqueue(start);
		while(! que.isEmpty()){
			Node temp = (Node) que.dequeue();
			s += temp.getId() + " ";
			Node counter = temp.getEmployeesList().gethead().getLinkedNext();
			while(counter.getLinkedNext() != null){	
				if (A.isEmpty() || (int) ((Node) A.top()).getId() > counter.getId()){
					A.push(counter);
					counter = counter.getLinkedNext();
				}else{
					while (!A.isEmpty() && (int) ( (Node) A.top()).getId() < counter.getId()){
						B.push(A.pop());
					}
					A.push(counter);
					while(! B.isEmpty()){
						A.push(B.pop());
					}
					counter = counter.getLinkedNext();
				}
			}
			if (que.isEmpty()){
				while(! A.isEmpty()){
					que.enqueue( (Node)A.pop());
				}
			}
		}
		return s;
	}
}

