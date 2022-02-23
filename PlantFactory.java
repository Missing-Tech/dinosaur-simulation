public class PlantFactory {
    //use getShape method to get object of type shape 
   public Plant getPlant(String plantType, Field field, Location location){
    if(plantType == null){
       return null;
    }		
    if (plantType.equalsIgnoreCase("GRASS")){
       return new Grass(true, field, location);
    }
    
    return null;
 }
}
