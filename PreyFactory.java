public class PreyFactory {
    //use getShape method to get object of type shape 
   public Prey getPrey(String preyType, Field field, Location location){
    if(preyType == null){
       return null;
    }		
    if(preyType.equalsIgnoreCase("TRICERATOPS")){
       return new Triceratops(true, field, location);
    } else if(preyType.equalsIgnoreCase("BRONTOSAURUS")){
       return new Brontosaurus(false, field, location);
    } else if(preyType.equalsIgnoreCase("STEGOSAURUS")){
      return new Stegosaurus(false, field, location);
   }
    
    
    return null;
 }
}
