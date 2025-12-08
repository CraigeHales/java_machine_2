package controller;
public class Addon {

    private String[] name;
    private int[] cost;

    private String myId;

    private int setting = 0;

    public Addon(Object... choiceNameAndPricePairs) {
        int nitems = choiceNameAndPricePairs.length;
        if (nitems % 2 != 0){
            System.out.println("--------------not even addon parms--------------");
            assert false;
        }
        int npairs = nitems/2;
        name = new String[npairs];
        cost = new int[ npairs];
        for(int i = 0; i<npairs; i+=1){
            Object o = choiceNameAndPricePairs[2*i+0];
            if (o instanceof String ){
                name[i] = (String)o;
            }
            else {
                System.out.println("--------------not String addon parms--------------");
                assert false;
            }
            o = choiceNameAndPricePairs[2*i+1];
            if (o instanceof Integer ){
                cost[i] = (Integer)o;
            }
            else {
                System.out.println("--------------not Integer addon parms--------------");
                assert false;
            }
        }
    }

    public static void reset(PostResult result){
        result.setText("tspan_add0","Pick a",0);
        result.setText("tspan_add1","Drink",0);
        result.setText("tspan_add2","Above",0);
    }

    public static void takeBelow(PostResult result, int delay){
        result.setText("tspan_add0","Take",delay);
        result.setText("tspan_add1","Drink",delay);
        result.setText("tspan_add2","Below",delay);
    }


    public void activateButton(PostResult result, String id) {
        myId = id;
        result.setText(id, name[setting],0);
    }

    public void press(PostResult result) {
        setting = setting % (name.length-1) + 1;
        activateButton(result,myId);
    }

    public int getPrice(PostResult result) {
        return cost[setting];
    }

    public String get() {
        return name[setting];
    }

}
