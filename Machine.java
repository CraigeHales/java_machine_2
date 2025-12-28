package controller; // the (virtual) pathname to the student-written files
import controller.Executer; // The interface the hardware expects this class to implement
import controller.PostResult; // The callback the hardware expects this class to use

public class Machine implements Executer {
    Selection[] selection;
    static Selection gCurrentSelection = null;
    boolean dispenserIsEmpty = true;
    Coinbox coinbox = null;
    final String caffeine = "Caffeine";
    final String ice = "Ice";
    final String warm = "Warm";
    final String sugar = "Sugar";
    final String unsweet = "Unsweet";
    final String diet = "Diet";
    final String lime = "Lime";
    final String lemon = "Lemon";
    final String nothanks = "none";
    final String chocolate = "Choc";
    final String vanilla = "Vanilla";
    final String nothing = "?";
    final String decaf = "Decaf";
    final String cinnamon = "Cinnamon";
    final String nutmeg = "Nutmeg";
    final String marshMal = "Marsh Mallows";
    final String whipCream = "Whipped Cream";
 //   static Coins coins = null;
    public Machine(/*PostResult result*/){
        // there are more addon possibilities than buttons; each selection will
        // choose exactly three addons for the three buttons. Each addon remembers
        // its current state; if it is shared between two selections then the
        // state is shared as well.
        Addon addIce = new Addon(nothing,0,ice,40,warm,0);
        Addon addSpice = new Addon(nothing,0,cinnamon,40,nutmeg,0);
        Addon addMarshMallow = new Addon(nothing,0,marshMal,25,whipCream,35);
        Addon addCaffeine = new Addon(nothing,0,caffeine,0,decaf,10);
        Addon addSugar = new Addon(nothing,0,sugar,30,unsweet,0,diet,50);
        Addon addLime = new Addon(nothing,0,lime,10,nothanks,0);
        Addon addLemon = new Addon(nothing,0,lemon,10,nothanks,0);
        Addon addChocolate = new Addon(nothing,0,chocolate,50,nothanks,0);
        Addon addVanilla = new Addon(nothing,0,vanilla,60,nothanks,0);
        selection = new Selection[6];
        selection[0] = new Selection("Hot Choc","variety0",//button label, internal variety ID
            "#ffffff","#ff3333",// button text and background
            175,20,// price, nStock
            addMarshMallow,addSprinkles,addSpice,// possible addons 
            "#351313",".87");// liquid color and transparency
        selection[1] = new Selection("7-Up","variety1","#333333","#66ff66",
            165,20,addIce,addLime,addSugar,"#005500",".11");
        selection[2] = new Selection("Sprite","variety2","#ffffff","#33aa33",
            155,20,addIce,addLemon,addSugar,"#444400",".11");
        selection[3] = new Selection("Water","variety3","#ffffff","#3377ff",
            135,20,addIce,addLemon,addLime,"#111111",".06");
        selection[4] = new Selection("Milk","variety4","#000000","#ffffff",
            250,20,addIce,addChocolate,addVanilla,"#ffffff",".87");
        selection[5] = new Selection("Citrus","variety5","#000000","#ffff00",
            200,20,addLemon,addLime,addSugar,"#999944",".33");
        coinbox = new Coinbox(); 
    }
    boolean x=false;
    public void init(PostResult result){
        result.println("Machine.init()");
        // which one are we?
        result.setText("tspan_load","T2",0);
        // give the machine a name
        result.setText("tspan_java_machine","Hot Chocolate Machine",0);
        result.setFontSize("text_java_machine","48px",0);
        result.setColor("tspan_java_machine","#3366ff",0);
        // initialize the rectangular selection buttons
        for(int i = 0; i<selection.length; i=i+1){
            selection[i].init(result); 
        }

        result.setOpacity("idIceCubes", "0", 0);
        result.setOpacity("idGlassCup", "0", 0);
        result.setOpacity("idLimeSlice", "0", 0);
        result.setOpacity("idLemonSlice", "0", 0);
        result.setOpacity("idCaffeineMolecule", "0", 0);
        result.setOpacity("idSugarMolecule", "0", 0);
        result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,0.001,0,274)", 0); // hide down
        result.setTransform("idLiquidDrinkTransform", "matrix(1,0,0,0.001,0,274)", 0); // hide down
        result.setTransform("idChocolateAddinTransform", "matrix(1,0,0,0.001,0,274)", 0); // hide down

