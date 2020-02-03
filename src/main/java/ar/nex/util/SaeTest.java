/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Renzo
 */
public class SaeTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
            Date i = fd.parse("01/01/2013");
            Date f = fd.parse("01/02/2020");
            System.out.println("ar.nex.util.SaeTest.main() i: " + SaeDate.getDateString(i));
            System.out.println("ar.nex.util.SaeTest.main() f: " + SaeDate.getDateString(f));
            System.out.println("ar.nex.util.SaeTest.main() antiguedad: " + SaeDate.getAntiguedad(i, f));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
