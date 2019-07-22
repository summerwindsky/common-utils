package zj.json;


import com.google.gson.Gson;

public class GsonUtil {

//    private static Gson gson = new GsonBuilder().serializeNulls().create();

    public static Gson gson = new Gson();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, Class<?> clz) {
        return (T) gson.fromJson(json, clz);
    }

    public static <T> T toJsonFromObj(Object obj, Class<?> clz) {
        String json = gson.toJson(obj);
        return (T) gson.fromJson(json, clz);
    }

}