        Addon.reset(result);
        result.setAudio("startup.mp3",0);
    }
    public void doClick(PostResult result, String id){
        result.println(id);
        
        if (id.startsWith("init")) {            
            
        }
        else {
            if ( id.startsWith("g_variety") ){ //  || id.startsWith("circle_add")
                makeSelection(result,id);
            }
            else if ( id.startsWith("g_add") ) {
                result.setAudio("keypress.mp3",0);
                result.println("machine.doclick("+id+")");
                int idi = Integer.parseInt(id.substring(5));

                if (gCurrentSelection != null){
                    gCurrentSelection.getAddon(idi).press(result);
                }
                else {
                    result.setAudio("groantick.mp3",0);
                }
            }
            else if (id.equals("g_coin_return")) {
                coinbox.move_tended_to_coin_return(result);
            }
            else if (id.equals("g_mc_visa")) {
                if (isReadyForMoney()) {
                    coinbox.pay_with_mc_visa(result);
                }
            }
            else if ( id.equals("g_change") ) {
                coinbox.emptyChangeReturn(result);
            }
            else if ( id.equals("g_dispenser") ) {
                takeSelection( result );
            }
            else if ( id.equals("g_5") || id.equals("g_10") || id.equals("g_25") || id.equals("g_100") ) {
                if (isReadyForMoney()) {
                    result.setAudio("keypress.mp3",0); // only the coin buttons
                    coinbox.addCentsToTended(result, Integer.parseInt(id.substring(2)));
                }
            }
            else {
                result.println("unhandled: "+id);
            }
        }
        if ( gCurrentSelection != null ) {
            int needed = gCurrentSelection.getPrice(result);
            int tended = coinbox.getTended(result,needed);
            String thanks = null;
            if (needed > 0) {
                if (tended > needed) {
                    thanks = "Change below";
                    serveSelection(result);
                }
                else if (tended==needed) {
                    thanks = "Thank you";
                    serveSelection(result);
                }
                else if (tended == -1) { // covered by mc/visa
                    thanks = "MC/Visa billed";
                    tended = needed;
                    serveSelection(result);
                }
                else {
                    assert tended < needed;
                    thanks = "need "+ String.format("%.2f",(needed-tended)/100.0 );
                }
            }
            else{
                thanks = "Pick a Drink";
                coinbox.move_tended_to_coin_return(result);
            }
            result.setText("tspan_still_needed",thanks,0);
            coinbox.showTended(result,needed);
        }
        
        if ( gCurrentSelection != null ) {
            coinbox.enableMoneyButtons(result);
        }
        else { // nothing selected, light them all (unless they are out?)
            coinbox.clearPrice(result);
            coinbox.disableMoneyButtons(result);
        }
    }
    
    public static String dim(String c) { // cut the brightness in half. more or less.
        c=c.replace("1","0");
        c=c.replace("2","1");
        c=c.replace("3","1");
        c=c.replace("4","2");
        c=c.replace("5","2");
        c=c.replace("6","3");
        c=c.replace("7","3");
        c=c.replace("8","4");
        c=c.replace("9","4");
        c=c.replace("a","5");
        c=c.replace("b","5");
        c=c.replace("c","6");
        c=c.replace("d","6");
        c=c.replace("e","7");
        c=c.replace("f","7");
        return c;
    }


    void makeSelection(PostResult result, String id){ // click on a variety button, Coke, for example

        if (!dispenserIsEmpty) {
            //result.println("<<< dispenser is not empty!!! >>>");
            result.setAudio("groantick.mp3",0);
            return;
        }

        

        result.setAudio("keypress.mp3",0);
        result.println("machine.doclick("+id+")");
        int idi = Integer.parseInt(id.substring(9)); // g_variety0..5
        selection[idi].press(result);
        gCurrentSelection = selection[idi];
        int demodelay =0;
        for(Selection s: selection){
            if ( s == gCurrentSelection ) {
                s.on(result,100);
            }
            else {
                demodelay += 1;
                s.off(result,100*demodelay);
            }
        }
    }

    void serveSelection(PostResult result){ // tended >= price
        // ice first, if needed
        int needed = gCurrentSelection.getPrice(result);
        coinbox.payFromTended(result,needed);
        if (coinbox.getTended(result, 0) > 0) {
            coinbox.move_tended_to_coin_return(result);
        }
        if (gCurrentSelection != null){
            int delay = 0;
            result.setOpacity("idGlassCup", "1", delay);
            delay += 100;

            if ( gCurrentSelection.wants(ice) ) {
                result.setOpacity("idIceCubes", "1", delay);
                result.setAudio("ice.mp3",delay);
                delay += 300;
            }

            result.setTransform("idLiquidDrinkTransform", "matrix(1,0,0,1,0,0.0)", delay);
            result.setAudio("pour.mp3",delay);
            delay += 2000;

            if ( gCurrentSelection.wants(vanilla) ) {
                result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,1,0,0.0)", delay);
                result.setAudio("pour.mp3",delay);
                delay += 1000;
            }

            if ( gCurrentSelection.wants(chocolate) ) {
                result.setTransform("idChocolateAddinTransform", "matrix(1,0,0,1,0,0.0)", delay);
                result.setAudio("pour.mp3",delay);
                delay += 1000;
            }

            if ( gCurrentSelection.wants(lime) ) {
                result.setOpacity("idLimeSlice", "1", delay);
                result.setAudio("lime.mp3",delay);
                delay += 100;
            }

            if ( gCurrentSelection.wants(lemon) ) {
                result.setOpacity("idLemonSlice", "1", delay);
                result.setAudio("lemon.mp3",delay);
                delay += 100;
            }

            if ( gCurrentSelection.wants(sugar) ) {
                result.setOpacity("idSugarMolecule", "1", delay);
                result.setAudio("sugar.mp3",delay);
                delay += 500;
            }
            
            if ( gCurrentSelection.wants(caffeine) ) {
                result.setOpacity("idCaffeineMolecule", "1", delay);
                result.setAudio("caffeine.mp3",delay);
                delay += 500;
            }

            dispenserIsEmpty = false;    
            Addon.takeBelow(result,delay);

            // disable the selection buttons until drink removed
            gCurrentSelection = null;
            for(Selection s: selection){
                s.gray(result,0);
            }
        }
    }

    void takeSelection(PostResult result){ // click on the dispenser
        if (!dispenserIsEmpty) {
            result.setOpacity("idIceCubes", "0", 1500);
            result.setOpacity("idGlassCup", "0", 2000); // remove cup last
            result.setOpacity("idLimeSlice", "0", 500);
            result.setOpacity("idLemonSlice", "0", 1000);
            result.setOpacity("idCaffeineMolecule", "0", 200);
            result.setOpacity("idSugarMolecule", "0", 700);
            result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,0.001,0,274)", 0); // hide down
            result.setTransform("idLiquidDrinkTransform", "matrix(1,0,0,0.001,0,274)", 0); // hide down
            result.setTransform("idChocolateAddinTransform", "matrix(1,0,0,0.001,0,274)", 0); // hide down

            result.setAudio("plop.mp3",0);
            dispenserIsEmpty = true;
            // enable the selection buttons after drink removed
            gCurrentSelection = null;
            for(Selection s: selection){
                s.on(result,1000+(int)(1000*Math.random()));
            }            
            Addon.reset(result);
        }
        else {
            result.setAudio("NothingInDispenser.mp3",0);
        }
    }

    boolean isSelectionMade() {
        return gCurrentSelection != null;
    }

    boolean isReadyForMoney() {
        return isSelectionMade();
    }

}
