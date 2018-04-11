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
import java.io.File;

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

    public Result profile() {
        return ok(profile.render(getUserFromSession()));
    }

    public Result listProduct() {
        Form<Product> listProductForm = formFactory.form(Product.class);
        // Render the Add Product View, passing the form object
        return ok(listproduct.render(listProductForm, User.getUserById(session().get("email")), env));
    }

    @Transactional
    public Result productSubmit() {
        try {
            String saveImageMsg;
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
                // Save to the database via Ebean (remember Product extends Model)
                p.setSeller(getUserFromSession().getUsername());
                p.save();
            }
            // Product already exists so update
            else if (p.getId() != null) {
                p.update();
            }



            MultipartFormData data = request().body().asMultipartFormData();
            FilePart image = data.getFile("upload");

            // Save the image file
            saveImageMsg = saveFile(p.getId(), image);

            // Set a success message in temporary flash
            // for display in return view

            flash("success" + p.getName() + " has been submitted");
            return redirect(controllers.routes.UserController.profile());

        } catch (Exception ex) {
            flash("exception", "Uh Oh looks like something went wrong press back to get out of here.");
            return redirect(routes.HomeController.index());
        }
    }

    @Transactional
    public Result bidSubmit() {


        try {


            Form<Product> listProductForm = formFactory.form(Product.class).bindFromRequest();
            Product p = listProductForm.get();


            if(p.getPrice() > 15)
            {
                p.update();
            }
            else
            {
                flash("Whoops!, Bid too low");
            }

            return redirect(routes.HomeController.productInfo(p.getId()));

        } catch (Exception ex) {
            flash("exception","Uh Oh Looks like something went wrong press back to get out of here.");
            return redirect(routes.HomeController.products(0, ""));
        }

    }

    // Save an image file
    public String saveFile(Long id,FilePart<File> image) {
        if (image != null) {
            // Get mimetype from image
            String mimeType = image.getContentType();
            // Check if uploaded file is an image
            if (mimeType.startsWith("image/")) {
                // Create file from uploaded image
                File file = image.getFile();
                // create ImageMagick command instance
                ConvertCmd cmd = new ConvertCmd();
                // create the operation, add images and operators/options
                //Standard Image
                IMOperation op = new IMOperation();
                // thumbnail
                IMOperation thumb = new IMOperation();

                // Get the uploaded image file
                op.addImage(file.getAbsolutePath());

                    // Resize using height and width constraints
                    op.resize(300,200);
                    // Save the  image
                    op.addImage("public/images/productImages/" + id + ".jpg");

                    // Get the uploaded image file
                    thumb.addImage(file.getAbsolutePath());
                    thumb.thumbnail(60);
                    // Save the  image
                    thumb.addImage("public/images/productImages/thumbnails/" + id + ".jpg");


                // execute the operation
                try{
                    cmd.run(op);
                    cmd.run(thumb);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                return " and image saved";
            }
        }
        return "image file missing";
    }

}

