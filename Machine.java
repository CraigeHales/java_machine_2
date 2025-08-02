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
        // give the machine a name
        result.setText("tspan_java_machine","Ice Cold Drinks",0);
        result.setColor("tspan_java_machine","#3366ff",0);
        result.setAudio("startup.mp3",0);
    }
    public void doClick(PostResult result, String id){
        result.println("you pressed: " + id);
        result.setAudio("keypress.mp3",0);
    }
}
