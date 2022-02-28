public class PreyFactory {
   // use getShape method to get object of type shape
   public Prey getPrey(Animals preyType, Field field, Location location) {
      if (preyType == null) {
         return null;
      }
      if (preyType == Animals.TRICERATOPS) {
         return new Triceratops(true, field, location);
      } else if (preyType == Animals.BRONTOSAURUS) {
         return new Brontosaurus(true, field, location);
      } else if (preyType == Animals.STEGOSAURUS) {
         return new Stegosaurus(true, field, location);
      }

      return null;
   }
}
