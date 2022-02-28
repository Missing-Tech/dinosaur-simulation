public class PreyFactory {
   // use getShape method to get object of type shape
   public Prey getPrey(Animals preyType, Field field, Location location) {
      if (preyType == null) {
         return null;
      }
      if (preyType == Animals.TRICERATOPS) {
         return new Triceratops(false, field, location);
      } else if (preyType == Animals.BRONTOSAURUS) {
         return new Brontosaurus(false, field, location);
      } else if (preyType == Animals.STEGOSAURUS) {
         return new Stegosaurus(false, field, location);
      }

      return null;
   }
}
