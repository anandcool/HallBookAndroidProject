package com.example.marriagehall;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.marriagehall.ui.bookings.BookingsFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String[] occasions = {"Wedding", "Anniversary", "Birthday", "Other"};
    String[] crowd = {"small(100-500)", "medium(500-1000)", "large(1000-5000)", "Supreme(5000-10000)"};
    int[] noGuests = {350, 750, 3500, 7500};
    float[] guestPrice = {300, 750, 3000, 7500};
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    String[] bookedStart, bookedEnd, bookedDates;

    Spinner occasionSpinner, crowdSpinner;
    OccasionAdapter occasionAdapter;
    CrowdAdapter crowdAdapter;
    Button calcPrice, bookNow;
    DatePicker startDatePicker, endDatePicker;
    EditText startDate, endDate;
    CheckBox veg, nonVeg;
    TextView price;
    ProgressBar pb;
    int guestNum, flag;
    String date;

    Calendar startDateCalendar;

    //Custom message Dialog box
    Dialog messageDialog;
    Button okBtn;
    ImageView crossBtn;

    int buttonPressed = 0;

    //to pass as parameters to req server
    String occasion = null;
    String guests_no = null;
    float amount;
    String hall_id=null;
    int customer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        startDatePicker = findViewById(R.id.start_date_picker);
        endDatePicker = findViewById(R.id.end_date_picker);
        occasionSpinner = findViewById(R.id.spinner_occasion);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        crowdSpinner = findViewById(R.id.crowd_spinner);
        veg = findViewById(R.id.veg);
        nonVeg = findViewById(R.id.nonveg);
        price = findViewById(R.id.price_view);
        pb = findViewById(R.id.price_load);
        calcPrice = findViewById(R.id.calc_price);
        bookNow = findViewById(R.id.btn_finalBook);

        //receiving intent from hall activity
        Intent intent = getIntent();
        hall_id = intent.getStringExtra("hall_id");
        customer_id = intent.getIntExtra("user_id", 0);

        //setting the adapter for the spinner which asks for occasion from user
        occasionAdapter = new OccasionAdapter(getApplicationContext(), occasions);
        occasionSpinner.setAdapter(occasionAdapter);
        occasionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                occasion = occasions[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(BookingActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        //Date picker to select dates
        startDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 0;
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);
                showDatePickerDialog(calendar);
            }
        });

        endDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                startDateCalendar.add(startDateCalendar.DATE, 1);
                showDatePickerDialog(startDateCalendar);
            }
        });


        //setting the adapter for the spinner which asks for crowd gathering range from user
        crowdAdapter = new CrowdAdapter(getApplicationContext(), crowd);
        crowdSpinner.setAdapter(crowdAdapter);
        crowdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                guestNum = i;
                guests_no = ""+noGuests[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(BookingActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


        //on the click of calculate price button
        calcPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPressed = 1;
                pb.setVisibility(View.VISIBLE);
                if (startDate.getText().toString() != null && endDate.getText().toString() != null && (veg.isChecked() || nonVeg.isChecked())) {
                    //to delay this event we here applied a handler
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pb.setVisibility(View.INVISIBLE);
//                            calculation of price
                            int dayCount = calcDays();
                            if (!nonVeg.isChecked())
                                amount = 10 * dayCount * guestPrice[guestNum];
                            else if (!veg.isChecked())
                                amount = 15 * dayCount * guestPrice[guestNum];
                            else
                                amount = 12 * dayCount * guestPrice[guestNum];
                            if (dayCount == -1)
                                price.setText("Price:   Rs 0.00");
                            else
                                price.setText("Price:   Rs " + amount);
                        }
                    }, 2000);
                } else {
                    //to delay this event we here applied a handler
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pb.setVisibility(View.INVISIBLE);
                            Toast.makeText(BookingActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                        }
                    }, 2000);
                }
            }
        });

        //instantiate messageDialog object
        messageDialog = new Dialog(this);
        //book now button functionality
        bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to check that price is calculated or not
                if(buttonPressed != 0)
                {
                    if(amount == 0.00)
                    {
                        Toast.makeText(BookingActivity.this, "Invalid Price", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //send request to server
                        StringRequest req = new StringRequest(Request.Method.POST, URL.ROOT_BOOK_HALL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if(response.equals("booked"))
                                            showMessageDialog();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(BookingActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("customer_id", ""+customer_id);
                                params.put("hall_id", hall_id);
                                params.put("occasion", occasion);
                                params.put("start_date", startDate.getText().toString());
                                params.put("end_date", endDate.getText().toString());
                                params.put("guests_no", guests_no);
                                params.put("amount", ""+amount);
                                return params;
                            }
                        };
                        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);
                    }
                }
                else{
                    Toast.makeText(BookingActivity.this, "Please calculate the price first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //date picker dialog
    private void showDatePickerDialog(final Calendar calendar) {
        //sending request to server to take all the booked dates for this particular hall
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.ROOT_BOOK_DATES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                bookedStart = new String[jsonArray.length()];
                                bookedEnd = new String[jsonArray.length()];
                                bookedDates = new String[15];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject myObj = jsonArray.getJSONObject(i);
                                    bookedStart[i] = myObj.getString("start_date").substring(0, 10);
                                    bookedEnd[i] = myObj.getString("end_date").substring(0, 10);
                                }

                                //to put all booked dates in one string array
                                int z=0;
                                for(int k=0; k<bookedStart.length; k++){
                                    String[] myDates = calcDates(bookedStart[k], bookedEnd[k]);
                                    for(int j=0; myDates[j]!=null; j++){
                                        bookedDates[z] = myDates[j];
                                        z++;
                                    }
                                }


                                DatePickerDialog dpd = DatePickerDialog.newInstance(
                                        BookingActivity.this,
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                );

                                dpd.setMinDate(calendar); //to disable the required dates
                                dpd.show(BookingActivity.this.getFragmentManager(), "DatePickerDialog");

                                //disabling the booked dates from the calendar
                                for (int j = 0; j < bookedStart.length; j++) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    java.util.Date date = null;
                                    for (int i = 0; bookedDates[i]!=null; i++) {
                                        try {
                                            date = sdf.parse(bookedDates[i]);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        Calendar calendar = dateToCalendar(date);
                                        System.out.println(calendar.getTime());

                                        List<Calendar> dates = new ArrayList<>();
                                        dates.add(calendar);
                                        Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
                                        dpd.setDisabledDays(disabledDays1);

                                    }
                                }

                            }
                            else{
                                DatePickerDialog dpd = DatePickerDialog.newInstance(
                                        BookingActivity.this,
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                );

                                dpd.setMinDate(calendar); //to disable the required dates
                                dpd.show(BookingActivity.this.getFragmentManager(), "DatePickerDialog");
                            }
                        } catch (JSONException e) {
                            Toast.makeText(BookingActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BookingActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("hall_id", hall_id);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


    }

    // function to count no of days from starting and ending dates
    public int calcDays() {
        int count = 0;

        String[] sDate = startDate.getText().toString().split("-");
        String[] eDate = endDate.getText().toString().split("-");

        int fromDate = Integer.parseInt(sDate[0]);
        int toDate = Integer.parseInt(eDate[0]);

        int sYear = Integer.parseInt(sDate[2]);
        int eYear = Integer.parseInt(eDate[2]);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        if (sYear > year + 1 || eYear > year + 1) {
            Toast.makeText(this, "Can't book dates of more than 1 year ahead", Toast.LENGTH_SHORT).show();
            return -1;
        }
        if (sDate[1].equals(eDate[1])) {
            count = toDate - fromDate + 1;
        } else {
            if (sDate[1].equals(months[1]))
                count = toDate + (28 + 1 - fromDate);
            else if (sDate[1].equals(months[3]) || sDate[1].equals(months[5]) || sDate[1].equals(months[8]) || sDate[1].equals(months[10]))
                count = toDate + (30 + 1 - fromDate);
            else
                count = toDate + (31 + 1 - fromDate);
        }
        if (count >= 15) {
            Toast.makeText(this, "Can't book this hall for more than 15 days", Toast.LENGTH_SHORT).show();
            return -1;
        }
        return count;
    }

    //function to create a message dialog box on completion of booking
    public void showMessageDialog() {
        messageDialog.setContentView(R.layout.booked_message_dailog);
        okBtn = messageDialog.findViewById(R.id.dialog_okBtn);
        crossBtn = messageDialog.findViewById(R.id.dialog_crossBtn);

        //OK button functionality
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                messageDialog.dismiss();
            }
        });

        //CROSS button functionality
        crossBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                messageDialog.dismiss();
            }
        });

