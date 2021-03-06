package controllers;

import controllers.security.*;
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




    // Declare a private FormFactory instance
    private FormFactory formFactory;

    //  Inject an instance of FormFactory it into the controller via its constructor

    @Inject
    public HomeController(FormFactory f) {
        this.formFactory = f;
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

	try{
    Form<User> signUpForm = formFactory.form(User.class).bindFromRequest();

	if(signUpForm.hasErrors()){
	return badRequest(signup.render(signUpForm, getUserFromSession()));
	}

	User newUser = signUpForm.get();
        if (newUser.getEmail().equals(User.find.ref(newUser.getEmail()))) {
            // Save to the database via Ebean (remember Product extends Model)
            flash("Email already exists");
            return redirect(routes.HomeController.signUp());
        }
        newUser.setRole("user");
        newUser.save();
        flash("Success", "You have been registered. You can now Login.");
	       return redirect(routes.HomeController.index());
  } catch (Exception ex) {
            flash("exception", "Uh Oh looks like something went wrong press back to get out of here.");
            return redirect(routes.HomeController.index());
        }
}




	public Result products(Long catId,String filter) {
        try
        {
    	    // Get list of all categories in ascending order
     	   List<Category> categoriesList = Category.findAll();
     	   List<Product> productsList;
	
      		  if (catId == 0) {
     	   	    // Get list of all categories in ascending order
     	  	     productsList = Product.findAll(filter);
     		   }
     	 	  else {
     	 	      // Get products for selected category
     	 	      // Find category first then extract products for that cat.
      	  	    productsList = Product.findFilter(catId,filter);
     	  	 }

      	  return ok(products.render(catId,filter,productsList, categoriesList, getUserFromSession()));
        } catch(Exception ex)
        {
            flash("exception","Uh Oh Something unexpected happened");
            return redirect(routes.HomeController.index());
        }
   	 }

    public Result productInfo(Long prodId)
    {

        try
        {
            Form<Product> listProductForm = formFactory.form(Product.class).bindFromRequest();
            Product selectedProd = Product.find.byId(prodId);
            User currentUser = getUserFromSession();

            return ok(productinfo.render(listProductForm,selectedProd,currentUser));
        } catch(Exception ex)
        {
            flash("exception","Uh Oh Something unexpected happened");
            return redirect(routes.HomeController.index());
        }
    }

}
