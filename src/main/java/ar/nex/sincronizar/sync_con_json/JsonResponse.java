package ar.nex.sincronizar.sync_con_json;

import ar.nex.entity.Item;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Renzo O. Gorosito <renzog6@gmail.com>
 */
public class JsonResponse {
    private boolean success;
    private List<Item> data;
    private String message;    

    public JsonResponse(String success, String data, String message) {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.success);
        hash = 59 * hash + Objects.hashCode(this.data);
        hash = 59 * hash + Objects.hashCode(this.message);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JsonResponse other = (JsonResponse) obj;
        if (!Objects.equals(this.success, other.success)) {
            return false;
        }
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (!Objects.equals(this.message, other.message)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JsonResponse{" + "success=" + success + ", data=" + data + ", message=" + message + '}';
    }
    
}
