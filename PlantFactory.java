public class PlantFactory {
    //use getShape method to get object of type shape 
   public Plant getPlant(String plantType, Field field, Location location){
    if(plantType == null){
       return null;
    }		
    if(plantType.equalsIgnoreCase("TREE")){
       return new Tree(true, field, location);
    } 
    else if (plantType.equalsIgnoreCase("GRASS")){
       return new Grass(true, field, location);
    }
    
    return null;
 }
}
