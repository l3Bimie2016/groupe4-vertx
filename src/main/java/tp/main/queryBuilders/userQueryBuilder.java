package tp.main.queryBuilders;

/**
 * Created by Polygone Asynchrone on 05/04/2016.
 */
public abstract class UserQueryBuilder {

    static private String insert = "INSERT INTO User (userLogin, userPassword, userFirstName, userLastName, userAddress, userCP, userCity)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";

    static private String retrieve = "SELECT userLogin, userPassword, userFirstName, userLastName, userAddress, userCP, userCity FROM User WHERE userLogin LIKE ? AND userPassword LIKE ?";

    static public String getRetrieve() {
        return retrieve;
    }

    static public String getInsert() {
        return insert;
    }

    /*public static String insert() {
        return "INSERT INTO user (userLogin, userPassword, userFirstName, userLastName, userAddress, userCP, userCity)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    public static String retrieve() {
        return "SELECT userLogin, userPassword, userFirstName, userLastName, userAddress, userCP, userCity FROM user WHERE userLogin LIKE ?";
    }*/

}
