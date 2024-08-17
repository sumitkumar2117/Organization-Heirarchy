public class MyStack implements StackInterface {  
    public Object[] A = new Object[1];          // Creates an array of size 1.
    public int last = -1;       //              // last denotes the index of the array A which contains the top element of the stack.
                                                // last == -1 indicates that the stack is empty.

    public void push(Object o){
        if (o == null){
            return;
        }
        int len = A.length;
        if (last == -1){                        // If stack is empty then just pushes the object into the array.
            last++;
            A[last] = o;

        }else if (last == len-1){               // This deals with the expansion of array(when last becomes more than (size-1)) by creating the 
            Object[] S = new Object[2*len];     // new array of double the size of initial one , copying all the elements into the new one and
            for (int i = 0; i < len; i++){      // then pointing the new array by A only.
                S[i] = A[i];
            }  
            A = S;           
            last++;
            A[last] = o;

        }else{
            last++;
            A[last] = o;
        }
    }

    public Object pop() { 
        if (last == -1){
            return null;
        }else{
            last--;
            return A[last+1];
        }
    }

    public Object top(){
        if (last == -1){
            return null;
        } else{
            return A[last];
        }
    }

    public boolean isEmpty(){
        if (last == -1){
            return true;
        }else{
            return false;
        }
    }

    public String toString(){
        String arr = "[";
        if (last == -1){
            arr+= "]";
            return arr;
        } else{
            for (int i = last; i >0; i--){      // Traverses the stack from 'last' index of the array A till the first(0th) index of A.
                arr+= A[i] + ", ";
            }
            arr+= A[0] + "]";                   // This is done so that there should not be extra ',' after the last element in set.
            return arr;
        }
    }

}
