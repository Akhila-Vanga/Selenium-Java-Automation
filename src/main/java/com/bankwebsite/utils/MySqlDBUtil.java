package com.bankwebsite.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.SecretKey;

import java.io.FileInputStream;
import java.io.InputStream;

public class MySqlDBUtil {

  /*  public static void main(String[] args) {
       try {
            // Load encrypted credentials from properties file
            
            InputStream input = MySqlDBUtil.class.getClassLoader().getResourceAsStream("config.properties");
            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath!");
            }

            Properties props = new Properties();
            props.load(input);

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.username");
            String encryptedPassword = props.getProperty("db.password").trim(); 
            System.out.println(encryptedPassword);

            // Decrypt password
            String password = new String(Base64.getDecoder().decode(encryptedPassword.trim()));
            String password = decrypt(encryptedPassword, key);
            
            //String password = props.getProperty("db.password");
            System.out.println(password);
            System.out.println(url);
            System.out.println(user);

            // Connect to DB
            Connection conn = DriverManager.getConnection(url, user, password);
            //System.out.println(user,password);
            System.out.println("Connected to database!");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                System.out.println("User: " + rs.getString("username") + ", Password: " + rs.getString("Password"));
            }

            conn.close();
        } 
            catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
       /* public static Connection getConnection(Properties props) throws Exception {
            String dbUrl = props.getProperty("db.url").trim();
            String dbUser = props.getProperty("db.username").trim();

            // Load your secret key from somewhere secure (env variable, config file, etc)
            String base64Key = props.getProperty("db.secretKey").trim();
            SecretKey key = SimpleEncryptDecrypt.getKeyFromBase64String(base64Key);

            // Get the encrypted password from properties
            String encryptedPassword = props.getProperty("db.password").trim();
            System.out.println(encryptedPassword);

            // Decrypt password at runtime
            String password = SimpleEncryptDecrypt.decrypt(encryptedPassword, key);
            System.out.println(password);

            // Connect using decrypted password
            return DriverManager.getConnection(dbUrl, dbUser, password);
        }*/
            private static Properties props;

            static {
                try {
                    props = new Properties();
                    InputStream input = MySqlDBUtil.class.getClassLoader().getResourceAsStream("config.properties");
                    if (input == null) {
                        throw new RuntimeException("config.properties not found in classpath!");
                    }
                    props.load(input);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

           /* public static Connection getConnection() throws Exception {
                String dbUrl = props.getProperty("db.url").trim();
                String dbUser = props.getProperty("db.username").trim();

                String base64Key = props.getProperty("db.secretKey").trim();
                SecretKey key = SimpleEncryptDecrypt.getKeyFromBase64String(base64Key);

                String encryptedPassword = props.getProperty("db.password").trim();
                String password = SimpleEncryptDecrypt.decrypt(encryptedPassword, key);

                return DriverManager.getConnection(dbUrl, dbUser, password);
            }
            */
            
            public static Connection getConnection() throws Exception {
                //System.out.println("db.url = " + props.getProperty("db.url"));
               // System.out.println("db.username = " + props.getProperty("db.username"));
              //  System.out.println("db.password = " + props.getProperty("db.password"));
              //  System.out.println("db.secretKey = " + props.getProperty("db.secretKey"));

                String dbUrl = props.getProperty("db.url");
                String dbUser = props.getProperty("db.username");
                String encryptedPassword = props.getProperty("db.password");
                String base64Key = props.getProperty("db.secretKey");

                // check for null before trim
                if (dbUrl == null || dbUser == null || encryptedPassword == null || base64Key == null) {
                    throw new RuntimeException("One or more required properties missing from config.properties");
                }

                dbUrl = dbUrl.trim();
                dbUser = dbUser.trim();
                encryptedPassword = encryptedPassword.trim();
                base64Key = base64Key.trim();

                SecretKey key = SimpleEncryptDecrypt.getKeyFromBase64String(base64Key);
                String password = SimpleEncryptDecrypt.decrypt(encryptedPassword, key);

                return DriverManager.getConnection(dbUrl, dbUser, password);
            }

        }








    

    


