package com.example.gamesaverx.gamesaverx.client;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gamesaverx.R;
import com.example.gamesaverx.gamesaverx.Interfaces.IsSavedListener;
import com.example.gamesaverx.gamesaverx.Interfaces.OnOfferClickListener;
import com.example.gamesaverx.gamesaverx.Interfaces.ResponseListener;
import com.example.gamesaverx.gamesaverx.Screens.Drawer;
import com.example.gamesaverx.gamesaverx.Screens.Login;
import com.example.gamesaverx.gamesaverx.Screens.Register;
import com.example.gamesaverx.gamesaverx.Utils.Offer;
import com.example.gamesaverx.gamesaverx.Utils.RecyclerAdapter;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestClient {

    private String BASE_URL = "http://10.0.2.2:8000";

    private Context context;

    private RequestQueue queue;

    private RestClient(Context context) {
        this.context = context;
    }

    private static RestClient singleton = null;

    public static RestClient getInstance(Context context) {
        if (singleton == null) {
            singleton = new RestClient(context);
        }
        return singleton;
    }

    //Método que borra las ofertas que ya expiraron
    public void deleteExpiredOffers() {
        String url = BASE_URL+"/v1/delete_expired/";
        queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        this.queue.add(request);
    }
    //Método que comprueba el token de usuario, si lo recibe inicia sesión automáticamente y sino te manda a Login
    public void isLogged(String sessionToken) {
        queue = Volley.newRequestQueue(context);

        JsonObjectRequestWithCustomAuth request = new JsonObjectRequestWithCustomAuth(
                Request.Method.GET,
                BASE_URL + "/v1/log",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(context, Drawer.class);
                        Toast.makeText(context, "¡Logueado con éxito!", Toast.LENGTH_LONG).show();
                        context.startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                        preferences.edit().remove("tokenSession").commit();
                        Intent intent = new Intent(context, Login.class);
                        context.startActivity(intent);
                    }
                },
                context
        );

        this.queue.add(request);
    }
    //Método que crea un nuevo usuario en la base de datos
    public void register(TextView editTextName, TextView editTextSurnames, TextView editTextEmail, TextView editTextPassword, TextView editTextPassword2) {
        queue = Volley.newRequestQueue(context);
        JSONObject requestBody = new JSONObject();
        try {

            requestBody.put("name", editTextName.getText().toString());
            requestBody.put("surnames", editTextSurnames.getText().toString());
            requestBody.put("email", editTextEmail.getText().toString());
            requestBody.put("password", editTextPassword.getText().toString());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + "/v1/users",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(context, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Login.class);
                        context.startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(context, "Sin conexión", Toast.LENGTH_LONG).show();

                        } else if (error.networkResponse.statusCode == 409) {
                            Toast.makeText(context, "Cuenta ya registrada", Toast.LENGTH_SHORT).show();

                        } else {
                            int serverCode = error.networkResponse.statusCode;
                            Toast.makeText(context, "Error: " + serverCode, Toast.LENGTH_LONG).show();

                        }
                    }
                });
        this.queue.add(request);
    }
    //Método que comprueba el email  y la contraseña de un usuario, si está en la base de datos lo envía a la pantalla principal y le asigna un token
    public void login(EditText email, EditText password, Context context) {
        queue = Volley.newRequestQueue(context);
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", email.getText().toString());
            requestBody.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + "/v1/sessions",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Guardando el id del usuario en las sharedPreferences
                        SharedPreferences prefs = context.getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        try {
                            editor.putString("tokenSession", response.getString("sessionToken"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editor.apply();
                        Intent intent = new Intent(context, Drawer.class);
                        context.startActivity(intent);

                        try {
                            Toast.makeText(context, response.getString("tokenSession"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        switch (error.networkResponse.statusCode) {
                            case 404:
                                email.setError("Usuario no registrado");
                                break;
                            case 401:
                                password.setError("Contraseña incorrecta");
                        }
                    }
                });
        queue.add(request);
    }
    //Método para listar todas la ofertas actuales en un recyclerview
    public void offers(String title, int size, int offset, OnOfferClickListener offerListener, RecyclerView recyclerView, ResponseListener listener) {
        queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                BASE_URL + "/v1/offers?size=" + size + "&offset=" + offset + "&title=" + title,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Offer> itemList = new ArrayList() {
                        };
                        int count = 0;
                        try {
                            count = response.getInt("count");
                            JSONArray results = response.getJSONArray("results");


                            for (int i = 0; i < results.length(); i++) {
                                JSONObject offer = results.getJSONObject(i);
                                Offer newOffer = new Offer();
                                newOffer.setId(offer.getString("id"));
                                newOffer.setTitle(offer.getString("title"));
                                newOffer.setStore(offer.getString("store__name"));
                                newOffer.setImage(BASE_URL + offer.getString("image"));
                                newOffer.setDiscount_percentage(String.valueOf(new BigDecimal(offer.getString("discount_percentage"))));
                                newOffer.setOriginal_price(String.valueOf(new BigDecimal(offer.getString("original_price"))));
                                newOffer.setEnd_date(offer.getString("end_date"));
                                itemList.add(newOffer);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(itemList, offerListener);
                        recyclerView.setAdapter(recyclerAdapter);
                        listener.onOffersResponse(count);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        queue.add(request);
    }
//Método que pide todos los campos de una oferta para poner cada campo en un textview
    public void offer(String id_game, Context context, TextView title, TextView description, ImageView image, TextView url, TextView original_price, TextView discount_price,
                      TextView genre, TextView release_date, TextView developer, TextView publisher, TextView discount_percentage, TextView end_date) {
        queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                BASE_URL + "/v1/offer/" + id_game,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            // Obtener los valores de la respuesta JSON
                            String image_url = BASE_URL + response.getString("image");
                            String titleText = response.getString("title");
                            String descriptionText = response.getString("description");
                            BigDecimal originalPriceValue = new BigDecimal(response.getString("original_price"));
                            String genreText = response.getString("genre");
                            String urlText = response.getString("url");
                            String releaseDateText = response.getString("release_date");
                            String developerText = response.getString("developer");
                            String publisherText = response.getString("publisher");
                            BigDecimal discountPercentageValue = new BigDecimal(response.getString("discount_percentage"));
                            String endDateText = response.getString("end_date");

                            Date releaseDate = inputFormat.parse(releaseDateText);
                            Date endDate = inputFormat.parse(endDateText);

                            releaseDateText = outputFormat.format(releaseDate);
                            endDateText = outputFormat.format(endDate);

                            // Calcular el precio de descuento
                            BigDecimal discountPriceValue = originalPriceValue.multiply(discountPercentageValue)
                                    .divide(new BigDecimal(100));
                            BigDecimal discountPrice = originalPriceValue.subtract(discountPriceValue);
                            discountPrice = discountPrice.setScale(2, RoundingMode.DOWN);

                            // Establecer los valores en los TextView correspondientes
                            Picasso.get().load(image_url).into(image);
                            title.setText(titleText);
                            description.setText(descriptionText);
                            original_price.setText(originalPriceValue +"€");
                            original_price.setPaintFlags(original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            discount_price.setText(discountPrice.toString()+"€");
                            genre.setText(genreText);
                            url.setText(urlText);
                            release_date.setText(releaseDateText);
                            developer.setText(developerText);
                            publisher.setText(publisherText);
                            discount_percentage.setText(discountPercentageValue.toString());
                            end_date.setText(endDateText);
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == 404) {
                            Toast.makeText(context, "Esta oferta no existe", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        this.queue.add(request);
    }

//Método que comprueba si la oferta está guardada
    public void isFavourite(String id_game, IsSavedListener listener) {

        JsonObjectRequestWithCustomAuth request = new JsonObjectRequestWithCustomAuth(
                Request.Method.GET,
                BASE_URL + "/v1/offer/" + id_game + "/saved",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponseReceived(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onResponseReceived(false);
                    }
                },
                context
        );
        this.queue.add(request);
    }
//Método que añade la oferta a la lista de guardados
    public void addFavorites(String id_game, ImageButton savedButton) {

        JsonObjectRequestWithCustomAuth request = new JsonObjectRequestWithCustomAuth(
                Request.Method.PUT,
                BASE_URL + "/v1/offer/" + id_game + "/saved",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        savedButton.setBackgroundResource(R.drawable.ic_full_saved);
                        savedButton.setEnabled(true);//HABILITA EL BOTÓN
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show();
                        savedButton.setEnabled(true);//HABILITA EL BOTÓN
                    }
                },
                context
        );
        this.queue.add(request);
    }
//Método que borra las ofertas de la lista de guardados
    public void deleteFavorites(String id_game, ImageButton savedButton) {

        JsonObjectRequestWithCustomAuth request = new JsonObjectRequestWithCustomAuth(
                Request.Method.DELETE,
                BASE_URL + "/v1/offer/" + id_game + "/saved",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        savedButton.setBackgroundResource(R.drawable.ic_save);
                        savedButton.setEnabled(true); //HABILITA EL BOTÓN
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show();
                        savedButton.setEnabled(true);//HABILITA EL BOTÓN
                    }
                },
                context
        );

        queue.add(request);
    }
//Método que indexa las ofertas de la lista guardados
    public void saved(RecyclerView recyclerView, OnOfferClickListener listener) {
        queue = Volley.newRequestQueue(context);

        JsonObjectRequestWithCustomAuth request = new JsonObjectRequestWithCustomAuth(
                Request.Method.GET,
                BASE_URL + "/v1/saved",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<Offer> itemList = new ArrayList() {
                            };
                            JSONArray results = response.getJSONArray("results");


                            for (int i = 0; i < results.length(); i++) {
                                JSONObject offer = results.getJSONObject(i);
                                Offer newOffer = new Offer();
                                newOffer.setId(offer.getString("id"));
                                newOffer.setTitle(offer.getString("title"));
                                newOffer.setStore(offer.getString("store__name"));
                                newOffer.setImage(BASE_URL + offer.getString("image"));
                                newOffer.setDiscount_percentage(String.valueOf(new BigDecimal(offer.getString("discount_percentage"))));
                                newOffer.setOriginal_price(String.valueOf(new BigDecimal(offer.getString("original_price"))));
                                newOffer.setEnd_date(offer.getString("end_date"));
                                itemList.add(newOffer);
                            }
                            LinearLayoutManager llm = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(llm);
                            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(itemList, listener);
                            recyclerView.setAdapter(recyclerAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                },
                context
        );
        queue.add(request);

    }
    //Método que pide los datos del usuario y los mete sus respectivos textviews
    public void profile(TextView name,TextView email){
        queue = Volley.newRequestQueue(context);

        JsonObjectRequestWithCustomAuth request = new JsonObjectRequestWithCustomAuth(
                Request.Method.GET,
                BASE_URL + "/v1/profile",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            name.setText(response.getString("name") + " " + response.getString("surnames"));
                            email.setText(response.getString("email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                },
                context
        );
        queue.add(request);
    }
    //Método que completa los edittext de editar perfil para conocer los anteriores datos
    public void fillProfile(EditText name, EditText surnames){

        queue = Volley.newRequestQueue(context);

        JsonObjectRequestWithCustomAuth request = new JsonObjectRequestWithCustomAuth(
                Request.Method.GET,
                BASE_URL + "/v1/profile",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            name.setText(response.getString("name"));
                            surnames.setText(response.getString("surnames"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                },
                context
        );

        this.queue.add(request);
    }
//Método que cambia los datos de nombre y apellidos de un usuario
    public void editProfile(EditText name, EditText surnames){

        queue = Volley.newRequestQueue(context);

        JSONObject body = new JSONObject();
        try {
            body.put("name", name.getText().toString());
            body.put("surnames", surnames.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequestWithCustomAuth request = new JsonObjectRequestWithCustomAuth(
                Request.Method.PUT,
                BASE_URL + "/v1/profile",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Drawer.class);
                        context.startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                },
                context
        );

        this.queue.add(request);
    }
    //Método que cambia la contraseña de un usuario
    public void changePassword(EditText oldPassword, EditText newPassword){

        queue = Volley.newRequestQueue(context);

        JSONObject body = new JSONObject();
        try {
            body.put("oldPassword", oldPassword.getText().toString());
            body.put("newPassword", newPassword.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequestWithCustomAuth request = new JsonObjectRequestWithCustomAuth(
                Request.Method.POST,
                BASE_URL + "/v1/password",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Contraseña Cambiada", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Drawer.class);
                        context.startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == 404){
                            Toast.makeText(context, "Contraseña antigua incorrecta", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                context
        );

        this.queue.add(request);
    }
//Clase utilizada para coger los token de usuario en los diferentes métodos que lo requieran
    class JsonObjectRequestWithCustomAuth extends JsonObjectRequest {
        private Context context;

        public JsonObjectRequestWithCustomAuth(int method,
                                               String url,
                                               @Nullable JSONObject jsonRequest,
                                               Response.Listener<JSONObject> listener,
                                               @Nullable Response.ErrorListener errorListener,
                                               Context context) {
            super(method, url, jsonRequest, listener, errorListener);
            this.context = context;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            String sessionToken = preferences.getString("tokenSession", null);
            //PARA PROBAR LA PANTALLA
            /*String sessionToken = "";*/

            HashMap<String, String> myHeaders = new HashMap<>();
            myHeaders.put("Token", sessionToken);
            return myHeaders;
        }
    }
}
