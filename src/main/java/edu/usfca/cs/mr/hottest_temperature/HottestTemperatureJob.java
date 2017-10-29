package edu.usfca.cs.mr.hottest_temperature;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * This is the main class. Hadoop will invoke the main method of this class.
 */
public class HottestTemperatureJob {
    public static void main(String[] args) {
        try {
            Configuration conf = new Configuration();
            // Give the MapRed job a name. You'll see this name in the Yarn
            // webapp.
            Job job = Job.getInstance(conf, "hottest_temperature_job");
            /*
            job.getConfiguration().set("mapreduce.task.io.sort.mb", "2047");
            job.getConfiguration().set("mapreduce.map.java.opts", "-Xmx4096m");
            job.getConfiguration().set("yarn.nodemanager.vmem-pmem-ratio", "4.2");
            */
            // Current class.
            job.setJarByClass(HottestTemperatureJob.class);
            // Mapper
            job.setMapperClass(HottestTemperatureMapper.class);
            // Combiner. We use the reducer as the combiner in this case.
            job.setCombinerClass(HottestTemperatureReducer.class);
            // Reducer
            job.setReducerClass(HottestTemperatureReducer.class);
            // Outputs from the Mapper.
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(FloatWritable.class);
            // Outputs from Reducer. It is sufficient to set only the following
            // two properties if the Mapper and Reducer has same key and value
            // types. It is set separately for elaboration.
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(FloatWritable.class);
            // path to input in HDFS
            FileInputFormat.addInputPaths(job, args[0]);
            // path to output in HDFS
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            // Block until the job is completed.
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
