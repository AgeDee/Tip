package sample;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class AvatarManager {
    static Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dwotvqtsr",
            "api_key", "778453778568752",
            "api_secret", "X6DbRCvbg5HFhReXzyUOFq5Lyu4"));

    public static String downloadAvatar(String userId) {
        ApiResponse apiResponse = null;
        try {

            apiResponse = cloudinary.api().resource(userId, ObjectUtils.emptyMap());

        } catch (Exception e) {
            System.out.println("Nie znaleziono avatara podanego użytkownika!");
        }

        if (apiResponse != null) {
            return apiResponse.values().toArray()[3].toString();

        } else {
            try {
                apiResponse = cloudinary.api().resource("defaultAvatar", ObjectUtils.emptyMap());
                return apiResponse.values().toArray()[3].toString();
            } catch (Exception e) {
                System.out.println("Nie znaleziono avatara domyślnego!");
            }
        }
        return null;
    }

    public static String uploadAvatar(File image, String userId) throws IOException {
        cloudinary.uploader().destroy(userId, ObjectUtils.emptyMap());
        Map params = ObjectUtils.asMap("public_id", userId);
        Map uploadResult = cloudinary.uploader().upload(image, params);

        String avatarUrl = uploadResult.values().toArray()[3].toString();


        return avatarUrl;
    }
}


