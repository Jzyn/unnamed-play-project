package models;
import models.users.*;
import java.util.*;
import com.avaje.ebean.Model;
import play.data.format.*;
import play.data.validation.*;
import javax.persistence.*;




@Entity
public class Product extends Model{
    
	// Properties
    // Annotate id as the primary key
    @Id
    private Long id;

    // Other fields marked as being required (for validation purposes)
    @Constraints.Required
    private String title;

    //Adds a category type i.e. book, action figure etc..
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="category")
    private Category category;

    //Adds function for products to be sorted using a filter.
    private List<Long> catSelect = new ArrayList<Long>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="useremail", nullable = false)
    private User seller;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<User> liked;

    private String latestBidder;

    @Column(columnDefinition="VARCHAR2(1000)")
    @Constraints.Required
    private String description;

    @Constraints.Required
    private int price;

    @Constraints.Required
    private int maxPrice;




    //@OneToMany(cascade = CascadeType.ALL)
    //List<OrderItem> orderItems = new ArrayList<OrderItem>();

    // Default constructor
    public Product() {
    }

    // Constructor to initialise object
    public  Product(Long id, String title, Category category, User seller, String latestBidder,String description, int price, int maxPrice, List<User> liked) {
        this.id = id;
        this.title = title;
	    this.category = category;
        this.seller = seller;
        this.latestBidder = latestBidder;
        this.description = description;
        this.price = price;
        this.maxPrice = maxPrice;
        this.liked = liked;

    }

    //Generic query helper for entity Computer with id Long
    public static Finder<Long,Product> find = new Finder<Long,Product>(Product.class);

    // Find all Products in the database
    // Filter product name
    public static List<Product> findAll() {
        return Product.find.all();
    }

    //Overidden
    public static List<Product> findAll(String filter) 
    {
        return Product.find.where().ilike("title","%"+filter+"%").orderBy("title asc").findList();
    }

    //This allows the user to find all products with a search filter and Category.
    public static List<Product> findFilter(Long catId,String filter) 
    {
		return Product.find.where().eq("category.id",catId).ilike("title","%"+filter+"%").orderBy("title asc").findList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMaxPrice() {return maxPrice; }

    public void setMaxPrice(int maxPrice) { this.maxPrice = maxPrice; }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getLatestBidder() { return latestBidder; }

    public void setLatestBidder(String latestBidder) {this.latestBidder = latestBidder; }

	//needed for filtering searches and organizing results
    public Category getCategory()
    {
        return category;
    }

    public List getCatSelect()
    {
		return catSelect;
    }
    public void setCategory(Category category)
    {
        this.category = category;
    }

    /*public List getOrderItems()
    {
		return orderItems;
    }
    public void setOrderItems(List<OrderItem> orderItems)
    {
		orderItems = orderItems;
    }
    */

    //Misc
    //Used for printing Categories due to the fact if you do it in html causes issues.



}
