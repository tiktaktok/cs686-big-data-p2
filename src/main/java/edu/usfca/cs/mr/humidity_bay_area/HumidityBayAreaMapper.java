package edu.usfca.cs.mr.humidity_bay_area;

import com.google.common.collect.Sets;
import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static edu.usfca.cs.mr.util.Feature.*;

public class HumidityBayAreaMapper extends Mapper<LongWritable, Text, IntWritable, FloatWritable> {

    private static final Set<String> BAY_AREA_PREFIXES = Sets.newHashSet(
            "9q8u", "9q8v", "9q8y", "9q8z",
            "9q9h", "9q9j", "9q9k", "9q9m", "9q9n", "9q9p"
    );

    private final Calendar CALENDAR = Calendar.getInstance();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Observation observation = new Observation(value.toString(), TIMESTAMP, GEOHASH, RELATIVE_HUMIDITY_ZERODEGC_ISOTHERM);
        String geohash = observation.getGeohash();

        if (BAY_AREA_PREFIXES.contains(geohash.substring(0, 4))) {
            Date date = new Date(Long.parseLong(observation.getFeature(TIMESTAMP, String.class)));
            CALENDAR.setTime(date);
            int month = CALENDAR.get(Calendar.MONTH) + 1;
            float humidity = observation.getFeature(RELATIVE_HUMIDITY_ZERODEGC_ISOTHERM, Float.class);
            context.write(new IntWritable(month), new FloatWritable(humidity));
        }
    }
}
