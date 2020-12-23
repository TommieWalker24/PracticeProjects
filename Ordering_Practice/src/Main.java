import java.math.BigDecimal;
import java.util.*;
class Main{
    //Problem
    /*
    Java's BigDecimal class can handle arbitrary-precision signed decimal numbers. Let's test your knowledge of them!

Given an array, , of  real number strings, sort them in descending order — but wait, there's more! Each number must be printed in the exact same format as it was read from stdin, meaning that  is printed as , and  is printed as . If two numbers represent numerically equivalent values (e.g., ), then they must be listed in the same order as they were received as input).

Complete the code in the unlocked section of the editor below. You must rearrange array 's elements according to the instructions above.

Input Format

The first line consists of a single integer, , denoting the number of integer strings.
Each line  of the  subsequent lines contains a real number denoting the value of .
INPUT:
10
123
45
766
324324
.324
0.325
-234
4546
100
0

OUTPUT:
324324
4546
766
123
100
45
0.325
.324
0
-234
     */
    public static void main(String []args){
        //Input
        Scanner sc= new Scanner(System.in);
        int n=sc.nextInt();
        String []s=new String[n+2];
        for(int i=0;i<n;i++){
            s[i]=sc.next();
        }
        sc.close();


        //Write your code here
        Map <String, Double> map = new HashMap<>();
        for(int i = 0; i < n; i++){
            double x =Double.parseDouble(s[i]) ;
             map.put(s[i], x);
        }
        LinkedHashMap<String, Double> reverseSortedMap = new LinkedHashMap<>();

//Use Comparator.reverseOrder() for reverse ordering
        map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
        for(int i=0;i<n;i++)
        {
            s = reverseSortedMap.keySet().toArray(new String[reverseSortedMap.size()]);;
        }
        //Output
        for(int i=0;i<n;i++)
        {
            System.out.println(s[i]);
        }
    }
}
