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
        if ( id.equals("idDispenserBackground")) {
            //result."idIceCubes"
        }
    }
}
