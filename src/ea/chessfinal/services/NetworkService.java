package ea.chessfinal.services;

/**
 * Created by elliotanderson on 5/5/16.
 * This class will act as a service (service design mattern) to
 * simplify the repetitive code and slim down the Player object that
 * interacts with the server (or any other object that has to make interactions
 * with the server)
 *
 * @note: this class supports daisy chaining, so the methods can be called on eachother
 *
 * @author Elliot Anderson
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NetworkService {

    /**
     * This is slightly confusing, so I will explain why this is a class constant
     * We want to keep an "open" connection with the server, so it doesn't make sense
     * to create a new connection every time we instantiate an instance of the networkservice class
     *  (as we will be doing that a lot).
     *  So, instead of adding this as a normal variable, I will add it as a constant that will be called
     *  from whatever class instantiates the server object.  This adds a level of abstraction and decoupling
     *  that hopefully makes this program less confusing.
     *  -- End Rant
     */
    //public static final String baseServerUrlString = "http://45.55.54.237/";


    public static final String serverUrl = "http://localhost:3000/";


    /**
     * @var instance of the server that the request will be sent to
     */
    private HttpURLConnection server;

    /**
     * @var universal charset to be used.  Making it a variable in case there is some
     * standard charset that I don't know about and have to implement later
     */
    private String charset;

    /**
     * @var this is the param StringBuilder that we will append to as the user adds a parameter
     */
    private StringBuilder params;

    /**
     * @var this is the string that the server sends back to us
     */
    private String result;

    /**
     * @var response code.  This is the HTTP response code so we can
     * see if the request was sent/processed OK (200) or if there was an error
     * (404, 500, etc)
     */
    private int responseCode;

    /**
     * Instantiate the NetworkService class.
     * @param server
     */
    public NetworkService(HttpURLConnection server) {
        this.server = server;

        this.charset = "UTF-8";

        // set the headers that we will pretty much always use
        this.server.setRequestProperty("User-Agent", "");
        this.server.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        this.server.setDoOutput(true);

        this.params = new StringBuilder();
    }

    /**
     * The postRequest() method will be the method that actually makes the call
     * to the server and requests a response (using the HTTP POST header)
     *
     * @param actionRequested is a message to the server so the server knows how to properly respond to the
     * information given (this allows me to keep the server processing to one file)
     * @return this
     */
    public NetworkService postRequest(String actionRequested)  {

        StringBuffer response = new StringBuffer();

        try {
            // append the requested action to the URL
            this.addParam("action", actionRequested);

            DataOutputStream wr = new DataOutputStream(this.server.getOutputStream());
            wr.writeBytes(this.params.toString());
            wr.flush();
            wr.close();

            int responseCode = this.server.getResponseCode(); // 200 means it went through
            this.responseCode = responseCode;

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(this.server.getInputStream())
            );

            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        this.result = response.toString();

        return this;
    }

    /**
     * Add param data to the URL
     * @note All String encoding is done inside this method, so there is no need to do any before passing
     * it along to here
     * @param key the key of the parameter
     * @param value the value of the parameter
     * @return an instance of this class
     */
    public NetworkService addParam(String key, String value)  {
        if (this.params.length() != 0) {
            // not the first param, safe to append a "&" to the beginning
            this.params.append("&");
        }

        try {
            this.params.append(URLEncoder.encode(key, this.charset)).append("=").append(URLEncoder.encode(value, this.charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * Only method that doesn't daisy chain.
     * It will return the raw string of the result from the server
     * It will also clear all the data so that we can re-use the same instance
     * of this class
     * @return String of result from the server
     */
    public String getResult() {
        String result = this.result;

        this.clearData();

        return result;
    }

    /**
     * clears all the data so we don't need to re-instantiate the class,
     * we can simply re-use its methods a-la the service design pattern
     */
    public void clearData() {
        this.params = new StringBuilder();
        this.responseCode = 0;
        this.result = null;
    }


}
