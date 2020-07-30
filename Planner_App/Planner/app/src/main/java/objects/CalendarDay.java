package objects;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.javatuples.Triplet;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class CalendarDay {
    //  month/day/year format
    Triplet<Integer, Integer, Integer> dayID;
    HashSet<String> notes;



    public String toString(){
        String id = dayID.getValue0().toString() + "/" + dayID.getValue1().toString() + "/"+ dayID.getValue2().toString();
        return  id;
    }

    public CalendarDay(int month, int day, int year) {
        this.dayID = Triplet.with(month,day,year);
    }
    public CalendarDay(String date){
        //array of Strings that specify a date should be length 3 every time
        String [] dateValues = date.split("/");
        //array of Integers that specify a date should be length 3 every time
        Integer[] dateValues2 =  new Integer[dateValues.length];;
        //convert strings to integer
        for(int i = 0; i<dateValues.length; i++){
            dateValues2[i] = Integer.parseInt(dateValues[i]);
        }
        this.dayID = Triplet.fromArray(dateValues2);
    }

    public HashSet<String> getNotes() {
        return notes;
    }

    public void setNotes(HashSet<String> notes) {
        this.notes = notes;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getNextDay(){
        return LocalDate.of(dayID.getValue2().intValue(), dayID.getValue0().intValue(),dayID.getValue1().intValue()).plusDays(1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getPreviousDay(){
        return LocalDate.of(dayID.getValue2().intValue(), dayID.getValue0().intValue(), dayID.getValue1().intValue()).minusDays(1);
    }
}
