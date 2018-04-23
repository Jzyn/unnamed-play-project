package models.users;

import java.util.*;
import javax.persistence.*;
import models.*;
import play.data.format.*;
import play.data.validation.*;
import com.avaje.ebean.*;
import models.Product;

@Entity
// This is a User of type admin
@DiscriminatorValue("admin")

// Administrator inherits from the User class
public class Admin extends User{

        public Admin()
        {

        }

        public Admin(String email,String name, String username, String password,String role, String address1, String address2, String city, List<Product> myproducts, List<Product> watchedItems)
        {
       		 super(email, name, username, password, role, address1, address2, city, myproducts, watchedItems);
        }

}
