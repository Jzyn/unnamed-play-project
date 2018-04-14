package controllers;

import controllers.security.*;

import models.*;
import models.users.User;
import play.db.ebean.Transactional;
import play.mvc.*;
import views.html.admin.*;

import play.api.Environment;
import play.data.*;


import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.FilePart;
import java.io.File;



// Authenticate user
@Security.Authenticated(Secured.class)
// Authorise user (check if admin)
@With(AuthAdmin.class)
public class AdminController extends Controller {
private FormFactory formFactory;
private Environment env;
@Inject
    public AdminController(FormFactory f, Environment e){
        this.formFactory = f;
        this.env = e;
    }


    @Transactional
       private User getCurrentUser() {
        User u = User.getUserById(session().get("email"));
        return u;
    }


    public Result vUsers() {
        List<User> userList = User.findAll();
        return ok(users.render(getCurrentUser(), userList));
    }

    public Result vProducts() {
       List<Product> productList = Product.findAll();
       return ok(products.render(getCurrentUser(), productList, env));
    }

    // Delete Product by id
    @Transactional
    public Result deleteUser(String email) {

        // find product by id and call delete method
        User.find.ref(email).delete();
        // Add message to flash session
        flash("success", "User has been deleted");

        // Redirect to products page
        return redirect(routes.AdminController.index());
    }






    // Render and return  the add new product page
    // The page will load and display an empty add product form

    public Result index() {
        List<Product> products = Product.findAll();
        List<User> users = User.findAll();
    return ok (index.render(getCurrentUser(), products, users, env));
}

@Transactional
public Result deleteProduct(Long id) {

Product.find.ref(id).delete();

flash("success", "Product has been deleted");

return redirect (controllers.routes.AdminController.index());
}

}
