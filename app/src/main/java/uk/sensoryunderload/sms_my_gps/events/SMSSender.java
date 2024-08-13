package uk.sensoryunderload.sms_my_gps.events;

import android.location.Location;
import android.location.LocationManager;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Locale;

public class SMSSender {
  private final String recipient;
  private final Location location;
  private final String descriptionString;

  public SMSSender(String recipient, Location location,
                   String descriptionString) {
    this.recipient = recipient;
    this.location = location;
    this.descriptionString = descriptionString;
  }

  public void sendMessage() {
    SmsManager smsManager = SmsManager.getDefault();
    ArrayList<String> messages =
            smsManager.divideMessage(generateBasicMessage());
    smsManager.sendMultipartTextMessage(
            recipient, null, messages, null, null);
  }

  private String generateBasicMessage() {
    if (location != null) {
      double lat = location.getLatitude();
      double lon = location.getLongitude();
      float accuracy = location.getAccuracy();
      float speed = location.getSpeed();

      return String.format(
              "%1$s:\n" +
              "%2$s,%3$s\n" +
              "Accuracy: %4$s m\n" +
              "Speed: %5$s km/h",
              descriptionString,
              String.format(Locale.US, "%.6f", lat),
              String.format(Locale.US, "%.6f", lon),
              Math.round(accuracy),
              Math.round(3.6 * speed));
    } else {
      return descriptionString;
    }
  }
}
