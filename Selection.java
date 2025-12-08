package controller;
//import controller.PostResult;
public class Selection {
    String prodName;
    String id;
    String textColor;
    String backColor;
    String liquidColor;
    String liquidTransparency;
    Addon[] add;
    int cents;
    int nStock;

    public Selection(String prodName,String id,String textColor,String backColor,int cents,int nStock,Addon add0,Addon add1,Addon add2, String liquidColor, String liquidTransparency){
        this.prodName = prodName;
        this.id = id;
        this.textColor = textColor;
        this.backColor = backColor;
        this.add = new Addon[3];
        this.add[0] = add0; 
        this.add[1] = add1; 
        this.add[2] = add2; 
        this.cents = cents;
        this.nStock = nStock;    
        this.liquidColor = liquidColor;
        this.liquidTransparency = liquidTransparency;
    }

    public boolean wants(String addon) {
        return add[0].get().equals(addon) | add[1].get().equals(addon) | add[2].get().equals(addon) ; 
    }

    public void init(PostResult result){
        result.println("Selection.init: "+prodName);
        result.setColor("rect_"+id, backColor,0);
        result.setColor("tspan_"+id, textColor,0);
        result.setText("tspan_"+id, prodName,0);
    }
    
    public void on(PostResult result,int delay){
        //result.println(prodName+" on "+delay);
        result.setColor("rect_"+id, backColor,delay);
        result.setColor("tspan_"+id, textColor,delay);
    }

    public void gray(PostResult result, int delay){
       // result.println(prodName+" off "+delay);
        result.setColor("rect_"+id, "#333333",delay);
        result.setColor("tspan_"+id, "#444444",delay);
    }

    public void off(PostResult result, int delay){
       // result.println(prodName+" off "+delay);
        result.setColor("rect_"+id, Machine.dim(backColor),delay);
        result.setColor("tspan_"+id, Machine.dim(textColor),delay);
    }
    
    public void press(PostResult result){

        // Fixme: make sure the dispenser is empty 

        result.println("select "+prodName);
        result.println("addons:");
        for(int i=0; i<add.length; i+=1) {
            add[i].activateButton(result, "tspan_add"+i);
        }

        result.setColor("idLiquidDrink",liquidColor,0);
        result.setOpacity("idLiquidDrink",liquidTransparency,0);
        // result.setAudio("plop.mp3",0);
        // if (prodName.equals("Coke")) {
        //     result.setAudio("ice.mp3",0);
        // }
    }

    public Addon getAddon(int i) {
        return add[i];
    }

    public int getPrice(PostResult result) {
        return cents + add[0].getPrice(result) + add[1].getPrice(result) + add[2].getPrice(result);
    }
}