//        messageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        messageDialog.show();
    }

    //function to inject all booked dates from start to end into a string
    public String[] calcDates(String dateFrom, String dateTo) {
        //spliting the given string of dates
        String[] str1 = dateFrom.split("-");
        String[] str2 = dateTo.split("-");


        //extracting year ,month & day from it
        int sYear = Integer.parseInt(str1[0]);
        int eYear = Integer.parseInt(str2[0]);
        int sMonth = Integer.parseInt(str1[1]);
        int eMonth = Integer.parseInt(str2[1]);
        int sDay = Integer.parseInt(str1[2]);
        int eDay = Integer.parseInt(str2[2]);

        String[] finalDates = new String[15];

        if (sMonth == eMonth && sYear == eYear) {
            int i = 0;
            while (sDay <= eDay) {
                String str = sYear + "-" + sMonth + "-" + sDay;
                sDay++;
                finalDates[i] = str;
                i++;
            }
        } else if (sYear == eYear && sMonth != eMonth) {
            int i = 0;
            if (sMonth == 2 && sYear % 4 != 0) {
                while (sDay <= eDay) {
                    if (sDay > 28) {
                        sDay = 1;
                    }
                    String str = sYear + "-" + sMonth + "-" + sDay;
                    sDay++;
                    finalDates[i] = str;
                    i++;
                }
            } else if (sMonth == 2 && sYear % 4 == 0) {
                while (sDay <= eDay) {
                    if (sDay > 29) {
                        sDay = 1;
                    }
                    String str = sYear + "-" + sMonth + "-" + sDay;
                    sDay++;
                    finalDates[i] = str;
                    i++;
                }
            } else if (sMonth == 1 || sMonth == 3 || sMonth == 5 || sMonth == 7 || sMonth == 8 || sMonth == 10 || sMonth == 12) {
                while (sDay <= eDay) {
                    if (sDay > 31) {
                        sDay = 1;
                    }
                    String str = sYear + "-" + sMonth + "-" + sDay;
                    sDay++;
                    finalDates[i] = str;
                    i++;
                }
            } else if (sMonth == 4 || sMonth == 6 || sMonth == 9 || sMonth == 11) {
                while (sDay <= eDay) {
                    if (sDay > 30) {
                        sDay = 1;
                    }
                    String str = sYear + "-" + sMonth + "-" + sDay;
                    sDay++;
                    finalDates[i] = str;
                    i++;
                }
            }
        }
        return finalDates;
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    //to set the date into edit text
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;
        if(month < 10){
            date = year + "-" + "0" + month + "-" + dayOfMonth;
        }
        else{
            date = year + "-" + month + "-" + dayOfMonth;
        }
        if (flag == 0) {
            startDateCalendar = Calendar.getInstance();
            startDateCalendar.set(year, monthOfYear, dayOfMonth);
            startDate.setText(date);
            endDate.setText("");  //setting end date text field empty as soon as we fill start date again
        } else if (flag == 1)
            endDate.setText(date);
    }
}
