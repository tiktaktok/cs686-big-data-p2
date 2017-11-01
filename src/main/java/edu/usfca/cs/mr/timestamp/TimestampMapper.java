package edu.usfca.cs.mr.timestamp;

import edu.usfca.cs.mr.util.Feature;
import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Mapper: Reads line by line, emit <"mm-dd", 1> pairs based on timestamp.
 */
public class TimestampMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd");

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String timestamp = (String) Observation.getFeatures(
                value.toString(), new Feature[]{Feature.TIMESTAMP}, new Class<?>[]{String.class})[0];
        Date date = new Date(Long.parseLong(timestamp));
        String dateStr = DATE_FORMAT.format(date);

        context.write(new Text(dateStr), new IntWritable(1));
    }
}
