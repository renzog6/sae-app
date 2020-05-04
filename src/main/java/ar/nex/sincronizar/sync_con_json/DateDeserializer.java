package ar.nex.sincronizar.sync_con_json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Renzo O. Gorosito <renzog6@gmail.com>
 */
public class DateDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        Date dd = new Date();
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            dd = dateFormat.parse(je.getAsString());            
        } catch (ParseException ex) {
            Logger.getLogger(DateDeserializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dd;
    }

}
