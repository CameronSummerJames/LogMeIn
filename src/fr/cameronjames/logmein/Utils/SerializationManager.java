package fr.cameronjames.logmein.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SerializationManager {

    private Gson gson;
    public SerializationManager(){
        this.gson = createGsonInstance();
    }
    private Gson createGsonInstance() {
        return new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
    }
    public String serialize(Account profile) {

        return this.gson.toJson(profile);

    }
    public Account deserialize(String json) {

        return this.gson.fromJson(json, Account.class);

    }
}
