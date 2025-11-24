package controller; // the (virtual) pathname 
import controller.Executer; // The interface the hardware expects this class to implement
import controller.PostResult; // The callback the hardware expects this class to use

public class Machine implements Executer {
    public Machine(){ // constructor
      // you can't do much here because there is no result
      // parameter. Use init, below, for most of the initialization.
    }
    public void init(PostResult result){
        result.println("Machine.init()");
        // which one are we?
        result.setText("tspan_load","T2",0);
        // give the machine a name
        result.setText("tspan_java_machine","Hot Chocolate",0);
        result.setColor("tspan_java_machine","#c77d29ff",0);

        result.setText("tspan_variety0","Milk Choc",0);
        result.setText("tspan_variety1","Cold Choc",0);
        result.setText("tspan_variety2","Hot Choc",0);
        result.setText("tspan_variety3","Pep Choc",0);
        result.setText("tspan_variety4","Cold H₂O",0);
        result.setText("tspan_variety5","Hot H₂O",0);

        result.setText("tspan_add0","Cream",0);
        result.setText("tspan_add1","Mallows",0);
        result.setText("tspan_add2","Sprinks",0);
        
        result.setAudio("startup.mp3",0);
    }
    public void doClick(PostResult result, String id){
        result.println("you pressed: " + id);
        if ( ! id.equals("initNoReload") ) {
           result.setAudio("keypress.mp3",0);
        }
        if ( id.equals("g_mc_visa")) {
            result.setAudio("thumpecho.mp3",0);
        }
        if ( id.equals("idDispenserBackground")) {
            //result."idIceCubes"
        }
        if (id.equals("g_variety0")) {
            // result.setOpacity("idIceCubes", "0", 1500);
            // result.setOpacity("idGlassCup", "0", 2000); // remove cup last
            // result.setOpacity("idLimeSlice", "0", 500);
            // result.setOpacity("idLemonSlice", "0", 1000);
            // result.setOpacity("idCaffeineMolecule", "0", 200);
            // result.setOpacity("idSugarMolecule", "0", 700);
            // result.setTransform("idVanillacreamAddinTransform", "matrix(1,0,0,0.001,0,274)", 0); // hide down
            // result.setTransform("idLiquidDrinkTransform", "matrix(1,0,0,0.001,0,274)", 0); // hide down
            // result.setTransform("idChocolateAddinTransform", "matrix(1,0,0,0.001,0,274)", 0); // hide down

            // result.setAudio("plop.mp3",0);

            // result.setOpacity("idGlassCup", "1", 2500); 

        }
    }
}
