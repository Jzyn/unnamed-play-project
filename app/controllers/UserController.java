package controllers;

//import controllers.security.AuthUser;
import controllers.security.Secured;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.mvc.*;
import javax.inject.Inject;
import java.util.*;


import java.util.ArrayList;
import java.util.List;
import play.api.Environment;

import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.FilePart;
import java.io.*;
import java.net.URL;

// File upload and image editing dependencies
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

import views.html.*;
import models.*;
import models.users.User;

import views.html.*;


@Security.Authenticated(Secured.class)
// Authorise user (check if logged in)
//*@With(AuthUser.class)*@
public class UserController extends Controller {


    // Declare a private FormFactory instance
    private FormFactory formFactory;

    /**
     * http://stackoverflow.com/a/37024198
     **/
    private Environment env;

    //  Inject an instance of FormFactory it into the controller via its constructor
    @Inject
    public UserController(Environment e, FormFactory f) {
        this.formFactory = f;
        this.env = e;
    }

    // Method returns the logged in user (or null)
    private User getUserFromSession() {
        return User.getUserById(session().get("email"));
    }

    @Transactional
    public Result updateProfile(String email) {

        User u;
        Form<User> editDetailsForm;

        try {
            u = User.find.byId(email);

            // Create a form based on the Room class and fill using r
            editDetailsForm = formFactory.form(User.class).fill(u);

            } catch (Exception ex) {
                // Display an error message or page
                return badRequest("error");
        }

        return ok(profile.render(getUserFromSession(), editDetailsForm));
    }

    public Result profile() {
        Form<User> userDetailsForm = formFactory.form(User.class);
        return ok(profile.render(getUserFromSession(),userDetailsForm));


        // Render the Add Room View, passing the form object
    }

    public Result listProduct() {
        Form<Product> listProductForm = formFactory.form(Product.class);
        // Render the Add Product View, passing the form object
        return ok(listproduct.render(listProductForm, User.getUserById(session().get("email")), env));
    }

    public Result editProfileSubmit() {
	Form<User> userDetailsForm = formFactory.form(User.class).bindFromRequest();
	 if (userDetailsForm.hasErrors()) {
                // If errors, show the form again
                return badRequest(profile.render(getUserFromSession(), userDetailsForm));
	 }
	        User u = userDetailsForm.get();
                u.setRole("user");
		u.update();

                return redirect(controllers.routes.HomeController.index());
            }

    @Transactional
    public Result productSubmit() {
       

        try {
            // Bind form instance to the values submitted from the form
            Form<Product> listProductForm = formFactory.form(Product.class).bindFromRequest();

            // Check for errors
            // Uses the validate method defined in the Login class
            if (listProductForm.hasErrors()) {
                // If errors, show the form again
                return badRequest(listproduct.render(listProductForm, getUserFromSession(), env));
            }
            Product p = listProductForm.get();

            if (p.getId() == null) {
                // Save to the database via Ebean0.0 (remember Product extends Model)
                p.setSeller(getUserFromSession().getUsername());
                p.save();
            }
            // Product already exists so update
            else if (p.getId() != null) {
                p.update();
            }


            // Save the image file
            // Set a success message in temporary flash
            // for display in return view

            flash("success" + p.getTitle() + " has been submitted");
            return redirect(controllers.routes.UserController.profile());
        } catch (Exception ex) {
            flash("exception", "Uh Oh looks like something went wrong press back to get out of here.");
            return redirect(routes.HomeController.index());
        }
    }

    @Transactional
    public Result bidSubmit(Long id) {

        try {
             Product p;
             Form<Product> bidForm;
             p = Product.find.byId(id);
            User u = getUserFromSession();

            // Create a form based on the Room class and fill using r
            bidForm = formFactory.form(Product.class).fill(p);
            if(p.getPrice() < p.getMaxPrice())
            {
                p.setPrice(p.getPrice() + 5);
                p.setLatestBidder(u.getEmail());
                p.update();
            }
            else if (p.getPrice() == p.getMaxPrice() && p.getLatestBidder() == u.getEmail())
            {
                    flash("success", "congrats " +p.getLatestBidder() +"! You have won the bid!");
            }
            else if (p.getPrice() == p.getMaxPrice() && p.getLatestBidder() != u.getEmail())
            {
                flash("unfortunate", "This bid has ended");
            }
            else
                {
                    flash("unfortunate", "Uh Oh looks like something went wrong press back to get out of here.");
            return redirect(routes.HomeController.productInfo(p.getId()));

            }
            return redirect(routes.HomeController.productInfo(p.getId()));

            } catch (Exception ex) {
                // Display an error message or page
                return badRequest("error");
        }

       
    }


}

