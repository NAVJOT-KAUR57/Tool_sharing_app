/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toolkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Anubhav
 */
@Path("app")
public class GenericResource {

    @Context
    private UriInfo context;

    public GenericResource() {
    }
    JSONObject jsondata = new JSONObject();
    ResultSet rs;
    Connectionclass conclass = new Connectionclass();
    Statement stm;
    int number = 0;

    Date todaysdate = new Date();
    java.sql.Timestamp sq = new java.sql.Timestamp(todaysdate.getTime());

    @GET
    @Path("registration&{firstName}&{lastName}&{email}&{phonenumber}&{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("firstName") String fn, @PathParam("lastName") String ln, @PathParam("email") String email,
            @PathParam("password") String pass, @PathParam("phonenumber") String pNumber) throws SQLException {
        int userid = 0;
        String email_id = null;
        try {
            stm = conclass.createConnection();
            try {
                ResultSet rs2 = stm.executeQuery("select email,user_id from users where exists(select email from users where email='" + email + "')");
                if (rs2.next() == false) {
                    ResultSet rs = stm.executeQuery("SELECT USER_ID,EMAIL FROM USERS order by USER_ID DESC");
                    rs.next();

                    userid = rs.getInt("USER_ID");
                    email_id = rs.getString("EMAIL");
                    System.out.println("USERID OF THE USER IS " + userid);
                    ++userid;

                    number = stm.executeUpdate("INSERT INTO USERS VALUES(+" + userid + "," + "'" + fn + "'" + "," + "'" + ln + "'" + "," + "'" + email + "'" + "," + "'" + pNumber + "'" + "," + "'" + pass + "'" + ")");
                    System.out.println("total inserted rows" + number);
                } else {

                    jsondata.accumulate("STATUS", "WRONG");
                    jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsondata.accumulate("Message", "Email is already Register");

                }
            } catch (SQLException sq) {

                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sq);

                userid = 100;
            }

            if (number == 1) {
                jsondata.accumulate("STATUS", "OK");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Active", true);
                jsondata.accumulate("User_id", userid);
                jsondata.accumulate("Message", "You are successfully Register");

            } else if (number == 0) {

            }

            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.accumulate("STATUS", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "DATABASE CONNECTIVITY ERROR");
        }
        return jsondata.toString();
    }

    @GET
    @Path("login&{email}&{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("email") String email, @PathParam("password") String pass) {

        stm = conclass.createConnection();

        try {
            System.out.println("select * from USERS WHERE EMAIL=" + "'" + email + "'" + " and PASSWORD=" + "'" + pass + "'");
            rs = stm.executeQuery("select * from USERS WHERE EMAIL=" + "'" + email + "'" + " AND PASSWORD=" + "'" + pass + "'");

            String fName, lName, contactnumber, userpassword, dateOfbirth;

            int user_Id = 0;

            while (rs.next()) {

                fName = rs.getString("FIRSTNAME");
                lName = rs.getString("LASTNAME");
                email = rs.getString("EMAIL");
                contactnumber = rs.getString("CONTACTNUMBER");
                userpassword = rs.getString("PASSWORD");
                user_Id = rs.getInt("USER_ID");
                System.out.println("username is " + fName);
                if (user_Id != 0) {

                    jsondata.accumulate("Status", "OK");
                    jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsondata.accumulate("Active", true);
                    jsondata.accumulate("User_id", user_Id);
                    jsondata.accumulate("Password", userpassword);

                }
            }
            if (user_Id == 0) {
                jsondata.clear();
                jsondata.accumulate("Status", "WRONG");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("MESSAGE", "YOUR ENTERED THE WRONG INFORMATION");
            }
            rs.close();
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            jsondata.clear();
            jsondata.accumulate("Status", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "Database connectivity error");
        }

        return jsondata.toString();
    }

    @GET
    @Path("myprofile&{userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("userid") int u_id) {

        stm = conclass.createConnection();
        try {
            System.out.println("select * from USERS WHERE USER_ID=" + u_id);
            rs = stm.executeQuery("select * from USERS WHERE USER_ID=" + u_id);

            String fName, lName, email, contactnumber, userpassword, username;

            int user_Id = 0;

            while (rs.next()) {

                fName = rs.getString("FIRSTNAME").trim();
                lName = rs.getString("LASTNAME").trim();
                email = rs.getString("EMAIL").trim();
                contactnumber = rs.getString("CONTACTNUMBER").trim();
                userpassword = rs.getString("PASSWORD").trim();
                user_Id = rs.getInt("USER_ID");
                System.out.println("username is " + fName);

                jsondata.accumulate("Status", "OK");
                jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
                jsondata.accumulate("firstname", fName);
                jsondata.accumulate("lastname", lName);
                jsondata.accumulate("email", email);
                jsondata.accumulate("contactnumber", contactnumber);
                jsondata.accumulate("Password", userpassword);
                jsondata.accumulate("user_id", user_Id);

            }
            if (user_Id == 0) {
                jsondata.accumulate("Status", "Wrong");
                jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
                jsondata.accumulate("MESSAGE", "something wrong in the userID");
            }

            rs.close();
            stm.close();

        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.accumulate("STATUS", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "Database connectivity error");
        }

        return jsondata.toString();
    }

    @GET
    @Path("changepassword&{user_id}&{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getchangepasswordJson(@PathParam("user_id") int u_id, @PathParam("password") String pass) throws SQLException {

        stm = conclass.createConnection();
        try {
            ResultSet rs = stm.executeQuery("SELECT PASSWORD FROM USERS WHERE USER_ID=" + u_id);
            rs.next();
            String cpass = rs.getString("PASSWORD");
            if (cpass.equals(pass)) {
                jsondata.accumulate("STATUS", "WRONG");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Message", "old password  is Selected");

            } else {
                number = stm.executeUpdate("UPDATE USERS SET PASSWORD='" + pass + "' WHERE USER_ID='" + u_id + "'");

                System.out.println("UPDATE USERS SET PASSWORD='" + pass + "' WHERE USER_ID='" + u_id + "'");
                System.out.println("total inserted rows" + number);

                jsondata.accumulate("STATUS", "OK");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Message", "Password is Changed");
            }

        } catch (SQLException sq) {

            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sq);

        }
        return jsondata.toString();
    }

    @GET
    @Path("forgetpassword&{user_id}&{email}&{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("user_id") int uid, @PathParam("email") String email,
            @PathParam("password") String pass) throws SQLException {
        int userid = 0;
        String email_id = null, cpass;
        try {
            stm = conclass.createConnection();
            try {
                ResultSet rs2 = stm.executeQuery("SELECT * FROM USERS WHERE USER_ID='" + uid + "' AND EMAIL='" + email + "'");
                if (rs2.next() == true) {
                    cpass = rs2.getString("PASSWORD");

                    if (cpass.equals(pass)) {
                        jsondata.clear();
                    jsondata.accumulate("STATUS", "WRONG");
                    jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsondata.accumulate("Message", "You cannot enter previous password");
                    } 
                    else {
                        number = stm.executeUpdate("UPDATE USERS SET PASSWORD='" + pass + "'");
                        System.out.println("total inserted rows" + number);
                        
                jsondata.accumulate("STATUS", "OK");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Active", true);
                jsondata.accumulate("User_id", uid);
                jsondata.accumulate("Message", "password set successfully");

                    }
                } else {

                    jsondata.clear();
                    jsondata.accumulate("STATUS", "WRONG");
                    jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsondata.accumulate("Message", "Email and user_id doesn't match");

                }
            } catch (SQLException sql) {

                jsondata.accumulate("STATUS", "ERROR");
                jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Message", "Email and user_id doesn't match");
                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sql);

            }

            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

        }
        return jsondata.toString();
    }

    @GET
    @Path("addpost&{Description}&{Location}&{Price}&{Category}&{StartTime}&{User_id}&{tool_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("Description") String desc, @PathParam("Location") String loc, @PathParam("Price") double price, @PathParam("Category") String cate,
            @PathParam("StartTime") String starttime, @PathParam("User_id") int userid,@PathParam("tool_name")String theToolname) throws SQLException {
        int postid = 0;

        try {
            stm = conclass.createConnection();
            try {
                rs = stm.executeQuery("SELECT POST_ID FROM POST ORDER BY POST_ID DESC");
                rs.next();
                postid = rs.getInt("POST_ID");
                System.out.println("POSTID OF THE POST IS " + postid);
                ++postid;
            } catch (SQLException sq) {
                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sq);

                postid = 1000;
            }
            System.out.println("INSERT INTO POST VALUES(" + postid + ",'" + desc + "','"
                    + loc + "'," + price + ",'" + cate + "','" + starttime + "',"
                    + userid + ")");
            number = stm.executeUpdate("INSERT INTO POST VALUES(" + postid + ",'" + desc + "','"
                    + loc + "'," + price + ",'" + cate + "','" + starttime + "',"
                    + userid + ")");
            System.out.println("total inserted rows" + number);

            if (price < 0) {

                jsondata.accumulate("STATUS", "WRONG");
                jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Message", "Your "
                        + "entered information is wrong");
            } else {
                
                    getJsontool(theToolname, postid,cate);
                
                jsondata.accumulate("STATUS", "OK");
                jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Post_id", postid);
                jsondata.accumulate("Message", "Your add is successfully Post");
            }

            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            jsondata.accumulate("STATUS", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "Database connectivity error");

        }
        return jsondata.toString();
    }

    @GET
    @Path("addadmin&{firstName}&{lastName}&{email}&{phonenumber}&{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonadmin(@PathParam("firstName") String fn, @PathParam("lastName") String ln, @PathParam("email") String email,
            @PathParam("password") String pass, @PathParam("phonenumber") String pNumber) throws SQLException {
        int adminid = 1;
        String email_id = null;
        try {
            stm = conclass.createConnection();
            try {
                ResultSet rs2 = stm.executeQuery("select admin_email,admin_id from users where exists"
                        + "(select admin_email from admins where admin_email='" + email + "')");
                if (rs2.next() == false) {
                    ResultSet rs = stm.executeQuery("SELECT ADMIN_ID,ADMIN_EMAIL FROM ADMINS order by ADMIN_ID DESC");
                    rs.next();

                    adminid = rs.getInt("ADMIN_I");
                    email_id = rs.getString("ADMIN_EMAIL");
                    System.out.println("ADMIN_ID OF THE ADMINS IS " + adminid);
                    ++adminid;

                    number = stm.executeUpdate("INSERT INTO ADMINS VALUES(+" + adminid + "," + "'" + fn + "'" + "," + "'" + ln + "'" + "," + "'" + email + "'" + "," + "'" + pNumber + "'" + "," + "'" + pass + "'" + ")");
                    System.out.println("total inserted rows" + number);
                } else {

                    jsondata.accumulate("STATUS", "WRONG");
                    jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsondata.accumulate("Message", "Email is already Register");

                }
            } catch (SQLException sq) {

                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sq);

                adminid = 100;
            }

            if (number == 1) {
                jsondata.accumulate("STATUS", "OK");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Active", true);
                jsondata.accumulate("User_id", adminid);
                jsondata.accumulate("Message", "You are successfully Register");

            } else if (number == 0) {

            }

            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.accumulate("STATUS", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "DATABASE CONNECTIVITY ERROR");
        }
        return jsondata.toString();
    }

    @GET
    @Path("addadmin&{adminname}&{email}&{phonenumber}&{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("adminname") String adminname, @PathParam("email") String email,
            @PathParam("password") String pass, @PathParam("phonenumber") String pNumber) throws SQLException {
        int adminid = 0;
        String email_id = null;
        try {
            stm = conclass.createConnection();
            try {
                ResultSet rs2 = stm.executeQuery("select email,user_id from users where exists(select email from users where email='" + email + "')");
                if (rs2.next() == false) {
                    ResultSet rs = stm.executeQuery("SELECT ADMIN_ID,ADMIN_EMAIL FROM ADMINS order by ADMIN_ID DESC");
                    rs.next();

                    adminid = rs.getInt("ADMIN_ID");
                    email_id = rs.getString("ADMIN_EMAIL");
                    System.out.println("ADMINID OF THE USER IS " + adminid);
                    ++adminid;

                    number = stm.executeUpdate("INSERT INTO ADMINS VALUES(+" + adminid + "," + "'" + adminname + "'" + ",'" + email
                            + "'" + "," + "'" + pass + "'" + "," + "'" + pNumber + "'" + ")");
                    System.out.println("total inserted rows" + number);
                } else {

                    jsondata.accumulate("STATUS", "WRONG");
                    jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsondata.accumulate("Message", "Email is already Register");

                }
            } catch (SQLException sq) {

                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sq);

                adminid = 1;
            }

            if (number == 1) {
                jsondata.accumulate("STATUS", "OK");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Active", true);
                jsondata.accumulate("Admin_id", adminid);
                jsondata.accumulate("Message", "You are successfully Register");

            } else if (number == 0) {

            }

            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.accumulate("STATUS", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "DATABASE CONNECTIVITY ERROR");
        }
        return jsondata.toString();
    }

    @GET
    @Path("adminlogin&{email}&{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonadmin(@PathParam("email") String email, @PathParam("password") String pass) {

        stm = conclass.createConnection();

        try {
            System.out.println("select * from ADMINS WHERE ADMIN_EMAIL=" + "'" + email + "'" + " and ADMIN_PASSWORD=" + "'" + pass + "'");
            rs = stm.executeQuery("select * from ADMINS WHERE ADMIN_EMAIL=" + "'" + email + "'" + " and ADMIN_PASSWORD=" + "'" + pass + "'");

            String adminname, contactnumber, userpassword, dateOfbirth;

            int user_Id = 0;

            while (rs.next()) {

                adminname = rs.getString("ADMIN_NAME");
                email = rs.getString("ADMIN_EMAIL");
                contactnumber = rs.getString("ADMIN_CONTACTNUMBER");
                userpassword = rs.getString("ADMIN_PASSWORD");
                user_Id = rs.getInt("ADMIN_ID");
                System.out.println("username is " + adminname);
                if (user_Id != 0) {

                    jsondata.accumulate("Status", "OK");
                    jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsondata.accumulate("Active", true);
                    jsondata.accumulate("User_id", user_Id);
                    jsondata.accumulate("Password", userpassword);

                }
            }
            if (user_Id == 0) {
                jsondata.clear();
                jsondata.accumulate("Status", "WRONG");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("MESSAGE", "YOUR ENTERED THE WRONG INFORMATION");
            }
            rs.close();
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
            jsondata.clear();
            jsondata.accumulate("Status", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "Database connectivity error");
        }

        return jsondata.toString();
    }

    JSONArray jsonarray = new JSONArray();
    String description = null, location = null, startdatetime = null;
    int user_id = 0;
    double price = 0.0;

    @GET
    @Path("viewpost")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewpostgetJson() throws SQLException {
        int post_id = 0;

        JSONObject postdata = new JSONObject();
        try {
            stm = conclass.createConnection();
            System.out.println("select * from POST");
            rs = stm.executeQuery("select * from POST");
            jsondata.accumulate("Status", "OK");
            jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
            while (rs.next()) {
                description = rs.getString("DESCRIPTION");
                location = rs.getString("LOCATION");
                startdatetime = rs.getTimestamp("STARTTIME").toString();

                price = rs.getDouble("PRICE");
                post_id = rs.getInt("POST_ID");
                user_id = rs.getInt("USER_ID");

                postdata.accumulate("DESCRIPTION", description);
                postdata.accumulate("LOCATION", location);
                postdata.accumulate("STARTTIME", startdatetime);

                postdata.accumulate("USER_ID", user_id);
                postdata.accumulate("POST_ID", post_id);
                postdata.accumulate("PRICE", price);
                jsonarray.add(postdata);
                postdata.clear();

            }
            jsondata.accumulate("POSTDATA", jsonarray);
            if (post_id == 0) {
                jsondata.accumulate("Status", "OK");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("MESSAGE", "SOME INFORMATION WENT WRONG");

            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.accumulate("Status", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "Database connectivity error");

        }
        return jsondata.toString();
    }

    @GET
    @Path("addtools&{tool_name}&{post_id}&{category_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsontool(@PathParam("tool_name") String toolname, @PathParam("post_id") int postId,@PathParam("category_name")String thecategoryname)
            throws SQLException {
        int toolid = 0;

        try {
            stm = conclass.createConnection();
            try {

                ResultSet rs = stm.executeQuery("SELECT TOOL_ID FROM TOOLS order by TOOL_ID DESC");
                rs.next();

                toolid = rs.getInt("TOOL_ID");
                ++toolid;

                System.out.println("INSERT INTO TOOLS VALUES(" + toolid + "," + "'" + toolname + "'" + "," + postId + ",'"+thecategoryname+"')");
                number = stm.executeUpdate("INSERT INTO TOOLS VALUES(" + toolid + "," + "'" + toolname + "'" + "," + postId + ",'"+thecategoryname+"')");
                System.out.println("total inserted rows" + number);

            } catch (SQLException sq) {

                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sq);

            }

            if (number == 1) {
                
                jsondata.accumulate("tool_id", toolid);
                jsondata.accumulate("Message", "You are successfully Register your tool");

                getJsonCaegory(thecategoryname, toolid);
            } else if (number == 0) {

                jsondata.accumulate("STATUS", "WRONG");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Message", "Tool is already Register");
            }

            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.accumulate("STATUS", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "DATABASE CONNECTIVITY ERROR");
        }
        return jsondata.toString();
    }

    @GET
    @Path("addcategory&{category_type}&{tool_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonCaegory(@PathParam("category_type") String categoryname, @PathParam("tool_id") int toolID)
            throws SQLException {
        int category_id = 0;

        try {
            stm = conclass.createConnection();
            try {

                ResultSet rs = stm.executeQuery("SELECT CATEGORY_ID FROM CATEGORY order by CATEGORY_ID DESC");
                rs.next();

                category_id = rs.getInt("CATEGORY_ID");
                ++category_id;

                number = stm.executeUpdate("INSERT INTO CATEGORY VALUES(+" + category_id + "," + "'" + categoryname + "'" + "," + toolID + ")");
                System.out.println("total inserted rows" + number);

            } catch (SQLException sq) {

                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sq);

            }

            if (number == 1) {
               
                jsondata.accumulate("Category", category_id);
                jsondata.accumulate("Message", "You are successfully Register your Category");

            } else if (number == 0) {

                jsondata.accumulate("STATUS", "WRONG");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Message", "Category is already Register");
            }

            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.accumulate("STATUS", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "DATABASE CONNECTIVITY ERROR");
        }
        return jsondata.toString();
    }

    @GET
    @Path("recieveposts&{cat}")
    @Produces(MediaType.APPLICATION_JSON)
    public String recievepostsgetJson(@PathParam("cat") String thecat) throws SQLException {
        String tool_name = null, cate_name = null, starttime;
        int post_id = 0;

        JSONObject viewpost = new JSONObject();
        try {
            stm = conclass.createConnection();

            System.out.println("SELECT POST.POST_ID,POST.STARTTIME,TOOLS.TOOL_NAME,CATEGORY,"
                    + "POST.LOCATION FROM POST FULL JOIN TOOLS ON POST.POST_ID=TOOLS.POST_ID WHERE CATEGORY='" + thecat + "'");
            rs = stm.executeQuery("SELECT POST.POST_ID,POST.STARTTIME,TOOLS.TOOL_NAME,CATEGORY,"
                    + "POST.LOCATION FROM POST FULL JOIN TOOLS ON POST.POST_ID=TOOLS.POST_ID WHERE CATEGORY='" + thecat + "'");

            viewpost.accumulate("Status", "OK");
            viewpost.accumulate("Timestamp", sq.toInstant().toEpochMilli());

            while (rs.next()) {

                tool_name = rs.getString("TOOL_NAME");
                post_id = rs.getInt("POST_ID");
                cate_name = rs.getString("CATEGORY");

                starttime = rs.getString("STARTTIME");

                jsondata.accumulate("POST_ID", post_id);
                jsondata.accumulate("CATEGORY_NAME", cate_name);

                jsondata.accumulate("STARTTIME", starttime);
                jsondata.accumulate("TOOL_NAME", tool_name);

                jsonarray.add(jsondata);

                jsondata.clear();
            }
            viewpost.accumulate("DATA", jsonarray);
            if (post_id == 0) {
                viewpost.clear();

                viewpost.accumulate("Status", "WRONG");
                viewpost.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                viewpost.accumulate("Message", "no message");
            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            viewpost.clear();
            viewpost.accumulate("STATUS", "ERROR");
            viewpost.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            viewpost.accumulate("MESSAGE", "Database connectivity error");

        }
        return viewpost.toString();
    }

    @GET
    @Path("recievepost&{post_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String RecieveMessagegetJson(@PathParam("post_id") int post_id) throws SQLException {
        String tool_name = null, cate_name = null, location, firstname, lastname, description, date, email_id, contactnumber;
        int user_id = 0;

        JSONObject viewpost = new JSONObject();
        try {
            stm = conclass.createConnection();

            rs = stm.executeQuery("SELECT POST.POST_ID,USERS.CONTACTNUMBER,TOOLS.TOOL_NAME,CATEGORY.CATEGORY_NAME,POST.LOCATION,USERS.FIRSTNAME,\n"
                    + "USERS.LASTNAME,USERS.EMAIL,POST.DESCRIPTION,POST.PRICE,POST.STARTTIME FROM POST \n"
                    + "FULL JOIN TOOLS ON POST.POST_ID=TOOLS.POST_ID FULL JOIN CATEGORY ON TOOLS.TOOL_ID=CATEGORY.TOOL_ID FULL JOIN USERS ON POST.USER_ID=USERS.USER_ID WHERE POST.POST_ID=" + post_id);

            System.out.println("SELECT POST.POST_ID,USERS.CONTACTNUMBER,TOOLS.TOOL_NAME,CATEGORY.CATEGORY_NAME,POST.LOCATION,USERS.FIRSTNAME,\n"
                    + "USERS.LASTNAME,USERS.EMAIL,POST.DESCRIPTION,POST.PRICE,POST.STARTTIME FROM POST \n"
                    + "JOIN TOOLS ON POST.POST_ID=TOOLS.POST_ID JOIN CATEGORY ON TOOLS.TOOL_ID=CATEGORY.TOOL_ID JOIN USERS ON POST.USER_ID=USERS.USER_ID WHERE POST.POST_ID=" + post_id);

            rs.next();

            viewpost.accumulate("Status", "OK");
            viewpost.accumulate("Timestamp", sq.toInstant().toEpochMilli());

            tool_name = rs.getString("TOOL_NAME");
            user_id = rs.getInt("POST_ID");
            cate_name = rs.getString("CATEGORY_NAME");

            email_id = rs.getString("EMAIL");
            firstname = rs.getString("FIRSTNAME");
            lastname = rs.getString("LASTNAME");
            description = rs.getString("DESCRIPTION");
            date = rs.getString("STARTTIME");
            contactnumber = rs.getString("CONTACTNUMBER");
            location = rs.getString("LOCATION");
            double price = rs.getDouble("PRICE");

            jsondata.accumulate("POST_ID", user_id);
            jsondata.accumulate("CATEGORY_NAME", cate_name);

            jsondata.accumulate("LOCATION", location);
            jsondata.accumulate("TOOL_NAME", tool_name);

            jsondata.accumulate("EMAIL", email_id);
            jsondata.accumulate("FIRSTNAME", firstname);
            jsondata.accumulate("LASTNAME", lastname);
            jsondata.accumulate("STARTTIME", date);
            jsondata.accumulate("DESCRIPTION", description);
            jsondata.accumulate("CONTACTNUMBER", contactnumber);
            jsondata.accumulate("PRICE", price);

            if (user_id == 0) {

                jsondata.accumulate("Status", "WRONG");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Message", "no message");
            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.accumulate("STATUS", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "Database connectivity error");

        }
        return jsondata.toString();
    }

    @GET
    @Path("recievedeletepost")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleterecievepostgetJson() throws SQLException {
        String tool_name = null, category_id, cate_name = null, starttime;
        int tool_id, post_id = 0;

        JSONObject viewpost = new JSONObject();
        try {
            stm = conclass.createConnection();

            rs = stm.executeQuery("SELECT POST.POST_ID,POST.STARTTIME,TOOLS.TOOL_NAME,CATEGORY.CATEGORY_NAME,CATEGORY.CATEGORY_ID,"
                    + "TOOLS.TOOL_ID FROM POST JOIN TOOLS ON POST.POST_ID=TOOLS.POST_ID JOIN CATEGORY ON TOOLS.TOOL_ID=CATEGORY.TOOL_ID");

            viewpost.accumulate("Status", "OK");
            viewpost.accumulate("Timestamp", sq.toInstant().toEpochMilli());

            while (rs.next()) {

                tool_name = rs.getString("TOOL_NAME");
                post_id = rs.getInt("POST_ID");
                cate_name = rs.getString("CATEGORY_NAME");

                starttime = rs.getString("STARTTIME");

                tool_id = rs.getInt("TOOL_ID");
                category_id = rs.getString("CATEGORY_ID");

                jsondata.accumulate("TOOL_ID", tool_id);
                jsondata.accumulate("CATEGORY_ID", category_id);
                jsondata.accumulate("POST_ID", post_id);
                jsondata.accumulate("CATEGORY_NAME", cate_name);
                jsondata.accumulate("STARTTIME", starttime);
                jsondata.accumulate("TOOL_NAME", tool_name);

                jsonarray.add(jsondata);

                jsondata.clear();
            }
            viewpost.accumulate("DATA", jsonarray);
            if (post_id == 0) {
                viewpost.clear();

                viewpost.accumulate("Status", "WRONG");
                viewpost.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                viewpost.accumulate("Message", "no message");
            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            viewpost.clear();
            viewpost.accumulate("STATUS", "ERROR");
            viewpost.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            viewpost.accumulate("MESSAGE", "Database connectivity error");

        }
        return viewpost.toString();
    }

    @GET
    @Path("deletepost&{post_id}&{tool_id}&{category_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePostgetJson(@PathParam("post_id") int postId, @PathParam("tool_id") int toolId, @PathParam("category_id") int categoryId) throws SQLException {
        String tool_name = null, cate_name = null, starttime;
        int post_id = 0;

        JSONObject viewpost = new JSONObject();
        try {
            stm = conclass.createConnection();

            if (number == 0) {
                number = stm.executeUpdate("DELETE FROM CATEGORY WHERE CATEGORY_ID=" + categoryId);
            }
            if (number == 1) {
                number = 0;
                number = stm.executeUpdate("DELETE FROM TOOLS WHERE TOOL_ID=" + toolId);
            }
            if (number == 1) {

                number = 0;
                number = stm.executeUpdate("DELETE FROM POST WHERE POST_ID=" + postId);
            }
            if (number == 1) {

                jsondata.accumulate("Status", "OK");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Message", "Post deletion is successfull");
            }

            if (number == 0) {
                jsondata.clear();

                jsondata.accumulate("Status", "WRONG");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Message", "post is already deleted");
            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.clear();
            jsondata.accumulate("STATUS", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "Database connectivity error");

        }
        return jsondata.toString();
    }

    @GET
    @Path("userList")
    @Produces(MediaType.APPLICATION_JSON)
    public String userlistgetJson() throws SQLException {
        String firstName = null, lastName = null, email, contactNumber;
        int user_id = 0, post_id;

        JSONObject viewpost = new JSONObject();
        try {
            stm = conclass.createConnection();

            rs = stm.executeQuery("SELECT * FROM USERS");

            viewpost.accumulate("Status", "OK");
            viewpost.accumulate("Timestamp", sq.toInstant().toEpochMilli());

            while (rs.next()) {

                firstName = rs.getString("FIRSTNAME").trim();
                lastName = rs.getString("LASTNAME").trim();
                email = rs.getString("EMAIL");
                user_id = rs.getInt("USER_ID");
                contactNumber = rs.getString("CONTACTNUMBER");

                jsondata.accumulate("USER_ID", user_id);
                jsondata.accumulate("USERNAME", firstName + " " + lastName);
                jsondata.accumulate("EMAIL", email);
                jsondata.accumulate("CONTACTNUMBER", contactNumber);

                jsondata.accumulate("MESSAGE", "YOUR USER LIST IS HERE");

                jsonarray.add(jsondata);

                jsondata.clear();
            }
            viewpost.accumulate("DATA", jsonarray);
            if (user_id == 0) {
                viewpost.clear();

                viewpost.accumulate("Status", "WRONG");
                viewpost.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                viewpost.accumulate("Message", "no user is registered");
            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            viewpost.clear();
            viewpost.accumulate("STATUS", "ERROR");
            viewpost.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            viewpost.accumulate("MESSAGE", "Database connectivity error");

        }
        return viewpost.toString();
    }

    @GET
    @Path("deleteuser&{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteUsergetJson(@PathParam("user_id") int userId
    ) throws SQLException {

        int post_id = 0, tool_id = 0, category_id = 0;

        ResultSet rs2, rs3;
        JSONObject viewpost = new JSONObject();
        try {
            stm = conclass.createConnection();
            rs = stm.executeQuery("SELECT POST_ID FROM POST WHERE POST.USER_ID=" + userId);

            while (rs.next()) {
                post_id = rs.getInt("POST_ID");
                System.out.println(post_id);

                if (post_id != 0) {
                    rs2 = stm.executeQuery("SELECT TOOL_ID FROM TOOLS WHERE POST_ID=" + post_id);

                    while (rs2.next()) {
                        tool_id = rs2.getInt("TOOL_ID");
                        if (tool_id != 0) {

                            rs3 = stm.executeQuery("SELECT CAGETORY_ID FROM CATEGORY WHERE TOOL_ID=" + tool_id);
                            while (rs3.next()) {
                                category_id = rs3.getInt("CATEGORY_ID");

                                number = stm.executeUpdate("DELETE FROM CATEGORY WHERE CATEGORY_ID=" + category_id);
                            }
                        }

                        number = stm.executeUpdate("DELETE FROM TOOLS WHERE TOOL_ID=" + tool_id);

                    }

                    number = stm.executeUpdate("DELETE FROM POST WHERE POST_ID=" + post_id);

                }

            }

            number = stm.executeUpdate("DELETE FROM USERS WHERE USER_ID=" + userId);
            if (number == 1) {

                jsondata.accumulate("Status", "OK");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Message", "user deletion is successfull");
            }

            if (number == 0) {
                jsondata.clear();

                jsondata.accumulate("Status", "WRONG");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Message", "user"
                        + " is already deleted");
            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.clear();
            jsondata.accumulate("STATUS", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "Database connectivity error");

        }
        return jsondata.toString();
    }

    @GET
    @Path("message&{message}&{sender_id}&{reciever_email}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("message") String message, @PathParam("sender_id") int senderId, @PathParam("reciever_email") String email) throws SQLException {
        int message_id = 0, user_id = 0;

        try {
            stm = conclass.createConnection();
            try {
                System.out.println("select user_id from users  where email='" + email + "'");
                ResultSet rs2 = stm.executeQuery("select user_id from users where email='" + email + "'");
                if (rs2.next() == true) {
                    user_id = rs2.getInt("USER_ID");
                    rs = stm.executeQuery("SELECT MESSAGE_ID FROM MESSAGE ORDER BY MESSAGE_ID DESC");
                    rs.next();
                    message_id = rs.getInt("MESSAGE_ID");

                    System.out.println("USERID OF THE USER IS " + user_id);
                    ++message_id;
                    System.out.println("INSERT INTO MESSAGE VALUES(" + message_id + "," + "'" + message + "'," + senderId + ","
                            + user_id + ",CURRENT_TIMESTAMP");
                    number = stm.executeUpdate("INSERT INTO MESSAGE VALUES(" + message_id + "," + "'" + message + "'," + senderId + ","
                            + user_id + "," + "CURRENT_TIMESTAMP" + ")");

                    System.out.println("total inserted rows" + number);
                } else {

                    jsondata.accumulate("STATUS", "WRONG");
                    jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsondata.accumulate("Message", "Email is not  available");

                }
            } catch (SQLException sq) {

                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sq);

                message_id = 2000;
            }

            if (number == 1) {
                jsondata.accumulate("STATUS", "OK");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Active", true);
                jsondata.accumulate("Message_id", message_id);
                jsondata.accumulate("Message", "You are successfully send message");

            }

            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.accumulate("STATUS", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "DATABASE CONNECTIVITY ERROR");
        }
        return jsondata.toString();
    }

    @GET
    @Path("recievemessage&{sender_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String RecieveMessagegtJson(@PathParam("sender_id") int sender_id) throws SQLException {
        String message = null, date = null, firstname, lastname, contactnumber, emailID;
        int user_id = 0, message_id, reciever_id;

        JSONObject viewmessage = new JSONObject();
        try {
            stm = conclass.createConnection();
            System.out.println("select * from Message WHERE RECIEVER_ID=" + sender_id);

            rs = stm.executeQuery("SELECT MESSAGE.SENDER_ID,USERS.CONTACTNUMBER,DATETIME,FIRSTNAME,LASTNAME,MESSAGE,"
                    + "EMAIL,MESSAGE_ID FROM USERS JOIN MESSAGE ON USERS.USER_ID=MESSAGE.SENDER_ID WHERE RECIEVER_ID=" + sender_id);

            viewmessage.accumulate("Status", "OK");
            viewmessage.accumulate("Timestamp", sq.toInstant().toEpochMilli());

            while (rs.next()) {

                message = rs.getString("MESSAGE");
                user_id = rs.getInt("SENDER_ID");
                date = rs.getString("DATETIME");

                firstname = rs.getString("FIRSTNAME").trim();
                lastname = rs.getString("LASTNAME").trim();
                contactnumber = rs.getString("CONTACTNUMBER");
                emailID = rs.getString("EMAIL");

                message_id = rs.getInt("MESSAGE_ID");

                jsondata.accumulate("Send_id", user_id);
                jsondata.accumulate("date", date);
                jsondata.accumulate("EMAIL_ID", emailID);
                jsondata.accumulate("FIRSTNAME", firstname);
                jsondata.accumulate("LASTNAME", lastname);
                jsondata.accumulate("CONTACTNUMBER", contactnumber);
                jsondata.accumulate("Message", message);

                jsondata.accumulate("MESSAGE_ID", message_id);
                jsonarray.add(jsondata);

                jsondata.clear();
            }
            viewmessage.accumulate("DATA", jsonarray);
            if (user_id == 0) {
                viewmessage.clear();

                viewmessage.accumulate("Status", "WRONG");
                viewmessage.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                viewmessage.accumulate("Message", "no message");
            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            viewmessage.clear();
            viewmessage.accumulate("STATUS", "ERROR");
            viewmessage.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            viewmessage.accumulate("MESSAGE", "Database connectivity error");

        }
        return viewmessage.toString();
    }

    @GET
    @Path("message&{message_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String messagegetJson(@PathParam("message_id") int messageId) throws SQLException {
        String message = null, occupation, qualification, date = null, firstname, lastname, contactnumber, emailID;
        int senderuser_id = 0;

        JSONObject viewmessage = new JSONObject();
        try {
            stm = conclass.createConnection();

            rs = stm.executeQuery("SELECT * FROM USERS JOIN MESSAGE ON USERS.USER_ID=MESSAGE.SENDER_ID WHERE MESSAGE_ID=" + messageId);

            System.out.println("SELECT * FROM USERS JOIN MESSAGE ON USERS.USER_ID=MESSAGE.SENDER_ID WHERE MESSAGE_ID=" + messageId);
            rs.next();
            message = rs.getString("MESSAGE").trim();

            date = rs.getString("DATETIME").trim();

            firstname = rs.getString("FIRSTNAME").trim();
            lastname = rs.getString("LASTNAME").trim();
            contactnumber = rs.getString("CONTACTNUMBER").trim();
            emailID = rs.getString("EMAIL").trim();
            senderuser_id = rs.getInt("SENDER_ID");

            jsondata.accumulate("Status", "OK");

            jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());

            jsondata.accumulate("date", date);
            jsondata.accumulate("EMAIL_ID", emailID);
            jsondata.accumulate("FIRSTNAME", firstname);
            jsondata.accumulate("LASTNAME", lastname);
            jsondata.accumulate("CONTACTNUMBER", contactnumber);
            jsondata.accumulate("Message", message);
            jsondata.accumulate("SENDER_ID", senderuser_id);

            if (messageId == 0) {
                jsondata.accumulate("Status", "WRONG");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Message", "no message");

            }
        } catch (SQLException ex) {

            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.clear();
            jsondata.accumulate("STATUS", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "Database connectivity error");

        }

        return jsondata.toString();
    }

    @GET
    @Path("recievecategory")
    @Produces(MediaType.APPLICATION_JSON)
    public String categorypostgetJson() throws SQLException {
        String tool_name = null, cate_name = null, starttime;
        int post_id = 1;

        JSONObject viewpost = new JSONObject();
        try {
            stm = conclass.createConnection();

            rs = stm.executeQuery("SELECT DISTINCT(CATEGORY) FROM POST");

            viewpost.accumulate("Status", "OK");
            viewpost.accumulate("Timestamp", sq.toInstant().toEpochMilli());

            while (rs.next()) {

                cate_name = rs.getString("CATEGORY");

                jsondata.accumulate("CATEGORY_NAME", cate_name);

                jsonarray.add(jsondata);

                jsondata.clear();
            }
            viewpost.accumulate("DATA", jsonarray);
            if (post_id == 0) {
                viewpost.clear();

                viewpost.accumulate("Status", "WRONG");
                viewpost.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                viewpost.accumulate("Message", "no message");
            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            viewpost.clear();
            viewpost.accumulate("STATUS", "ERROR");
            viewpost.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            viewpost.accumulate("MESSAGE", "Database connectivity error");

        }
        return viewpost.toString();
    }

    @GET
    @Path("viewposts&{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewpostusergetJson(@PathParam("user_id") int user_iD) throws SQLException {
        int post_id = 0;
        String cat;
        try {
            JSONObject postdata = new JSONObject();

            stm = conclass.createConnection();
            System.out.println("select * from POST WHERE USER_ID=" + user_iD);
            rs = stm.executeQuery("select * from POST WHERE USER_ID=" + user_iD);
            jsondata.accumulate("Status", "OK");
            jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
            while (rs.next()) {
                description = rs.getString("DESCRIPTION");
                location = rs.getString("LOCATION");
                startdatetime = rs.getTimestamp("STARTTIME").toString();

                price = rs.getDouble("PRICE");
                post_id = rs.getInt("POST_ID");
                user_id = rs.getInt("USER_ID");
                cat = rs.getString("CATEGORY");

                postdata.accumulate("DESCRIPTION", description);
                postdata.accumulate("LOCATION", location);
                postdata.accumulate("STARTTIME", startdatetime);

                postdata.accumulate("CATEGORY_NAME", cat);
                postdata.accumulate("USER_ID", user_id);
                postdata.accumulate("POST_ID", post_id);
                postdata.accumulate("PRICE", price);
                jsonarray.add(postdata);
                postdata.clear();

            }
            jsondata.accumulate("POSTDATA", jsonarray);
            if (post_id == 0) {
                jsondata.accumulate("Status", "OK");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("MESSAGE", "SOME INFORMATION WENT WRONG");

            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.accumulate("Status", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "Database connectivity error");

        }
        return jsondata.toString();
    }

    @GET
    @Path("viewpost&{post_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String viewposusergetJson(@PathParam("post_id") int post_ID) throws SQLException {
        int post_id = 0;
        String cat;
        JSONObject postdata = new JSONObject();
        try {

            stm = conclass.createConnection();
            System.out.println("select * from POST WHERE POST_ID=" + post_ID);
            rs = stm.executeQuery("select * from POST WHERE POST_ID=" + post_ID);

            rs.next();
            description = rs.getString("DESCRIPTION");
            location = rs.getString("LOCATION");
            startdatetime = rs.getTimestamp("STARTTIME").toString();

            price = rs.getDouble("PRICE");
            post_id = rs.getInt("POST_ID");
            user_id = rs.getInt("USER_ID");
            cat = rs.getString("CATEGORY");

            postdata.accumulate("Status", "OK");
            postdata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
            postdata.accumulate("DESCRIPTION", description);
            postdata.accumulate("LOCATION", location);
            postdata.accumulate("STARTTIME", startdatetime);

            postdata.accumulate("CATEGORY_NAME", cat);
            postdata.accumulate("USER_ID", user_id);
            postdata.accumulate("POST_ID", post_id);
            postdata.accumulate("PRICE", price);

            if (post_id == 0) {
                postdata.accumulate("Status", "OK");
                postdata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                postdata.accumulate("MESSAGE", "SOME INFORMATION WENT WRONG");

            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            postdata.accumulate("Status", "ERROR");
            postdata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            postdata.accumulate("MESSAGE", "Database connectivity error");

        }
        return postdata.toString();
    }

    @GET
    @Path("editposts&{Description}&{Location}&{Price}&{Category}&{StartTime}&{post_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String editgetJson(@PathParam("Description") String desc, @PathParam("Location") String loc, @PathParam("Price") double price, @PathParam("Category") String cate,
            @PathParam("StartTime") String starttime, @PathParam("post_id") int post_ID) throws SQLException {
        int post_id = 0;
        String cat;
        JSONObject postdata = new JSONObject();
        stm = conclass.createConnection();
        try {

            System.out.println("UPDATE POST SET DESCRIPTION='" + desc + "' , LOCATION='" + loc + "' , "
                    + "PRICE=" + price + ",CATEGORY='" + cate + "',STARTTIME='" + starttime + ":00' WHERE POST_ID=" + post_ID);

            number = stm.executeUpdate("UPDATE POST SET DESCRIPTION='" + desc + "' , LOCATION='" + loc + "' , "
                    + "PRICE=" + price + ",CATEGORY='" + cate + "',STARTTIME='" + starttime + ":00' WHERE POST_ID=" + post_ID);

            if (number == 1) {
                postdata.accumulate("Status", "OK");
                postdata.accumulate("Timestamp", sq.toInstant().toEpochMilli());

            } else {
                postdata.clear();
                postdata.accumulate("Status", "WRONG");
                postdata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                postdata.accumulate("MESSAGE", "SOME INFORMATION WENT WRONG");
            }
            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            postdata.accumulate("Status", "ERROR");
            postdata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            postdata.accumulate("MESSAGE", "Database connectivity error");

        }
        return postdata.toString();
    }

    @GET
    @Path("update&{firstName}&{lastName}&{phonenumber}&{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updategetJson(@PathParam("firstName") String fn, @PathParam("lastName") String ln,
            @PathParam("password") String pass, @PathParam("phonenumber") String pNumber, @PathParam("user_id") int userId) throws SQLException {
        int userid = 0;
        String email_id = null;
        try {
            stm = conclass.createConnection();
            try {

                number = stm.executeUpdate("UPDATE  USERS SET FIRSTNAME='" + fn + "',LASTNAME='" + ln + "',CONTACTNUMBER='" + pNumber + "'  WHERE USER_ID=" + userId);
                System.out.println("total inserted rows" + number);

                if (userId == 0) {
                    jsondata.accumulate("STATUS", "WRONG");
                    jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                    jsondata.accumulate("Message", "Email is already Register");

                }
            } catch (SQLException sq) {

                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, sq);

                userid = 100;
            }

            if (number == 1) {
                jsondata.accumulate("STATUS", "OK");
                jsondata.accumulate("Timestamp", sq.toInstant().toEpochMilli());
                jsondata.accumulate("Active", true);
                jsondata.accumulate("User_id", userId);
                jsondata.accumulate("Message", "You are successfully UPDATED");

            } else if (number == 0) {

            }

            stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);

            jsondata.accumulate("STATUS", "ERROR");
            jsondata.accumulate("TIMESTAMP", sq.toInstant().toEpochMilli());
            jsondata.accumulate("MESSAGE", "DATABASE CONNECTIVITY ERROR");
        }
        return jsondata.toString();
    }
}


