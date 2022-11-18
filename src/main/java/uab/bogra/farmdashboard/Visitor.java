package uab.bogra.farmdashboard;


public class Visitor {
    private double totPrice;
    private double totVal;
    
    public Visitor (){
        totPrice = 0;
        totVal = 0;
    }

    public void visit(Container cont){
        //System.out.println("visitcall");
        if (cont instanceof Item) {
            totPrice += cont.getPrice();
            totVal += cont.getmVal();
            //System.out.println("itemvisit");
            //System.out.println(totPrice);

        } else {
            totPrice += cont.getPrice();
            totVal += cont.getmVal();
            //System.out.println("contvisit");
            for (Container i : ((Container) cont).getChildrenList()){
                //System.out.println("inner contvisit");
                //System.out.println(totPrice);
                visit(i);
            }
        }
    }


    public double getPrice(){
        return totPrice;
    }

    public double getmVal(){
        return totVal;
    }

}
