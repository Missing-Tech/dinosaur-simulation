/**
 * Factory class to instantiate plant objects
 * 
 * @author David J. Barnes and Michael Kölling and Joseph Grabski and Yukesh Shrestha
 * @version 2022.03.01 
 */
public class PlantFactory {
   // use getShape method to get object of type shape
   public Plant getPlant(Plants plantType, Field field, Location location) {
      if (plantType == null) {
         return null;
      }
      if (plantType == Plants.GRASS) {
         return new Grass(true, field, location);
      }

      return null;
   }
}
