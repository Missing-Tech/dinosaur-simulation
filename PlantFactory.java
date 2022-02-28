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
