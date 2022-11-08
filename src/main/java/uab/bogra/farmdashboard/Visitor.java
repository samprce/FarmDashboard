package uab.bogra.farmdashboard;


public class Visitor {
    private double totVal;
    
    public Visitor (){
        totVal = 0;
    }

    public void visit(Container cont){
        System.out.println("visitcall");
        if (cont instanceof Item) {
            totVal += cont.getPrice();
            System.out.println("itemvisit");
            System.out.println(totVal);

        } else {
            totVal += cont.getPrice();
            System.out.println("contvisit");
            for (Container i : ((Container) cont).getChildrenList()){
                System.out.println("inner contvisit");
                System.out.println(totVal);
                visit(i);
            }
        }
    }


    public double getVal(){
        return totVal;
    }

}
