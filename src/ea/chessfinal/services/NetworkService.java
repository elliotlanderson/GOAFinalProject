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
import java.net.*;

import java.util.List;
import java.util.ArrayList;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class NetworkService {

    /**
     * @var connectionScheme -- (gonna be http)
     */
    private String connectionScheme = "http";


    /**
     * @var host - string of the host url
     */
    private String host = "localhost:3000";

    /**
     * @var instance of the URI, which we will use to send the request
     */
    private URI uri;

    /**
     * @var instance of the server that the request will be sent to
     */
    private HttpClient httpClient;

    /**
     * @var instance of the request to be sent -- in this case
     * we will always use post so i am going to skip the whole scalability
     * thing for ease and time constraints
     */
    private HttpPost request;

    /**
     * @var this is the param StringBuilder that we will append to as the user adds a parameter
     */
    private List<NameValuePair> params;

    /**
     * @var this is the response we get from the server
     */
    private HttpResponse response;

    /**
     * @var this is the response string converted from the
     * HttpResponse retrieved from the server
     */
    private String responseString;


    /**
     * Instantiate the NetworkService class.
     */
    public NetworkService() {

        // build the URI
        try {
            this.uri = new URIBuilder()
                    .setScheme(this.connectionScheme)
                    .setHost(this.host)
                    .build();
        } catch (URISyntaxException e) {
            System.out.println("Invalid URI");
        }

        // create the connection to the server
        try {
            this.httpClient = HttpClients.createDefault();
            this.request = new HttpPost(this.uri);
            System.out.println("Established connection to server.");
        } catch (Exception e) {
            throw new IllegalStateException("Error connecting to the server.");
        };

        this.params = new ArrayList<NameValuePair>();
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

        this.addParam("action", actionRequested);

        try {
            this.request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            this.response = this.httpClient.execute(this.request);

            HttpEntity entity = this.response.getEntity();

            if (entity != null) {
                this.responseString = EntityUtils.toString(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



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

        this.params.add(new BasicNameValuePair(key, value));

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
        String result = this.responseString;

        this.clearData();

        return result;
    }

    /**
     * clears all the data so we don't need to re-instantiate the class,
     * we can simply re-use its methods a-la the service design pattern
     */
    public void clearData() {
        this.params = new ArrayList<NameValuePair>();
        this.response = null;
    }


}
