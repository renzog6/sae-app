package ar.nex.sincronizar.sync;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import ar.nex.entity.Item;
import ar.nex.jpa.service.JpaService;
import ar.nex.sincronizar.Actividad;
import ar.nex.sincronizar.ActividadController;
import com.google.gson.GsonBuilder;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/**
 *
 * @author Renzo O. Gorosito <renzog6@gmail.com>
 */
public class CallingService {

    public static final String HOSTNAME = "localhost";
    public static final int PORT = 8000;
    public static final String BASE_URL = "http://" + HOSTNAME;// + ":" + PORT;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        remoteCreate();
        // remoteLogin();
        // remoteList();

        //System.out.println("main()::: " + sendPOST());
//        Item item = new JpaService().getItem().findItem(3L);
//        Usuario usuario = new JpaService().getUsuario().findUsuario(1L);
//        Actividad act = new Actividad("create", usuario, item);
//        System.out.println("Actividad::: " +act.toString());
//        new JpaService().getActividad().create(act);
    }

    public static String sendPOST() {
        String result = "";
        try {
            URL url = new URL("http://localhost:8080/sync/api.php");
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            int responseCode = huc.getResponseCode();
            System.out.println("responseCode::: " + responseCode);

            String urls = URLEncoder.encode("http://localhost:8080/sync/api.php", "UTF-8");
            System.out.println("sendPOST()::: " + urls);
            HttpPost post = new HttpPost(urls);
            //HttpPost post = new HttpPost("http://localhost:8080/sync/api.php");

            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"name\":\"mkyong\",");
            json.append("\"notes\":\"hello\"");
            json.append("}");

            // send a JSON data
            post.setEntity(new StringEntity(json.toString()));

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                    CloseableHttpResponse response = httpClient.execute(post)) {

                result = EntityUtils.toString(response.getEntity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void remoteList() {
        try {

            //Creating Http client.          
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet getRequest = new HttpGet("http://localhost:8080/sync/api.php");

            getRequest.addHeader("Content-Type", "application/json; charset=UTF-8");
            getRequest.addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiMTY4MzBiMzE0OWFkMjAwNzYzZjQzNDFjZjc2MzlkYzEzNWEyZTA3MTNlYTI4OWNiNmRkNjQ5MWVlYzIxN2U2YjZmZjM5YzIzMjA5ZDQyYWIiLCJpYXQiOjE1ODI3NTQzOTUsIm5iZiI6MTU4Mjc1NDM5NSwiZXhwIjoxNjE0Mzc2Nzk1LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.k811Fqv7TGWgWMWab284qkbIf3RAQnt6PSZmA0UjfXud-AtQy3HMRThpRo3b9Bb38hktrWKRbiqBU2Iy-w8040_dQBjLbA4HiU22xZGb1EdL0uzw0FQ-3cLCufAvMc4wXKVByF7pvfCMrakmc1DRvzY3D6Vl2EngKDQadnIRiyDTEowKYYDHizX-TQ12vjNqUeO77FaH3rXBfjPyZyAKpvZfZVGWPbTZsF1O7b1njGM3VfHRyFevvzOKLbBTMCPtN8N82ELz7pT2VfQmWilx8G60Clbt-Pv8nL7DNKKs0f819-wToE0d0Kzoyy38ETJnAhbnor1Hv9UxA3dkGW1VJV01E_cb4YJq4FtsHxBUWNrdW9w6WevmiVL3J6jpb7xilkDiz5RPRHjfemjoN6EzsWzX-YEDwCeplGnFOcVqe0H1xxRXKjThcgSt-mw-kDk8W5PE_aRcoz9poiyCeGARiFcd-fTD-7iPGsTqIXZjeeyLNYeAB9hKfpTVbSUuiY1voLibzxAyc_I2FIm1HKdduEfp2qQDpaQTTAJd8XkNeeXBLsTqOo7Yikud1utQDSIjqeip9O1rw-ZQqujpqmp-gv41gPAvUQpOk7OEEorHF7Z765ubYc1Q_T-o6s7NQS4tFmjxkCZrJOo-HbtSEl3Y7HbZxdwEJApsJd9IyhVx_qs");

            HttpResponse response = httpClient.execute(getRequest);
            HttpEntity responseEntity = response.getEntity();

            System.out.println("responseEntity::: " + EntityUtils.toString(responseEntity));

            System.out.println("Status Code::: " + response.getStatusLine().getStatusCode());

            JSONObject jsonObject = new JSONObject();
            if (responseEntity != null) {
                jsonObject = new JSONObject(EntityUtils.toString(responseEntity));
            }

           // ItemApi itemApi = new ItemApi();

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

            Item[] list = gson.fromJson(jsonObject.getJSONArray("data").toString(), Item[].class);
            for (Item item : list) {
                System.out.println(" Item[]::: " + item);

                String itemJson = gson.toJson(item);
                System.out.println("itemJson::: " + itemJson);
                //  itemApi.create(item);

            }
            //Close the client. 
            //response.close();
            //httpClient.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void remoteLogin() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(BASE_URL + "/api/login");

            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("email", "renzog6@gmail.com"));
            nvps.add(new BasicNameValuePair("password", "undernex"));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response = httpClient.execute(httpPost);

            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            System.out.println("ar.nex.sync.CallingService.remoteLogin():::" + EntityUtils.toString(entity));
            // and ensure it is fully consumed
            EntityUtils.consume(entity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void remoteCreate() {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://localhost:8080/sync/api.php");
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiZmU5Y2ZkYzZhODJkOGMzZmJlOTZjN2NjMWY5MjJhMzg5MGZlYjg3ZGU3YzZhNTJmNDk2M2YwYWY3MTExMGM2YjdhOTMxMTAzMGI4NzdmMDYiLCJpYXQiOjE1ODIwNTYyODcsIm5iZiI6MTU4MjA1NjI4NywiZXhwIjoxNjEzNjc4Njg3LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.dgboElpIIBuUs1ec-UrKnYggaXnRijCZ_R7humULHXlcBYQ4QQfss3e0tNYvJPKKl14y9WasQZIxRt03SSRXrI9mLPLXvy1om9zc6jI-Xah5hSAHBCEF3LHFcSR_clrweMfuV4Rz93fbJfQkKibLlN9R8OjBMnW3nbRY_I-Ims-_rk1e6nakwixSl0THiF6D9ZjWJ8YJfFXx6UTLebwGcoJFtl5HSppwb2eq4XaseFvkhA9AQwMPxc8pZcjbeN9Eq6gl6U5gBMpWZkhjOeLXZ0MAOzC3MGPibGS9ZLtktuAWbJo2IzxW0qFiQcy42cs27f6LbXPuntj2BHRi8YaT8qU9hapvwzcox8TcEuskVpuKIjeLpwoA1Jm8s1brkYoTqnpVqWQQsNvAkd4UgJ_caqQ_quj9b8BLWu_2pZsa9BCFXuMqcnlhxIFOY3EGRDbbFcWv35h5i9uaRpEB5WD0VIa09MlzqUYeVkRtBpe0MY0rVoLo1C8a0Po21NVuhPy_yIfNwtvSzk5-79Gi5s7IVDRfIoHEO9JXpOQu-TEbcW1xGMapslRXwbpdHbCZW-nmZerCfVo23Nq3IpbM_BU_ADaF515BpCLX1G0lMqdI5Fi8T-9cv7p8JdxkLvdrWuwxGRzv94BXSSHLEaEPy7-mZgEllFy---YvgHrbjL_Ddow");

            // Item item = new JpaService().getItem().findItem(3L);
            Actividad item = new JpaService().getActividad().findActividad("296L");
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
            StringEntity postingString = new StringEntity(gson.toJson(item));
            //System.out.println("Item::: " + gson.toJson(item));
            httpPost.setEntity(postingString);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            System.out.println(response.getStatusLine());

            HttpEntity responseEntity = response.getEntity();            
            if (responseEntity != null) {
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(responseEntity));
                
                Long idd = Long.parseUnsignedLong(jsonObject.get("data").toString()); //getJSONArray("data").toString();
                
            }

            //System.out.println("remoteLogin()::: " + EntityUtils.toString(entity));
            // and ensure it is fully consumed
            EntityUtils.consume(responseEntity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
