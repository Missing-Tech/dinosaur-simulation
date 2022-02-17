public class PredatorFactory {
    //use getShape method to get object of type shape 
   public Predator getPredator(String predatorType, Field field, Location location){
    if(predatorType == null){
       return null;
    }		
    if(predatorType.equalsIgnoreCase("TREX")){
       return new TRex(true, field, location);
       
    } else if(predatorType.equalsIgnoreCase("VELOCIRAPTOR")){
       return new Velociraptor(true, field, location);
       
    }
    
    return null;
 }
}
