import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Assignment {

    public static void main(String[] args) {
        searchData("Assignment_Timecard.csv");
    }

    private static void searchData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            Date previousEndTime = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh.mm a");

            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                String position = data[1];
                String startTimeStr = data[2];
                String endTimeStr = data[3];

                if (startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
                    continue;
                }

                Date startTime = dateFormat.parse(startTimeStr);
                Date endTime = dateFormat.parse(endTimeStr);

                // a) 7 consecutive days
                if (daysBetween(startTime, endTime) >= 7) {
                    System.out.println(name + " (" + position + ") worked for 7 consecutive days.");
                }

                // b) r less than 10 hours between shifts but greater than 1 hour
                long timeBetweenShifts = previousEndTime != null
                        ? (startTime.getTime() - previousEndTime.getTime()) / 3600000
                        : 0;
                if (1 < timeBetweenShifts && timeBetweenShifts < 10) {
                    System.out.println(name + " (" + position
                            + ") has less than 10 hours between shifts but greater than 1 hour.");
                }

                // c) more than 14 hours in a single shift
                if ((endTime.getTime() - startTime.getTime()) / 3600000 > 14) {
                    System.out.println(name + " (" + position + ") worked for more than 14 hours in a single shift.");
                }

                previousEndTime = endTime;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static long daysBetween(Date start, Date end) {
        long differenceMillis = Math.abs(end.getTime() - start.getTime());
        return differenceMillis / (24 * 60 * 60 * 1000);
    }
}
