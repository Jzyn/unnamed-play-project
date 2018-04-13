package controllers;

import controllers.security.*;
import play.api.Environment;
import play.mvc.*;
import play.data.*;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import views.html.*;

import models.users.*;

import models.*;



/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    
    private Environment env;


    // Declare a private FormFactory instance
    private FormFactory formFactory;

    //  Inject an instance of FormFactory it into the controller via its constructor
    @Inject

    public HomeController(Environment e, FormFactory f) {
        this.formFactory = f;
        this.env = e;
	}

    private User getUserFromSession(){
        return User.getUserById(session().get("email"));
    }

    public Result index() {
        return ok(index.render(getUserFromSession()));
    }


    /**
     *  Sign Up Methods
     *
     */
    public Result signUp() {
        Form<User> signUpForm = formFactory.form(User.class);

        return ok(signup.render(signUpForm, getUserFromSession()));
    }

    public Result signupSubmit() {

    Form<User> newUserForm = formFactory.form(User.class).bindFromRequest();

	if(newUserForm.hasErrors()){

	return badRequest(signup.render(newUserForm, getUserFromSession()));
	}

	User newUser = newUserForm.get();
        if (newUser.getEmail() == null) {
            // Save to the database via Ebean (remember Product extends Model)
            newUser.setRole("user");
            newUser.save();
	    flash("Success", "You have been registered. You can now Login.");
        }
        // Product already exists so update
        else if (newUser.getEmail() != null) {
            newUser.update();
        }

	return redirect(controllers.routes.HomeController.index());

}




	public Result products(Long catId,String filter) {

    	    // Get list of all categories in ascending order
     	   List<Category> categoriesList = Category.findAll();
     	   List<Product> productsList = new ArrayList<Product>();
	
      		  if (catId == 0) {
     	   	    // Get list of all categories in ascending order
     	  	     productsList = Product.findAll(filter);
     		   }
     	 	  else {
     	 	      // Get products for selected category
     	 	      // Find category first then extract products for that cat.
      	  	    productsList = Product.findFilter(catId,filter);
     	  	 }

      	  return ok(products.render(catId,filter,productsList, categoriesList, getUserFromSession(), env));
   	 }

    public Result productInfo(Long prodId)
    {

        try
        {
            Form<Product> listProductForm = formFactory.form(Product.class).bindFromRequest();
            Product selectedProd = Product.find.byId(prodId);
            User currentUser = getUserFromSession();
            return ok(productinfo.render(listProductForm,selectedProd,currentUser,env));
        } catch(Exception ex)
        {
            flash("exception","Uh Oh Something unexpected happened");
            return redirect(routes.HomeController.index());
        }
    }

}
