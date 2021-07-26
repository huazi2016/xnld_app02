package com.zxqy.xunilaidian.utils.httputil;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.zhy.http.okhttp.callback.Callback;
import com.zxqy.xunilaidian.utils.Utils;

import java.io.IOException;

import okhttp3.Response;

/**
 * Json封装
 * Created by dl on 2015/12/20.
 */
public abstract class JsonCallback<T> extends Callback<T> {
    private Class<T> mClass;
    private Gson mGson;

    public JsonCallback(Class<T> clazz) {
        this.mClass = clazz;
//        mGson = new GsonBuilder().serializeNulls().create();
        mGson  = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws IOException {
        try {
            String jsonString = response.body().string();
            if (Utils.isDebug){
                Log.e("GSOOOOOOOOOOOOON", "parseNetworkResponse: "+response );
            }
            return mGson.fromJson(jsonString, mClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringNullAdapter();
        }
    }

    public class StringNullAdapter extends TypeAdapter<String> {
        @Override
        public String read(JsonReader reader) throws IOException {
            // TODO Auto-generated method stub
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }
        @Override
        public void write(JsonWriter writer, String value) throws IOException {
            // TODO Auto-generated method stub
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

}
