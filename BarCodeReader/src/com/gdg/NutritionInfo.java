package com.gdg;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.gdg.nutritioninfo.FoodInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NutritionInfo {
	// link for geting info http://www.opennutritiondatabase.com/foods/{UPC}.json
	private String returnedJSON = null;
	private static String urlStart = "http://www.opennutritiondatabase.com/foods/";
	private static String urlEnd = ".json";
	FoodInfo foodInfo = null;

	public void getJSON(String UPC) {

		try {	
			URL url = new URL(urlStart+UPC+urlEnd);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				// should handle errors here
				// as this is just demo code I will not make it perfect
			}
			Gson gson = new GsonBuilder().create();
			Reader reader = new InputStreamReader(connection.getInputStream());
			foodInfo = gson.fromJson(reader, FoodInfo.class);

		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public FoodInfo getFoodInfo() {
		return foodInfo;
	}

	public void setFoodInfo(FoodInfo foodInfo) {
		this.foodInfo = foodInfo;
	}

}
