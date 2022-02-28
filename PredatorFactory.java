public class PredatorFactory {
   // use getShape method to get object of type shape
   public Predator getPredator(Animals predatorType, Field field, Location location) {
      if (predatorType == null) {
         return null;
      }
      if (predatorType == Animals.TREX) {
         return new TRex(false, field, location);

      } else if (predatorType == Animals.VELOCIRAPTOR) {
         return new Velociraptor(false, field, location);
      }

      return null;
   }
}
